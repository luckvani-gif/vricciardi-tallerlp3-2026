package py.edu.uc.lp3.domain;

public final class GranadaHumo extends Granada {

    /** Constructor simple: una granada de humo. */
    public GranadaHumo(String nombre, int precio) {
        this(nombre, precio, 1);
    }

    /** Constructor sobrecargado: el llamador decide cuántas granadas se llevan. */
    public GranadaHumo(String nombre, int precio, int cantidad) {
        super(nombre, precio, 0, 3000, 5, cantidad);
    }

    @Override
    protected String describirEfecto(double distancia) {
        return "Cortina de humo a " + distancia + " m (bloquea visión)";
    }

    @Override
    public String getTipo() {
        return "Granada de humo";
    }
}
