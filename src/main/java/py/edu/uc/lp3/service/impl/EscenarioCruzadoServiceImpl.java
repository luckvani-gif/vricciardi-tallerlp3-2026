package py.edu.uc.lp3.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.exceptions.RechazoCompaneroException;
import py.edu.uc.lp3.exceptions.SolicitudInvalidaException;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.rest.dto.EscenarioCruzadoRequest;
import py.edu.uc.lp3.rest.dto.JugadaCruzadaResponse;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.CompaneroClient;
import py.edu.uc.lp3.service.EscenarioCruzadoService;
import py.edu.uc.lp3.service.RespuestaExterna;

@Service
public class EscenarioCruzadoServiceImpl implements EscenarioCruzadoService {

    private final ArmaService armaService;
    private final CompaneroClient companeroClient;
    private final RegistroJugadaService registroJugadaService;
    private final JugadaCruzadaRepository jugadaRepository;

    public EscenarioCruzadoServiceImpl(ArmaService armaService,
            CompaneroClient companeroClient,
            RegistroJugadaService registroJugadaService,
            JugadaCruzadaRepository jugadaRepository) {
        this.armaService = armaService;
        this.companeroClient = companeroClient;
        this.registroJugadaService = registroJugadaService;
        this.jugadaRepository = jugadaRepository;
    }

    @Override
    public JugadaCruzadaResponse ejecutar(EscenarioCruzadoRequest solicitud) {
        validar(solicitud);
        double distancia = solicitud.distancia() == null ? 10.0 : solicitud.distancia();
        double x = solicitud.x() == null ? 0.0 : solicitud.x();
        double y = solicitud.y() == null ? 0.0 : solicitud.y();
        double z = solicitud.z() == null ? 0.0 : solicitud.z();
        boolean critico = Boolean.TRUE.equals(solicitud.critico());

        if (!(armaService.obtener(solicitud.fuegoId()) instanceof ArmaDeFuego)) {
            throw new SolicitudInvalidaException("fuegoId no es un arma de fuego de A1");
        }
        if (!(armaService.obtener(solicitud.granadaId()) instanceof Granada)) {
            throw new SolicitudInvalidaException("granadaId no es una granada de A1");
        }

        RespuestaExterna a3 = companeroClient.validarExplosionA3(
                solicitud.a3GranadaId(), x, y, z);
        if (!a3.ok()) {
            throw new RechazoCompaneroException("A3", a3.codigo(), a3.cuerpo());
        }

        RespuestaExterna a2 = companeroClient.validarDisparoA2(
                solicitud.a2AtacanteId(), solicitud.a2ObjetivoId(), distancia, critico);
        if (!a2.ok()) {
            throw new RechazoCompaneroException("A2", a2.codigo(), a2.cuerpo());
        }

        return registroJugadaService.guardar(solicitud, distancia, a2, a3);
    }

    @Override
    public List<JugadaCruzadaResponse> listar() {
        return jugadaRepository.findAll().stream()
                .map(jugada -> new JugadaCruzadaResponse(
                        jugada.getId(),
                        jugada.getRegistradaEn(),
                        jugada.getNombre(),
                        jugada.getFuegoId(),
                        jugada.getGranadaId(),
                        jugada.getDistancia(),
                        jugada.getA2AtacanteId(),
                        jugada.getA2ObjetivoId(),
                        jugada.getA2Codigo(),
                        jugada.getA3GranadaId(),
                        jugada.getA3Codigo(),
                        jugada.getA2Resumen(),
                        jugada.getA3Resumen(),
                        jugada.getResultadoLocal()))
                .toList();
    }

    private static void validar(EscenarioCruzadoRequest solicitud) {
        requerido(solicitud.fuegoId(), "fuegoId");
        requerido(solicitud.granadaId(), "granadaId");
        requerido(solicitud.a2AtacanteId(), "a2AtacanteId");
        requerido(solicitud.a2ObjetivoId(), "a2ObjetivoId");
        requerido(solicitud.a3GranadaId(), "a3GranadaId");
        if (solicitud.distancia() != null && solicitud.distancia() < 0) {
            throw new SolicitudInvalidaException("distancia no puede ser negativa");
        }
    }

    private static void requerido(Long valor, String nombre) {
        if (valor == null) {
            throw new SolicitudInvalidaException(nombre + " es obligatorio");
        }
    }
}
