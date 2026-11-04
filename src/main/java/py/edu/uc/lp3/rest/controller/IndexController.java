package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.constants.TiposArma;
import py.edu.uc.lp3.service.ArmaService;

/** Informacion del servicio: que tipos de arma hay y como llamarlos. */
@RestController
@RequestMapping(ApiPaths.INDEX)
public class IndexController {

	@Autowired
	private ArmaService armaService;

	@GetMapping
	public Map<String, Object> index() {
		return Map.of(
				"servicio", "Taller Git 2026 - Armas de Counter-Strike 2",
				"armasPersistidas", armaService.listar().size(),
				"tipos", TiposArma.TODOS,
				"endpoints", List.of(
						"GET " + ApiPaths.ARMAS_CRUD,
						"POST " + ApiPaths.ARMAS_CRUD,
						"GET " + ApiPaths.ARMAS_CRUD + "/{id}",
						"PUT " + ApiPaths.ARMAS_CRUD + "/{id}",
						"DELETE " + ApiPaths.ARMAS_CRUD + "/{id}",
						"POST " + ApiPaths.ARMAS_CRUD + "/{id}/disparar?distancia=10",
						"POST " + ApiPaths.ARMAS_CRUD + "/{id}/recargar",
						"POST " + ApiPaths.ARMAS_CRUD + "/simular?fuegoId={id}&granadaId={id}",
						"GET " + ApiPaths.ARMAS_CRUD + "/equipables",
						"GET " + ApiPaths.ARMAS + "/{tipo}",
						"GET " + ApiPaths.ARMAS + "/{tipo}/disparar?veces=3",
						"GET " + ApiPaths.INVENTARIO,
						"GET " + ApiPaths.INVENTARIO + "/disparar?veces=2"));
	}

}
