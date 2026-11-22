package py.edu.uc.lp3.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.JugadaCruzada;
import py.edu.uc.lp3.domain.ResultadoSimulacion;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.rest.dto.EscenarioCruzadoRequest;
import py.edu.uc.lp3.rest.dto.JugadaCruzadaResponse;
import py.edu.uc.lp3.service.ArmaService;
import py.edu.uc.lp3.service.RespuestaExterna;

/**
 * Único punto que escribe en el H2 de A1 durante el escenario. Es
 * transaccional: aplica la simulación local y guarda la jugada, o no guarda
 * nada si falla alguna regla propia.
 */
@Service
public class RegistroJugadaService {

    private final ArmaService armaService;
    private final JugadaCruzadaRepository jugadaRepository;

    public RegistroJugadaService(ArmaService armaService, JugadaCruzadaRepository jugadaRepository) {
        this.armaService = armaService;
        this.jugadaRepository = jugadaRepository;
    }

    @Transactional
    public JugadaCruzadaResponse guardar(EscenarioCruzadoRequest solicitud, double distancia,
            RespuestaExterna a2, RespuestaExterna a3) {
        ResultadoSimulacion local = armaService.simular(
                solicitud.fuegoId(), solicitud.granadaId(), distancia);
        JugadaCruzada jugada = new JugadaCruzada(
                solicitud.nombre() == null || solicitud.nombre().isBlank()
                        ? "Ataque coordinado" : solicitud.nombre(),
                solicitud.fuegoId(),
                solicitud.granadaId(),
                distancia,
                solicitud.a2AtacanteId(),
                solicitud.a2ObjetivoId(),
                a2.codigo(),
                solicitud.a3GranadaId(),
                a3.codigo(),
                a2.cuerpo(),
                a3.cuerpo(),
                local.resumen());
        return mapa(jugadaRepository.save(jugada));
    }

    private static JugadaCruzadaResponse mapa(JugadaCruzada jugada) {
        return new JugadaCruzadaResponse(
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
                jugada.getResultadoLocal());
    }
}
