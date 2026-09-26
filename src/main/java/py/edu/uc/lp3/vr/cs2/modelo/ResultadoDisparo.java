package py.edu.uc.lp3.vr.cs2.modelo;

/** Resultado inmutable de usar un arma. Es lo que se serializa a JSON. */
public record ResultadoDisparo(String arma, String tipo, boolean exito,
                               int danio, String efecto, String estado) {

    static ResultadoDisparo exitoso(Arma a, int danio, String efecto) {
        return new ResultadoDisparo(a.getNombre(), a.getTipo(), true, danio, efecto, a.getEstado());
    }

    static ResultadoDisparo fallido(Arma a, String motivo) {
        return new ResultadoDisparo(a.getNombre(), a.getTipo(), false, 0, motivo, a.getEstado());
    }
}
