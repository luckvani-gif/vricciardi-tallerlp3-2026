package py.edu.uc.lp3.rest.dto;

import java.time.Instant;

/** Jugada guardada en el H2 de A1 después de que A2 y A3 aceptaran. */
public record JugadaCruzadaResponse(
        Long id,
        Instant registradaEn,
        String nombre,
        Long fuegoId,
        Long granadaId,
        double distancia,
        Long a2AtacanteId,
        Long a2ObjetivoId,
        int a2Codigo,
        Long a3GranadaId,
        int a3Codigo,
        String a2Resumen,
        String a3Resumen,
        String resultadoLocal) {
}
