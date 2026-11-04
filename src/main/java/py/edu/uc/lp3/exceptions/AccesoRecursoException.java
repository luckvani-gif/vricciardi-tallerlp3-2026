package py.edu.uc.lp3.exceptions;

/**
 * Hija 2 de ApiException.
 * Errores de acceso a recursos persistidos: id inexistente.
 * Se traduce a 404.
 */
public class AccesoRecursoException extends ApiException {

    public AccesoRecursoException(String mensaje) {
        super(mensaje);
    }

    public AccesoRecursoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
