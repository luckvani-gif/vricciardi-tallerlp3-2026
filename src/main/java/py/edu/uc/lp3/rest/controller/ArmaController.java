package py.edu.uc.lp3.rest.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.constants.ApiPaths;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.FichaTienda;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.service.ArmaService;

/**
 * Construye un arma con parametros de la URL. Siempre trabaja con el tipo padre
 * Arma: el controller nunca menciona una clase concreta del dominio.
 */
@RestController
@RequestMapping(ApiPaths.ARMAS)
public class ArmaController {

	// Simulamos el design pattern de Controller-Service del template
	@Autowired
	private ArmaService armaService;

	@GetMapping("/{tipo}")
	public FichaTienda ver(@PathVariable String tipo,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) Integer precio,
			@RequestParam(required = false) Integer danio,
			@RequestParam(required = false) Integer cargador,
			@RequestParam(required = false) Integer reserva) {
		Arma arma = armaService.crear(tipo, nombre, precio, danio, cargador, reserva);
		return arma.mostrarEnTienda();
	}

	@GetMapping("/{tipo}/disparar")
	public Map<String, Object> disparar(@PathVariable String tipo,
			@RequestParam(required = false) Double distancia,
			@RequestParam(defaultValue = "1") int veces,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) Integer precio,
			@RequestParam(required = false) Integer danio,
			@RequestParam(required = false) Integer cargador,
			@RequestParam(required = false) Integer reserva) {
		if (veces < 1 || veces > 100) {
			throw new IllegalArgumentException("veces debe estar entre 1 y 100");
		}
		Arma arma = armaService.crear(tipo, nombre, precio, danio, cargador, reserva);
		List<ResultadoDisparo> disparos = new ArrayList<>();
		for (int i = 0; i < veces; i++) {
			// Sin distancia en la URL entra la SOBRECARGA disparar(); con distancia,
			// la versión con argumento. El dominio decide en los dos casos.
			disparos.add(distancia == null ? arma.disparar() : arma.disparar(distancia));
		}
		Map<String, Object> respuesta = new LinkedHashMap<>();
		respuesta.put("arma", arma.mostrarEnTienda());
		respuesta.put("disparos", disparos);
		respuesta.put("recarga", arma.recargar());
		respuesta.put("estadoFinal", arma.getEstado());
		return respuesta;
	}

}