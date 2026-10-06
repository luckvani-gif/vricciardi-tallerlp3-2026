package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.FichaTienda;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.service.InventarioService;

/**
 * Muestra el polimorfismo: el mismo llamado produce un comportamiento distinto
 * por arma. Solo conoce el servicio, nunca las clases concretas.
 */
@RestController
@RequestMapping(ApiPaths.INVENTARIO)
public class InventarioController {

	@Autowired
	private InventarioService inventarioService;

	@GetMapping
	public List<FichaTienda> tienda() {
		return inventarioService.tienda();
	}

	@GetMapping("/disparar")
	public Map<String, Object> disparar(@RequestParam(required = false) Double distancia,
			@RequestParam(defaultValue = "1") int veces) {
		if (veces < 1 || veces > 20) {
			throw new IllegalArgumentException("veces debe estar entre 1 y 20");
		}
		Inventario inventario = inventarioService.inventarioCompleto();
		Map<String, Object> respuesta = new LinkedHashMap<>();
		for (int i = 1; i <= veces; i++) {
			// Sin distancia entra la SOBRECARGA dispararTodas()
			List<ResultadoDisparo> ronda = distancia == null
					? inventario.dispararTodas()
					: inventario.dispararTodas(distancia);
			respuesta.put("ronda" + i, ronda);
		}
		respuesta.put("recargas", inventario.recargarTodas());
		return respuesta;
	}

}