package py.edu.uc.lp3.tp2026;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.GranadaFlash;
import py.edu.uc.lp3.domain.GranadaHumo;
import py.edu.uc.lp3.domain.GranadaIncendiaria;
import py.edu.uc.lp3.domain.Inventario;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.domain.ResultadoDisparo;
import py.edu.uc.lp3.domain.Rifle;
import py.edu.uc.lp3.domain.Subfusil;
import py.edu.uc.lp3.service.impl.ArmaServiceImpl;

/**
 * Comprueba los dos mecanismos que pide el enunciado:
 * sobrecarga (mismo nombre, otra lista de argumentos) y sobreescritura
 * (misma firma, cada hija con su propia implementacion).
 */
class SobrecargaSobreescrituraTests {

    // ---- Constructores simples: dejan el arma en un estado legal ----

    @Test
    @DisplayName("Constructor simple de cada arma de fuego: deja cargador y reserva definidos")
    void constructorSimpleDejaMunicionLegal() {
        assertEquals("20/120", new Pistola("Glock-18", 200, 30).getEstado());
        assertEquals("30/120", new Subfusil("MP9", 1250, 26).getEstado());
        assertEquals("30/90", new Rifle("AK-47", 2700, 36).getEstado());
        assertEquals("8/32", new Escopeta("Nova", 1050, 104).getEstado());
        assertEquals("5/30", new Francotirador("AWP", 4750, 115).getEstado());
    }

    @Test
    @DisplayName("Constructor simple de cada granada: deja una cantidad valida")
    void constructorSimpleDeGranada() {
        assertEquals(2, new GranadaFlash("Flashbang", 200).getCantidad());
        assertEquals(1, new GranadaHumo("Humo", 300).getCantidad());
        assertEquals(1, new GranadaIncendiaria("Molotov", 400, 40).getCantidad());
    }

    // ---- Constructores sobrecargados: el llamador decide ----

    @Test
    @DisplayName("Constructor sobrecargado: la municion que pasa el llamador es la que queda")
    void constructorSobrecargadoUsaLoQuePasaElLlamador() {
        assertEquals("10/20", new Pistola("Glock-18", 200, 30, 10, 20).getEstado());
        assertEquals(5, new GranadaHumo("Humo", 300, 5).getCantidad());
        assertEquals(3, new GranadaIncendiaria("Molotov", 400, 40, 3).getCantidad());
    }

    @Test
    @DisplayName("Ninguna firma deja el arma en un estado imposible")
    void ningunaFirmaDejaEstadoImposible() {
        assertThrows(IllegalArgumentException.class, () -> new Pistola("  ", 200, 30));
        assertThrows(IllegalArgumentException.class, () -> new Pistola("Glock-18", -1, 30));
        assertThrows(IllegalArgumentException.class, () -> new Pistola("Glock-18", 200, 30, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new GranadaHumo("Humo", 300, 0));
    }

    // ---- Sobrecarga del mensaje del dominio ----

    @Test
    @DisplayName("disparar() es la sobrecarga de disparar(double): misma distancia por defecto")
    void dispararSinArgumentosUsaLaDistanciaPorDefecto() {
        Arma rifle = new Rifle("AK-47", 2700, 36);
        ResultadoDisparo sinDistancia = rifle.disparar();
        ResultadoDisparo conDistancia = new Rifle("AK-47", 2700, 36).disparar(Arma.DISTANCIA_POR_DEFECTO);

        assertEquals(Arma.DISTANCIA_POR_DEFECTO, 10);
        assertEquals(conDistancia.danio(), sinDistancia.danio());
        assertEquals(conDistancia.efecto(), sinDistancia.efecto());
        assertTrue(sinDistancia.exito());
    }

    @Test
    @DisplayName("Inventario: dispararTodas() es la sobrecarga de dispararTodas(double)")
    void inventarioSobrecargaDispararTodas() {
        Inventario inventario = new Inventario();
        inventario.agregar(new Pistola("Glock-18", 200, 30));
        inventario.agregar(new GranadaHumo("Humo", 300));

        assertEquals(2, inventario.dispararTodas().size());
        assertEquals(2, inventario.dispararTodas(25).size());
    }

    // ---- La fabrica de la capa de servicios tambien respeta las dos firmas ----

    @Test
    @DisplayName("Sin municion en la URL entra el constructor simple; con municion, el sobrecargado")
    void laFabricaUsaElConstructorQueCorresponde() {
        ArmaServiceImpl servicio = new ArmaServiceImpl();

        assertEquals("30/90", servicio.crear("rifle", null, null, null, null, null).getEstado());
        assertEquals("6/12", servicio.crear("rifle", null, null, null, 6, 12).getEstado());
    }

    @Test
    @DisplayName("Cargador y reserva tienen que venir juntos: la mitad no es un estado valido")
    void laFabricaRechazaMunicionIncompleta() {
        ArmaServiceImpl servicio = new ArmaServiceImpl();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> servicio.crear("rifle", null, null, null, 5, null));
        assertTrue(error.getMessage().contains("juntos"));
    }

    // ---- Sobreescritura: misma firma, implementacion propia en cada hija ----

    @Test
    @DisplayName("Cada hija sobreescribe factorDistancia con su propia caida de dano")
    void cadaHijaSobreescribeConSuPropiaCaida() {
        double distancia = 30;
        Arma subfusil = new Subfusil("MP9", 1250, 26);
        Arma francotirador = new Francotirador("AWP", 4750, 115);

        assertEquals((int) Math.round(26 * Math.max(0.3, 1 - distancia / 40.0)),
                subfusil.disparar(distancia).danio());
        assertEquals(115, francotirador.disparar(distancia).danio());
        assertNotEquals(subfusil.disparar(distancia).danio(), francotirador.disparar(distancia).danio());
    }

    @Test
    @DisplayName("Cada hija sobreescribe getTipo: el JSON de cada una sale de ahi")
    void cadaHijaSobreescribeGetTipo() {
        assertEquals("Pistola", new Pistola("Glock-18", 200, 30).getTipo());
        assertEquals("Escopeta", new Escopeta("Nova", 1050, 104).getTipo());
        assertEquals("Granada de humo", new GranadaHumo("Humo", 300).getTipo());
    }

    @Test
    @DisplayName("El padre no pregunta el tipo concreto: el Inventario usa polimorfismo")
    void elInventarioHablaConElTipoPadre() {
        Inventario inventario = new Inventario();
        inventario.agregar(new Escopeta("Nova", 1050, 104));
        inventario.agregar(new Francotirador("AWP", 4750, 115));

        // Sin if ni instanceof: el comportamiento sale del metodo sobreescrito
        assertEquals("Escopeta", inventario.dispararTodas(4).get(0).tipo());
        assertEquals("Francotirador", inventario.dispararTodas(4).get(1).tipo());
    }
}