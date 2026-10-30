package py.edu.uc.lp3.exceptions;

/** El cuerpo o los parámetros de la petición no respetan una regla del dominio (400). */
public class SolicitudInvalidaException extends ApiException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }

    public SolicitudInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
