package py.edu.uc.lp3.exceptions;

/** Discriminador no soportado al crear/actualizar. */
public class TipoNoSoportadoException extends ViolacionReglaException {

    public TipoNoSoportadoException(String mensaje) {
        super(mensaje);
    }
}
