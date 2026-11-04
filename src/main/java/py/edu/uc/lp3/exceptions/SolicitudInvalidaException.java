package py.edu.uc.lp3.exceptions;

/** Específica de violación de regla: parámetros/cuerpo inválidos. */
public class SolicitudInvalidaException extends ViolacionReglaException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }

    public SolicitudInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
