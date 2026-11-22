package py.edu.uc.lp3.service;

import py.edu.uc.lp3.rest.dto.EscenarioCruzadoRequest;
import py.edu.uc.lp3.rest.dto.JugadaCruzadaResponse;

import java.util.List;

public interface EscenarioCruzadoService {

    JugadaCruzadaResponse ejecutar(EscenarioCruzadoRequest solicitud);

    List<JugadaCruzadaResponse> listar();
}
