package py.edu.uc.lp3.domain;

public interface Equipable {

    String ranura();

    default String mensajeDeEquipado() {
        return "Se equipa en la ranura " + ranura() + ".";
    }
}
