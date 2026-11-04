package py.edu.uc.lp3.exceptions;

/**
 * Base de los conflictos de estado de una partida (cargador, reserva, granadas
 * o tipo persistido). Es una rama de ViolacionReglaException que el handler
 * traduce a 409; cada hija concreta nombra la regla del juego que falló.
 */
public class ConflictoDePartidaException extends ViolacionReglaException {

    public ConflictoDePartidaException(String mensaje) {
        super(mensaje);
    }
}
