package py.edu.uc.lp3.vr.cs2.modelo;

public final class Francotirador extends ArmaDeFuego {

    public Francotirador(String nombre, int precio, int danioBase, int cargador, int reserva) {
        super(nombre, precio, danioBase, cargador, reserva);
    }

    /** No pierde daño con la distancia. */
    @Override
    protected double factorDistancia(double distancia) {
        return 1.0;
    }

    @Override
    protected String describirEfecto(double distancia) {
        return "Disparo con mira a " + distancia + " m";
    }

    @Override
    public String getTipo() {
        return "Francotirador";
    }
}
