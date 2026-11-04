# Especificaciones aplicadas a CS2 (POO-08)

## Datos
- **Estudiante:** luckvani-gif
- **Repositorio:** https://github.com/luckvani-gif/vricciardi-tallerlp3-2026
- **Commit:** 3bc471ca3ed4687f52c7b9ad5dff7c8a9290ea40
- **Dominio:** Counter-Strike 2

## Objetivo
Servicio HTTP con Spring Boot que guarda estado en **H2**, con **tres clases persistidas** (Arma, ArmaDeFuego, Granada), **CRUD REST completo**, **dos reglas de negocio persistidas** (daño/consumo de munición y recarga con control de reserva), **jerarquía de excepciones** (base + dos hijas) con **handler por tipo**, **interfaz de dominio** `Equipable`, y **colección Insomnia** versionada.

## URLs
| Método | URL | Descripción |
|---|---|---|
| GET | `/` | Estado |
| POST | `/api/armas` | Crear |
| GET | `/api/armas` | Listar |
| GET | `/api/armas/{id}` | Obtener |
| PUT | `/api/armas/{id}` | Actualizar (sin cambiar tipo) |
| DELETE | `/api/armas/{id}` | Eliminar |
| POST | `/api/armas/{id}/disparar?distancia=10` | Simulación regla 1 |
| POST | `/api/armas/{id}/recargar` | Simulación regla 2 |
| GET | `/api/armas/equipables` | Interfaz Equipable |

## Invariantes
- Nombre no vacío, precio >= 0, daño >= 0.
- ArmaDeFuego: cargador >=1, balas 0..capacidad, reserva >=0.
- Granada: cooldown>=0, radio>0, cantidad>=1.
- `cargador` y `reserva` van juntos al crear.
- No se puede cambiar tipo con PUT.
- Disparar con cargador vacío o en cooldown → `ConflictoDeEstadoException` (409).
- Recargar sin reserva o cargador lleno → `ConflictoDeEstadoException` (409).
- Id inexistente → `RecursoNoEncontradoException` (404).
- Datos inválidos/tipo desconocido → `ViolacionReglaException` (400).

## Cómo probarlo
```bash
./mvnw spring-boot:run
./mvnw test
```
Importar `insomnia/CS2-CRUD-Insomnia.json`. Ver consola H2 en `/h2-console` con `jdbc:h2:file:./data/cs2`.
