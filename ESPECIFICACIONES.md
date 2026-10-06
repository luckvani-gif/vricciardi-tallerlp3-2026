# Especificaciones del ejercicio POO-06

## Datos

- **Estudiante:** [luckvani-gif](https://github.com/luckvani-gif)
- **Repositorio:** `https://github.com/GioMonteggia/INICIALES-taller-git-2026`
- **Commit de la solución:** `https://github.com/GioMonteggia/INICIALES-taller-git-2026/commit/bfba2c39ffe6efca430e664703ba12a52724666e`
- **Rama publicada:** `INICIALES-contribucion-lp3`
- **Dominio elegido:** Counter-Strike 2 (CS2)

## Objetivo

Publicar un servicio HTTP con Spring Boot, sobre el modelado de Counter-Strike 2. Se aplican herencia, sobreescritura, ocultamiento de la información, paquetes con sentido, constructores simples y sobrecargados, y sobrecarga de al menos un mensaje del dominio. El servicio debe arrancar con `./mvnw spring-boot:run` y poder usarse por HTTP, sin ejecutar un `main()` desde el IDE.

## Consignas aplicadas al dominio

1. **Paquetes**: las clases siguen el template de [lp3-template-tp](https://github.com/alefq/lp3-template-tp/tree/main/src/main/java/py/edu/uc/lp3). El aporte no modifica archivos existentes: se agregan clases nuevas en `com.gio.cs2api.model`, `com.gio.cs2api.controller` y sus tests.
2. **Modelado**: se agrega la especialización que faltaba en la jerarquía: **`Rifle`** (`com.gio.cs2api.model.Rifle`), que hereda de `Arma`.
3. **Sobreescritura**: `Rifle` **sobreescribe** `disparar()` con su propia implementación (devuelve un mensaje descriptivo del disparo). Además, agrega `disparar(int distancia)` para ofrecer otra versión del comportamiento.
4. **Servicios REST**:
   - El proyecto ya cuenta con `GET /` que confirma que el servicio está vivo (`ArmaController`).
   - Se agrega `RifleController` con rutas propias: `GET /rifles/{nombre}` (construye y devuelve la ficha del rifle en JSON) y `GET /rifles/{nombre}/disparar` (construye el rifle y devuelve los disparos en JSON).
5. **Construcción por URL**: el controller recibe parámetros vía `@RequestParam` (`precio`, `dano`, `cargador`, `distancia`, `veces`). Los valores alimentan los constructores o el mensaje de disparo. Si un valor rompe alguna regla, se devuelve un error con código **400**.
6. **Constructores simples y sobrecargados**:
   - Constructor simple: `Rifle(String nombre, float precio)` — usa valores por defecto (daño 36, cargador 30).
   - Constructor sobrecargado: `Rifle(String nombre, float precio, int dano, int cargador)` — el llamador decide el daño y el cargador. Ambos validan los invariantes (cargador > 0, daño > 0).
7. **Sobrecarga del mensaje del dominio**: el método `disparar()` está **sobrecargado**. `disparar()` usa un alcance por defecto (100 m) y delega en la versión con distancia; `disparar(int distancia)` atenúa el daño con la distancia y **nunca baja de la mitad** del daño base.
8. **Visibilidad y polimorfismo**: atributos privados, sin setters, validación en constructores. El controller trata al objeto como `Rifle` (tipo concreto de la especialización agregada), sin romper el encapsulamiento del dominio.
9. **Pruebas**: se agregan `com.gio.cs2api.model.RifleTests` (5 pruebas) que verifican constructores simples/sobrecargados, invariantes, sobrecarga y la atenuación con la distancia.

## Cómo probarlo

Requisitos: Java 21 y Maven (se usa el wrapper incluido).

```bash
cd INICIALES-taller-git-2026
./mvnw -q compile
./mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8080`.

### Pruebas manuales

```bash
# Servicio vivo (ya existente)
curl "http://localhost:8080/"

# Ficha del rifle (constructor simple)
curl "http://localhost:8080/rifles/AK-47"

# Disparos sin distancia (sobrecarga: usa valor por defecto 100 m)
curl "http://localhost:8080/rifles/AK-47/disparar?veces=2"

# Disparos con distancia (versión sobrecargada con argumento)
curl "http://localhost:8080/rifles/M4A4/disparar?cargador=10&distancia=20&veces=3"

# Validación: cargador inválido -> 400 Bad Request
curl "http://localhost:8080/rifles/AK-47?cargador=0"

# Validación: veces inválido -> 400 Bad Request
curl "http://localhost:8080/rifles/AK-47/disparar?veces=0"

# Verifica que el endpoint ya existente sigue funcionando
curl "http://localhost:8080/armas/pistola"
```

### Ejecución de pruebas automatizadas

```bash
./mvnw test
```

Resultado esperado: `BUILD SUCCESS` con 6 tests en total (1 del contexto de Spring + 5 de `RifleTests`).

## Archivos agregados

- `src/main/java/com/gio/cs2api/model/Rifle.java` — Especialización `Rifle extends Arma`. Constructores simple y sobrecargado. Sobrecarga `disparar()` / `disparar(int distancia)`. Validaciones de invariantes.
- `src/main/java/com/gio/cs2api/controller/RifleController.java` — Endpoints `/rifles/**`. Construye instancias vía URL, devuelve JSON y traduce validaciones a `400 Bad Request`.
- `src/test/java/com/gio/cs2api/model/RifleTests.java` — Pruebas que demuestran sobrecarga, sobreescritura y que ninguna firma deja al objeto en estado inválido.

**Nota:** No se modificó ningún archivo existente. El aporte son exclusivamente 3 archivos nuevos.