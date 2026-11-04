package py.edu.uc.lp3.rest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import py.edu.uc.lp3.exceptions.AccesoRecursoException;
import py.edu.uc.lp3.exceptions.ApiException;
import py.edu.uc.lp3.exceptions.ConflictoDeEstadoException;
import py.edu.uc.lp3.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.exceptions.SolicitudInvalidaException;
import py.edu.uc.lp3.exceptions.TipoNoSoportadoException;
import py.edu.uc.lp3.exceptions.ViolacionReglaException;

/**
 * Jerarquía de excepciones con un handler HTTP por tipo.
 * Base ApiException con dos hijas: ViolacionReglaException y AccesoRecursoException.
 */
@RestControllerAdvice
public class ManejadorErrores {

    /** Hija 2: recursos no encontrados -> 404 */
    @ExceptionHandler(AccesoRecursoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> accesoRecurso(AccesoRecursoException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noEncontrado(RecursoNoEncontradoException e) {
        return Map.of("error", e.getMessage());
    }

    /** Hija 1: violaciones de regla -> 400/409 */
    @ExceptionHandler(ViolacionReglaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> violacionRegla(ViolacionReglaException e) {
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

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> apiException(ApiException e) {
        return Map.of("error", e.getMessage());
    }
}
