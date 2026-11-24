package py.edu.uc.lp3.domain;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("fuego")
public abstract class ArmaDeFuego extends Arma {

    @Column(name = "balas_en_cargador")
    private int balasEnCargador;

    @Column(name = "capacidad_cargador")
    private int capacidadCargador;

    @Column
    private int reserva;

    protected ArmaDeFuego() {
    }

    protected ArmaDeFuego(String nombre, int precio, int danioBase,
                          int balasEnCargador, int capacidadCargador, int reserva) {
        super(nombre, precio, danioBase);
        this.balasEnCargador = balasEnCargador;
        this.capacidadCargador = capacidadCargador;
        this.reserva = reserva;
    }

    protected ArmaDeFuego(String nombre, int precio, int danioBase,
                          int balasEnCargador, int reserva) {
        this(nombre, precio, danioBase, balasEnCargador, balasEnCargador, reserva);
    }

    public int getBalasEnCargador() {
        return balasEnCargador;
    }

    public int getCapacidadCargador() {
        return capacidadCargador;
    }

    public int getReserva() {
        return reserva;
    }

    @Override
    public final boolean esRecargable() {
        return true;
    }

    @Override
    public final String recargar() {
        int faltan = capacidadCargador - balasEnCargador;
        if (faltan == 0 || reserva == 0) {
            return "Sin recarga (" + getEstado() + ")";
        }
        int carga = Math.min(faltan, reserva);
        balasEnCargador += carga;
        reserva -= carga;
        return "Recargó " + carga + " balas (" + getEstado() + ")";
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
    @jakarta.persistence.Transient
    public final String getEstado() {
        return balasEnCargador + "/" + reserva;
    }

    protected abstract double factorDistancia(double distancia);

    @Override
    protected String describirEfecto(double distancia) {
        return "Disparo a " + distancia + " m (" + getEstado() + ")";
    }

    @Override
    protected final int calcularDanio(double distancia) {
        return (int) Math.round(getDanioBase() * factorDistancia(distancia));
    }

    @Override
    protected final String detalleTienda() {
        return "Cargador " + capacidadCargador + " | Reserva " + reserva;
    }
}
