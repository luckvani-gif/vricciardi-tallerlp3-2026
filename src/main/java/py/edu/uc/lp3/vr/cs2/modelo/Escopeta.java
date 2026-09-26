package py.edu.uc.lp3.vr.cs2.modelo;

public final class Escopeta extends ArmaDeFuego {

    private static final int PERDIGONES = 8;

    public Escopeta(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    /** Cada 2 m de distancia se pierde un perdigón de los 8. */
    @Override
    protected double factorDistancia(double distancia) {
        return perdigonesQueImpactan(distancia) / (double) PERDIGONES;
    }

    @Override
    protected String describirEfecto(double distancia) {
        return perdigonesQueImpactan(distancia) + "/" + PERDIGONES + " perdigones a " + distancia + " m";
    }

    private int perdigonesQueImpactan(double distancia) {
        return (int) Math.max(0, PERDIGONES - Math.floor(distancia / 2));
    }

    @Override
    public String getTipo() {
        return "Escopeta";
    }
}
