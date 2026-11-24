package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Cuerpo del escenario propio: ids de A1, A2 y A3 más el centro de la explosión. */
@Schema(name = "EscenarioCruzadoRequest", description = "Datos para el ataque coordinado: armas locales de A1, parámetros de A2 y centro de la explosión validada por A3.")
public record EscenarioCruzadoRequest(
        @Schema(description = "Nombre con el que se guarda la jugada; si falta o está vacío se usa «Ataque coordinado».", example = "Ataque coordinado POO-09") String nombre,
        @Schema(description = "Id local de A1 del arma de fuego que dispara y recarga en la simulación final.", example = "1") Long fuegoId,
        @Schema(description = "Id local de A1 de la granada que se lanza en la simulación final.", example = "2") Long granadaId,
        @Schema(description = "Distancia del disparo en metros; si se omite, A1 usa 10.0 m. No puede ser negativa.", example = "10.0") Double distancia,
        @Schema(description = "Id del arma de A2 que dispara contra a2ObjetivoId; es un id de A2, distinto de fuegoId de A1.", example = "1") Long a2AtacanteId,
        @Schema(description = "Id del objetivo de A2 alcanzado por el disparo validado.", example = "2") Long a2ObjetivoId,
        @Schema(description = "Marca A2 de tiro crítico; si se omite, A1 la envía como false.", example = "false") Boolean critico,
        @Schema(description = "Id de la granada de A3 cuya explosión se valida antes del disparo de A2.", example = "13") Long a3GranadaId,
        @Schema(description = "Coordenada x del centro de la explosión en el sistema de A3; si se omite vale 0.0.", example = "0.0") Double x,
        @Schema(description = "Coordenada y del centro de la explosión en el sistema de A3; si se omite vale 0.0.", example = "0.0") Double y,
        @Schema(description = "Coordenada z del centro de la explosión en el sistema de A3; si se omite vale 0.0.", example = "0.0") Double z) {
}
