package py.edu.uc.lp3.vr.cs2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
                "servicio", "Taller Git 2026 - Armas de Counter-Strike 2",
                "tipos", List.of("pistola", "subfusil", "rifle", "escopeta",
                        "francotirador", "humo", "flash", "incendiaria"),
                "endpoints", List.of(
                        "GET /armas/{tipo}?nombre=&precio=&danio=&cargador=&reserva=",
                        "GET /armas/{tipo}/disparar?distancia=10&veces=3",
                        "GET /inventario",
                        "GET /inventario/disparar?distancia=5&veces=2"));
    }
}
