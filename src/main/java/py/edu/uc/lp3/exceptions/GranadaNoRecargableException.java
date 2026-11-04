package py.edu.uc.lp3.exceptions;

/** Las granadas no se recargan: hay que comprar otra. */
public class GranadaNoRecargableException extends ConflictoDePartidaException {

    public GranadaNoRecargableException(String mensaje) {
        super(mensaje);
    }
}
