package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("subfusil")
public class Subfusil extends ArmaDeFuego {

    /** Constructor vacío para JPA. */
    protected Subfusil() {
    }

    /** Constructor simple: el subfusil con su cargador y reserva de fábrica. */
    public Subfusil(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, 30, 120);
    }

    /** Constructor sobrecargado: el llamador decide la munición. */
    public Subfusil(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    /** Pierde daño rápido: pensado para corta distancia. */
    @Override
    protected double factorDistancia(double distancia) {
        return Math.max(0.3, 1 - distancia / 40);
    }

    @Override
    public String getTipo() {
        return "Subfusil";
    }
}
