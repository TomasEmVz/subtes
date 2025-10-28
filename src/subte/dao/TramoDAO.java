package subte.dao;

import java.util.List;

import subte.modelo.Tramo;

public interface TramoDAO {
	void insertar(Tramo tramo);

	void actualizar(Tramo tramo);

	void borrar(Tramo tramo);

	List<Tramo> buscarTodos();
}
