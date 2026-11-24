package py.edu.uc.lp3.service.impl;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import py.edu.uc.lp3.config.CompanerosProperties;
import py.edu.uc.lp3.service.CompaneroClient;
import py.edu.uc.lp3.service.RespuestaExterna;

/**
 * Traduce las dos operaciones del escenario a pedidos HTTP según los OpenAPI
 * leídos de A2 y A3. Las rutas y los cuerpos salen de esos documentos; acá no
 * se copia ninguna clase del dominio ajeno.
 */
@Component
public class CompaneroRestClient implements CompaneroClient {

    private static final Logger LOG = LogManager.getLogger(CompaneroRestClient.class);

    private final RestClient a2;
    private final RestClient a3;

    @Autowired
    public CompaneroRestClient(CompanerosProperties propiedades) {
        this(propiedades,
                RestClient.builder().requestFactory(factoria(propiedades)),
                RestClient.builder().requestFactory(factoria(propiedades)));
    }

    CompaneroRestClient(CompanerosProperties propiedades, RestClient.Builder a2Builder, RestClient.Builder a3Builder) {
        this.a2 = a2Builder.baseUrl(propiedades.a2BaseUrl()).build();
        this.a3 = a3Builder.baseUrl(propiedades.a3BaseUrl()).build();
        LOG.debug("Clientes listos: A2={}, A3={}, timeout={}ms",
                propiedades.a2BaseUrl(), propiedades.a3BaseUrl(), timeout(propiedades).toMillis());
    }

    private static SimpleClientHttpRequestFactory factoria(CompanerosProperties propiedades) {
        int timeoutMs = (int) timeout(propiedades).toMillis();
        SimpleClientHttpRequestFactory factoria = new SimpleClientHttpRequestFactory();
        factoria.setConnectTimeout(timeoutMs);
        factoria.setReadTimeout(timeoutMs);
        return factoria;
    }

    private static Duration timeout(CompanerosProperties propiedades) {
        return propiedades.timeout() == null ? Duration.ofSeconds(5) : propiedades.timeout();
    }

    @Override
    public RespuestaExterna validarDisparoA2(Long atacanteId, Long objetivoId, double distancia, boolean critico) {
        LOG.debug("A2 request: POST /api/armas/disparar atacanteId={} objetivoId={} distancia={} critico={}",
                atacanteId, objetivoId, distancia, critico);
        try {
            ResponseEntity<String> respuesta = a2.post()
                    .uri("/api/armas/{id}/disparar?distancia={distancia}", atacanteId, distancia)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "objetivoId", objetivoId,
                            "distancia", distancia,
                            "critico", critico))
                    .retrieve()
                    .onStatus(estado -> estado.is4xxClientError() || estado.is5xxServerError(), (peticion, r) -> { })
                    .toEntity(String.class);
            RespuestaExterna resultado = new RespuestaExterna(respuesta.getStatusCode().value(), respuesta.getBody());
            LOG.debug("A2 response: status={} body={}", resultado.codigo(), resultado.cuerpo());
            return resultado;
        } catch (ResourceAccessException e) {
            LOG.debug("A2 sin respuesta: {}", e.getMessage(), e);
            return new RespuestaExterna(502, cuerpoNoDisponible("A2", e));
        } catch (RestClientException e) {
            LOG.debug("A2 error de cliente: {}", e.getMessage(), e);
            return new RespuestaExterna(502, cuerpoNoDisponible("A2", e));
        }
    }

    @Override
    public RespuestaExterna validarExplosionA3(Long granadaId, double x, double y, double z) {
        LOG.debug("A3 request: POST /api/explosiones/validar granadaId={} x={} y={} z={}", granadaId, x, y, z);
        try {
            ResponseEntity<String> respuesta = a3.post()
                    .uri("/api/explosiones/validar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "granadaId", granadaId,
                            "x", x,
                            "y", y,
                            "z", z))
                    .retrieve()
                    .onStatus(estado -> estado.is4xxClientError() || estado.is5xxServerError(), (peticion, r) -> { })
                    .toEntity(String.class);
            RespuestaExterna resultado = new RespuestaExterna(respuesta.getStatusCode().value(), respuesta.getBody());
            LOG.debug("A3 response: status={} body={}", resultado.codigo(), resultado.cuerpo());
            return resultado;
        } catch (ResourceAccessException e) {
            LOG.debug("A3 sin respuesta: {}", e.getMessage(), e);
            return new RespuestaExterna(502, cuerpoNoDisponible("A3", e));
        } catch (RestClientException e) {
            LOG.debug("A3 error de cliente: {}", e.getMessage(), e);
            return new RespuestaExterna(502, cuerpoNoDisponible("A3", e));
        }
    }

    private static String cuerpoNoDisponible(String companero, Exception e) {
        String detalle = String.valueOf(e.getMessage()).replace("\"", "'");
        return "{\"error\":\"" + companero + " no disponible\",\"detalle\":\"" + detalle + "\"}";
    }
}
