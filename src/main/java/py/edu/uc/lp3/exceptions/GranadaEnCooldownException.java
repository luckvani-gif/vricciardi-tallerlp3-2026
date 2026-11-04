package py.edu.uc.lp3.exceptions;

/** Se intentó lanzar una granada que todavía está en cooldown. */
public class GranadaEnCooldownException extends ConflictoDePartidaException {

    public GranadaEnCooldownException(String mensaje) {
        super(mensaje);
    }
}
