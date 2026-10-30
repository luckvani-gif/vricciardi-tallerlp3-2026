# Especificaciones aplicadas al dominio (POO-08)

## Datos

- **Estudiante:** [luckvani-gif](https://github.com/luckvani-gif)
- **Repositorio:** `https://github.com/luckvani-gif/vricciardi-tallerlp3-2026`
- **Commit de la solución:** `1f6c59fefbcd15e4a3a7c436b1374ba828784545`
- **Rama publicada:** `main`
- **Dominio elegido:** Counter-Strike 2 (CS2)

## Objetivo

Publicar un servicio HTTP con Spring Boot sobre el modelado de Counter-Strike 2 con **persistencia en H2**, **CRUD REST completo**, **herencia persistida** (clase base + dos clases hijas), **interfaz de dominio**, **jerarquía de excepciones con handler por tipo**, **colección Insomnia** versionada y reutilización de los mecanismos de POO-06 (sobrecarga y sobreescritura).

## Consignas aplicadas

1. **Paquetes**: `domain`, `rest.controller`, `service` + `service.impl`, `repository`, `exceptions`, `constants`, `rest.dto` siguiendo el template.
2. **Persistencia**: `spring-boot-starter-data-jpa` + H2. `Arma` es `@Entity` con `@Inheritance(SINGLE_TABLE)`, `@DiscriminatorColumn("tipo_arma")`. Constructor vacío `protected` para JPA; constructores de negocio mantienen ocultamiento e invariantes. `@PrePersist/@PreUpdate` validan antes de guardar.
3. **Herencia**: `Arma` (base) → `ArmaDeFuego` y `Granada` (dos hijas independientes). Cinco armas de fuego y cuatro granadas. Método abstracto existe y se sobreescribe.
4. **Interfaz de dominio**: `Equipable` con `ranura()` y `mensajeDeEquipado()`. Implementado por `Rifle` y `GranadaHumo`. Endpoint `GET /api/armas/equipables` usa el tipo `Equipable` (polimorfismo entre familias).
5. **Repositorio**: `ArmaRepository extends JpaRepository<Arma,Long>` con `findByNombreContainingIgnoreCase`. Interfaz: Spring Data genera implementación. Controller/servicio dependen de la interfaz.
6. **CRUD REST** (`/api/armas`):
   - `POST` → crea, guarda con `id`, responde `201`
   - `GET` → lista desde repositorio; `GET /{id}` devuelve `404` si no existe
   - `PUT` → reemplaza datos; rechaza cambio de tipo con `409`
   - `DELETE` → borra, responde `204`; `404` si no existe
   - `POST /{id}/disparar` y `/recargar` modifican estado persistido
7. **Errores**: jerarquía `ApiException` con subclases; `ManejadorErrores` tiene handler por tipo → `400/404/409`. `IllegalArgumentException` también mapeado a `400`. `POST` ilegal no persiste filas inválidas.
8. **Sobrecarga y sobreescritura**: se mantienen. `disparar()` sin argumentos sobrecarga `disparar(double)`. Constructores simples/sobrecargados con delegación.
9. **Colección Insomnia**: `insomnia/CS2-CRUD-Insomnia.json` con entorno, un pedido por verbo, casos de error, endpoint de interfaz.
10. **Pruebas**: `ArmaCrudIntegrationTest` cubre CRUD, errores por tipo, simulación persistente, endpoint de interfaz. Todos pasan.

## Cómo probarlo

```bash
cd vricciardi-tallerlp3-2026
./mvnw -q compile
./mvnw spring-boot:run
```

Servicio en `http://localhost:8080`. Consola H2: `http://localhost:8080/h2-console` (URL `jdbc:h2:file:./data/cs2`).

### Pruebas manuales (CRUD)
```bash
# Crear
curl -X POST http://localhost:8080/api/armas -H 'Content-Type: application/json' \
  -d '{"tipo":"rifle","nombre":"AK-47","precio":2700,"danio":36,"cargador":30,"reserva":90}'

# Listar / leer
curl http://localhost:8080/api/armas
curl http://localhost:8080/api/armas/1

# Actualizar
curl -X PUT http://localhost:8080/api/armas/1 -H 'Content-Type: application/json' \
  -d '{"tipo":"rifle","nombre":"AK-47 Redline","precio":3000,"danio":40}'

# Simular
curl -X POST http://localhost:8080/api/armas/1/disparar?distancia=10
curl -X POST http://localhost:8080/api/armas/1/recargar

# Interfaz Equipable
curl http://localhost:8080/api/armas/equipables

# Errores
curl -X POST http://localhost:8080/api/armas -H 'Content-Type: application/json' -d '{"tipo":"rifle","precio":-5,"danio":36}'
curl http://localhost:8080/api/armas/999999
curl -X DELETE http://localhost:8080/api/armas/999999
curl -X PUT http://localhost:8080/api/armas/1 -H 'Content-Type: application/json' -d '{"tipo":"humo","nombre":"X","precio":1,"danio":0}'

# Borrar
curl -X DELETE http://localhost:8080/api/armas/1
```

### Pruebas automatizadas
```bash
./mvnw test
```
Esperado: `BUILD SUCCESS` con 21 tests.

## Archivos relevantes
- **Dominio/entidades**: `src/main/java/py/edu/uc/lp3/domain/` (Arma, ArmaDeFuego, Granada, hijas, Equipable)
- **Persistencia**: `src/main/java/py/edu/uc/lp3/repository/ArmaRepository.java`
- **Excepciones**: `src/main/java/py/edu/uc/lp3/exceptions/`
- **Servicios**: `src/main/java/py/edu/uc/lp3/service/ArmaService.java`, `src/main/java/py/edu/uc/lp3/service/impl/ArmaServiceImpl.java`
- **REST**: `src/main/java/py/edu/uc/lp3/rest/controller/ArmaCrudController.java`, `ManejadorErrores.java`, `IndexController.java`
- **Config**: `src/main/resources/application.properties`, `pom.xml`
- **Pruebas**: `src/test/java/py/edu/uc/lp3/tp2026/ArmaCrudIntegrationTest.java`, `SobrecargaSobreescrituraTests.java`
- **Insomnia**: `insomnia/CS2-CRUD-Insomnia.json`
- **Docs**: `README.md`, `BITACORA.md`, `ESPECIFICACIONES.md`

## Preguntas de anclaje (respuestas)

- **Recurso/verbo**: Recurso `armas` en `/api/armas`. Verbo HTTP indica acción (POST crear, GET leer, PUT actualizar, DELETE borrar). No se usan verbos en la URL.
- **Identidad**: Clave `id` generado por JPA (`@GeneratedValue`). El JSON de creación omite `id`.
- **Herencia/tablas**: `SINGLE_TABLE` con `tipo_arma` discriminador. Permite polimorfismo con una sola tabla. El tipo se ve en el discriminador/serialización.
- **Interfaz/repositorio**: `ArmaRepository` es interfaz (desacoplamiento, fácil cambio de implementación). `Equipable` es protocolo del dominio usado por endpoint.
- **Ocultamiento**: validación en constructores, `@PrePersist/@PreUpdate`, servicio/controller delegan al dominio. `POST` inválido devuelve `400` y no persiste.
- **PUT/PATCH**: Se usa **PUT** para reemplazar datos editables; no se permite cambiar `tipo` (409). Campos no enviados → validación falla o se mantienen según lógica.
- **DELETE**: Borrado **físico** con `delete(Arma)`; responde `204`. `GET` posterior devuelve `404`.

## Referencias
- Enunciado POO-08: [ejercicio-poo-08-crud-h2-insomnia-2026-10-28.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/ejercicio-poo-08-crud-h2-insomnia-2026-10-28.md)
- Enunciado POO-06: [ejercicio-poo-06-revision-paquetes-constructores-2026-09-30.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/ejercicio-poo-06-revision-paquetes-constructores-2026-09-30.md)
- Rúbrica: [RUBRICA-ejercicios-lp3-2026.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/RUBRICA-ejercicios-lp3-2026.md)
- Template: [lp3-template-tp](https://github.com/alefq/lp3-template-tp/tree/main/src/main/java/py/edu/uc/lp3)
