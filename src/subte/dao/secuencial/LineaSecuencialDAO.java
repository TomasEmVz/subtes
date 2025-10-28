package subte.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;

import subte.dao.LineaDAO;
import subte.modelo.Linea;

public class LineaSecuencialDAO implements LineaDAO {

	private List<Linea> list;
	private String name;
	private boolean actualizar;

	public LineaSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("linea");
		actualizar = true;
	}

	private List<Linea> readFromFile(String file) {
		List<Linea> list = new ArrayList<>();
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(file));
			inFile.useDelimiter("\\s*;\\s*");
			while (inFile.hasNext()) {
				Linea e = new Linea();
				e.setCodigo(inFile.next());
				e.setNombre(inFile.next());				
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

	private void writeToFile(List<Linea> list, String file) {
		Formatter outFile = null;		
		try {
			outFile = new Formatter(file);
			for (Linea e : list) {				
				outFile.format("%s;%s;\n", e.getCodigo(), e.getNombre());
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
	public List<Linea> buscarTodos() {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

	@Override
	public void insertar(Linea linea) {
		list.add(linea);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Linea linea) {
		int pos = list.indexOf(linea);
		list.set(pos, linea);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void borrar(Linea linea) {
		list.remove(linea);
		writeToFile(list, name);
		actualizar = true;
	}

}
