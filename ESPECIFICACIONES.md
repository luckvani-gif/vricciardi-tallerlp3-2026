# Especificaciones del ejercicio POO-06

## Datos

- **Estudiante:** [luckvani-gif](https://github.com/luckvani-gif)
- **Repositorio:** `https://github.com/luckvani-gif/vricciardi-tallerlp3-2026`
- **Commit de la solución:** `https://github.com/luckvani-gif/vricciardi-tallerlp3-2026/commit/532c5f43c4d1902e3163c2820848d212333ba09f`
- **Rama publicada:** `vr-contribucion-lp3`
- **Dominio elegido:** Counter-Strike 2 (CS2)

## Objetivo

Publicar un servicio HTTP con Spring Boot, sobre el modelado de Counter-Strike 2. Se aplican herencia, sobreescritura, ocultamiento de la información, paquetes con sentido, constructores simples y sobrecargados, y sobrecarga de al menos un mensaje del dominio. El servicio debe arrancar con `./mvnw spring-boot:run` y poder usarse por HTTP, sin ejecutar un `main()` desde el IDE.

## Consignas aplicadas al dominio

1. **Paquetes**: las clases siguen el template de [lp3-template-tp](https://github.com/alefq/lp3-template-tp/tree/main/src/main/java/py/edu/uc/lp3). El proyecto está organizado en `py.edu.uc.lp3.domain`, `py.edu.uc.lp3.rest.controller`, `py.edu.uc.lp3.service` + `service.impl` y `py.edu.uc.lp3.constants`.
2. **Modelado**: se mantiene la jerarquía completa del modelo (`Arma`, `ArmaDeFuego`, `Granada`, las 5 armas de fuego y las 3 granadas), con atributos privados, sin setters y validaciones en los constructores.
3. **Sobreescritura**: las clases hijas **sobreescriben** los métodos abstractos definidos en el padre (`getTipo()`, `getEstado()`, `recargar()`, `describirEfecto(double)`, `detalleTienda()`, `motivoBloqueo()`, `consumirUso()`, `calcularDanio(double)`). `ArmaDeFuego` deja `final` parte del flujo y agrega `factorDistancia(double)`, que cada arma de fuego sobreescribe con su propia caída de daño.
4. **Servicios REST**:
   - `IndexController` en `GET /` informa el estado del servicio y lista los endpoints disponibles.
   - `ArmaController` construye armas desde la URL (`/armas/{tipo}` y `/armas/{tipo}/disparar`).
   - `InventarioController` arma un inventario completo (`/inventario` y `/inventario/disparar`) y muestra el comportamiento de cada clase hija polimórficamente.
5. **Construcción por URL**: los controllers reciben parámetros vía `@RequestParam` (`nombre`, `precio`, `danio`, `cargador`, `reserva`, `distancia`, `veces`). `distancia` es **opcional** para permitir la sobrecarga del mensaje. Si los valores rompen una regla, `ManejadorErrores` traduce `IllegalArgumentException` a **400 Bad Request**.
6. **Constructores simples y sobrecargados**:
   - Armas de fuego: constructor simple (3 parámetros: nombre, precio, daño) y constructor sobrecargado (5 parámetros: +cargador y reserva). El simple delega en el sobrecargado con `this(...)`.
   - Granadas: constructor simple (nombre, precio —y daño en el caso de incendiaria) y constructor sobrecargado con `cantidad`. Delegación con `this(...)`.
7. **Sobrecarga del mensaje del dominio**: `Arma.disparar()` (sin argumentos) **sobrecarga** a `Arma.disparar(double distancia)`. Usa `DISTANCIA_POR_DEFECTO = 10` y delega en la versión con distancia. `Inventario.dispararTodas()` sobrecarga a `Inventario.dispararTodas(double distancia)`, delegando también en la versión con argumento.
8. **Visibilidad y polimorfismo**: todos los atributos son `private`. `Inventario` trabaja con `List<Arma>` y no usa `if` ni `instanceof`: el comportamiento sale de los métodos sobreescritos. `ArmaServiceImpl` es el único punto donde se elige el tipo concreto.
9. **Pruebas**: `Tp2026ApplicationTests` verifica que la aplicación levanta. `SobrecargaSobreescrituraTests` (11 pruebas) cubre constructores simples/sobrecargados, sobrecarga de mensajes, sobreescritura, polimorfismo e invariantes. Todos pasan (`mvn test` → BUILD SUCCESS, 12 tests).

## Cómo probarlo

Requisitos: Java 21 y Maven (se usa el wrapper incluido).

```bash
cd vricciardi-tallerlp3-2026
./mvnw -q compile
./mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8080`.

### Pruebas manuales

```bash
# Servicio vivo (index)
curl "http://localhost:8080/"

# Construir un arma por URL (constructor simple del dominio)
curl "http://localhost:8080/armas/pistola"
curl "http://localhost:8080/armas/rifle"

# Construir con parámetros sobrecargados (cargador/reserva)
curl "http://localhost:8080/armas/pistola?cargador=5&reserva=10"

# Disparar sin distancia (SOBRECARGA: usa distancia por defecto 10 m)
curl "http://localhost:8080/armas/rifle/disparar?veces=2"

# Disparar con distancia (versión con argumento)
curl "http://localhost:8080/armas/rifle/disparar?distancia=60&veces=2"

# Inventario completo (polimorfismo: una clase hija por fila)
curl "http://localhost:8080/inventario"

# Disparar todo el inventario (sobrecarga sin distancia)
curl "http://localhost:8080/inventario/disparar?veces=1"

# Disparar todo el inventario con distancia
curl "http://localhost:8080/inventario/disparar?distancia=5&veces=1"

# Validación: parámetros inválidos -> 400 Bad Request
curl "http://localhost:8080/armas/pistola?precio=-5"
curl "http://localhost:8080/armas/pistola?cargador=5"        # faltan cargador y reserva juntos
curl "http://localhost:8080/armas/lanzador?veces=1"
curl "http://localhost:8080/armas/rifle/disparar?veces=0"
```

### Ejecución de pruebas automatizadas

```bash
./mvnw test
```

Resultado esperado: `BUILD SUCCESS` con 12 tests en total (1 de `Tp2026ApplicationTests` + 11 de `SobrecargaSobreescrituraTests`).

## Archivos relevantes

- **Código fuente:** `src/main/java/py/edu/uc/lp3/` — `domain`, `rest.controller`, `service` + `service.impl`, `constants`, `Application.java`
- **Pruebas:** `src/test/java/py/edu/uc/lp3/tp2026/Tp2026ApplicationTests.java`, `SobrecargaSobreescrituraTests.java`
- **Documentación:** `README.md` (organización, endpoints, diagrama Mermaid, apartado sobrecarga y sobreescritura), `BITACORA.md` (asistente, modelo LLM, resumen de prompts), `LICENSE` (Apache 2.0)

## Referencias

- Enunciado: [ejercicio-poo-06-revision-paquetes-constructores-2026-09-30.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/ejercicio-poo-06-revision-paquetes-constructores-2026-09-30.md)
- Rúbrica: [RUBRICA-ejercicios-lp3-2026.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/RUBRICA-ejercicios-lp3-2026.md)
- Template: [lp3-template-tp](https://github.com/alefq/lp3-template-tp/tree/main/src/main/java/py/edu/uc/lp3)