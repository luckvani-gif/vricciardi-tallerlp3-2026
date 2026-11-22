package py.edu.uc.lp3.tp2026;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.service.CompaneroClient;
import py.edu.uc.lp3.service.RespuestaExterna;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EscenarioCruzadoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JugadaCruzadaRepository jugadaRepository;

    @Autowired
    private FakeCompaneroClient fake;

    @BeforeEach
    void reiniciarFake() {
        fake.a2Respuesta = new RespuestaExterna(200, "{\"danoAplicado\":36,\"objetivoDestruido\":false}");
        fake.a3Respuesta = new RespuestaExterna(200, "{\"simulacion\":true,\"resumen\":\"explosion valida\"}");
        fake.llamadasA2 = 0;
        fake.llamadasA3 = 0;
    }

    @Test
    @DisplayName("Escenario feliz: A3 y A2 aceptan, la jugada se guarda y se puede leer")
    void escenarioFelizPersiste() throws Exception {
        long fuegoId = crearRifle();
        long granadaId = crearGranada();

        mockMvc.perform(post("/api/escenarios/ataque-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo(fuegoId, granadaId, 21L, 22L, 13L)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.a2Codigo").value(200))
                .andExpect(jsonPath("$.a3Codigo").value(200))
                .andExpect(jsonPath("$.resultadoLocal").value(org.hamcrest.Matchers.containsString("AK-47")));

        assertEquals(1, jugadaRepository.count());
        assertEquals(1, fake.llamadasA2);
        assertEquals(1, fake.llamadasA3);

        mockMvc.perform(get("/api/escenarios/ataque-coordinado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].a2AtacanteId").value(21));

        mockMvc.perform(get("/api/armas/{id}", fuegoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balasEnCargador").value(5))
                .andExpect(jsonPath("$.reserva").value(14));
    }

    @Test
    @DisplayName("Si A2 rechaza, responde 404 con origen A2 y el H2 queda igual")
    void rechazoDeA2NoPersiste() throws Exception {
        long fuegoId = crearRifle();
        long granadaId = crearGranada();
        fake.a2Respuesta = new RespuestaExterna(404, "{\"mensaje\":\"no existe el objetivo\"}");

        mockMvc.perform(post("/api/escenarios/ataque-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo(fuegoId, granadaId, 999999L, 22L, 13L)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.origen").value("A2"))
                .andExpect(jsonPath("$.tipo").value("RechazoCompaneroException"))
                .andExpect(jsonPath("$.codigo").value(404))
                .andExpect(jsonPath("$.cuerpoCompanero").value(org.hamcrest.Matchers.containsString("no existe el objetivo")));

        assertEquals(0, jugadaRepository.count());
        mockMvc.perform(get("/api/armas/{id}", fuegoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balasEnCargador").value(5))
                .andExpect(jsonPath("$.reserva").value(15));
    }

    @Test
    @DisplayName("Si A3 rechaza, responde 409 con origen A3 y A2 ni siquiera es llamado")
    void rechazoDeA3NoPersiste() throws Exception {
        long fuegoId = crearRifle();
        long granadaId = crearGranada();
        fake.a3Respuesta = new RespuestaExterna(409, "{\"tipo\":\"GranadaYaDetonadaException\"}");

        mockMvc.perform(post("/api/escenarios/ataque-coordinado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo(fuegoId, granadaId, 21L, 22L, 6L)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.origen").value("A3"))
                .andExpect(jsonPath("$.codigo").value(409));

        assertEquals(0, jugadaRepository.count());
        assertEquals(0, fake.llamadasA2);
        assertEquals(1, fake.llamadasA3);
    }

    @Test
    @DisplayName("El OpenAPI propio publica el escenario y las reglas")
    void openapiPublicado() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/escenarios/ataque-coordinado'].post").exists())
                .andExpect(jsonPath("$.paths['/api/armas/{id}/disparar'].post").exists())
                .andExpect(jsonPath("$.paths['/api/armas/{id}/recargar'].post").exists());
    }

    private String cuerpo(long fuegoId, long granadaId, long a2Atacante, long a2Objetivo, long a3Granada) {
        return """
                {
                  "nombre": "Ataque coordinado POO-09",
                  "fuegoId": %d,
                  "granadaId": %d,
                  "distancia": 10,
                  "a2AtacanteId": %d,
                  "a2ObjetivoId": %d,
                  "critico": false,
                  "a3GranadaId": %d,
                  "x": 0,
                  "y": 0,
                  "z": 0
                }
                """.formatted(fuegoId, granadaId, a2Atacante, a2Objetivo, a3Granada);
    }

    private long crearRifle() throws Exception {
        return crear("""
                {"tipo":"rifle","nombre":"AK-47","precio":2700,"danio":36,"cargador":5,"reserva":15}
                """);
    }

    private long crearGranada() throws Exception {
        return crear("""
                {"tipo":"humo","nombre":"Humo","precio":300}
                """);
    }

    private long crear(String json) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode arbol = objectMapper.readTree(resultado.getResponse().getContentAsString());
        return arbol.get("id").asLong();
    }

    @TestConfiguration
    static class ClientesFalsos {

        @Bean
        @Primary
        FakeCompaneroClient fakeCompaneroClient() {
            return new FakeCompaneroClient();
        }
    }

    static class FakeCompaneroClient implements CompaneroClient {

        private RespuestaExterna a2Respuesta = new RespuestaExterna(200, "{}");
        private RespuestaExterna a3Respuesta = new RespuestaExterna(200, "{}");
        private int llamadasA2;
        private int llamadasA3;

        @Override
        public RespuestaExterna validarDisparoA2(Long atacanteId, Long objetivoId, double distancia, boolean critico) {
            llamadasA2++;
            return a2Respuesta;
        }

        @Override
        public RespuestaExterna validarExplosionA3(Long granadaId, double x, double y, double z) {
            llamadasA3++;
            return a3Respuesta;
        }
    }
}
