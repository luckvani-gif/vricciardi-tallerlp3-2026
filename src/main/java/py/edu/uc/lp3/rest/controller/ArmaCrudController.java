package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipable;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.domain.ResultadoRecarga;
import py.edu.uc.lp3.domain.ResultadoSimulacion;
import py.edu.uc.lp3.rest.dto.ArmaRequest;
import py.edu.uc.lp3.service.ArmaService;

/**
 * CRUD REST de la familia persistida en H2.
 * La URL nombra el recurso (/api/armas) y el verbo HTTP dice que hacer.
 * Trabaja con tres clases persistidas: Arma (padre), ArmaDeFuego y Granada.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS_CRUD)
@Tag(name = "Armas - CRUD y reglas", description = "CRUD persistido, regla 1 (consumo de munición/cooldown) y regla 2 (recarga con reserva).")
public class ArmaCrudController {

    @Autowired
    private ArmaService armaService;

    @PostMapping
    @Operation(summary = "Crear arma", description = "Discriminador `tipo`: rifle, pistola, subfusil, escopeta, francotirador, humo, flash, incendiaria o senal. `cargador` y `reserva` van juntos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Arma creada y persistida"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, tipo desconocido o cargador sin reserva") })
    public ResponseEntity<Arma> crear(@RequestBody ArmaRequest solicitud) {
        Arma creada = armaService.crear(
                solicitud.tipo(), solicitud.nombre(), solicitud.precio(),
                solicitud.danio(), solicitud.cargador(), solicitud.reserva());
        return ResponseEntity.status(HttpStatus.CREATED).body(armaService.guardar(creada));
    }

    @GetMapping
    public List<Arma> listar(@RequestParam(required = false) String nombre,
                             @RequestParam(required = false) Integer precioMax) {
        return armaService.buscar(nombre, precioMax);
    }

    @GetMapping("/equipables")
    public List<Map<String, Object>> equipables() {
        List<Equipable> equipables = armaService.equipables();
        return equipables.stream().map(equipable -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("clase", equipable.getClass().getSimpleName());
            item.put("tipoDeInterfaz", Equipable.class.getSimpleName());
            item.put("ranura", equipable.ranura());
            item.put("mensaje", equipable.mensajeDeEquipado());
            return item;
        }).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener arma por id", description = "Devuelve el arma persistida con su estado actual de munición o granadas.")
    @ApiResponse(responseCode = "404", description = "No existe un arma con ese id")
    public Arma obtener(@PathVariable Long id) {
        return armaService.obtener(id);
    }

    @PutMapping("/{id}")
    public Arma actualizar(@PathVariable Long id, @RequestBody ArmaRequest solicitud) {
        return armaService.actualizar(id, solicitud.tipo(), solicitud.nombre(),
                solicitud.precio(), solicitud.danio());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        armaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disparar")
    @Operation(summary = "Regla 1: disparar o lanzar", description = "Consume una bala o una granada y calcula el daño según la distancia.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disparo ejecutado y estado persistido"),
            @ApiResponse(responseCode = "400", description = "Distancia negativa"),
            @ApiResponse(responseCode = "409", description = "CargadorVacioException, GranadaAgotadaException o GranadaEnCooldownException") })
    public ResultadoDisparo disparar(@PathVariable Long id,
            @RequestParam(required = false) Double distancia) {
        return armaService.disparar(id, distancia);
    }

    @PostMapping("/{id}/recargar")
    @Operation(summary = "Regla 2: recargar", description = "Pasa munición de la reserva al cargador; las granadas no se recargan.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recarga persistida"),
            @ApiResponse(responseCode = "409", description = "CargadorLlenoException, SinReservaException o GranadaNoRecargableException") })
    public ResultadoRecarga recargar(@PathVariable Long id) {
        return armaService.recargar(id);
    }

    @PostMapping("/simular")
    @Operation(summary = "Simulación local", description = "Dispara y recarga un arma de fuego, y lanza una granada. Transaccional: si falla una regla, no queda estado a medias.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Simulación persistida"),
            @ApiResponse(responseCode = "400", description = "Ids inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de alguna de las dos reglas") })
    public ResultadoSimulacion simular(@RequestParam Long fuegoId,
            @RequestParam Long granadaId,
            @RequestParam(required = false) Double distancia) {
        return armaService.simular(fuegoId, granadaId, distancia);
    }
}
