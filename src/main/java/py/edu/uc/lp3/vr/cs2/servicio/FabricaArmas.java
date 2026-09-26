package py.edu.uc.lp3.vr.cs2.servicio;

import py.edu.uc.lp3.vr.cs2.modelo.*;

/**
 * Único lugar donde se decide el tipo concreto (creación a partir de la URL).
 * Todo lo demás trabaja con el tipo padre Arma.
 */
public final class FabricaArmas {

    private FabricaArmas() {
    }

    public static Arma crear(String tipo, String nombre, Integer precio, Integer danio,
                             Integer cargador, Integer reserva) {
        return switch (tipo.toLowerCase()) {
            case "pistola" -> new Pistola(o(nombre, "Glock-18"), o(precio, 200), o(danio, 30), o(cargador, 20), o(reserva, 120));
            case "subfusil" -> new Subfusil(o(nombre, "MP9"), o(precio, 1250), o(danio, 26), o(cargador, 30), o(reserva, 120));
            case "rifle" -> new Rifle(o(nombre, "AK-47"), o(precio, 2700), o(danio, 36), o(cargador, 30), o(reserva, 90));
            case "escopeta" -> new Escopeta(o(nombre, "Nova"), o(precio, 1050), o(danio, 104), o(cargador, 8), o(reserva, 32));
            case "francotirador" -> new Francotirador(o(nombre, "AWP"), o(precio, 4750), o(danio, 115), o(cargador, 5), o(reserva, 30));
            case "humo" -> new GranadaHumo(o(nombre, "Humo"), o(precio, 300));
            case "flash" -> new GranadaFlash(o(nombre, "Flashbang"), o(precio, 200));
            case "incendiaria" -> new GranadaIncendiaria(o(nombre, "Molotov"), o(precio, 400), o(danio, 40));
            default -> throw new IllegalArgumentException("Tipo de arma desconocido: " + tipo);
        };
    }

    public static Inventario inventarioCompleto() {
        Inventario inv = new Inventario();
        for (String t : new String[]{"pistola", "subfusil", "rifle", "escopeta",
                "francotirador", "humo", "flash", "incendiaria"}) {
            inv.agregar(crear(t, null, null, null, null, null));
        }
        return inv;
    }

    private static <T> T o(T valor, T porDefecto) {
        return valor != null ? valor : porDefecto;
    }
}
