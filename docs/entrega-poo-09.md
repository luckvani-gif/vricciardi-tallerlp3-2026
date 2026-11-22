# POO-09 — Enunciado aplicado a mi entrega

## Rol del grupo

| Rol | Estudiante | Servicio | OpenAPI |
|---|---|---|---|
| **A1 (yo)** | luckvani-gif (Vricciardi) | `http://100.95.213.93:8081` | `http://100.95.213.93:8081/v3/api-docs` |
| **A2** | compañero de CS2 Armas API | `https://desktop-hrm8vjo.tailde75e4.ts.net` | `https://desktop-hrm8vjo.tailde75e4.ts.net/v3/api-docs` |
| **A3** | Ariel Gaona | `https://ariel.tail26e794.ts.net` | `https://ariel.tail26e794.ts.net/v3/api-docs` |

Regla tomada de **A2**: `POST /api/armas/{id}/disparar` aplica su regla de daño y munición sobre un arma de su catálogo; responde 200 si el disparo es válido y 404/400 si el id o el estado no sirven.

Regla tomada de **A3**: `POST /api/explosiones/validar` aplica daño por radio y empuje de una granada HE **sin detonarla**; responde 200 si es válida y 409 si la granada ya explotó.

Regla propia de **A1** que se reutiliza: `ArmaService.simular(fuegoId, granadaId, distancia)` dispara, recarga con control de reserva y lanza la granada, todo en una transacción.

## Escenario en una frase

Un jugador ejecuta un ataque coordinado: A1 dispara y recarga con su propio rifle, pide a A3 que valide una explosión HE y a A2 que valide el disparo, y guarda la jugada en su H2 **sólo si los dos compañeros la aceptan**.

## Orden y persistencia

1. A3 se consulta primero con `POST /api/explosiones/validar` (no modifica su base).
2. A2 se consulta después con `POST /api/armas/{id}/disparar` (su regla sí escribe en su base).
3. Con los dos `200`, A1 ejecuta su simulación local y graba `jugadas_cruzadas` en una transacción.
4. Si A2 o A3 rechaza, A1 devuelve el código del compañero con `origen: "A2"` o `"A3"` y su H2 queda igual.

Los diagramas Mermaid de cada caso están en [`docs/POO-09-diagramas.md`](POO-09-diagramas.md).

## Cómo probarlo

```bash
./mvnw spring-boot:run
```

1. Abrir `http://localhost:8080/swagger-ui/index.html` (o `/v3/api-docs`) y verificar que aparezcan el CRUD, las dos reglas y `POST /api/escenarios/ataque-coordinado`.
2. Importar `insomnia/CS2-POO09-Insomnia.json` y seguir el orden de la carpeta `0. Preparación`: crear en A1 un rifle con munición y una granada, crear en A2 el arma atacante y el objetivo, y tomar de A3 el id de una HE sin detonar.
3. Ejecutar `POST Escenario feliz`: esperar **201** y luego `GET Jugadas guardadas`: debe aparecer una fila nueva; el rifle de A1 queda con la munición ya gastada y recargada.
4. Ejecutar `POST Rechazo de A2` (`a2AtacanteId=999999`): esperar **404** con `origen: "A2"` y volver a `GET Jugadas guardadas`: la cantidad de filas no cambia.
5. Ejecutar `POST Rechazo de A3` (`a3GranadaId` de una HE detonada): esperar **409** con `origen: "A3"`; la cantidad de filas sigue igual y A2 no es llamado.
6. Las URLs de A2 y A3 se pueden cambiar sin tocar el código con `A2_BASE_URL`, `A3_BASE_URL` y `COMPANEROS_TIMEOUT`.

## Enlace al commit de la solución

`PENDIENTE_DE_COMMIT` — reemplazar por:

`https://github.com/luckvani-gif/vricciardi-tallerlp3-2026/commit/<HASH_DEL_COMMIT>`
