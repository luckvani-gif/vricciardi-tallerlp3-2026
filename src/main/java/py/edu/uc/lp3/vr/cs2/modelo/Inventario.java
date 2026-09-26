package py.edu.uc.lp3.vr.cs2.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Contiene cualquier Arma y la usa sin preguntar su tipo concreto (sin if/instanceof). */
public class Inventario {

    private final List<Arma> armas = new ArrayList<>();

    public void agregar(Arma arma) {
        armas.add(Objects.requireNonNull(arma, "El arma no puede ser null"));
    }

    public List<Arma> getArmas() {
        return Collections.unmodifiableList(armas);
    }

    public List<ResultadoDisparo> dispararTodas(double distancia) {
        return armas.stream().map(a -> a.disparar(distancia)).toList();
    }

    public List<String> recargarTodas() {
        return armas.stream().map(a -> a.getNombre() + ": " + a.recargar()).toList();
    }

    public List<FichaTienda> tienda() {
        return armas.stream().map(Arma::mostrarEnTienda).toList();
    }
}
