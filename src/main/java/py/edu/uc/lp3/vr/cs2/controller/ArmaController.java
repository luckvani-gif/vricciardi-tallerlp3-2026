package py.edu.uc.lp3.vr.cs2.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.vr.cs2.modelo.Arma;
import py.edu.uc.lp3.vr.cs2.modelo.FichaTienda;
import py.edu.uc.lp3.vr.cs2.modelo.ResultadoDisparo;
import py.edu.uc.lp3.vr.cs2.servicio.FabricaArmas;

/** Construye un arma con parámetros de la URL. Siempre trabaja con el tipo padre Arma. */
@RestController
@RequestMapping("/armas")
public class ArmaController {

    @GetMapping("/{tipo}")
    public FichaTienda ver(@PathVariable String tipo,
                           @RequestParam(required = false) String nombre,
                           @RequestParam(required = false) Integer precio,
                           @RequestParam(required = false) Integer danio,
                           @RequestParam(required = false) Integer cargador,
                           @RequestParam(required = false) Integer reserva) {
        Arma arma = FabricaArmas.crear(tipo, nombre, precio, danio, cargador, reserva);
        return arma.mostrarEnTienda();
    }

    @GetMapping("/{tipo}/disparar")
    public Map<String, Object> disparar(@PathVariable String tipo,
                                        @RequestParam(defaultValue = "10") double distancia,
                                        @RequestParam(defaultValue = "1") int veces,
                                        @RequestParam(required = false) String nombre,
                                        @RequestParam(required = false) Integer precio,
                                        @RequestParam(required = false) Integer danio,
                                        @RequestParam(required = false) Integer cargador,
                                        @RequestParam(required = false) Integer reserva) {
        if (veces < 1 || veces > 100) {
            throw new IllegalArgumentException("veces debe estar entre 1 y 100");
        }
        Arma arma = FabricaArmas.crear(tipo, nombre, precio, danio, cargador, reserva);
        List<ResultadoDisparo> disparos = new ArrayList<>();
        for (int i = 0; i < veces; i++) {
            disparos.add(arma.disparar(distancia));
        }
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("arma", arma.mostrarEnTienda());
        respuesta.put("disparos", disparos);
        respuesta.put("recarga", arma.recargar());
        respuesta.put("estadoFinal", arma.getEstado());
        return respuesta;
    }
}
