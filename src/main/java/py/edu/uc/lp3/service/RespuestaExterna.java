package py.edu.uc.lp3.service;

/**
 * Resultado crudo de una validación ajena: código HTTP y cuerpo recibido.
 * El cliente no decide si es aceptación o rechazo; eso lo hace el escenario.
 */
public record RespuestaExterna(int codigo, String cuerpo) {

    public boolean ok() {
        return codigo >= 200 && codigo < 300;
    }
}
