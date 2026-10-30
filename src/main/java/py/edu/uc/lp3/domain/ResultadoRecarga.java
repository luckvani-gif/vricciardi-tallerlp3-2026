package py.edu.uc.lp3.domain;

/** Resultado inmutable de una recarga sobre un arma ya persistida. */
public record ResultadoRecarga(Long id, String arma, String mensaje, String estado) {
}
