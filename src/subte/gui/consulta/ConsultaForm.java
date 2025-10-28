package subte.gui.consulta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.datastructures.TreeMap;
import subte.controlador.Coordinador;
import subte.modelo.Estacion;
import subte.modelo.Linea;

public class ConsultaForm extends JDialog {

	private Coordinador coordinador;

	private JPanel contentPane;

	private JButton btnRapido;
	private JButton btnTrasbordo;
	private JButton btnCongestion;
	private JButton btnCancelar;
	private JLabel lblEstacion1;
	private JLabel lblEstacion2;
	private JComboBox<Estacion> cbxEstacion1;
	private JComboBox<Estacion> cbxEstacion2;
	private List<Estacion> estaciones;


	/**
	 * Create the frame.
	 */
	public ConsultaForm() {
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Handler handler = new Handler();

		btnRapido = new JButton("Más Rápido");
		btnRapido.setBounds(65, 150, 156, 32);
		contentPane.add(btnRapido);
		btnRapido.addActionListener(handler);

		btnTrasbordo = new JButton("Menos Trasbordos");
		btnTrasbordo.setBounds(231, 150, 155, 32);
		contentPane.add(btnTrasbordo);
		btnTrasbordo.addActionListener(handler);

		btnCongestion = new JButton("Menos Congestión");
		btnCongestion.setBounds(397, 150, 155, 32);
		contentPane.add(btnCongestion);
		btnCongestion.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(231, 206, 155, 32);
		contentPane.add(btnCancelar);

		lblEstacion1 = new JLabel("Estaci\u00F3n Origen:");
		lblEstacion1.setBounds(34, 35, 118, 14);
		contentPane.add(lblEstacion1);

		lblEstacion2 = new JLabel("Estaci\u00F3n Destino:");
		lblEstacion2.setBounds(34, 71, 118, 14);
		contentPane.add(lblEstacion2);

		cbxEstacion1 = new JComboBox<Estacion>();
		cbxEstacion1.setBounds(162, 31, 390, 22);
		contentPane.add(cbxEstacion1);

		cbxEstacion2 = new JComboBox<Estacion>();
		cbxEstacion2.setBounds(162, 67, 390, 22);
		contentPane.add(cbxEstacion2);

		btnCancelar.addActionListener(handler);

		setModal(true);
	}

	public void accion() {
		if (estaciones != null)
			return;
		estaciones = coordinador.listarEstaciones();
		for (int i = 0; i < estaciones.size(); i++) {
			cbxEstacion1.addItem(estaciones.get(i));
			cbxEstacion2.addItem(estaciones.get(i));
		}
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnCancelar) {
				coordinador.cancelarConsulta();
				return;
			}

			if (event.getSource() == btnRapido)
				coordinador.masRapido((Estacion) cbxEstacion1.getSelectedItem(),
						(Estacion) cbxEstacion2.getSelectedItem());

			if (event.getSource() == btnTrasbordo)
				coordinador.menosTrasbordo((Estacion) cbxEstacion1.getSelectedItem(),
						(Estacion) cbxEstacion2.getSelectedItem());

			if (event.getSource() == btnCongestion)
				coordinador.menosCongestion((Estacion) cbxEstacion1.getSelectedItem(),
						(Estacion) cbxEstacion2.getSelectedItem());

		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
