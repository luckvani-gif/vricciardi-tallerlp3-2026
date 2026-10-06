package py.edu.uc.lp3.domain;

public final class GranadaIncendiaria extends Granada {

    /** Constructor simple: una granada incendiaria. */
    public GranadaIncendiaria(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, 1);
    }

    /** Constructor sobrecargado: el llamador decide cuántas granadas se llevan. */
    public GranadaIncendiaria(String nombre, int precio, int danioBase, int cantidad) {
        super(nombre, precio, danioBase, 4000, 6, cantidad);
    }

    @Override
    protected String describirEfecto(double distancia) {
        return "Fuego en el área (radio " + getRadio() + " m)";
    }

    @Override
    public String getTipo() {
        return "Granada incendiaria";
    }
}
