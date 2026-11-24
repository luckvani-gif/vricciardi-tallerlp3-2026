package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Objects;
import java.util.Optional;

import py.edu.uc.lp3.constants.TiposArma;

@Entity
@Table(name = "armas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_arma")
public abstract class Arma {

    public static final int DISTANCIA_POR_DEFECTO = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false)
    private int precio;

    @Column(name = "danio_base", nullable = false)
    private int danioBase;

    protected Arma() {
    }

    protected Arma(String nombre, int precio, int danioBase) {
        this.nombre = nombre;
        this.precio = precio;
        this.danioBase = danioBase;
    }

    public final ResultadoDisparo disparar(double distancia) {
        if (distancia < 0) {
            return ResultadoDisparo.fallido(this, "La distancia no puede ser negativa");
        }
        Optional<String> bloqueo = motivoBloqueo();
        if (bloqueo.isPresent()) {
            return ResultadoDisparo.fallido(this, bloqueo.get());
        }
        consumirUso();
        return ResultadoDisparo.exitoso(this, calcularDanio(distancia), describirEfecto(distancia));
    }

    public final ResultadoDisparo disparar() {
        return disparar(DISTANCIA_POR_DEFECTO);
    }

    public final FichaTienda mostrarEnTienda() {
        return new FichaTienda(nombre, getTipo(), precio, danioBase, detalleTienda());
    }

    public abstract String recargar();

    public boolean esRecargable() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Transient
    @JsonProperty("nombreTipo")
    public abstract String getTipo();

    @Transient
    @JsonProperty("tipo")
    public final String getCodigoTipo() {
        return switch (getTipo()) {
            case "Pistola" -> TiposArma.PISTOLA;
            case "Subfusil" -> TiposArma.SUBFUSIL;
            case "Rifle" -> TiposArma.RIFLE;
            case "Escopeta" -> TiposArma.ESCOPETA;
            case "Francotirador" -> TiposArma.FRANCOTIRADOR;
            case "Granada de humo" -> TiposArma.HUMO;
            case "Granada flash" -> TiposArma.FLASH;
            case "Granada incendiaria" -> TiposArma.INCENDIARIA;
            case "Granada señuelo" -> TiposArma.SENAL;
            default -> getTipo().toLowerCase();
        };
    }

    public final void actualizarDatos(String nombre, int precio, int danioBase) {
        this.nombre = nombre;
        this.precio = precio;
        this.danioBase = danioBase;
    }

    @Transient
    public abstract String getEstado();

    protected abstract Optional<String> motivoBloqueo();

    protected abstract void consumirUso();

    protected abstract int calcularDanio(double distancia);

    protected abstract String describirEfecto(double distancia);

    protected abstract String detalleTienda();

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Arma otra) || id == null) {
            return false;
        }
        return Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
