package py.edu.uc.lp3.rest.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.service.EntidadService;

@RestController
@RequestMapping("/entidades")
public class EntidadController {

    @Autowired
    private EntidadService entidadService;

    @PostMapping
    public ResponseEntity<Entidad> crear(@RequestBody Entidad entidad) {
        Entidad creada = entidadService.crear(entidad);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Entidad>> listar() {
        return ResponseEntity.ok(entidadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entidad> obtenerPorId(@PathVariable Long id) {
        return entidadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entidad> actualizar(@PathVariable Long id, @RequestBody Entidad entidad) {
        Entidad actualizada = entidadService.actualizar(id, entidad);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        entidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
