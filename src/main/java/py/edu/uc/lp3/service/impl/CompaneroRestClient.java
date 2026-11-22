package py.edu.uc.lp3.service.impl;

import java.time.Duration;
import java.util.Map;

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

    private final RestClient a2;
    private final RestClient a3;

    public CompaneroRestClient(CompanerosProperties propiedades) {
        Duration timeout = propiedades.timeout() == null ? Duration.ofSeconds(5) : propiedades.timeout();
        int timeoutMs = (int) timeout.toMillis();
        SimpleClientHttpRequestFactory factoria = new SimpleClientHttpRequestFactory();
        factoria.setConnectTimeout(timeoutMs);
        factoria.setReadTimeout(timeoutMs);
        this.a2 = RestClient.builder()
                .baseUrl(propiedades.a2BaseUrl())
                .requestFactory(factoria)
                .build();
        this.a3 = RestClient.builder()
                .baseUrl(propiedades.a3BaseUrl())
                .requestFactory(factoria)
                .build();
    }

    @Override
    public RespuestaExterna validarDisparoA2(Long atacanteId, Long objetivoId, double distancia, boolean critico) {
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
            return new RespuestaExterna(respuesta.getStatusCode().value(), respuesta.getBody());
        } catch (ResourceAccessException e) {
            return new RespuestaExterna(502, cuerpoNoDisponible("A2", e));
        } catch (RestClientException e) {
            return new RespuestaExterna(502, cuerpoNoDisponible("A2", e));
        }
    }

    @Override
    public RespuestaExterna validarExplosionA3(Long granadaId, double x, double y, double z) {
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
            return new RespuestaExterna(respuesta.getStatusCode().value(), respuesta.getBody());
        } catch (ResourceAccessException e) {
            return new RespuestaExterna(502, cuerpoNoDisponible("A3", e));
        } catch (RestClientException e) {
            return new RespuestaExterna(502, cuerpoNoDisponible("A3", e));
        }
    }

    private static String cuerpoNoDisponible(String companero, Exception e) {
        String detalle = String.valueOf(e.getMessage()).replace("\"", "'");
        return "{\"error\":\"" + companero + " no disponible\",\"detalle\":\"" + detalle + "\"}";
    }
}
