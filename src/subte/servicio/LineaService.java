package subte.servicio;

import java.util.List;

import subte.modelo.Linea;

public interface LineaService {

	void insertar(Linea linea);

	void actualizar(Linea linea);

	void borrar(Linea linea);

	List<Linea> buscarTodos();

}
