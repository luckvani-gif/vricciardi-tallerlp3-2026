package py.edu.uc.lp3.exceptions;

/** Se intentó disparar un arma de fuego sin balas en el cargador. */
public class CargadorVacioException extends ConflictoDePartidaException {

    public CargadorVacioException(String mensaje) {
        super(mensaje);
    }
}
