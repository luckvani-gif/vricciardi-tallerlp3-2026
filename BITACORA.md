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
- Verificación final: `./mvnw test` ejecuta **26 pruebas**; `SobrecargaSobreescrituraTests`
  aporta **11**, y el diagrama Mermaid muestra los constructores simple/sobrecargado y
  las dos firmas de `disparar`/`dispararTodas`.

## Sin IA

El modelado original de las armas, la herencia `Arma`/`ArmaDeFuego`/`Granada`, los
constructores y las decisiones de diseño del dominio son del grupo. La IA se usó para
revisar el cumplimiento del enunciado, ordenar la documentación y proponer la
simulación y los handlers.
