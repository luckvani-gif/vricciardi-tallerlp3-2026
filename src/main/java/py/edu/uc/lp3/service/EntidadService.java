package py.edu.uc.lp3.service;

import java.util.List;
import java.util.Optional;

import py.edu.uc.lp3.domain.Entidad;

public interface EntidadService {

    Entidad crear(Entidad entidad);

    List<Entidad> listar();

    Optional<Entidad> obtenerPorId(Long id);

    Entidad actualizar(Long id, Entidad entidad);

    void eliminar(Long id);
}
