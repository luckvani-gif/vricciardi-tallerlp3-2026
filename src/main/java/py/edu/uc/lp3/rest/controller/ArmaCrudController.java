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

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipable;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.domain.ResultadoRecarga;
import py.edu.uc.lp3.rest.dto.ArmaRequest;
import py.edu.uc.lp3.service.ArmaService;

/**
 * CRUD REST de la familia persistida en H2.
 * La URL nombra el recurso (/api/armas) y el verbo HTTP dice que hacer.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS_CRUD)
public class ArmaCrudController {

    @Autowired
    private ArmaService armaService;

    @PostMapping
    public ResponseEntity<Arma> crear(@RequestBody ArmaRequest solicitud) {
        Arma creada = armaService.crear(
                solicitud.tipo(), solicitud.nombre(), solicitud.precio(),
                solicitud.danio(), solicitud.cargador(), solicitud.reserva());
        return ResponseEntity.status(HttpStatus.CREATED).body(armaService.guardar(creada));
    }

    @GetMapping
    public List<Arma> listar(@RequestParam(required = false) String nombre) {
        return armaService.buscarPorNombre(nombre);
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
    public ResultadoDisparo disparar(@PathVariable Long id,
            @RequestParam(required = false) Double distancia) {
        return armaService.disparar(id, distancia);
    }

    @PostMapping("/{id}/recargar")
    public ResultadoRecarga recargar(@PathVariable Long id) {
        return armaService.recargar(id);
    }
}
