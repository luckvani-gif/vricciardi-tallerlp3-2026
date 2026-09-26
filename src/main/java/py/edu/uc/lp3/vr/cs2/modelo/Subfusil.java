package py.edu.uc.lp3.vr.cs2.modelo;

public final class Subfusil extends ArmaDeFuego {

    public Subfusil(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    /** Pierde daño rápido: pensado para corta distancia. */
    @Override
    protected double factorDistancia(double distancia) {
        return Math.max(0.3, 1 - distancia / 40);
    }

    @Override
    public String getTipo() {
        return "Subfusil";
    }
}
