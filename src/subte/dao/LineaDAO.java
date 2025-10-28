package subte.dao;

import java.util.List;

import subte.modelo.Linea;

public interface LineaDAO {
	void insertar(Linea linea);

	void actualizar(Linea linea);

	void borrar(Linea linea);

	List<Linea> buscarTodos();
}
