package py.edu.uc.lp3.domain;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Contrato común de todas las armas de CS2.
 *
 * - Todos los campos son private: nadie (ni las hijas) puede dejar un arma
 *   con precio negativo o nombre vacío.
 * - disparar() y mostrarEnTienda() son final: el ALGORITMO es el mismo para
 *   todas (Template Method); las hijas solo completan los pasos abstractos.
 * - La familia se persiste en una sola tabla (SINGLE_TABLE) con un
 *   discriminador; JPA usa el constructor vacío y valida antes de guardar.
 */
@Entity
@Table(name = "armas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_arma", discriminatorType = DiscriminatorType.STRING)
public abstract class Arma {

    /** Distancia que se usa cuando se dispara sin indicar ninguna. */
    public static final double DISTANCIA_POR_DEFECTO = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int precio;

    @Column(nullable = false)
    private int danioBase;

    /** Constructor vacío para JPA. No es público: el dominio se crea con los constructores de negocio. */
    protected Arma() {
    }

    protected Arma(String nombre, int precio, int danioBase) {
        validarDatos(nombre, precio, danioBase);
        this.nombre = nombre;
        this.precio = precio;
        this.danioBase = danioBase;
    }

    private static void validarDatos(String nombre, int precio, int danioBase) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del arma es obligatorio");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (danioBase < 0) {
            throw new IllegalArgumentException("El daño base no puede ser negativo");
        }
    }

    /**
     * Regla de ocultamiento: una fila no se guarda si el objeto quedó en un
     * estado imposible, aunque alguien intente saltear los constructores.
     */
    @PrePersist
    @PreUpdate
    private void validarAntesDeGuardar() {
        validarDatos(nombre, precio, danioBase);
    }

    /** PUT reemplaza solo los datos editables; el id y la munición no se tocan. */
    public final void actualizarDatos(String nombre, int precio, int danioBase) {
        validarDatos(nombre, precio, danioBase);
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

    /**
     * ¿Esta rama de la familia admite recarga? La regla vive en el dominio:
     * las armas de fuego sí, las granadas no. El servicio no decide esto.
     */
    public boolean esRecargable() {
        return false;
    }

    /**
     * Código estable que viaja en el JSON como "tipo" (rifle, humo, ...).
     * Es el mismo valor que acepta el POST/PUT, así copiar el tipo del GET
     * no provoca un 409 al actualizar.
     */
    @JsonProperty("tipo")
    public final String getCodigoTipo() {
        Class<?> clase = getClass();
        while (clase != null) {
            DiscriminatorValue valor = clase.getAnnotation(DiscriminatorValue.class);
            if (valor != null) {
                return valor.value();
            }
            clase = clase.getSuperclass();
        }
        return getClass().getSimpleName().toLowerCase();
    }

    /** Nombre legible de la familia para la tienda y la documentación. */
    @JsonProperty("nombreTipo")
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
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public int getDanioBase() {
        return danioBase;
    }
}
