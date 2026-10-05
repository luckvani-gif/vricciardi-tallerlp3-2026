package py.edu.uc.lp3.domain;

import java.util.Optional;

/**
 * Generaliza el control de munición: antes cada arma repetía la lógica de
 * cargador y recarga; ahora vive una sola vez acá y es final.
 * Las hijas solo definen cómo cae el daño con la distancia.
 */
public abstract class ArmaDeFuego extends Arma {

    /** Munición que se usa cuando el llamador no indica cargador ni reserva. */
    protected static final int MUNICION_POR_DEFECTO = 30;
    protected static final int RESERVA_POR_DEFECTO = 90;

    private final int capacidadCargador;
    private int balasEnCargador;
    private int reserva;

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
    public final String recargar() {
        int faltan = capacidadCargador - balasEnCargador;
        if (faltan == 0) {
            return "El cargador ya está lleno";
        }
        if (reserva == 0) {
            return "Sin munición de reserva";
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
