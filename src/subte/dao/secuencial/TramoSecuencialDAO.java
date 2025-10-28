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
import subte.dao.TramoDAO;
import subte.modelo.Estacion;
import subte.modelo.Tramo;

public class TramoSecuencialDAO implements TramoDAO {

	private List<Tramo> list;
	private String name;
	private Hashtable<String, Estacion> estaciones;
	private boolean actualizar;

	public TramoSecuencialDAO() {
		estaciones = cargarEstaciones();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("tramo");
		actualizar = true;
	}

	private List<Tramo> readFromFile(String file) {
		List<Tramo> list = new ArrayList<>();
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(file));
			inFile.useDelimiter("\\s*;\\s*");
			while (inFile.hasNext()) {
				Tramo e = new Tramo();
				e.setEstacion1(estaciones.get(inFile.next()));
				e.setEstacion2(estaciones.get(inFile.next()));
				e.setTiempo(inFile.nextInt());
				e.setCongestion(inFile.nextInt());
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

	private void writeToFile(List<Tramo> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Tramo e : list) {
				outFile.format("%s;%s;%d;%d;\n", e.getEstacion1().getCodigo(), e.getEstacion2().getCodigo(),
						e.getTiempo(), e.getCongestion());
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
	public List<Tramo> buscarTodos() {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

	@Override
	public void insertar(Tramo tramo) {
		list.add(tramo);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Tramo tramo) {
		int pos = list.indexOf(tramo);
		list.set(pos, tramo);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void borrar(Tramo tramo) {
		list.remove(tramo);
		writeToFile(list, name);
		actualizar = true;
	}

	private Hashtable<String, Estacion> cargarEstaciones() {
		Hashtable<String, Estacion> estaciones = new Hashtable<String, Estacion>();
		EstacionDAO estacionDAO = new EstacionSecuencialDAO();
		List<Estacion> ds = estacionDAO.buscarTodos();
		for (Estacion d : ds)
			estaciones.put(d.getCodigo(), d);
		return estaciones;
	}
}
