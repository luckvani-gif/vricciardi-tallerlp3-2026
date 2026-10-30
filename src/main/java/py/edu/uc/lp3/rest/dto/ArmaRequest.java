package py.edu.uc.lp3.rest.dto;

/**
 * Cuerpo JSON del CRUD. "tipo" es el discriminador que elige la clase hija
 * (rifle, pistola, humo, ...). Los campos opcionales usan los valores de
 * fabrica del dominio cuando el cliente no los manda.
 */
public record ArmaRequest(
        String tipo,
        String nombre,
        Integer precio,
        Integer danio,
        Integer cargador,
        Integer reserva) {
}
