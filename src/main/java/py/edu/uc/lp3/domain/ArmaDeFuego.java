package py.edu.uc.lp3.domain;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import py.edu.uc.lp3.exceptions.ConflictoDeEstadoException;

/**
 * Generaliza el control de munición: antes cada arma repetía la lógica de
 * cargador y recarga; ahora vive una sola vez acá y es final.
 * Las hijas solo definen cómo cae el daño con la distancia.
 */
@Entity
@DiscriminatorValue("arma_de_fuego")
public abstract class ArmaDeFuego extends Arma {

    /** Munición que se usa cuando el llamador no indica cargador ni reserva. */
    protected static final int MUNICION_POR_DEFECTO = 30;
    protected static final int RESERVA_POR_DEFECTO = 90;

    @Column
    private Integer capacidadCargador;

    @Column
    private Integer balasEnCargador;

    @Column
    private Integer reserva;

    /** Constructor vacío para JPA. */
    protected ArmaDeFuego() {
    }

    /**
     * Constructor simple: deja el arma con la munición por defecto, en estado legal.
     * Es el que usan las hijas cuando se construye sin indicar cargador ni reserva.
     */
    protected ArmaDeFuego(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, MUNICION_POR_DEFECTO, RESERVA_POR_DEFECTO);
    }

    /** Constructor sobrecargado: el llamador decide la munición. */
    protected ArmaDeFuego(String nombre, int precio, int danioBase,
                           int capacidadCargador, int reserva) {
        super(nombre, precio, danioBase);
        if (capacidadCargador <= 0) {
            throw new IllegalArgumentException("El cargador debe tener al menos 1 bala");
        }
        if (reserva < 0) {
            throw new IllegalArgumentException("La reserva no puede ser negativa");
        }
        this.capacidadCargador = capacidadCargador;
        this.balasEnCargador = capacidadCargador;
        this.reserva = reserva;
    }

    @PrePersist
    @PreUpdate
    private void validarMunicionAntesDeGuardar() {
        if (capacidadCargador == null || capacidadCargador <= 0) {
            throw new IllegalArgumentException("El cargador debe tener al menos 1 bala");
        }
        int cargador = capacidadCargador;
        int balas = balasEnCargador == null ? cargador : balasEnCargador;
        int res = reserva == null ? 0 : reserva;
        if (balas < 0 || balas > cargador) {
            throw new IllegalArgumentException("Las balas en el cargador deben estar entre 0 y la capacidad");
        }
        if (res < 0) {
            throw new IllegalArgumentException("La reserva no puede ser negativa");
        }
        if (balasEnCargador == null) {
            balasEnCargador = cargador;
        }
        if (reserva == null) {
            reserva = 0;
        }
    }

    @Override
    protected final Optional<String> motivoBloqueo() {
        return balasEnCargador == 0
                ? Optional.of("Cargador vacío: hay que recargar")
                : Optional.empty();
    }

    @Override
    protected final void consumirUso() {
        balasEnCargador--;
    }

    @Override
    public final boolean esRecargable() {
        return true;
    }

    /**
     * REGLA DE NEGOCIO 2 (en el dominio): solo se recarga si falta munición en el
     * cargador y hay reserva. Si la regla no se cumple, el objeto lanza la
     * excepción; el servicio solo persiste el estado resultante.
     */
    @Override
    public final String recargar() {
        int faltan = capacidadCargador - balasEnCargador;
        if (faltan == 0) {
            throw new ConflictoDeEstadoException("El cargador ya está lleno");
        }
        if (reserva == 0) {
            throw new ConflictoDeEstadoException("Sin munición de reserva");
        }
        int carga = Math.min(faltan, reserva);
        balasEnCargador += carga;
        reserva -= carga;
        return "Recargó " + carga + " balas (" + getEstado() + ")";
    }

    @Override
    protected final int calcularDanio(double distancia) {
        return (int) Math.round(getDanioBase() * factorDistancia(distancia));
    }

    /** Lo único que cambia entre armas de fuego: la caída del daño (0..1). */
    protected abstract double factorDistancia(double distancia);

    @Override
    protected String describirEfecto(double distancia) {
        return "Impacto de bala a " + distancia + " m";
    }

    @Override
    protected String detalleTienda() {
        return "Cargador " + capacidadCargador + " | Reserva " + reserva;
    }

    @Override
    public final String getEstado() {
        return balasEnCargador + "/" + reserva;
    }

    public final int getBalasEnCargador() {
        return balasEnCargador;
    }

    public final int getReserva() {
        return reserva;
    }

    public final int getCapacidadCargador() {
        return capacidadCargador;
    }
}
