package subte.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Position;
import net.datastructures.PositionalList;
import net.datastructures.TreeMap;
import net.datastructures.Vertex;
import subte.controlador.Coordinador;
import subte.modelo.Estacion;
import subte.modelo.Tramo;

public class Calculo implements Observer {

	private final static Logger logger = Logger.getLogger(Calculo.class);

	private Coordinador coordinador;

	private Subject subject;
	private boolean actualizar;

	private Graph<Estacion, Tramo> subte;
	private TreeMap<String, Vertex<Estacion>> vertices;
	private Map<Integer, Double> congestiones;
	private static final int MAX_TIEMPO = 1000;

	public Calculo() {
	}

	public void init(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
		this.actualizar = true;
		congestiones = coordinador.getCongestion();
	}

	public void cargarDatos() {

		if (actualizar) {
			
			List<Tramo> tramos = coordinador.listarTramos();
			List<Estacion> est = coordinador.listarEstaciones();

			TreeMap<String, Estacion> estaciones = new TreeMap<String, Estacion>();
			for (Estacion e : est)
				estaciones.put(e.getCodigo(), e);

			subte = new AdjacencyMapGraph<>(false);

			// Cargar estaciones
			vertices = new TreeMap<String, Vertex<Estacion>>();
			for (Entry<String, Estacion> estacion : estaciones.entrySet())
				vertices.put(estacion.getKey(), subte.insertVertex(estacion.getValue()));

			// Cargar tramos
			for (Tramo tramo : tramos)
				subte.insertEdge(vertices.get(tramo.getEstacion1().getCodigo()),
						vertices.get(tramo.getEstacion2().getCodigo()), tramo);

			actualizar = false;
			logger.info("Se actualizaron los datos para realizar calculos");
		} else
			logger.info("No se actualizaron los datos");
	}

	public List<Tramo> masRapido(Estacion estacion1, Estacion estacion2) {
		// crear el grafo
		cargarDatos();
		
		// copia grafo
		Graph<Estacion, Integer> grafo = new AdjacencyMapGraph<>(false);
		Map<Estacion, Vertex<Estacion>> res = new HashMap<>();

		for (Vertex<Estacion> result : subte.vertices())
			res.put(result.getElement(), grafo.insertVertex(result.getElement()));

		Vertex<Estacion>[] vert;

		for (Edge<Tramo> result : subte.edges()) {
			vert = subte.endVertices(result);
			grafo.insertEdge(res.get(vert[0].getElement()), res.get(vert[1].getElement()),
					result.getElement().getTiempo());
		}
		PositionalList<Vertex<Estacion>> lista = GraphAlgorithms.shortestPathList(grafo, res.get(estacion1),
				res.get(estacion2));

		return obtenerTramos(lista);
	}

	public List<Tramo> menosCongestion(Estacion estacion1, Estacion estacion2) {
		// crear el grafo
		cargarDatos();

		// copia grafo
		Graph<Estacion, Integer> grafo = new AdjacencyMapGraph<>(false);
		Map<Estacion, Vertex<Estacion>> res = new HashMap<>();

		for (Vertex<Estacion> result : subte.vertices())
			res.put(result.getElement(), grafo.insertVertex(result.getElement()));

		Vertex<Estacion>[] vert;
		double tiempo, congestion;
		int indiceCongestion, tiempoFinal;
		for (Edge<Tramo> result : subte.edges()) {
			vert = subte.endVertices(result);
			tiempo = result.getElement().getTiempo();
			indiceCongestion = result.getElement().getCongestion();
			congestion = (tiempo * (congestiones.get(indiceCongestion)));
			tiempoFinal = (int) Math.round(congestion);
			grafo.insertEdge(res.get(vert[0].getElement()), res.get(vert[1].getElement()), tiempoFinal);
		}
		PositionalList<Vertex<Estacion>> lista = GraphAlgorithms.shortestPathList(grafo, res.get(estacion1),
				res.get(estacion2));

		return obtenerTramos(lista);
	}

	public List<Tramo> menosTrasbordo(Estacion estacion1, Estacion estacion2) {
		// crear el grafo
		cargarDatos();

		// copia grafo
		Graph<Estacion, Integer> grafo = new AdjacencyMapGraph<>(false);
		Map<Estacion, Vertex<Estacion>> res = new HashMap<>();

		for (Vertex<Estacion> result : subte.vertices())
			res.put(result.getElement(), grafo.insertVertex(result.getElement()));

		Vertex<Estacion>[] vert;

		int tiempo;
		for (Edge<Tramo> result : subte.edges()) {
			vert = subte.endVertices(result);
			if (vert[0].getElement().getLinea().equals(vert[1].getElement().getLinea()))
				tiempo = 1;
			else
				tiempo = MAX_TIEMPO;
			grafo.insertEdge(res.get(vert[0].getElement()), res.get(vert[1].getElement()), tiempo);
		}

		PositionalList<Vertex<Estacion>> lista = GraphAlgorithms.shortestPathList(grafo, res.get(estacion1),
				res.get(estacion2));

		return obtenerTramos(lista);
	}

	private List<Tramo> obtenerTramos(PositionalList<Vertex<Estacion>> lista) {

		List<Tramo> tramos = new ArrayList<Tramo>();

		Vertex<Estacion> v1, v2;
		Position<Vertex<Estacion>> aux = lista.first();
		while (lista.after(aux) != null) {
			v1 = aux.getElement();
			aux = lista.after(aux);
			v2 = aux.getElement();
			tramos.add(
					subte.getEdge(vertices.get(v1.getElement().getCodigo()), vertices.get(v2.getElement().getCodigo()))
							.getElement());
		}
		return tramos;
	}

	@Override
	public void update() {
		actualizar = true;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
