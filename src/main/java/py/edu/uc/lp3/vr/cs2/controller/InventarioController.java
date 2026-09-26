package py.edu.uc.lp3.vr.cs2.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.vr.cs2.modelo.FichaTienda;
import py.edu.uc.lp3.vr.cs2.modelo.Inventario;
import py.edu.uc.lp3.vr.cs2.servicio.FabricaArmas;

/** Muestra el polimorfismo: el mismo llamado produce un comportamiento distinto por arma. */
@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @GetMapping
    public List<FichaTienda> tienda() {
        return FabricaArmas.inventarioCompleto().tienda();
    }

    @GetMapping("/disparar")
    public Map<String, Object> disparar(@RequestParam(defaultValue = "5") double distancia,
                                        @RequestParam(defaultValue = "1") int veces) {
        if (veces < 1 || veces > 20) {
            throw new IllegalArgumentException("veces debe estar entre 1 y 20");
        }
        Inventario inventario = FabricaArmas.inventarioCompleto();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        for (int i = 1; i <= veces; i++) {
            respuesta.put("ronda" + i, inventario.dispararTodas(distancia));
        }
        respuesta.put("recargas", inventario.recargarTodas());
        return respuesta;
    }
}
