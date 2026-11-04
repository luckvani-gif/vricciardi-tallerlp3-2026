package py.edu.uc.lp3.exceptions;

/** Estado inválido para la operación (cargador vacío, sin reserva, cambio de tipo). */
public class ConflictoDeEstadoException extends ViolacionReglaException {

    public ConflictoDeEstadoException(String mensaje) {
        super(mensaje);
    }
}
