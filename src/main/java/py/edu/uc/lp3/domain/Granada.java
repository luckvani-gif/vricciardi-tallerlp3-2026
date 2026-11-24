package py.edu.uc.lp3.domain;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("granada")
public abstract class Granada extends Arma {

    @Column
    private int cantidad;

    @Column(name = "cooldown_ms")
    private int cooldownMs;

    @Column(name = "radio_m")
    private int radio;

    @Column(name = "ultimo_lanzamiento_ms")
    private long ultimoLanzamientoMs;

    protected Granada() {
    }

    protected Granada(String nombre, int precio, int danioBase,
                      int cooldownMs, int radio, int cantidad) {
        super(nombre, precio, danioBase);
        this.cooldownMs = cooldownMs;
        this.radio = radio;
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCooldownMs() {
        return cooldownMs;
    }

    public int getRadio() {
        return radio;
    }

    public long getUltimoLanzamientoMs() {
        return ultimoLanzamientoMs;
    }

    @jakarta.persistence.Transient
    public long getCooldownRestanteMs() {
        long transcurrido = System.currentTimeMillis() - ultimoLanzamientoMs;
        return Math.max(0, cooldownMs - transcurrido);
    }

    public final boolean sinGranadas() {
        return cantidad == 0;
    }

    @Override
    public final String recargar() {
        return "Las granadas no se recargan: hay que comprar otra";
    }

    @Override
    protected final Optional<String> motivoBloqueo() {
        if (cantidad == 0) {
            return Optional.of("No quedan granadas");
        }
        long restante = getCooldownRestanteMs();
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
    @jakarta.persistence.Transient
    public final String getEstado() {
        long restante = getCooldownRestanteMs();
        return restante == 0
                ? cantidad + " granadas | lista"
                : cantidad + " granadas | cooldown " + restante + " ms";
    }

    @Override
    protected int calcularDanio(double distancia) {
        if (distancia > radio) {
            return 0;
        }
        return (int) Math.round(getDanioBase() * (1 - distancia / radio));
    }

    @Override
    protected final String detalleTienda() {
        return "Radio " + radio + " m | Cantidad " + cantidad;
    }
}
