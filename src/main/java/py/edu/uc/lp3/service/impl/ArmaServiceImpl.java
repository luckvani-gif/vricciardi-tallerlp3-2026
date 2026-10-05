package py.edu.uc.lp3.service.impl;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.constants.TiposArma;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.GranadaFlash;
import py.edu.uc.lp3.domain.GranadaHumo;
import py.edu.uc.lp3.domain.GranadaIncendiaria;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.domain.Rifle;
import py.edu.uc.lp3.domain.Subfusil;
import py.edu.uc.lp3.service.ArmaService;

@Service
public class ArmaServiceImpl implements ArmaService {

	/*
	 * Cada case devuelve el tipo concreto del dominio (Pistola, Escopeta, ...)
	 * pero el metodo declara que devuelve Arma: quien lo usa no depende del tipo
	 * concreto, solo del padre.
	 *
	 * Cuando la URL no manda municion entra el CONSTRUCTOR SIMPLE de cada arma de
	 * fuego (el que trae su cargador y reserva de fabrica); cuando manda, entra el
	 * constructor sobrecargado. Las granadas siempre usan el simple, porque la
	 * cantidad de granadas no es un dato que se mande por la URL.
	 */
	@Override
	public Arma crear(String tipo, String nombre, Integer precio, Integer danio, Integer cargador, Integer reserva) {
		if ((cargador == null) != (reserva == null)) {
			throw new IllegalArgumentException("cargador y reserva van juntos: mandalos los dos o ninguno");
		}
		boolean simple = cargador == null;
		return switch (tipo.toLowerCase()) {
			case TiposArma.PISTOLA -> simple
					? new Pistola(o(nombre, "Glock-18"), o(precio, 200), o(danio, 30))
					: new Pistola(o(nombre, "Glock-18"), o(precio, 200), o(danio, 30), cargador, reserva);
			case TiposArma.SUBFUSIL -> simple
					? new Subfusil(o(nombre, "MP9"), o(precio, 1250), o(danio, 26))
					: new Subfusil(o(nombre, "MP9"), o(precio, 1250), o(danio, 26), cargador, reserva);
			case TiposArma.RIFLE -> simple
					? new Rifle(o(nombre, "AK-47"), o(precio, 2700), o(danio, 36))
					: new Rifle(o(nombre, "AK-47"), o(precio, 2700), o(danio, 36), cargador, reserva);
			case TiposArma.ESCOPETA -> simple
					? new Escopeta(o(nombre, "Nova"), o(precio, 1050), o(danio, 104))
					: new Escopeta(o(nombre, "Nova"), o(precio, 1050), o(danio, 104), cargador, reserva);
			case TiposArma.FRANCOTIRADOR -> simple
					? new Francotirador(o(nombre, "AWP"), o(precio, 4750), o(danio, 115))
					: new Francotirador(o(nombre, "AWP"), o(precio, 4750), o(danio, 115), cargador, reserva);
			case TiposArma.HUMO -> new GranadaHumo(o(nombre, "Humo"), o(precio, 300));
			case TiposArma.FLASH -> new GranadaFlash(o(nombre, "Flashbang"), o(precio, 200));
			case TiposArma.INCENDIARIA -> new GranadaIncendiaria(o(nombre, "Molotov"), o(precio, 400), o(danio, 40));
			default -> throw new IllegalArgumentException("Tipo de arma desconocido: " + tipo);
		};
	}

	private static <T> T o(T valor, T porDefecto) {
		return valor != null ? valor : porDefecto;
	}

}