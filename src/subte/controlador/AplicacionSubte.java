package subte.controlador;

import subte.gui.aplicacion.DesktopFrameAplicacion;
import subte.gui.consulta.ConsultaForm;
import subte.gui.consulta.ResultadoForm;
import subte.gui.datos.LineaForm;
import subte.gui.datos.LineaList;
import subte.negocio.Calculo;
import subte.negocio.Empresa;
import subte.negocio.LineaExisteException;
import subte.negocio.Subject;

public class AplicacionSubte {

	// lógica
	private Empresa empresa;
	private Calculo calculo;
	private Configuracion configuracion;
	private Subject subject;

	// vista
	private DesktopFrameAplicacion desktopFrameAplicacion;
	private ConsultaForm consultaForm;
	private ResultadoForm resultadoForm;
	private LineaList lineaList;
	private LineaForm lineaForm;

	// controlador
	private Coordinador coordinador;

	public static void main(String[] args) throws LineaExisteException {
		AplicacionSubte miAplicacion = new AplicacionSubte();
		miAplicacion.iniciar();
	}

	private void iniciar() throws LineaExisteException {
		/* Se instancian las clases */
			
		coordinador = new Coordinador();		
		empresa = Empresa.getEmpresa();
		calculo = new Calculo();		
		configuracion = Configuracion.getConfiguracion();
		subject = new Subject();
		desktopFrameAplicacion = new DesktopFrameAplicacion();
		consultaForm = new ConsultaForm();
		resultadoForm = new ResultadoForm();
		lineaList = new LineaList();
		lineaForm = new LineaForm();

		/* Se establecen las relaciones entre clases */		
		empresa.setCoordinador(coordinador);
		calculo.setCoordinador(coordinador);	
		configuracion.setCoordinador(coordinador);
		desktopFrameAplicacion.setCoordinador(coordinador);
		consultaForm.setCoordinador(coordinador);
		resultadoForm.setCoordinador(coordinador);
		lineaList.setCoordinador(coordinador);
		lineaForm.setCoordinador(coordinador);

		/* Se establecen relaciones con la clase coordinador */
		coordinador.setEmpresa(empresa);
		coordinador.setCalculo(calculo);		
		coordinador.setConfiguracion(configuracion);		
		coordinador.setDesktopFrameAplicacion(desktopFrameAplicacion);
		coordinador.setConsultaForm(consultaForm);
		coordinador.setResultadoForm(resultadoForm);
		coordinador.setLineaList(lineaList);
		coordinador.setLineaForm(lineaForm);
				
		empresa.init(subject);
		calculo.init(subject);
		desktopFrameAplicacion.init();
		lineaList.init();
		lineaForm.init();
		desktopFrameAplicacion.setVisible(true);
	}

}
