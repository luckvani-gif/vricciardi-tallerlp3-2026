# POO-09 — Enunciado aplicado a mi entrega

## Rol del grupo

| Rol | Estudiante | Servicio | OpenAPI |
|---|---|---|---|
| **A1 (yo)** | Vanessa Ricciardi | `http://100.95.213.93:8081` | `http://100.95.213.93:8081/v3/api-docs` |
| **A2** | Mauricio Pecci | `https://desktop-hrm8vjo.tailde75e4.ts.net` | `https://desktop-hrm8vjo.tailde75e4.ts.net/v3/api-docs` |
| **A3** | Ariel Gaona | `https://ariel.tail26e794.ts.net` | `https://ariel.tail26e794.ts.net/v3/api-docs` |

Observación: Usamos Tailscale porque los servicios corren en las PCs de cada estudiante, detrás de routers con NAT y sin IP pública. Tailscale crea una red privada cifrada (WireGuard) con IPs y nombres estables, y permite que A1, A2 y A3 se consuman entre sí sin abrir puertos ni exponer los servicios a internet.

Regla tomada de **A2**: `POST /api/armas/{id}/disparar` aplica su regla de daño y munición sobre un arma de su catálogo; responde 200 si el disparo es válido y 404/400 si el id o el estado no sirven.

Regla tomada de **A3**: `POST /api/explosiones/validar` aplica daño por radio y empuje de una granada HE **sin detonarla**; responde 200 si es válida y 409 si la granada ya explotó.

Regla propia de **A1** que se reutiliza: `ArmaService.simular(fuegoId, granadaId, distancia)` dispara, recarga con control de reserva y lanza la granada, todo en una transacción.

## Escenario en una frase

Un jugador ejecuta un ataque coordinado: A1 dispara y recarga con su propio rifle, pide a A3 que valide una explosión HE y a A2 que valide el disparo, y guarda la jugada en su H2 **solo si los dos compañeros la aceptan**.

## Orden y persistencia

0. A1 valida primero sus propios ids, tipos, cargador, reserva, cantidad y cooldown en `ReglasJuego`. Si una regla local falla, no llama a A2 ni a A3.
1. A3 se consulta después con `POST /api/explosiones/validar` (no modifica su base).
2. A2 se consulta luego con `POST /api/armas/{id}/disparar` (su regla sí escribe en su base; A1 no puede revertir la base ajena).
3. Con los dos `200`, A1 ejecuta su simulación local y graba `jugadas_cruzadas` en una transacción.
4. Si A2 o A3 rechaza, A1 traduce el rechazo a `RechazoCompaneroException`, devuelve el código del compañero con `origen: "A2"` o `"A3"` y su H2 queda igual. Con este orden, el caso "A2 acepta y A3 rechaza" no se da: A3 se consulta antes y su rechazo corta el flujo.
5. Si un compañero no responde en `COMPANEROS_TIMEOUT` (5 s por defecto), no hay reintentos: A1 responde 502 con `origen` del compañero y no guarda nada.

Los diagramas Mermaid de cada caso están en [`docs/POO-09-diagramas.md`](POO-09-diagramas.md).

## Cómo probarlo

```bash
./mvnw spring-boot:run
```

1. Arrancar primero A2 y A3 (verificar sus `/v3/api-docs`) y después A1. A1 escucha en `http://localhost:8081`.
2. Abrir `http://localhost:8081/swagger-ui.html` (o `/v3/api-docs`) y verificar que aparezcan el CRUD, las dos reglas y `POST /api/escenarios/ataque-coordinado`.
3. Importar `insomnia/CS2-POO09-Insomnia.json` y seguir el orden de la carpeta `1. Preparación de datos`: crear en A1 un rifle con munición y una granada, crear en A2 el arma atacante y el objetivo, y tomar de A3 el id de una HE sin detonar (13 en los datos de referencia).
4. Ejecutar `POST Escenario feliz`: esperar **201** y luego `GET Jugadas guardadas`: debe aparecer una fila nueva; el rifle de A1 queda con la munición ya gastada y recargada.
5. Ejecutar `POST Rechazo de A2` (`a2AtacanteId=999999`): esperar **404** con `origen: "A2"` y volver a `GET Jugadas guardadas`: la cantidad de filas no cambia.
6. Ejecutar `POST Rechazo de A3` (`a3GranadaId=6`, si esa HE sigue detonada en la base de Ariel; si cambió, buscar otra HE detonada con `GET /api/armas` de A3): esperar **409** con `origen: "A3"`; la cantidad de filas sigue igual y A2 no es llamado.
7. Las URLs de A2 y A3 se pueden cambiar sin tocar el código con `A2_BASE_URL`, `A3_BASE_URL` y `COMPANEROS_TIMEOUT`.

## Enlace al commit de la solución

[`da94f4641b676abd4c40123f904f43b1db6ca8d4`](https://github.com/luckvani-gif/vricciardi-tallerlp3-2026/commit/da94f4641b676abd4c40123f904f43b1db6ca8d4) — último commit de la entrega (`git rev-parse HEAD`).

Si se vuelve a enviar la tarea después de las correcciones, reemplazar este hash por el del
último commit (`git rev-parse HEAD`) también en Classroom.
