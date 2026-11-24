package py.edu.uc.lp3.tp2026;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.GranadaHumo;
import py.edu.uc.lp3.domain.Rifle;
import py.edu.uc.lp3.exceptions.CargadorLlenoException;
import py.edu.uc.lp3.exceptions.GranadaNoRecargableException;
import py.edu.uc.lp3.exceptions.SinReservaException;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.service.impl.ArmaServiceImpl;

class ReglasDominioTest {

    @Test
    @DisplayName("Regla 1: disparar consume una bala y no deja el cargador negativo")
    void dispararConsumeMunicion() {
        Arma rifle = new Rifle("AK-47", 2700, 36, 2, 10);

        assertTrue(rifle.disparar(10).exito());
        assertEquals("1/10", rifle.getEstado());
        assertTrue(rifle.disparar(10).exito());
        assertEquals("0/10", rifle.getEstado());
        assertFalse(rifle.disparar(10).exito());
        assertEquals("0/10", rifle.getEstado());
    }

    @Test
    @DisplayName("Regla 2: no se recarga un cargador lleno ni sin reserva")
    void recargaRespetaSusLimites() {
        ArmaRepository repo = mock(ArmaRepository.class);
        ArmaServiceImpl servicio = servicioCon(repo);

        ArmaDeFuego llena = new Rifle("AK-47", 2700, 36, 30, 90);
        when(repo.findById(1L)).thenReturn(Optional.of(llena));
        assertThrows(CargadorLlenoException.class, () -> servicio.recargar(1L));

        ArmaDeFuego sinReserva = new Rifle("AK-47", 2700, 36, 2, 0);
        sinReserva.disparar(10);
        sinReserva.disparar(10);
        when(repo.findById(2L)).thenReturn(Optional.of(sinReserva));
        assertThrows(SinReservaException.class, () -> servicio.recargar(2L));
    }

    @Test
    @DisplayName("Regla 2: recargar toma de la reserva solo lo que falta")
    void recargarTomaDeLaReserva() {
        ArmaRepository repo = mock(ArmaRepository.class);
        ArmaServiceImpl servicio = servicioCon(repo);

        ArmaDeFuego rifle = new Rifle("AK-47", 2700, 36, 5, 15);
        rifle.disparar(10);
        when(repo.findById(1L)).thenReturn(Optional.of(rifle));
        when(repo.save(rifle)).thenReturn(rifle);

        assertEquals("Recargó 1 balas (5/14)", servicio.recargar(1L).mensaje());
        assertEquals("5/14", rifle.getEstado());
        assertThrows(CargadorLlenoException.class, () -> servicio.recargar(1L));
    }

    @Test
    @DisplayName("Las granadas no son recargables: la excepción la lanza el servicio")
    void granadaNoSeRecarga() {
        ArmaRepository repo = mock(ArmaRepository.class);
        ArmaServiceImpl servicio = servicioCon(repo);

        GranadaHumo humo = new GranadaHumo("Humo", 300);
        when(repo.findById(1L)).thenReturn(Optional.of(humo));

        assertFalse(humo.esRecargable());
        assertThrows(GranadaNoRecargableException.class, () -> servicio.recargar(1L));
    }

    private static ArmaServiceImpl servicioCon(ArmaRepository repo) {
        ArmaServiceImpl servicio = new ArmaServiceImpl();
        try {
            var campo = ArmaServiceImpl.class.getDeclaredField("armaRepository");
            campo.setAccessible(true);
            campo.set(servicio, repo);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return servicio;
    }
}
