package py.edu.uc.lp3.exceptions;

/**
 * Hija 1 de ApiException.
 * Agrupa violaciones a reglas de negocio/validación (datos ilegales,
 * tipo no soportado, intento de cambiar el tipo persistido, munición inválida).
 * Se traduce a 400/409 según el caso concreto.
 */
public class ViolacionReglaException extends ApiException {

    public ViolacionReglaException(String mensaje) {
        super(mensaje);
    }

    public ViolacionReglaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
