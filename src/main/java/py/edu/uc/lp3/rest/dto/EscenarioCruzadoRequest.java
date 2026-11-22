package py.edu.uc.lp3.rest.dto;

/** Cuerpo del escenario propio: ids de A1, A2 y A3 más el centro de la explosión. */
public record EscenarioCruzadoRequest(
        String nombre,
        Long fuegoId,
        Long granadaId,
        Double distancia,
        Long a2AtacanteId,
        Long a2ObjetivoId,
        Boolean critico,
        Long a3GranadaId,
        Double x,
        Double y,
        Double z) {
}
