package py.edu.uc.lp3.exceptions;

/**
 * Raíz de la jerarquía de errores de la API.
 * Cada subclase tiene su propio handler en ManejadorErrores: el tipo de
 * excepción decide el código HTTP, no un if dentro del controller.
 */
public abstract class ApiException extends RuntimeException {

    protected ApiException(String mensaje) {
        super(mensaje);
    }

    protected ApiException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
