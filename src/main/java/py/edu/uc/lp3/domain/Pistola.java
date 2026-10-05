package py.edu.uc.lp3.domain;

public final class Pistola extends ArmaDeFuego {

    /** Constructor simple: la pistola con su cargador y reserva de fábrica. */
    public Pistola(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, 20, 120);
    }

    /** Constructor sobrecargado: el llamador decide la munición. */
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
