package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.constants.TiposArma;

/** Informacion del servicio: que tipos de arma hay y como llamarlos. */
@RestController
@RequestMapping(ApiPaths.INDEX)
public class IndexController {

	@GetMapping
	public Map<String, Object> index() {
		return Map.of(
				"servicio", "Taller Git 2026 - Armas de Counter-Strike 2",
				"tipos", TiposArma.TODOS,
				"endpoints", List.of(
						"GET " + ApiPaths.ARMAS + "/{tipo}?nombre=&precio=&danio=&cargador=&reserva=",
						"GET " + ApiPaths.ARMAS + "/{tipo}/disparar?veces=3",
						"GET " + ApiPaths.ARMAS + "/{tipo}/disparar?distancia=4&veces=3",
						"GET " + ApiPaths.INVENTARIO,
						"GET " + ApiPaths.INVENTARIO + "/disparar?veces=2",
						"GET " + ApiPaths.INVENTARIO + "/disparar?distancia=5&veces=2"));
	}

}