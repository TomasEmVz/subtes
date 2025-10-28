package subte.negocio;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import subte.controlador.Coordinador;
import subte.modelo.Estacion;
import subte.modelo.Linea;
import subte.modelo.Tramo;
import subte.servicio.EstacionService;
import subte.servicio.EstacionServiceImpl;
import subte.servicio.LineaService;
import subte.servicio.LineaServiceImpl;
import subte.servicio.TramoService;
import subte.servicio.TramoServiceImpl;

public class Empresa {

	private final static Logger logger = Logger.getLogger(Empresa.class);
	
	private static Empresa empresa = null;

	private Coordinador coordinador;
	
	private Subject subject;

	private String nombre;
	private List<Linea> lineas;
	private LineaService lineaService;
	private List<Estacion> estaciones;
	private EstacionService estacionService;
	private List<Tramo> tramos;
	private TramoService tramoService;

	public static Empresa getEmpresa() {
		if (empresa == null) {
			empresa = new Empresa();
		}
		return empresa;
	}

	private Empresa() {
		super();		
		lineas = new ArrayList<Linea>();
		lineaService = new LineaServiceImpl();
		lineas.addAll(lineaService.buscarTodos());
		estaciones = new ArrayList<Estacion>();
		estacionService = new EstacionServiceImpl();
		estaciones.addAll(estacionService.buscarTodos());
		tramos = new ArrayList<Tramo>();
		tramoService = new TramoServiceImpl();
		tramos.addAll(tramoService.buscarTodos());
	}

	public void init(Subject subject) {
		this.subject = subject;
	}
	
	public void agregarLinea(Linea linea) {
		if (lineas.contains(linea))
			throw new LineaExisteException();
		lineas.add(linea);
		lineaService.insertar(linea);
		subject.refresh();
		logger.info("Se agrega una línea");
	}

	public void modificarLinea(Linea linea) {
		int pos = lineas.indexOf(linea);
		lineas.set(pos, linea);
		lineaService.actualizar(linea);
		subject.refresh();
		logger.info("Se modifica una línea");
	}

	public void borrarLinea(Linea linea) {
		for (Estacion e : estaciones)
			if (e.getLinea().equals(linea))
				throw new LineaReferenciaException();
		Linea emp = buscarLinea(linea);
		lineas.remove(emp);
		lineaService.borrar(linea);
		subject.refresh();
		logger.info("Se borra una línea");
	}

	public Linea buscarLinea(Linea Linea) {
		int pos = lineas.indexOf(Linea);
		if (pos == -1)
			return null;
		return lineas.get(pos);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Linea> getLineas() {
		return lineas;
	}

	public List<Estacion> getEstaciones() {
		return estaciones;
	}

	public List<Tramo> getTramos() {
		return tramos;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
