package py.edu.uc.lp3.domain;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import py.edu.uc.lp3.exceptions.ConflictoDeEstadoException;

/**
 * Las granadas no tienen cargador: se controlan por cantidad y cooldown.
 * Ese control es final y private: desde afuera no se puede saltear el cooldown.
 */
@Entity
@DiscriminatorValue("granada")
public abstract class Granada extends Arma {

    @Column
    private Long cooldownMs;

    @Column
    private Double radio;

    @Column
    private Integer cantidad;

    private long ultimoLanzamientoMs = -1;

    /** Constructor vacío para JPA. */
    protected Granada() {
    }

    /** Constructor simple: cooldown, radio y cantidad de fábrica. */
    protected Granada(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, 3000, 5, 1);
    }

    /** Constructor sobrecargado: el llamador decide cooldown, radio y cantidad. */
    protected Granada(String nombre, int precio, int danioBase,
                      long cooldownMs, double radio, int cantidad) {
        super(nombre, precio, danioBase);
        if (cooldownMs < 0) {
            throw new IllegalArgumentException("El cooldown no puede ser negativo");
        }
        if (radio <= 0) {
            throw new IllegalArgumentException("El radio debe ser positivo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Debe haber al menos una granada");
        }
        this.cooldownMs = cooldownMs;
        this.radio = radio;
        this.cantidad = cantidad;
    }

    @PrePersist
    @PreUpdate
    private void validarGranadaAntesDeGuardar() {
        if (cooldownMs == null || cooldownMs < 0) {
            throw new IllegalArgumentException("El cooldown no puede ser negativo");
        }
        if (radio == null || radio <= 0) {
            throw new IllegalArgumentException("El radio debe ser positivo");
        }
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de granadas no puede ser negativa");
        }
        if (ultimoLanzamientoMs < -1) {
            throw new IllegalArgumentException("El último lanzamiento no es válido");
        }
        // cantidad == 0 es legal después de lanzar la última granada. El mínimo
        // de 1 se exige al construir, no al persistir el estado ya usado.
    }

    @Override
    protected final Optional<String> motivoBloqueo() {
        if (cantidad == 0) {
            return Optional.of("No quedan granadas");
        }
        long restante = cooldownRestanteMs();
        if (restante > 0) {
            return Optional.of("En cooldown: faltan " + restante + " ms");
        }
        return Optional.empty();
    }

    @Override
    protected final void consumirUso() {
        cantidad--;
        ultimoLanzamientoMs = System.currentTimeMillis();
    }

    /** Las granadas no son recargables: la regla la sostiene el dominio. */
    @Override
    public final String recargar() {
        throw new ConflictoDeEstadoException("Las granadas no se recargan: hay que comprar otra");
    }

    /** Daño en área: máximo en el centro, 0 fuera del radio. */
    @Override
    protected int calcularDanio(double distancia) {
        if (distancia > radio) {
            return 0;
        }
        return (int) Math.round(getDanioBase() * (1 - distancia / radio));
    }

    @Override
    protected String detalleTienda() {
        return "Radio " + radio + " m | Cooldown " + cooldownMs + " ms";
    }

    @Override
    public final String getEstado() {
        return "Quedan " + cantidad + " | cooldown " + cooldownRestanteMs() + " ms";
    }

    private long cooldownRestanteMs() {
        if (ultimoLanzamientoMs < 0) {
            return 0;
        }
        long pasado = System.currentTimeMillis() - ultimoLanzamientoMs;
        return Math.max(0, cooldownMs - pasado);
    }

    public final double getRadio() {
        return radio;
    }

    public final int getCantidad() {
        return cantidad;
    }
}
