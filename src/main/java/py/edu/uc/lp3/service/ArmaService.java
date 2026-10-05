package py.edu.uc.lp3.service;

import py.edu.uc.lp3.domain.Arma;

/**
 * Servicio que construye armas. Es el unico lugar donde se decide el tipo
 * concreto: el resto de la aplicacion trabaja siempre con el tipo padre Arma.
 */
public interface ArmaService {

	Arma crear(String tipo, String nombre, Integer precio, Integer danio, Integer cargador, Integer reserva);

}