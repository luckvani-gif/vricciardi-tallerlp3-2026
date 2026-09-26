package py.edu.uc.lp3.vr.cs2.modelo;

public final class Rifle extends ArmaDeFuego {

    public Rifle(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    @Override
    protected double factorDistancia(double distancia) {
        return Math.max(0.7, 1 - distancia / 200);
    }

    @Override
    public String getTipo() {
        return "Rifle";
    }
}
