package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import py.edu.uc.lp3.constants.TiposArma;
import py.edu.uc.lp3.domain.FichaTienda;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.InventarioService;

@Service
public class InventarioServiceImpl implements InventarioService {

	@Autowired
	private ArmaService armaService;

	/*
	 * El inventario se arma con un arma de cada tipo. Todas se guardan como Arma,
	 * asi el Inventario las usa sin preguntar por su tipo concreto.
	 */
	@Override
	public Inventario inventarioCompleto() {
		Inventario inventario = new Inventario();
		for (String tipo : TiposArma.TODOS) {
			inventario.agregar(armaService.crear(tipo, null, null, null, null, null));
		}
		return inventario;
	}

	@Override
	public List<FichaTienda> tienda() {
		return inventarioCompleto().tienda();
	}

}