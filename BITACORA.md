# Bitácora de asistencia de inteligencia artificial

Registro del uso de IA en este repositorio, exigido por la consigna del ejercicio POO-06.

## Herramienta y modelo

| Dato | Valor |
|---|---|
| Asistente / agente | **OpenCode** (opencode.ai), ejecutado en la terminal y en VS Code |
| Modelo exacto del LLM | **`opencode/big-pickle`** (id tal como lo muestra la herramienta) |
| Dónde se usó | Local, sobre el repositorio ya clonado |
| Cuándo | Octubre de 2026 |

## Qué hizo la IA

| Tarea | Qué se le pidió | Resultado |
|---|---|---|
| Paquetes | Reordenar las clases siguiendo el árbol de `alefq/lp3-template-tp` | `py.edu.uc.lp3.vr.cs2.modelo/controller/servicio` → `py.edu.uc.lp3.domain`, `rest.controller`, `service` + `service.impl`, `constants` |
| Capa de servicios | Reemplazar la clase estática `FabricaArmas` por interfaz + implementación con inyección de Spring | `ArmaService`/`InventarioService` y sus `impl`, conectadas con `@Autowired` |
| Constantes | Sacar las rutas y los tipos de arma hardcodeados | `constants/ApiPaths.java` y `constants/TiposArma.java` |
| Sobrecarga | Agregar constructores simples y sobrecargados, y la sobrecarga del mensaje `disparar()` | 5 armas de fuego, 3 granadas, `Arma.disparar()` e `Inventario.dispararTodas()` |
| Pruebas | Cubrir la sobrecarga y la sobreescritura | `SobrecargaSobreescrituraTests` (9 pruebas) |
| Documentación | Escribir el apartado de sobrecarga y sobreescritura, y actualizar el diagrama | Sección del README |

## Resumen de los prompts

- "Organizá las clases del taller con la misma organización de paquetes que este template de LP3."
- "Pasá los controladores a la capa de servicios con interfaz e implementación, e inyectalos."
- "Las rutas y los tipos de arma tienen que estar en constantes, no escritos en el controller."
- "Falta la sobrecarga: agregá constructores simples y sobrecargados y un `disparar()` sin argumentos."
- "Escribí pruebas que dejen claro la diferencia entre sobrecarga y sobreescritura."
- "Agregá al README un apartado que explique qué se agregó con sobrecarga y qué con sobreescritura, y cómo se distinguen."

## Qué NO hizo la IA

- El **modelado** del dominio (`Arma`, `ArmaDeFuego`, `Granada`, `Pistola`, `Escopeta`, `Francotirador`,
  `Subfusil` y las granadas) es del alumno: se escribió antes de usar IA, en los commits
  `feat(cs2): jerarquía de armas` y `Endurece modelo`.
- Las **decisiones de diseño** (ocultamiento con atributos privados y sin setters, Template Method en
  `disparar()`, generalización de la munición y del cooldown, validación en los constructores) son
  del alumno.
- La IA no ejecutó el servicio ni decidió los valores de juego: verificó que lo existente siguiera
  funcionando igual y ejecutó las pruebas.

Toda la bitácora de prompts se resume aquí en frases propias; los archivos del repositorio son los
que se pueden revisar línea por línea en el historial de Git.