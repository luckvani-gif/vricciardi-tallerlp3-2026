package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("rifle")
public class Rifle extends ArmaDeFuego implements Equipable {

    /** Constructor vacío para JPA. */
    protected Rifle() {
    }

    /** Constructor simple: el rifle con su cargador y reserva de fábrica. */
    public Rifle(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, 30, 90);
    }

    /** Constructor sobrecargado: el llamador decide la munición. */
    public Rifle(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    @Override
    protected double factorDistancia(double distancia) {
        return Math.max(0.7, 1 - distancia / 200);
    }

    @Override
    public String ranura() {
        return "primaria";
    }

    @Override
    public String getTipo() {
        return "Rifle";
    }
}
