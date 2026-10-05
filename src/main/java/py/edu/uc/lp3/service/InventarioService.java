package py.edu.uc.lp3.service;

import java.util.List;

import py.edu.uc.lp3.domain.FichaTienda;
import py.edu.uc.lp3.domain.Inventario;

/** Servicio que arma el inventario completo de la tienda y lo expone a la capa REST. */
public interface InventarioService {

	Inventario inventarioCompleto();

	List<FichaTienda> tienda();

}