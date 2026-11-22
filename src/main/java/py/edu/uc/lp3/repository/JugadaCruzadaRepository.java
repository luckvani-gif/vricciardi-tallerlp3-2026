package py.edu.uc.lp3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.domain.JugadaCruzada;

@Repository
public interface JugadaCruzadaRepository extends JpaRepository<JugadaCruzada, Long> {
}
