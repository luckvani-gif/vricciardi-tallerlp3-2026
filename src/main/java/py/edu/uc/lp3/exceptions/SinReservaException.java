package py.edu.uc.lp3.exceptions;

/** Se intentó recargar sin munición de reserva disponible. */
public class SinReservaException extends ConflictoDePartidaException {

    public SinReservaException(String mensaje) {
        super(mensaje);
    }
}
