package py.edu.uc.lp3.vr.cs2.modelo;

public final class Pistola extends ArmaDeFuego {

    public Pistola(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    @Override
    protected double factorDistancia(double distancia) {
        return Math.max(0.5, 1 - distancia / 100);
    }

    @Override
    public String getTipo() {
        return "Pistola";
    }
}
