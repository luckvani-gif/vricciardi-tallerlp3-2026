package py.edu.uc.lp3.exceptions;

/** El id pedido no existe en H2 (404). */
public class RecursoNoEncontradoException extends ApiException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
