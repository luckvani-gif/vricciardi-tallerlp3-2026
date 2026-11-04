package py.edu.uc.lp3.exceptions;

/** Se intentó cambiar el tipo de un arma ya persistida. */
public class CambioDeTipoException extends ConflictoDePartidaException {

    public CambioDeTipoException(String mensaje) {
        super(mensaje);
    }
}
