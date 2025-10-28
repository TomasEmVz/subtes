package subte.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;

import subte.dao.EstacionDAO;
import subte.dao.LineaDAO;
import subte.modelo.Estacion;
import subte.modelo.Linea;

public class EstacionSecuencialDAO implements EstacionDAO {

	private List<Estacion> list;
	private String name;
	private Hashtable<String, Linea> lineas;
	private boolean actualizar;

	public EstacionSecuencialDAO() {
		lineas = cargarLineas();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("estacion");
		actualizar = true;
	}

	private List<Estacion> readFromFile(String file) {

		List<Estacion> list = new ArrayList<>();
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(file));
			inFile.useDelimiter("\\s*;\\s*");
			while (inFile.hasNext()) {
				Estacion e = new Estacion();
				e.setCodigo(inFile.next());
				e.setNombre(inFile.next());
				e.setLinea(lineas.get(inFile.next()));
				list.add(e);
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error opening file.");
			fileNotFoundException.printStackTrace();
		} catch (NoSuchElementException noSuchElementException) {
			System.err.println("Error in file record structure");
			noSuchElementException.printStackTrace();
		} catch (IllegalStateException illegalStateException) {
			System.err.println("Error reading from file.");
			illegalStateException.printStackTrace();
		} finally {
			if (inFile != null)
				inFile.close();
		}
		return list;
	}

	private void writeToFile(List<Estacion> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Estacion e : list) {
				outFile.format("%s;%s;%s;\n", e.getCodigo(), e.getNombre(), e.getLinea().getCodigo());
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error creating file.");
		} catch (FormatterClosedException formatterClosedException) {
			System.err.println("Error writing to file.");
		} finally {
			if (outFile != null)
				outFile.close();
		}
	}

	@Override
	public List<Estacion> buscarTodos() {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

	@Override
	public void insertar(Estacion estacion) {
		list.add(estacion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Estacion estacion) {
		int pos = list.indexOf(estacion);
		list.set(pos, estacion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void borrar(Estacion estacion) {
		list.remove(estacion);
		writeToFile(list, name);
		actualizar = true;
	}

	private Hashtable<String, Linea> cargarLineas() {
		Hashtable<String, Linea> lineas = new Hashtable<String, Linea>();
		LineaDAO lineaDAO = new LineaSecuencialDAO();
		List<Linea> ds = lineaDAO.buscarTodos();
		for (Linea d : ds)
			lineas.put(d.getCodigo(), d);
		return lineas;
	}
}
