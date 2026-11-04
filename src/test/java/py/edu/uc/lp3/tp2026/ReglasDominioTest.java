package py.edu.uc.lp3.tp2026;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.GranadaHumo;
import py.edu.uc.lp3.domain.Rifle;
import py.edu.uc.lp3.exceptions.ConflictoDeEstadoException;

/**
 * Las dos reglas de negocio viven en el dominio: el servicio no las inventa.
 * Estas pruebas las ejercitan sin pasar por HTTP.
 */
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
        ArmaDeFuego llena = new Rifle("AK-47", 2700, 36, 30, 90);
        assertThrows(ConflictoDeEstadoException.class, llena::recargar);

        ArmaDeFuego sinReserva = new Rifle("AK-47", 2700, 36, 2, 0);
        sinReserva.disparar(10);
        assertThrows(ConflictoDeEstadoException.class, sinReserva::recargar);
    }

    @Test
    @DisplayName("Regla 2: recargar toma de la reserva solo lo que falta")
    void recargarTomaDeLaReserva() {
        ArmaDeFuego rifle = new Rifle("AK-47", 2700, 36, 5, 15);
        rifle.disparar(10);

        assertEquals("Recargó 1 balas (5/14)", rifle.recargar());
        assertEquals("5/14", rifle.getEstado());
        assertThrows(ConflictoDeEstadoException.class, rifle::recargar);
    }

    @Test
    @DisplayName("Las granadas no son recargables: la excepción la lanza el dominio")
    void granadaNoSeRecarga() {
        GranadaHumo humo = new GranadaHumo("Humo", 300);
        assertFalse(humo.esRecargable());
        assertThrows(ConflictoDeEstadoException.class, humo::recargar);
    }
}
