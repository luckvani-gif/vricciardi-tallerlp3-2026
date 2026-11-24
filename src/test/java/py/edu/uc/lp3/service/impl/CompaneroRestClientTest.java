package py.edu.uc.lp3.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.io.IOException;
import java.time.Duration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import py.edu.uc.lp3.config.CompanerosProperties;
import py.edu.uc.lp3.service.RespuestaExterna;

class CompaneroRestClientTest {

    @Test
    void a2RecibeLaRutaElCuerpoYDevuelveElEstado() {
        RestClient.Builder builderA2 = RestClient.builder();
        MockRestServiceServer servidorA2 = MockRestServiceServer.bindTo(builderA2).build();
        CompaneroRestClient cliente = cliente(builderA2, RestClient.builder());
        servidorA2.expect(requestTo(Matchers.containsString("/api/armas/21/disparar?distancia=10.0")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"objetivoId\":22,\"distancia\":10.0,\"critico\":false}"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"danoAplicado\":36}"));

        RespuestaExterna respuesta = cliente.validarDisparoA2(21L, 22L, 10.0, false);

        assertEquals(200, respuesta.codigo());
        assertTrue(respuesta.cuerpo().contains("danoAplicado"));
        servidorA2.verify();
    }

    @Test
    void a3PropagaElRechazoConSuCuerpo() {
        RestClient.Builder builderA3 = RestClient.builder();
        MockRestServiceServer servidorA3 = MockRestServiceServer.bindTo(builderA3).build();
        CompaneroRestClient cliente = cliente(RestClient.builder(), builderA3);
        servidorA3.expect(requestTo(Matchers.containsString("/api/explosiones/validar")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"granadaId\":6,\"x\":0.0,\"y\":0.0,\"z\":0.0}"))
                .andRespond(withStatus(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"tipo\":\"GranadaYaDetonadaException\"}"));

        RespuestaExterna respuesta = cliente.validarExplosionA3(6L, 0, 0, 0);

        assertEquals(409, respuesta.codigo());
        assertTrue(respuesta.cuerpo().contains("GranadaYaDetonadaException"));
        servidorA3.verify();
    }

    @Test
    void compaSinRespuestaDevuelve502ConOrigen() {
        RestClient.Builder builderA3 = RestClient.builder();
        MockRestServiceServer servidorA3 = MockRestServiceServer.bindTo(builderA3).build();
        CompaneroRestClient cliente = cliente(RestClient.builder(), builderA3);
        servidorA3.expect(requestTo(Matchers.containsString("/api/explosiones/validar")))
                .andRespond(withException(new IOException("timeout")));

        RespuestaExterna respuesta = cliente.validarExplosionA3(13L, 0, 0, 0);

        assertEquals(502, respuesta.codigo());
        assertTrue(respuesta.cuerpo().contains("A3 no disponible"));
        assertTrue(respuesta.cuerpo().contains("timeout"));
        servidorA3.verify();
    }

    private static CompaneroRestClient cliente(RestClient.Builder a2, RestClient.Builder a3) {
        return new CompaneroRestClient(
                new CompanerosProperties("http://a2", "http://a3", Duration.ofSeconds(5)), a2, a3);
    }
}
