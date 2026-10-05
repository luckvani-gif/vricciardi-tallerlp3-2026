package py.edu.uc.lp3.domain;

public final class Francotirador extends ArmaDeFuego {

    /** Constructor simple: el francotirador con su cargador y reserva de fábrica. */
    public Francotirador(String nombre, int precio, int danioBase) {
        this(nombre, precio, danioBase, 5, 30);
    }

    /** Constructor sobrecargado: el llamador decide la munición. */
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
