# Especificaciones aplicadas a CS2 (POO-08)

## Datos

- **Estudiante:** luckvani-gif
- **Repositorio:** https://github.com/luckvani-gif/vricciardi-tallerlp3-2026
- **Commit de la solución:** `6a6e206d15a48cf74753f41d663c77e0d6737158`
- **Dominio:** Counter-Strike 2

## Objetivo

Servicio HTTP con Spring Boot que guarda estado en **H2**, con **tres clases
persistidas** (`Arma`, `ArmaDeFuego`, `Granada`), **CRUD REST completo**, **dos reglas
de negocio persistidas** (daño/consumo de munición y recarga con control de reserva),
**simulación que involucra las tres clases**, **jerarquía de excepciones** (base + dos
hijas) con **handler por tipo**, **interfaz de dominio `Equipable`** y **colección
Insomnia** versionada.

## URLs

| Método | URL | Descripción |
|---|---|---|
| GET | `/` | Estado del servicio y endpoints. |
| POST | `/api/armas` | Crear (discriminador `tipo`); 201 + `id`. |
| GET | `/api/armas` | Listar; filtros `nombre` y `precioMax`. |
| GET | `/api/armas/{id}` | Obtener; 404 si no existe. |
| PUT | `/api/armas/{id}` | Actualizar nombre/precio/daño; no cambia el tipo (409). |
| DELETE | `/api/armas/{id}` | Eliminar; 204 o 404. |
| POST | `/api/armas/{id}/disparar?distancia=10` | Regla 1: daño y consumo persistido. |
| POST | `/api/armas/{id}/recargar` | Regla 2: carga de munición persistida. |
| POST | `/api/armas/simular?fuegoId=1&granadaId=2&distancia=10` | Simulación con las tres clases. |
| GET | `/api/armas/equipables` | Endpoint de la interfaz `Equipable`. |

## Invariantes

- Nombre no vacío, precio >= 0, daño >= 0.
- `ArmaDeFuego`: cargador >= 1, balas entre 0 y capacidad, reserva >= 0.
- `Granada`: cooldown >= 0, radio > 0, cantidad >= 1 al construir y >= 0 al usar.
- `cargador` y `reserva` van juntos al crear.
- El JSON de un arma usa `tipo` con el código estable (`rifle`, `humo`, …) y
  `nombreTipo` con el nombre legible (`Rifle`, `Granada de humo`, …). El `tipo` del
  `GET` es el mismo que acepta el `PUT`.
- No se puede cambiar el tipo de un arma persistida con `PUT` → `CambioDeTipoException` (409).
- Disparar con cargador vacío → `CargadorVacioException` (409); sin granadas →
  `GranadaAgotadaException` (409); en cooldown → `GranadaEnCooldownException` (409).
- Recargar sin reserva → `SinReservaException` (409); con cargador lleno →
  `CargadorLlenoException` (409); una granada → `GranadaNoRecargableException` (409).
- Los conflictos de estado de la partida heredan de `ConflictoDePartidaException`, que es
  una rama de `ViolacionReglaException` y se traduce a 409.
- Id inexistente → `RecursoNoEncontradoException` (404).
- Datos inválidos → `IllegalArgumentException` o `SolicitudInvalidaException` (400).
- Tipo desconocido → `TipoNoSoportadoException` (400).

## Reglas de negocio

1. **Daño y consumo:** al disparar un arma de fuego se consume una bala y el daño depende
   de la distancia; una granada consume una unidad y respeta el cooldown.
2. **Carga de munición:** la recarga completa solo lo faltante desde la reserva; el
   cargador lleno o la reserva vacía son estados inválidos. Las granadas no se recargan.

La simulación `POST /api/armas/simular` dispara un `ArmaDeFuego`, la recarga y lanza una
`Granada`; es transaccional y deja el resultado persistido para un `GET` posterior.

## Cómo probarlo

```bash
./mvnw test
./mvnw spring-boot:run
```

- Importar `insomnia/CS2-CRUD-Insomnia.json` (el workspace declara
  `"scope": "collection"` y el `Base Environment` cuelga de él).
- Configurar en el entorno `base_url`, `tipo`, `arma_id`, `granada_id`,
  `arma_vacia_id` y `arma_llena_id`.
- Seguir el orden: crear rifle y granada, leer, actualizar, simular, ver el estado
  persistido, preparar los rifles de los conflictos, probar errores y eliminar.
- Abrir la consola H2 en `http://localhost:8080/h2-console` con
  `jdbc:h2:file:./data/cs2`, usuario `sa`, sin contraseña, y ver la tabla `ARMAS`.
