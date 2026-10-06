package py.edu.uc.lp3.constants;

import java.util.List;

//Clase estática que utilizamos para centralizar todas las constantes
//que tienen que ver con los tipos de arma que expone el taller
public class TiposArma {

	public static final String PISTOLA = "pistola";
	public static final String SUBFUSIL = "subfusil";
	public static final String RIFLE = "rifle";
	public static final String ESCOPETA = "escopeta";
	public static final String FRANCOTIRADOR = "francotirador";
	public static final String HUMO = "humo";
	public static final String FLASH = "flash";
	public static final String INCENDIARIA = "incendiaria";

	//Lista de tipos que se ofrecen en la tienda y en /inventario
	public static final List<String> TODOS = List.of(PISTOLA, SUBFUSIL, RIFLE, ESCOPETA,
			FRANCOTIRADOR, HUMO, FLASH, INCENDIARIA);

}