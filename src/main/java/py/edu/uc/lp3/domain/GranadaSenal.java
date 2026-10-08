package py.edu.uc.lp3.domain;

public final class GranadaSenal extends Granada {

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