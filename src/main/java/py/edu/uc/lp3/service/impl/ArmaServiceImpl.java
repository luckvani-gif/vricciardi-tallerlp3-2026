package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.constants.TiposArma;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.Equipable;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.GranadaFlash;
import py.edu.uc.lp3.domain.GranadaHumo;
import py.edu.uc.lp3.domain.GranadaIncendiaria;
import py.edu.uc.lp3.domain.GranadaSenal;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.domain.ResultadoRecarga;
import py.edu.uc.lp3.domain.ResultadoSimulacion;
import py.edu.uc.lp3.domain.Rifle;
import py.edu.uc.lp3.domain.Subfusil;
import py.edu.uc.lp3.exceptions.ConflictoDeEstadoException;
import py.edu.uc.lp3.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.exceptions.SolicitudInvalidaException;
import py.edu.uc.lp3.exceptions.TipoNoSoportadoException;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.service.ArmaService;

@Service
public class ArmaServiceImpl implements ArmaService {

	@Autowired
	private ArmaRepository armaRepository;

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
		if (tipo == null || tipo.isBlank()) {
			throw new TipoNoSoportadoException("El tipo de arma es obligatorio");
		}
		if ((cargador == null) != (reserva == null)) {
			throw new SolicitudInvalidaException("cargador y reserva van juntos: mandalos los dos o ninguno");
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
			case TiposArma.SENAL -> new GranadaSenal(o(nombre, "Decoy"), o(precio, 50));
			default -> throw new TipoNoSoportadoException("Tipo de arma desconocido: " + tipo);
		};
	}

	private static <T> T o(T valor, T porDefecto) {
		return valor != null ? valor : porDefecto;
	}

	@Override
	public Arma guardar(Arma arma) {
		if (arma == null) {
			throw new SolicitudInvalidaException("El arma no puede ser nula");
		}
		return armaRepository.save(arma);
	}

	@Override
	public List<Arma> listar() {
		return armaRepository.findAll();
	}

	@Override
	public List<Arma> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.isBlank()) {
			return listar();
		}
		return armaRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public List<Arma> buscar(String nombre, Integer precioMax) {
		boolean hayNombre = nombre != null && !nombre.isBlank();
		if (hayNombre && precioMax != null) {
			return armaRepository.findByNombreContainingIgnoreCaseAndPrecioLessThanEqual(nombre, precioMax);
		}
		if (hayNombre) {
			return armaRepository.findByNombreContainingIgnoreCase(nombre);
		}
		if (precioMax != null) {
			return armaRepository.findByPrecioLessThanEqual(precioMax);
		}
		return listar();
	}

	@Override
	public Arma obtener(Long id) {
		validarId(id);
		return armaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("No existe un arma con id " + id));
	}

	@Override
	public Arma actualizar(Long id, String tipo, String nombre, Integer precio, Integer danio) {
		Arma existente = obtener(id);
		if (tipo != null && !tipo.isBlank() && !tipo.equalsIgnoreCase(codigoDe(existente))) {
			throw new ConflictoDeEstadoException(
					"No se puede cambiar el tipo de un arma persistida: era " + codigoDe(existente));
		}
		if (nombre == null || nombre.isBlank() || precio == null || danio == null) {
			throw new SolicitudInvalidaException("PUT necesita nombre, precio y danio");
		}
		existente.actualizarDatos(nombre, precio, danio);
		return armaRepository.save(existente);
	}

	@Override
	public void eliminar(Long id) {
		Arma existente = obtener(id);
		armaRepository.delete(existente);
	}

	/**
	 * REGLA DE NEGOCIO 1 (persistida): Disparo consume munición/cooldown según el
	 * estado del arma. Si el arma no puede disparar se lanza ConflictoDeEstadoException
	 * y NO se persiste un estado ilegal. El cambio se guarda en H2.
	 */
	@Override
	@Transactional
	public ResultadoDisparo disparar(Long id, Double distancia) {
		Arma arma = obtener(id);
		ResultadoDisparo resultado = distancia == null ? arma.disparar() : arma.disparar(distancia);
		if (!resultado.exito()) {
			throw new ConflictoDeEstadoException(resultado.efecto());
		}
		armaRepository.save(arma);
		return resultado;
	}

	/**
	 * REGLA DE NEGOCIO 2 (persistida): la validez de la recarga la decide el
	 * dominio (ArmaDeFuego.recargar / Granada.recargar). El servicio solo persiste
	 * el estado resultante; si la regla falla, la excepción viaja al handler.
	 */
	@Override
	@Transactional
	public ResultadoRecarga recargar(Long id) {
		Arma arma = obtener(id);
		String mensaje = arma.recargar();
		armaRepository.save(arma);
		return new ResultadoRecarga(arma.getId(), arma.getNombre(), mensaje, arma.getEstado());
	}

	/**
	 * SIMULACIÓN PERSISTIDA (dos reglas juntas): involucra Arma (tipo padre),
	 * ArmaDeFuego y Granada. Dispara el arma de fuego, la recarga y lanza la
	 * granada. Es transaccional: si una regla falla, no queda un estado a medias.
	 */
	@Override
	@Transactional
	public ResultadoSimulacion simular(Long idArmaDeFuego, Long idGranada, Double distancia) {
		Arma fuego = obtener(idArmaDeFuego);
		if (!(fuego instanceof ArmaDeFuego)) {
			throw new SolicitudInvalidaException("El id " + idArmaDeFuego + " no es un arma de fuego");
		}
		Arma granada = obtener(idGranada);
		if (!(granada instanceof Granada)) {
			throw new SolicitudInvalidaException("El id " + idGranada + " no es una granada");
		}

		ResultadoDisparo disparo = distancia == null ? fuego.disparar() : fuego.disparar(distancia);
		if (!disparo.exito()) {
			throw new ConflictoDeEstadoException(disparo.efecto());
		}
		String recarga = fuego.recargar();

		ResultadoDisparo lanzamiento = distancia == null ? granada.disparar() : granada.disparar(distancia);
		if (!lanzamiento.exito()) {
			throw new ConflictoDeEstadoException(lanzamiento.efecto());
		}

		armaRepository.save(fuego);
		armaRepository.save(granada);

		return new ResultadoSimulacion(
				fuego.getId(),
				disparo,
				recarga,
				fuego.getEstado(),
				granada.getId(),
				lanzamiento,
				"Se disparó " + fuego.getNombre() + ", se recargó y se lanzó " + granada.getNombre());
	}

	@Override
	public List<Equipable> equipables() {
		return armaRepository.findAll().stream()
				.filter(Equipable.class::isInstance)
				.map(Equipable.class::cast)
				.toList();
	}

	private static void validarId(Long id) {
		if (id == null || id <= 0) {
			throw new SolicitudInvalidaException("El id debe ser un numero positivo");
		}
	}

	/** Codigo que usa el JSON de creacion; sirve para impedir cambios de tipo en PUT. */
	private static String codigoDe(Arma arma) {
		if (arma instanceof Pistola) {
			return TiposArma.PISTOLA;
		}
		if (arma instanceof Subfusil) {
			return TiposArma.SUBFUSIL;
		}
		if (arma instanceof Rifle) {
			return TiposArma.RIFLE;
		}
		if (arma instanceof Escopeta) {
			return TiposArma.ESCOPETA;
		}
		if (arma instanceof Francotirador) {
			return TiposArma.FRANCOTIRADOR;
		}
		if (arma instanceof GranadaHumo) {
			return TiposArma.HUMO;
		}
		if (arma instanceof GranadaFlash) {
			return TiposArma.FLASH;
		}
		if (arma instanceof GranadaIncendiaria) {
			return TiposArma.INCENDIARIA;
		}
		if (arma instanceof GranadaSenal) {
			return TiposArma.SENAL;
		}
		return arma.getClass().getSimpleName().toLowerCase();
	}

}
