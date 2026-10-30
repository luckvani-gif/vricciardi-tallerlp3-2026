# Bitácora de asistencia de inteligencia artificial

Registro del uso de IA en este repositorio, exigido por la consigna del ejercicio POO-08.

## Herramienta y modelo

| Dato | Valor |
|---|---|
| Asistente / agente | **OpenCode** (opencode.ai), ejecutado en la terminal |
| Modelo exacto del LLM | **`opencode/big-pickle`** (id tal como lo muestra la herramienta) |
| Dónde se usó | Local, sobre el repositorio ya clonado |
| Cuándo | Octubre de 2026 |

## Qué hizo la IA

| Tarea | Qué se le pidió | Resultado |
|---|---|---|
| Persistencia JPA | Adaptar la jerarquía `Arma/ArmaDeFuego/Granada` a entidades con `SINGLE_TABLE`, discriminador, constructor vacío para JPA, validaciones `@PrePersist/@PreUpdate` | `Arma` como `@Entity` base, hijas anotadas, invariantes mantenidas |
| Repositorio | Crear repositorio Spring Data para la familia persistida con búsquedas | `ArmaRepository` con `JpaRepository<Arma,Long>` y `findByNombreContainingIgnoreCase` |
| CRUD REST | Implementar POST/GET/PUT/DELETE sobre `/api/armas`, disparar/recargar, endpoint para interfaz `Equipable` | `ArmaCrudController`, DTO `ArmaRequest`, servicio ampliado |
| Jerarquía de excepciones | Crear excepciones por tipo y handler unificado | `exceptions/*` + `ManejadorErrores` con `@ExceptionHandler` por cada tipo |
| Configuración H2 | Configurar datasource archivo, consola H2, tests con `application.properties` en memoria | `application.properties` actualizado, `src/test/resources/application.properties` |
| Colección Insomnia | Generar colección con CRUD, errores y endpoint de interfaz | `insomnia/CS2-CRUD-Insomnia.json` |
| Pruebas | Agregar tests de integración para CRUD, errores, simulación persistente | `ArmaCrudIntegrationTest` |
| Documentación | Actualizar README, ESPECIFICACIONES, BITACORA | Completados con respuestas al enunciado POO-08 |

## Resumen de los prompts

- "El CRUD sigue completo y se puede ver en H2. Tres clases del modelo participan. En Minecraft la simulación es de explosiones; en CS2, de daño o de carga de munición. Hay una jerarquía de excepciones y un handler por tipo. La colección de Insomnia cubre el caso feliz y cada error. El README nombra las dos reglas en una frase cada una. tengo que tener esto"
- El trabajo siguió las consignas del enunciado POO-08: persistencia H2, herencia persistida con dos hijas, interfaz de dominio, CRUD completo, excepciones por tipo, colección Insomnia.

## Qué NO hizo la IA

- El **modelado original** del dominio (jerarquía de armas/granadas, Template Method, sobrecarga/sobreescritura, polimorfismo en Inventario) fue desarrollado anteriormente y se mantuvo.
- Las **decisiones pedagógicas** (qué reglas mostrar, qué nombres usar) se ajustaron para cumplir la rúbrica POO-08 sin romper los tests existentes de POO-06.
- Se verificó con `./mvnw test` que los tests de sobrecarga/sobreescritura y los nuevos tests de CRUD pasaran.

Toda la bitácora se resume aquí; el historial de Git muestra los commits.
