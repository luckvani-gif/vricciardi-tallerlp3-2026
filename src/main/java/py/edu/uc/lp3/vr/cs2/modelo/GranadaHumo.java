package py.edu.uc.lp3.vr.cs2.modelo;

public final class GranadaHumo extends Granada {

    public GranadaHumo(String nombre, int precio) {
        super(nombre, precio, 0, 3000, 5, 1);
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
