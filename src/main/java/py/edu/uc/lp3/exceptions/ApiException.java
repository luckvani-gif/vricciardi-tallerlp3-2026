package py.edu.uc.lp3.exceptions;

/**
 * Base de la jerarquía de excepciones del dominio.
 * Se exige: una base y dos hijas con un handler HTTP por tipo.
 */
public abstract class ApiException extends RuntimeException {

    protected ApiException(String mensaje) {
        super(mensaje);
    }

    protected ApiException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
