# POO-09 — Diagramas de secuencia (Mermaid)

Escenario: **ataque coordinado**. El cliente de Insomnia habla solo con A1; A1 valida sus propias reglas, consulta las reglas de A3 y A2 y recién guarda en su H2 si ambas aceptan.

Orden elegido: **validación local primero** (ids, tipos, cargador, reserva, cantidad y cooldown) para no molestar a los compañeros por un error propio; **A3 después** porque `POST /api/explosiones/validar` no detona ni modifica su base; **A2 al final** porque `POST /api/armas/{id}/disparar` sí aplica el disparo en el H2 de A2. Si A3 rechaza, se evita gastar munición ajena. Si A2 acepta y A3 rechaza, ese caso no ocurre: A3 ya habría cortado el flujo antes de llamar a A2.

## 1. Camino feliz — 201 (diagrama limpio)

```mermaid
sequenceDiagram
    actor Cliente as Insomnia (cliente)
    participant A1 as A1 (vricciardi, escenario)
    participant A3 as A3 (Ariel, validación HE)
    participant A2 as A2 (CS2 Armas, disparo)
    participant H2 as H2 de A1

    Cliente->>A1: POST /api/escenarios/ataque-coordinado
    A1->>A3: POST /api/explosiones/validar
    A3-->>A1: 200 explosión válida (sin efectos)
    A1->>A2: POST /api/armas/{id}/disparar
    A2-->>A1: 200 disparo aceptado
    A1->>A1: aplica su simulación local (disparo + recarga + lanzamiento)
    A1->>H2: INSERT en jugadas_cruzadas
    A1-->>Cliente: 201 jugada persistida (a2Codigo=200, a3Codigo=200)
```

## 2. Rechazo de A2 — 404

Ejemplo: `a2AtacanteId=999999` no existe en el catálogo de A2. A1 no escribe en su H2.

```mermaid
sequenceDiagram
    actor Cliente as Insomnia (cliente)
    participant A1 as A1 (vricciardi, escenario)
    participant A3 as A3 (Ariel, validación HE)
    participant A2 as A2 (CS2 Armas, disparo)
    participant H2 as H2 de A1

    Cliente->>A1: POST /api/escenarios/ataque-coordinado
    A1->>A3: POST /api/explosiones/validar
    A3-->>A1: 200 explosión válida
    A1->>A2: POST /api/armas/999999/disparar
    A2-->>A1: 404 id inexistente
    Note over A1,H2: no se llama a armaService.simular; H2 queda igual
    A1-->>Cliente: 404 {"origen":"A2","tipo":"RechazoCompaneroException"}
```

## 3. Rechazo de A3 — 409

Ejemplo: `a3GranadaId=6` ya está detonada en el servicio de Ariel. A2 no es llamado.

```mermaid
sequenceDiagram
    actor Cliente as Insomnia (cliente)
    participant A1 as A1 (vricciardi, escenario)
    participant A3 as A3 (Ariel, validación HE)
    participant A2 as A2 (CS2 Armas, disparo)
    participant H2 as H2 de A1

    Cliente->>A1: POST /api/escenarios/ataque-coordinado
    A1->>A3: POST /api/explosiones/validar
    A3-->>A1: 409 GranadaYaDetonadaException
    Note over A1,A2: A2 no es invocado
    Note over A1,H2: sin INSERT; H2 queda igual
    A1-->>Cliente: 409 {"origen":"A3","tipo":"RechazoCompaneroException"}
```

## 4. Regla propia de A1 — 409

La regla local falla (por ejemplo, la granada de A1 está agotada o el cargador está vacío). `ReglasJuego.validarSimulacion` falla antes de cualquier llamada remota, así que ninguna base ajena se modifica.

```mermaid
sequenceDiagram
    actor Cliente as Insomnia (cliente)
    participant A1 as A1 (vricciardi, escenario)
    participant A3 as A3 (Ariel, validación HE)
    participant A2 as A2 (CS2 Armas, disparo)
    participant H2 as H2 de A1

    Cliente->>A1: POST /api/escenarios/ataque-coordinado
    A1->>A1: ReglasJuego.validarSimulacion(...) lanza GranadaAgotadaException
    Note over A1,A2: A3 y A2 no son invocados
    Note over A1,H2: sin INSERT; H2 queda igual
    A1-->>Cliente: 409 {"tipo":"GranadaAgotadaException"}
```

## 5. Compañero no disponible — 502

El timeout de conexión y lectura se configura con `COMPANEROS_TIMEOUT` (5 s por defecto). No hay reintentos: el cuerpo conserva el `origen` y un `cuerpoCompanero` con `{"error":"A3 no disponible","detalle":"..."}`.

```mermaid
sequenceDiagram
    actor Cliente as Insomnia (cliente)
    participant A1 as A1 (vricciardi, escenario)
    participant A3 as A3 (Ariel, validación HE)
    participant H2 as H2 de A1

    Cliente->>A1: POST /api/escenarios/ataque-coordinado
    A1->>A3: POST /api/explosiones/validar
    A3--xA1: timeout / ResourceAccessException
    Note over A1,H2: H2 queda igual
    A1-->>Cliente: 502 {"origen":"A3","cuerpoCompanero":"{\"error\":\"A3 no disponible\"}"}
```

## Tabla de códigos

| Situación | Código en A1 | `origen` | ¿H2 de A1 cambia? |
|---|---:|---|---|
| A2 y A3 aceptan | 201 | — | Sí: nueva fila en `jugadas_cruzadas` |
| A2 rechaza (id inexistente) | 404 | A2 | No |
| A3 rechaza (HE detonada) | 409 | A3 | No |
| Regla local falla (`ErrorResponse`) | 400 o 409 | — (sin `origen`) | No: se valida antes de llamar |
| Compañero sin respuesta | 502 | A2 o A3 | No |
