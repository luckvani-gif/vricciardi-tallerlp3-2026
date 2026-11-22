# Bitácora de asistencia de inteligencia artificial

## Herramienta y modelo

| Dato | Valor |
|---|---|
| Asistente / agente | **OpenCode** (`opencode`, CLI) |
| Modelo exacto del LLM | **`opencode/big-pickle`** |
| Asistente / agente adicional | **Claude** |
| Modelo exacto del LLM adicional | **`claude-opus-5-5`** |
| Entorno | Local, repositorio `vricciardi-tallerlp3-2026` |
| Cuándo | Octubre y noviembre de 2026 |

## Resumen de prompts usados

1. «Revisá el enunciado POO-08 y la rúbrica, y decime qué falta para cumplirlos.»
2. «Completá la jerarquía de excepciones con base y dos hijas, y un handler HTTP por
   tipo; que el JSON permita distinguir a las hermanas.»
3. «Llevá las dos reglas de negocio al dominio: consumo de munición y recarga con
   control de reserva; el controller no debe improvisar reglas.»
4. «Agregá una simulación persistida que involucre `Arma`, `ArmaDeFuego` y `Granada`,
   y mostrala en Insomnia.»
5. «Actualizá la colección de Insomnia con variables de entorno, el CRUD, la simulación
   y un pedido por cada excepción.»
6. «Actualizá README, ESPECIFICACIONES y BITACORA; que el diagrama Mermaid y las
   respuestas coincidan con el código.»
7. «Ejecutá `./mvnw test` y corregí lo que falle.»
8. «Revisá la entrega contra la rúbrica: corregí la importación de Insomnia, el enlace
   del commit, el `tipo` del JSON y los nombres de las excepciones.»
9. «Leé el enunciado POO-09 y los grupos; decime quién es A1, A2 y A3 y qué regla conviene
   tomar de cada compañero.»
10. «Consultá los OpenAPI de A2 y A3 y sus datos vivos para elegir ids válidos y un id que
    rechace de forma determinista.»
11. «Agregá Springdoc a A1 y publicá el CRUD, las reglas y el escenario en /v3/api-docs.»
12. «Implementá el cliente HTTP a A2 y A3 con URLs por variables de entorno y timeout
    configurable.»
13. «Hacé que A1 consulte A3 primero y A2 después; si alguno rechaza, propagá su código y
    cuerpo, y no escribas nada en el H2 local.»
14. «Agregá la entidad JugadaCruzada y una transacción que persista recién cuando los dos
    compañeros acepten.»
15. «Escribí tests con clientes falsos para el happy path, el rechazo de A2 y el de A3;
    corregí los errores de compilación y de contexto que aparezcan.»
16. «Generá un diagrama Mermaid por caso, una colección Insomnia POO-09 y actualizá
    README y BITACORA con el enlace al commit final.»

## Uso concreto en este práctico

- Se reestructuró la jerarquía de excepciones a **base + dos hijas** con handler por tipo.
- Se documentaron las **dos reglas de negocio persistidas** en lenguaje de CS2.
- Se agregó la simulación `POST /api/armas/simular` con las tres clases.
- Se ampliaron README/ESPECIFICACIONES con diagrama, respuestas y colección.
- Se amplió la colección Insomnia con variables, simulación, filtros y excepciones.
- Se corrigió la importación de Insomnia (`scope: collection` y entorno colgado del
  workspace) y se agregaron pedidos deterministas para los conflictos de partida.
- Se unificó el `tipo` del JSON con el código estable y se agregó `nombreTipo`; ahora el
  `tipo` que devuelve el `GET` sirve para el `PUT`.
- Se reemplazó la excepción genérica por reglas con nombre de CS2
  (`CargadorVacioException`, `SinReservaException`, `CambioDeTipoException`, etc.).
- Se sumó **Springdoc 3.1.1** para publicar `/v3/api-docs` y Swagger UI en A1.
- Se externalizaron las URLs de A2/A3 y el timeout con `A2_BASE_URL`, `A3_BASE_URL` y
  `COMPANEROS_TIMEOUT`.
- Se decidió consultar **A3 primero** porque `POST /api/explosiones/validar` no modifica su
  base, y **A2 después** porque `POST /api/armas/{id}/disparar` sí escribe en su H2.
- Se creó `jugadas_cruzadas` con escritura transaccional: sólo se inserta cuando A2 y A3
  responden 200; ante un rechazo se propaga `origen`, `codigo` y `cuerpoCompanero`.
- Se corrigió el handler de `RestClient.onStatus` para la firma de Spring Framework 7
  (`(peticion, respuesta) -> ...`) y el `timeout` nulo en el contexto de tests.
- Se agregaron `EscenarioCruzadoTest` con clientes falsos, diagramas Mermaid por caso y la
  colección `insomnia/CS2-POO09-Insomnia.json` con preparación, happy path y rechazos.
- Verificación final: `./mvnw test` ejecuta **30 pruebas** con **BUILD SUCCESS**;
  `SobrecargaSobreescrituraTests` aporta **11**, `EscenarioCruzadoTest` aporta **4** y el
  diagrama Mermaid de POO-08 muestra los constructores simple/sobrecargado y las dos firmas
  de `disparar`/`dispararTodas`.
- Commit de la solución POO-09: `91179e0ba41f336421a02b21e61efc6a8ba08b13`.

## Sin IA

El modelado original de las armas, la herencia `Arma`/`ArmaDeFuego`/`Granada`, los
constructores, las decisiones de diseño del dominio y la elección del escenario de POO-09
son del grupo. La IA se usó para revisar el cumplimiento de los enunciados, consultar los
OpenAPI de A2 y A3, ordenar la documentación y proponer la simulación, los handlers, el
cliente HTTP, las pruebas y los diagramas.
