package py.edu.uc.lp3.domain;

import java.util.Optional;

/**
 * Contrato común de todas las armas de CS2.
 *
 * - Todos los campos son private: nadie (ni las hijas) puede dejar un arma
 *   con precio negativo o nombre vacío.
 * - disparar() y mostrarEnTienda() son final: el ALGORITMO es el mismo para
 *   todas (Template Method); las hijas solo completan los pasos abstractos.
 */
public abstract class Arma {

    /** Distancia que se usa cuando se dispara sin indicar ninguna. */
    public static final double DISTANCIA_POR_DEFECTO = 10;

    private final String nombre;
    private final int precio;
    private final int danioBase;

    protected Arma(String nombre, int precio, int danioBase) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del arma es obligatorio");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (danioBase < 0) {
            throw new IllegalArgumentException("El daño base no puede ser negativo");
        }
        this.nombre = nombre;
        this.precio = precio;
        this.danioBase = danioBase;
    }

    /** Disparar (o lanzar, en granadas). El flujo no se puede pisar desde afuera. */
    public final ResultadoDisparo disparar(double distancia) {
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa");
        }
        Optional<String> bloqueo = motivoBloqueo();
        if (bloqueo.isPresent()) {
            return ResultadoDisparo.fallido(this, bloqueo.get());
        }
        consumirUso();
        return ResultadoDisparo.exitoso(this, calcularDanio(distancia), describirEfecto(distancia));
    }

    /**
     * SOBRECARGA de disparar(double): el mismo mensaje con otra lista de argumentos.
     * Disparar sin indicar distancia usa la distancia por defecto. No duplica el
     * algoritmo: delega en la versión con distancia, que es la que tiene el flujo.
     */
    public final ResultadoDisparo disparar() {
        return disparar(DISTANCIA_POR_DEFECTO);
    }

    public final FichaTienda mostrarEnTienda() {
        return new FichaTienda(nombre, getTipo(), precio, danioBase, detalleTienda());
    }

    // ---- Comportamiento público que cada rama define ----
    public abstract String recargar();

    public abstract String getTipo();

    /** Estado legible (munición, cooldown...). */
    public abstract String getEstado();

    // ---- Pasos del algoritmo: protected, solo para la jerarquía ----
    /** Vacío si el arma puede usarse; si no, el motivo. */
    protected abstract Optional<String> motivoBloqueo();

    protected abstract void consumirUso();

    protected abstract int calcularDanio(double distancia);

    protected abstract String describirEfecto(double distancia);

    protected abstract String detalleTienda();

    // ---- Getters de solo lectura (no hay setters) ----
    public final String getNombre() {
        return nombre;
    }

    public final int getPrecio() {
        return precio;
    }

    public final int getDanioBase() {
        return danioBase;
    }
}
