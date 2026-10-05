package py.edu.uc.lp3.domain;

import java.util.Optional;

/**
 * Las granadas no tienen cargador: se controlan por cantidad y cooldown.
 * Ese control es final y private: desde afuera no se puede saltear el cooldown.
 */
public abstract class Granada extends Arma {

    private final long cooldownMs;
    private final double radio;
    private int cantidad;
    private long ultimoLanzamientoMs = -1;

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

    @Override
    public final String recargar() {
        return "Las granadas no se recargan: hay que comprar otra";
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
