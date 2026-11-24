package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.rest.dto.EscenarioCruzadoRequest;
import py.edu.uc.lp3.rest.dto.JugadaCruzadaResponse;
import py.edu.uc.lp3.service.EscenarioCruzadoService;

@RestController
@RequestMapping(ApiPaths.ESCENARIOS)
@Tag(name = "Escenario cruzado POO-09", description = "Ataque coordinado: A2 valida el disparo y A3 valida la explosión antes de guardar en el H2 de A1.")
public class EscenarioCruzadoController {

    private static final Logger LOG = LogManager.getLogger(EscenarioCruzadoController.class);

    private final EscenarioCruzadoService escenarioService;

    public EscenarioCruzadoController(EscenarioCruzadoService escenarioService) {
        this.escenarioService = escenarioService;
    }

    @PostMapping("/ataque-coordinado")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ejecutar el ataque coordinado",
            description = "Valida primero las reglas locales de A1 (ids, munición y granada); después consulta A3 (/api/explosiones/validar) y por último A2 (POST /api/armas/{id}/disparar). Solo si ambos responden 2xx se aplica la simulación local y se guarda la jugada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "A2 y A3 aceptaron; jugada persistida en H2"),
            @ApiResponse(responseCode = "400", description = "Datos del escenario inválidos; las reglas propias de A1 pueden dar 400 o 409",
                    content = @Content(schema = @Schema(implementation = ManejadorErrores.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rechazo de A2 con ese estado; `cuerpoCompanero` conserva lo que respondió. La base de A1 queda igual",
                    content = @Content(schema = @Schema(implementation = ManejadorErrores.RechazoResponse.class))),
            @ApiResponse(responseCode = "409", description = "Rechazo de A3 con ese estado; `cuerpoCompanero` conserva lo que respondió. La base de A1 queda igual",
                    content = @Content(schema = @Schema(implementation = ManejadorErrores.RechazoResponse.class))),
            @ApiResponse(responseCode = "502", description = "A2 o A3 no respondió dentro del timeout configurado; se traduce a un rechazo",
                    content = @Content(schema = @Schema(implementation = ManejadorErrores.RechazoResponse.class)))
    })
    public JugadaCruzadaResponse ejecutar(@RequestBody EscenarioCruzadoRequest solicitud) {
        LOG.debug("POST /api/escenarios/ataque-coordinado body={}", solicitud);
        JugadaCruzadaResponse respuesta = escenarioService.ejecutar(solicitud);
        LOG.debug("POST /api/escenarios/ataque-coordinado -> 201 jugadaId={}", respuesta.id());
        return respuesta;
    }

    @GetMapping("/ataque-coordinado")
    @Operation(summary = "Leer las jugadas guardadas",
            description = "Devuelve todas las jugadas persistidas; permite comprobar que un rechazo no agregó filas.")
    public List<JugadaCruzadaResponse> listar() {
        return escenarioService.listar();
    }
}
