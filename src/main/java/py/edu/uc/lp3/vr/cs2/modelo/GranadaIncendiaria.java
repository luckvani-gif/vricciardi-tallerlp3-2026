package py.edu.uc.lp3.vr.cs2.modelo;

public final class GranadaIncendiaria extends Granada {

    public GranadaIncendiaria(String nombre, int precio, int danioBase) {
        super(nombre, precio, danioBase, 4000, 6, 1);
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
