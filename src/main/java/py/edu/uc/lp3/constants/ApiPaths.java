package py.edu.uc.lp3.constants;

//Clase estática que utilizamos para centralizar todas las constantes
//que utilizaremos como parte de la API REST para el taller de CS2
public class ApiPaths {

	private static final String BASE_API = "";

	public static final String INDEX = BASE_API + "/";

	//Operaciones con armas (catálogo en memoria de POO-06)
	public static final String ARMAS = BASE_API + "/armas";

	//CRUD persistido en H2 (POO-08)
	public static final String API = BASE_API + "/api";
	public static final String ARMAS_CRUD = API + "/armas";

	//Operaciones con el inventario
	public static final String INVENTARIO = BASE_API + "/inventario";

	//Escenario propio de POO-09: consulta a A2 y A3 antes de persistir.
	public static final String ESCENARIOS = API + "/escenarios";
	public static final String ATAQUE_COORDINADO = ESCENARIOS + "/ataque-coordinado";

}
