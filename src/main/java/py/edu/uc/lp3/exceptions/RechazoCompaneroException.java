package py.edu.uc.lp3.exceptions;

/** Rechazo ajeno traducido a la jerarquía de A1, conservando quién lo dijo. */
public class RechazoCompaneroException extends ViolacionReglaException {

    private final String companero;
    private final int codigoHttp;
    private final String cuerpoCompanero;

    public RechazoCompaneroException(String companero, int codigoHttp, String cuerpoCompanero) {
        super("Rechazo de " + companero + " (HTTP " + codigoHttp + "): " + resumen(cuerpoCompanero));
        this.companero = companero;
        this.codigoHttp = codigoHttp;
        this.cuerpoCompanero = cuerpoCompanero == null ? "" : cuerpoCompanero;
    }

    public String companero() {
        return companero;
    }

    public int codigoHttp() {
        return codigoHttp;
    }

    public String cuerpoCompanero() {
        return cuerpoCompanero;
    }

    private static String resumen(String cuerpo) {
        if (cuerpo == null || cuerpo.isBlank()) {
            return "sin cuerpo";
        }
        String plano = cuerpo.replace("\n", " ").trim();
        return plano.length() <= 180 ? plano : plano.substring(0, 180) + "…";
    }
}
