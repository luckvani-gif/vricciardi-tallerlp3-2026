package py.edu.uc.lp3.vr.cs2;

public abstract class Arma {

    protected String nombre;
    protected float precio;
    protected int daño;
    protected float peso;
    protected int municionMax;
    protected int municionActual;

    public void disparar() {
    }

    public void recargar() {
    }

    public int obtenerPrecio() {
        return (int) precio;
    }

    public String obtenerNombre() {
        return nombre;
    }
}
