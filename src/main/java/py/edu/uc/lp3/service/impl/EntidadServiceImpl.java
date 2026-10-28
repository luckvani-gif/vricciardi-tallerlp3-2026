package py.edu.uc.lp3.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.service.EntidadService;

@Service
@Transactional
public class EntidadServiceImpl implements EntidadService {

    @Autowired
    private EntidadRepository entidadRepository;

    @Override
    public Entidad crear(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la entidad es obligatorio");
        }
        entidad.setId(null);
        return entidadRepository.save(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entidad> listar() {
        return entidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Entidad> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser valido");
        }
        return entidadRepository.findById(id);
    }

    @Override
    public Entidad actualizar(Long id, Entidad entidad) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser valido");
        }
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la entidad es obligatorio");
        }
        Entidad existente = entidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entidad no encontrada con id: " + id));
        existente.setNombre(entidad.getNombre());
        existente.setDescripcion(entidad.getDescripcion());
        return entidadRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser valido");
        }
        if (!entidadRepository.existsById(id)) {
            throw new IllegalArgumentException("Entidad no encontrada con id: " + id);
        }
        entidadRepository.deleteById(id);
    }
}
