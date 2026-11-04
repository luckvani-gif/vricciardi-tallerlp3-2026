package py.edu.uc.lp3.exceptions;

/** La granada ya no tiene unidades disponibles. */
public class GranadaAgotadaException extends ConflictoDePartidaException {

    public GranadaAgotadaException(String mensaje) {
        super(mensaje);
    }
}
