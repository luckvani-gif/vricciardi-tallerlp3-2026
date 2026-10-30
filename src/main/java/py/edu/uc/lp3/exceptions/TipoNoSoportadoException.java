package py.edu.uc.lp3.exceptions;

/** El discriminador del JSON no corresponde a ningún tipo persistible (400). */
public class TipoNoSoportadoException extends SolicitudInvalidaException {

    public TipoNoSoportadoException(String mensaje) {
        super(mensaje);
    }
}
