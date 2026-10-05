package py.edu.uc.lp3.constants;

//Clase estática que utilizamos para centralizar todas las constantes
//que utlilizaremos como parte de la API REST para el taller de CS2
public class ApiPaths {

	private static final String BASE_API = "";

	public static final String INDEX = BASE_API + "/";

	//Operaciones con armas
	public static final String ARMAS = BASE_API + "/armas";

	//Operaciones con el inventario
	public static final String INVENTARIO = BASE_API + "/inventario";

}