package py.edu.uc.lp3.service;

/** Cliente HTTP hacia los dos OpenAPI ajenos. No contiene reglas de dominio. */
public interface CompaneroClient {

    /** Regla de A2: dispara con un Fusil desde su propio catálogo. */
    RespuestaExterna validarDisparoA2(Long atacanteId, Long objetivoId, double distancia, boolean critico);

    /** Regla de A3: valida una explosión HE sin detonarla ni modificar su H2. */
    RespuestaExterna validarExplosionA3(Long granadaId, double x, double y, double z);
}
