package subte.dao;

import java.util.List;

import subte.modelo.Estacion;

public interface EstacionDAO {
	void insertar(Estacion estacion);

	void actualizar(Estacion estacion);

	void borrar(Estacion estacion);

	List<Estacion> buscarTodos();
}
