package py.edu.uc.lp3.tp2026;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ArmaCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("CRUD completo: crear, leer, actualizar y borrar contra H2")
    void crudCompleto() throws Exception {
        long id = crear(new RifleJson("AK-47", 2700, 36, 30, 90));

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("AK-47"))
                .andExpect(jsonPath("$.tipo").value("Rifle"));

        mockMvc.perform(put("/api/armas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"rifle","nombre":"AK-47 Redline","precio":3000,"danio":40}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("AK-47 Redline"))
                .andExpect(jsonPath("$.precio").value(3000));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("AK-47 Redline"));

        mockMvc.perform(delete("/api/armas/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Un POST ilegal responde 400 y no deja una fila sucia")
    void postIlegalNoDejaBasura() throws Exception {
        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"rifle","nombre":"AK-47","precio":-5,"danio":36}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El precio no puede ser negativo"));

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Cada error de la jerarquia tiene su codigo HTTP")
    void erroresTienenHandlerPorTipo() throws Exception {
        mockMvc.perform(get("/api/armas/{id}", 999999))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/armas/{id}", 999999))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"lanzamisiles","nombre":"RL","precio":100,"danio":10}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Tipo de arma desconocido: lanzamisiles"));

        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"rifle","nombre":"AK-47","precio":2700,"danio":36,"cargador":5}
                                """))
                .andExpect(status().isBadRequest());

        long id = crear(new RifleJson("AK-47", 2700, 36, 30, 90));
        mockMvc.perform(put("/api/armas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"humo","nombre":"AK-47","precio":2700,"danio":36}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("La simulacion de dano y de recarga modifica la fila en H2")
    void simulacionDeDanioYRecarga() throws Exception {
        long id = crear(new RifleJson("AK-47", 2700, 36, 2, 2));

        mockMvc.perform(post("/api/armas/{id}/disparar?distancia=10", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true));

        mockMvc.perform(post("/api/armas/{id}/disparar?distancia=10", id))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/armas/{id}/disparar?distancia=10", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Cargador vacío: hay que recargar"));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balasEnCargador").value(0))
                .andExpect(jsonPath("$.reserva").value(2));

        mockMvc.perform(post("/api/armas/{id}/recargar", id))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/armas/{id}/recargar", id))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balasEnCargador").value(2))
                .andExpect(jsonPath("$.reserva").value(0));
    }

    @Test
    @DisplayName("El endpoint de la interfaz devuelve equipables leidos de H2")
    void endpointDeInterfaz() throws Exception {
        crear(new RifleJson("AK-47", 2700, 36, 30, 90));
        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"humo","nombre":"Humo","precio":300}
                                """))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/api/armas/equipables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        JsonNode equipables = objectMapper.readTree(result.getResponse().getContentAsString());
        Set<String> ranuras = new HashSet<>();
        equipables.forEach(equipable -> ranuras.add(equipable.get("ranura").asText()));
        assertEquals(Set.of("primaria", "explosiva"), ranuras);
        assertTrue(equipables.get(0).has("mensaje"));
    }

    private long crear(RifleJson rifle) throws Exception {
        String cuerpo = """
                {"tipo":"rifle","nombre":"%s","precio":%d,"danio":%d,"cargador":%d,"reserva":%d}
                """.formatted(rifle.nombre(), rifle.precio(), rifle.danio(), rifle.cargador(), rifle.reserva());
        MvcResult result = mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private record RifleJson(String nombre, int precio, int danio, int cargador, int reserva) {
    }
}
