package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
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

    private final EscenarioCruzadoService escenarioService;

    public EscenarioCruzadoController(EscenarioCruzadoService escenarioService) {
        this.escenarioService = escenarioService;
    }

    @PostMapping("/ataque-coordinado")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ejecutar el ataque coordinado",
            description = "Valida primero con A3 (/api/explosiones/validar) y después con A2 (POST /api/armas/{id}/disparar). Sólo si ambos responden 2xx se aplica la simulación local y se guarda la jugada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "A2 y A3 aceptaron; jugada persistida en H2"),
            @ApiResponse(responseCode = "400", description = "Datos del escenario inválidos o regla propia de A1 incumplida"),
            @ApiResponse(responseCode = "404", description = "Rechazo de A2 o id local inexistente; la base de A1 queda igual"),
            @ApiResponse(responseCode = "409", description = "Rechazo de A3 o conflicto de la partida local; la base de A1 queda igual"),
            @ApiResponse(responseCode = "502", description = "A2 o A3 no respondió dentro del timeout configurado")
    })
    public JugadaCruzadaResponse ejecutar(@RequestBody EscenarioCruzadoRequest solicitud) {
        return escenarioService.ejecutar(solicitud);
    }

    @GetMapping("/ataque-coordinado")
    @Operation(summary = "Leer las jugadas guardadas",
            description = "Devuelve todas las jugadas persistidas; permite comprobar que un rechazo no agregó filas.")
    public List<JugadaCruzadaResponse> listar() {
        return escenarioService.listar();
    }
}
