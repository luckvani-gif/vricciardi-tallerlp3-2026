package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("flash")
public class GranadaFlash extends Granada {

    /** Constructor vacío para JPA. */
    protected GranadaFlash() {
    }

    /** Constructor simple: dos granadas flash, la cantidad que trae el arma de fábrica. */
    public GranadaFlash(String nombre, int precio) {
        this(nombre, precio, 2);
    }

    /** Constructor sobrecargado: el llamador decide cuántas granadas se llevan. */
    public GranadaFlash(String nombre, int precio, int cantidad) {
        super(nombre, precio, 0, 1500, 15, cantidad);
    }

    @Override
    protected String describirEfecto(double distancia) {
        return distancia <= getRadio()
                ? "Enceguece a quien esté a " + distancia + " m"
                : "Demasiado lejos para encandilar";
    }

    @Override
    public String getTipo() {
        return "Granada flash";
    }
}
