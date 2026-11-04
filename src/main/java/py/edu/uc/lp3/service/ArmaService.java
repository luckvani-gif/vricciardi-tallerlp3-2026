package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipable;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.domain.ResultadoRecarga;

/**
 * Servicio que construye armas y administra su ciclo de vida en H2.
 * Es el unico lugar donde se decide el tipo concreto: el resto de la
 * aplicacion trabaja siempre con el tipo padre Arma.
 */
public interface ArmaService {

	Arma crear(String tipo, String nombre, Integer precio, Integer danio, Integer cargador, Integer reserva);

	Arma guardar(Arma arma);

	List<Arma> listar();

	List<Arma> buscarPorNombre(String nombre);

	Arma obtener(Long id);

	Arma actualizar(Long id, String tipo, String nombre, Integer precio, Integer danio);

	void eliminar(Long id);

	/** Regla 1: Control de munición por disparo (consume balas). Persiste el estado. */
	ResultadoDisparo disparar(Long id, Double distancia);

	/** Regla 2: Recarga con control de reserva (no recarga si sin munición o cargador lleno). Persiste el estado. */
	ResultadoRecarga recargar(Long id);

	/** Usa la interfaz Equipable para listar objetos de familias distintas. */
	List<Equipable> equipables();

}
