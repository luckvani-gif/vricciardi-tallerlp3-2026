package py.edu.uc.lp3.domain;

/**
 * Resultado inmutable de la simulación que involucra las tres clases del
 * modelado persistido: Arma (tipo padre), ArmaDeFuego y Granada.
 *
 * Incluye el disparo del arma de fuego, la recarga (regla 2) y el lanzamiento
 * de la granada. Todo queda persistido en H2 al terminar la operación.
 */
public record ResultadoSimulacion(
        Long idArmaDeFuego,
        ResultadoDisparo disparo,
        String recarga,
        String estadoArmaTrasRecarga,
        Long idGranada,
        ResultadoDisparo lanzamientoGranada,
        String resumen) {
}
