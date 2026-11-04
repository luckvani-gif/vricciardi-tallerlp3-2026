package py.edu.uc.lp3.exceptions;

/** Se intentó recargar un arma cuyo cargador ya está lleno. */
public class CargadorLlenoException extends ConflictoDePartidaException {

    public CargadorLlenoException(String mensaje) {
        super(mensaje);
    }
}
