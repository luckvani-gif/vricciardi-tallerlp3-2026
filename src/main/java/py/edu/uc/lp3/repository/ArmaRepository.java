package py.edu.uc.lp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.domain.Arma;

/**
 * Repositorio de la familia persistida. Es una interfaz: Spring Data genera
 * la implementación y el controller nunca ve una clase concreta de acceso a datos.
 */
@Repository
public interface ArmaRepository extends JpaRepository<Arma, Long> {

    List<Arma> findByNombreContainingIgnoreCase(String nombre);

    List<Arma> findByPrecioLessThanEqual(int precio);
}
