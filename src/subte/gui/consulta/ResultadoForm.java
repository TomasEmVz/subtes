package subte.gui.consulta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import subte.controlador.Coordinador;
import subte.modelo.Tramo;

public class ResultadoForm extends JDialog {

	private Coordinador coordinador;

	private JPanel contentPane;
	private JButton btnCancelar;
	private JTextArea txtResultado;

	/**
	 * Create the frame.
	 */
	public ResultadoForm() {
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Handler handler = new Handler();

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(249, 205, 155, 32);
		contentPane.add(btnCancelar);

		txtResultado = new JTextArea();		
		JScrollPane scroll = new JScrollPane(txtResultado);
		scroll.setBounds(29, 23, 592, 171);
		contentPane.add(scroll);

		btnCancelar.addActionListener(handler);

		setModal(true);
	}

	public String verDatos(List<Tramo> recorrido) {
		String resultado = "";
		int tiempoTotal = 0;
		int tiempoSubte = 0;
		int tiempoCaminando = 0;
		for (Tramo t : recorrido) {
			resultado += t.getEstacion1().getLinea().getCodigo() + "-" + t.getEstacion1().getNombre() + " > "
					+ t.getEstacion2().getLinea().getCodigo() + "-" + t.getEstacion2().getNombre() + " :"
					+ t.getTiempo() + "\n";
			tiempoTotal += t.getTiempo();
			if (t.getEstacion1().getLinea().equals(t.getEstacion2().getLinea()))
				tiempoSubte += t.getTiempo();
			else
				tiempoCaminando += t.getTiempo();
		}
		resultado += "Tiempo Total: " + tiempoTotal + "\n";
		resultado += "Tiempo Subte: " + tiempoSubte + "\n";
		resultado += "Tiempo Caminando: " + tiempoCaminando + "\n";

		return resultado;
	}

	public void accion(String resultado) {

		txtResultado.setText(resultado);
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnCancelar) {
				coordinador.cancelarResultado();
				return;
			}
		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
}
