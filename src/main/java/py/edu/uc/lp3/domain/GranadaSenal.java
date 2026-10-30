package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("senal")
public class GranadaSenal extends Granada {

    /** Constructor vacío para JPA. */
    protected GranadaSenal() {
    }

    public GranadaSenal(String nombre, int precio) {
        this(nombre, precio, 1);
    }

    public GranadaSenal(String nombre, int precio, int cantidad) {
        super(nombre, precio, 0, 5000, 1, cantidad);
    }

    @Override
    protected String describirEfecto(double distancia) {
        return "Genera ruido para distraer al enemigo";
    }

    @Override
    public String getTipo() {
        return "Granada señuelo";
    }
}