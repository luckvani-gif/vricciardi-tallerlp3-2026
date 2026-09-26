package py.edu.uc.lp3.vr.cs2.modelo;

public final class GranadaFlash extends Granada {

    public GranadaFlash(String nombre, int precio) {
        super(nombre, precio, 0, 1500, 15, 2);
    }

    @Override
    protected String describirEfecto(double distancia) {
        return distancia <= getRadio()
                ? "Enceguece a quien esté a " + distancia + " m"
                : "Demasiado lejos para encandilar";
    }

    @Override
    public String getTipo() {
        return "Granada flash";
    }
}
