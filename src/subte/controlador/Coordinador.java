package subte.controlador;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import subte.gui.aplicacion.DesktopFrameAplicacion;
import subte.gui.consulta.ConsultaForm;
import subte.gui.consulta.ResultadoForm;
import subte.gui.datos.LineaForm;
import subte.gui.datos.LineaList;
import subte.modelo.Estacion;
import subte.modelo.Linea;
import subte.modelo.Tramo;
import subte.negocio.Calculo;
import subte.negocio.Empresa;
import subte.negocio.LineaExisteException;
import subte.negocio.Subject;

public class Coordinador {

	private Empresa empresa;
	private Calculo calculo;
	private Configuracion configuracion;	

	private DesktopFrameAplicacion desktopFrameAplicacion;

	private ConsultaForm consultaForm;
	private ResultadoForm resultadoForm;

	private LineaList lineaList;
	private LineaForm lineaForm;

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Calculo getCalculo() {
		return calculo;
	}

	public void setCalculo(Calculo calculo) {
		this.calculo = calculo;
	}

	public Configuracion getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}
	
	// Empresa
	
	public List<Linea> listarLineas() {
		return empresa.getLineas();
	}

	public List<Estacion> listarEstaciones() {
		return empresa.getEstaciones();
	}

	public List<Tramo> listarTramos() {
		return empresa.getTramos();
	}

	public Linea buscarLinea(Linea linea) {
		return empresa.buscarLinea(linea);
	}

	public Map<Integer, Double> getCongestion() {	
		return configuracion.getCongestion();
	}

	public ResourceBundle getResourceBundle() {		
		return configuracion.getResourceBundle();
	}
	
	// GUI Aplicacion

	public DesktopFrameAplicacion getDesktopFrameAplicacion() {
		return desktopFrameAplicacion;
	}

	public void setDesktopFrameAplicacion(DesktopFrameAplicacion desktopFrameAplicacion) {
		this.desktopFrameAplicacion = desktopFrameAplicacion;
	}

	// GUI Consulta

	public ConsultaForm getConsultaForm() {
		return consultaForm;
	}

	public void setConsultaForm(ConsultaForm consultaForm) {
		this.consultaForm = consultaForm;
	}

	public ResultadoForm getResultadoForm() {
		return resultadoForm;
	}

	public void setResultadoForm(ResultadoForm resultadoForm) {
		this.resultadoForm = resultadoForm;
	}

	// GUI Datos

	public LineaList getLineaList() {
		return lineaList;
	}

	public void setLineaList(LineaList lineaList) {
		this.lineaList = lineaList;
	}

	public LineaForm getLineaForm() {
		return lineaForm;
	}

	public void setLineaForm(LineaForm lineaForm) {
		this.lineaForm = lineaForm;
	}

	// DesktopFrame Consulta

	public void mostrarConsulta() {
		consultaForm.accion();
		consultaForm.setVisible(true);
	}

	// ConsultaForm

	public void cancelarConsulta() {
		consultaForm.setVisible(false);
	}

	public void masRapido(Estacion estacion1, Estacion estacion2) {
		List<Tramo> resultado = calculo.masRapido(estacion1, estacion2);
		resultadoForm.accion(resultadoForm.verDatos(resultado));
		resultadoForm.setVisible(true);
	}

	public void menosTrasbordo(Estacion estacion1, Estacion estacion2) {
		List<Tramo> resultado = calculo.menosTrasbordo(estacion1, estacion2);
		resultadoForm.accion(resultadoForm.verDatos(resultado));
		resultadoForm.setVisible(true);
	}

	public void menosCongestion(Estacion estacion1, Estacion estacion2) {
		List<Tramo> resultado = calculo.menosCongestion(estacion1, estacion2);
		resultadoForm.accion(resultadoForm.verDatos(resultado));
		resultadoForm.setVisible(true);
	}

	// ResultadoForm

	public void cancelarResultado() {
		resultadoForm.setVisible(false);
	}

	// DesktopFrame Datos

	public void mostrarLineaList() {
		lineaList.loadTable();
		lineaList.setVisible(true);
	}

	// LineaList

	public void insertarLineaForm() {
		lineaForm.accion(Constantes.INSERTAR, null);
		lineaForm.setVisible(true);
	}

	public void modificarLineaForm(Linea linea) {
		lineaForm.accion(Constantes.MODIFICAR, linea);
		lineaForm.setVisible(true);
	}

	public void borrarLineaForm(Linea linea) {
		lineaForm.accion(Constantes.BORRAR, linea);
		lineaForm.setVisible(true);
	}

	// LineaForm

	public void cancelarLinea() {
		lineaForm.setVisible(false);
	}

	public void insertarLinea(Linea linea) throws LineaExisteException {
		empresa.agregarLinea(linea);
		lineaForm.setVisible(false);
		lineaList.addRow(linea);
	}

	public void modificarLinea(Linea linea) {
		empresa.modificarLinea(linea);
		lineaList.setAccion(Constantes.MODIFICAR);
		lineaList.setLinea(linea);
		lineaForm.setVisible(false);
	}

	public void borrarLinea(Linea linea) {
		empresa.borrarLinea(linea);
		lineaList.setAccion(Constantes.BORRAR);
		lineaForm.setVisible(false);
	}

}
