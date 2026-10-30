package py.edu.uc.lp3.exceptions;

/** La operación es válida, pero el arma no está en un estado que la permita (409). */
public class ConflictoDeEstadoException extends ApiException {

    public ConflictoDeEstadoException(String mensaje) {
        super(mensaje);
    }
}
