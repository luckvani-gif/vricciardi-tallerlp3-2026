package py.edu.uc.lp3.tp2026;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.GranadaSenal;
import py.edu.uc.lp3.domain.ResultadoDisparo;

class GranadaSenalTest {

    @Test
    void constructorSimpleDejaUnaGranadaDisponible() {
        GranadaSenal granada = new GranadaSenal("Decoy", 50);

        assertEquals(1, granada.getCantidad());
        assertEquals("Granada señuelo", granada.getTipo());
    }

    @Test
    void constructorSobrecargadoUsaLaCantidadIndicada() {
        GranadaSenal granada = new GranadaSenal("Decoy", 50, 3);

        assertEquals(3, granada.getCantidad());
    }

    @Test
    void granadaSenalNoHaceDanio() {
        GranadaSenal granada = new GranadaSenal("Decoy", 50);

        ResultadoDisparo resultado = granada.disparar(5);

        assertEquals(0, resultado.danio());
    }

    @Test
    void granadaSenalTieneEfectoPropio() {
        GranadaSenal granada = new GranadaSenal("Decoy", 50);

        ResultadoDisparo resultado = granada.disparar(5);

        assertEquals(
            "Genera ruido para distraer al enemigo",
            resultado.efecto()
        );
    }
}