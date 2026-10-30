package py.edu.uc.lp3.rest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import py.edu.uc.lp3.exceptions.ConflictoDeEstadoException;
import py.edu.uc.lp3.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.exceptions.SolicitudInvalidaException;
import py.edu.uc.lp3.exceptions.TipoNoSoportadoException;

/**
 * Un handler por cada tipo de la jerarquia de excepciones.
 * El controller no decide codigos HTTP: solo llama al dominio y al servicio.
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noEncontrado(RecursoNoEncontradoException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(TipoNoSoportadoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tipoNoSoportado(TipoNoSoportadoException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> solicitudInvalida(SolicitudInvalidaException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ConflictoDeEstadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictoDeEstado(ConflictoDeEstadoException e) {
        return Map.of("error", e.getMessage());
    }

    /** Red de seguridad: los constructores del dominio siguen lanzando IllegalArgumentException. */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> datosInvalidos(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> parametroConTipoIncorrecto(MethodArgumentTypeMismatchException e) {
        return Map.of("error", "El parametro '" + e.getName() + "' tiene un tipo invalido");
    }
}
