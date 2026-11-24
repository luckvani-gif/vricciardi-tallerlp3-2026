package py.edu.uc.lp3.service.impl;

import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.exceptions.CargadorLlenoException;
import py.edu.uc.lp3.exceptions.CargadorVacioException;
import py.edu.uc.lp3.exceptions.GranadaAgotadaException;
import py.edu.uc.lp3.exceptions.GranadaEnCooldownException;
import py.edu.uc.lp3.exceptions.GranadaNoRecargableException;
import py.edu.uc.lp3.exceptions.SinReservaException;
import py.edu.uc.lp3.exceptions.SolicitudInvalidaException;

public final class ReglasJuego {

    private ReglasJuego() {
    }

    public static void validarDatos(String nombre, Integer precio, Integer danioBase) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del arma es obligatorio");
        }
        if (precio == null || precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (danioBase == null || danioBase < 0) {
            throw new IllegalArgumentException("El daño base no puede ser negativo");
        }
    }

    public static void validarMunicion(Integer cargador, Integer reserva) {
        if (cargador == null && reserva == null) {
            return;
        }
        if (cargador == null || reserva == null) {
            throw new SolicitudInvalidaException("Cargador y reserva deben venir juntos");
        }
        if (cargador <= 0) {
            throw new SolicitudInvalidaException("El cargador debe ser mayor a 0");
        }
        if (reserva < 0) {
            throw new SolicitudInvalidaException("La reserva no puede ser negativa");
        }
    }

    public static void validarCantidad(Integer cantidad) {
        if (cantidad != null && cantidad <= 0) {
            throw new SolicitudInvalidaException("La cantidad debe ser mayor a 0");
        }
    }

    public static void validarDistancia(Double distancia) {
        if (distancia != null && distancia < 0) {
            throw new SolicitudInvalidaException("La distancia no puede ser negativa");
        }
    }

    public static void validarDisparo(Arma arma, double distancia) {
        validarDistancia(distancia);
        if (arma instanceof Granada granada) {
            if (granada.sinGranadas()) {
                throw new GranadaAgotadaException("No quedan granadas");
            }
            long restante = granada.getCooldownRestanteMs();
            if (restante > 0) {
                throw new GranadaEnCooldownException("Granada en cooldown: esperá " + restante + " ms");
            }
            return;
        }
        if (arma instanceof ArmaDeFuego fuego && fuego.getBalasEnCargador() == 0) {
            throw new CargadorVacioException("Cargador vacío: hay que recargar");
        }
    }

    public static void validarSimulacion(ArmaDeFuego fuego, Granada granada, double distancia) {
        validarDisparo(fuego, distancia);
        if (fuego.getReserva() == 0) {
            throw new SinReservaException("No hay reserva disponible");
        }
        validarDisparo(granada, distancia);
    }

    public static void validarRecarga(Arma arma) {
        if (!(arma instanceof ArmaDeFuego fuego)) {
            throw new GranadaNoRecargableException("Las granadas no se recargan: comprá otra.");
        }
        if (fuego.getBalasEnCargador() == fuego.getCapacidadCargador()) {
            throw new CargadorLlenoException("El cargador ya está lleno");
        }
        if (fuego.getReserva() == 0) {
            throw new SinReservaException("No hay reserva disponible");
        }
    }
}
