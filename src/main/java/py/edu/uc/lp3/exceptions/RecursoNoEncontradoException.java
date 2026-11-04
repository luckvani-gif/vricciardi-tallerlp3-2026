package py.edu.uc.lp3.exceptions;

/** Recurso persistido no encontrado por id. */
public class RecursoNoEncontradoException extends AccesoRecursoException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
