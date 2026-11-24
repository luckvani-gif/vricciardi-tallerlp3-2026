package py.edu.uc.lp3.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Jugada cruzada persistida en A1: existe solo si A2 y A3 aceptaron. */
@Entity
@Table(name = "jugadas_cruzadas")
public class JugadaCruzada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant registradaEn;

    @Column(nullable = false)
    private String nombre;

    private Long fuegoId;
    private Long granadaId;

    @Column(nullable = false)
    private double distancia;

    private Long a2AtacanteId;
    private Long a2ObjetivoId;

    @Column(nullable = false)
    private int a2Codigo;

    private Long a3GranadaId;

    @Column(nullable = false)
    private int a3Codigo;

    @Column(length = 4000)
    private String a2Resumen;

    @Column(length = 4000)
    private String a3Resumen;

    @Column(length = 4000)
    private String resultadoLocal;

    protected JugadaCruzada() {
    }

    public JugadaCruzada(String nombre, Long fuegoId, Long granadaId, double distancia,
            Long a2AtacanteId, Long a2ObjetivoId, int a2Codigo,
            Long a3GranadaId, int a3Codigo,
            String a2Resumen, String a3Resumen, String resultadoLocal) {
        this.registradaEn = Instant.now();
        this.nombre = nombre;
        this.fuegoId = fuegoId;
        this.granadaId = granadaId;
        this.distancia = distancia;
        this.a2AtacanteId = a2AtacanteId;
        this.a2ObjetivoId = a2ObjetivoId;
        this.a2Codigo = a2Codigo;
        this.a3GranadaId = a3GranadaId;
        this.a3Codigo = a3Codigo;
        this.a2Resumen = a2Resumen;
        this.a3Resumen = a3Resumen;
        this.resultadoLocal = resultadoLocal;
    }

    public Long getId() {
        return id;
    }

    public Instant getRegistradaEn() {
        return registradaEn;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getFuegoId() {
        return fuegoId;
    }

    public Long getGranadaId() {
        return granadaId;
    }

    public double getDistancia() {
        return distancia;
    }

    public Long getA2AtacanteId() {
        return a2AtacanteId;
    }

    public Long getA2ObjetivoId() {
        return a2ObjetivoId;
    }

    public int getA2Codigo() {
        return a2Codigo;
    }

    public Long getA3GranadaId() {
        return a3GranadaId;
    }

    public int getA3Codigo() {
        return a3Codigo;
    }

    public String getA2Resumen() {
        return a2Resumen;
    }

    public String getA3Resumen() {
        return a3Resumen;
    }

    public String getResultadoLocal() {
        return resultadoLocal;
    }
}
