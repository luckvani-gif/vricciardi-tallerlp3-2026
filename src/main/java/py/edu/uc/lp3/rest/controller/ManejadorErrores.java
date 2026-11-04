package py.edu.uc.lp3.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import py.edu.uc.lp3.exceptions.AccesoRecursoException;
import py.edu.uc.lp3.exceptions.ApiException;
import py.edu.uc.lp3.exceptions.ConflictoDePartidaException;
import py.edu.uc.lp3.exceptions.RecursoNoEncontradoException;
import py.edu.uc.lp3.exceptions.SolicitudInvalidaException;
import py.edu.uc.lp3.exceptions.TipoNoSoportadoException;
import py.edu.uc.lp3.exceptions.ViolacionReglaException;

/**
 * Jerarquía de excepciones con un handler HTTP por tipo.
 * Base ApiException con dos hijas: ViolacionReglaException y AccesoRecursoException.
 *
 * Cada respuesta incluye "tipo" (la clase concreta de la excepción) y "codigo"
 * (el estado HTTP). Así el cliente distingue a las hermanas aunque el mensaje
 * cambie, tal como pide la colección de Insomnia.
 */
@RestControllerAdvice
public class ManejadorErrores {

    /** Cuerpo único de error: tipo de excepción, código HTTP y mensaje del dominio. */
    public record ErrorResponse(String tipo, int codigo, String error) {
    }

    private static ErrorResponse cuerpo(ApiException e, int codigo) {
        return new ErrorResponse(e.getClass().getSimpleName(), codigo, e.getMessage());
    }

    /** Hija 2: acceso a un recurso persistido que no existe -> 404 */
    @ExceptionHandler(AccesoRecursoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse accesoRecurso(AccesoRecursoException e) {
        return cuerpo(e, HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse noEncontrado(RecursoNoEncontradoException e) {
        return cuerpo(e, HttpStatus.NOT_FOUND.value());
    }

    /** Hija 1: violación de regla de negocio/validación -> 400 */
    @ExceptionHandler(ViolacionReglaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse violacionRegla(ViolacionReglaException e) {
        return cuerpo(e, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(TipoNoSoportadoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse tipoNoSoportado(TipoNoSoportadoException e) {
        return cuerpo(e, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse solicitudInvalida(SolicitudInvalidaException e) {
        return cuerpo(e, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Conflictos de estado de la partida -> 409. El cuerpo conserva la clase
     * concreta (CargadorVacioException, CambioDeTipoException, ...), así el
     * cliente distingue qué regla del juego se incumplió.
     */
    @ExceptionHandler(ConflictoDePartidaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictoDePartida(ConflictoDePartidaException e) {
        return cuerpo(e, HttpStatus.CONFLICT.value());
    }

    /** Red de seguridad: los constructores del dominio siguen lanzando IllegalArgumentException. */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse datosInvalidos(IllegalArgumentException e) {
        return new ErrorResponse(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse parametroConTipoIncorrecto(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.value(),
                "El parametro '" + e.getName() + "' tiene un tipo invalido");
    }

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse apiException(ApiException e) {
        return cuerpo(e, HttpStatus.BAD_REQUEST.value());
    }
}
