package subte.servicio;

import java.util.List;

import subte.conexion.Factory;
import subte.dao.TramoDAO;
import subte.modelo.Tramo;

public class TramoServiceImpl implements TramoService {

	private TramoDAO tramoDAO; 
		
	public TramoServiceImpl(){
		tramoDAO = (TramoDAO) Factory.getInstancia("TRAMO");
	}
	
	@Override
	public void insertar(Tramo tramo) {
		tramoDAO.insertar(tramo);				
	}

	@Override
	public void actualizar(Tramo tramo) {
		tramoDAO.actualizar(tramo);						
	}

	@Override
	public void borrar(Tramo tramo) {
		tramoDAO.borrar(tramo);
		
	}

	@Override
	public List<Tramo> buscarTodos() {
		return tramoDAO.buscarTodos();
		
	}

}
