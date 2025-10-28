package subte.gui.datos;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import subte.controlador.Constantes;
import subte.controlador.Coordinador;
import subte.modelo.Linea;
import subte.negocio.LineaExisteException;
import subte.negocio.LineaReferenciaException;

public class LineaForm extends JDialog {

	final static Logger logger = Logger.getLogger(LineaForm.class);
	
	private Coordinador coordinador;
	private ResourceBundle rb;

	private JPanel contentPane;
	private JTextField jtfCodigo;
	private JTextField jtfNombre;

	private JLabel lblErrorCodigo;
	private JLabel lblErrorNombre;

	private JButton btnInsertar;
	private JButton btnModificar;
	private JButton btnBorrar;
	private JButton btnCancelar;

	/**
	 * Create the frame.
	 */
	public LineaForm() {
		
	}
	
	public void init() {
		rb = coordinador.getResourceBundle();
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblCodigo = new JLabel(rb.getString("LineaForm_code"));
		lblCodigo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCodigo.setBounds(42, 24, 107, 14);
		contentPane.add(lblCodigo);

		jtfCodigo = new JTextField();
		jtfCodigo.setBounds(159, 24, 86, 20);
		contentPane.add(jtfCodigo);
		jtfCodigo.setColumns(10);

		JLabel lblNombre = new JLabel(rb.getString("LineaForm_name"));
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombre.setBounds(42, 55, 107, 14);
		contentPane.add(lblNombre);

		jtfNombre = new JTextField();
		jtfNombre.setBounds(159, 55, 86, 20);
		contentPane.add(jtfNombre);
		jtfNombre.setColumns(10);

		lblErrorCodigo = new JLabel("");
		lblErrorCodigo.setForeground(Color.RED);
		lblErrorCodigo.setBounds(255, 24, 300, 14);
		contentPane.add(lblErrorCodigo);

		lblErrorNombre = new JLabel("");
		lblErrorNombre.setForeground(Color.RED);
		lblErrorNombre.setBounds(255, 55, 300, 14);
		contentPane.add(lblErrorNombre);

		Handler handler = new Handler();

		btnInsertar = new JButton(rb.getString("LineaForm_insert"));
		btnInsertar.setBounds(85, 202, 114, 32);
		contentPane.add(btnInsertar);
		btnInsertar.addActionListener(handler);

		btnModificar = new JButton(rb.getString("LineaForm_update"));
		btnModificar.setBounds(85, 202, 114, 32);
		contentPane.add(btnModificar);
		btnModificar.addActionListener(handler);

		btnBorrar = new JButton(rb.getString("LineaForm_delete"));
		btnBorrar.setBounds(85, 202, 114, 32);
		contentPane.add(btnBorrar);
		btnBorrar.addActionListener(handler);

		btnCancelar = new JButton(rb.getString("LineaForm_cancel"));
		btnCancelar.setBounds(225, 202, 114, 32);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		setModal(true);
	}

	public void accion(int accion, Linea linea) {
		btnInsertar.setVisible(false);
		btnModificar.setVisible(false);
		btnBorrar.setVisible(false);
		jtfCodigo.setEditable(true);
		jtfNombre.setEditable(true);

		if (accion == Constantes.INSERTAR) {
			btnInsertar.setVisible(true);
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			btnModificar.setVisible(true);
			jtfCodigo.setEditable(false);
			mostrar(linea);
		}

		if (accion == Constantes.BORRAR) {
			btnBorrar.setVisible(true);
			jtfCodigo.setEditable(false);
			jtfNombre.setEditable(false);
			mostrar(linea);
		}
	}

	private void mostrar(Linea linea) {
		jtfCodigo.setText(linea.getCodigo());
		jtfNombre.setText(linea.getNombre());
	}

	private void limpiar() {
		jtfCodigo.setText("");
		jtfNombre.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			String codigo = jtfCodigo.getText().trim();
			String nombre = jtfNombre.getText().trim();
			Linea linea = new Linea(codigo, nombre);

			if (event.getSource() == btnInsertar) {
				if (!registroValido())
					return;
				try {
					coordinador.insertarLinea(linea);
					logger.info("Insertar línea");
					
				} catch (LineaExisteException e) {
					JOptionPane.showMessageDialog(null, rb.getString("LineaForm_error_1"));
					logger.error("Error al insertar linea");
					return;
				}
			}

			if (event.getSource() == btnModificar) {
				if (!registroValido())
					return;
				coordinador.modificarLinea(linea);
				logger.info("Modificar línea");
			}

			if (event.getSource() == btnBorrar) {
				int resp = JOptionPane.showConfirmDialog(null, rb.getString("LineaForm_confirm_1"), rb.getString("LineaForm_confirm_2"),
						JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp)
					try {
						coordinador.borrarLinea(linea);
						logger.info("Borrar línea");
					} catch (LineaReferenciaException e) {
						JOptionPane.showMessageDialog(null, rb.getString("LineaForm_error_2"));
						logger.error("Error al borrar linea");
						
						return;
					}
				return;
			}

			if (event.getSource() == btnCancelar) {
				coordinador.cancelarLinea();
				return;
			}
		}
	}

	public boolean registroValido() {
		lblErrorCodigo.setText("");
		lblErrorNombre.setText("");

		// validar codigo
		String codigo = jtfCodigo.getText().trim();
		if (codigo.isEmpty()) {
			lblErrorCodigo.setText(rb.getString("LineaForm_valid_1"));
			return false;
		}
		if (codigo.matches("[A-Z][a-zA-Z]*") != true) {
			lblErrorCodigo.setText(rb.getString("LineaForm_valid_2"));
			return false;
		}

		// validar nombre
		String nombre = jtfNombre.getText().trim();
		if (nombre.isEmpty()) {
			lblErrorNombre.setText("Campo obligatorio");
			return false;
		}

		return true;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
