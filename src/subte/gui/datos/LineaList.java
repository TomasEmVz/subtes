package subte.gui.datos;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import subte.controlador.Constantes;
import subte.controlador.Coordinador;
import subte.modelo.Linea;

public class LineaList extends JDialog {

	private Coordinador coordinador;
	private ResourceBundle rb;
	private int accion;
	private Linea linea;

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable tableLinea;
	private JButton btnInsertar;
	
	/**
	 * Create the frame.
	 */
	public LineaList() {
	}
	
	public void init() {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rb = coordinador.getResourceBundle();
		setBounds(100, 100, 756, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnInsertar = new JButton(rb.getString("LineaList_insert"));
		btnInsertar.setBounds(38, 280, 114, 32);
		contentPane.add(btnInsertar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 25, 673, 244);
		contentPane.add(scrollPane);

		tableLinea = new JTable();
		tableLinea.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { rb.getString("LineaList_code"), rb.getString("LineaList_name"), rb.getString("LineaList_update"), rb.getString("LineaList_delete") }) {
					boolean[] columnEditables = new boolean[] { false, false, true, true };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});

		tableLinea.getColumn(rb.getString("LineaList_update")).setCellRenderer(new ButtonRenderer());
		tableLinea.getColumn(rb.getString("LineaList_update")).setCellEditor(new ButtonEditor(new JCheckBox()));
		tableLinea.getColumn(rb.getString("LineaList_delete")).setCellRenderer(new ButtonRenderer());
		tableLinea.getColumn(rb.getString("LineaList_delete")).setCellEditor(new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(tableLinea);

		Handler handler = new Handler();
		btnInsertar.addActionListener(handler);

		setModal(true);
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			LineaForm lineaForm = null;
			if (event.getSource() == btnInsertar)
				coordinador.insertarLineaForm();
		}
	}

	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableLinea.getModel()).setRowCount(0);
		for (Linea linea : coordinador.listarLineas())
			if (linea instanceof Linea)
				addRow((Linea) linea);
	}

	public void addRow(Linea linea) {
		Object[] row = new Object[tableLinea.getModel().getColumnCount()];
		row[0] = linea.getCodigo();
		row[1] = linea.getNombre();
		row[2] = "edit";
		row[3] = "drop";
		((DefaultTableModel) tableLinea.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		tableLinea.setValueAt(linea.getCodigo(), row, 0);
		tableLinea.setValueAt(linea.getNombre(), row, 1);
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			// setText((value == null) ? "" : value.toString());
			Icon icon = null;
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_edit.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_drop.png"));
			setIcon(icon);
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable table;
		private boolean isDeleteRow = false;
		private boolean isUpdateRow = false;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}

			label = (value == null) ? "" : value.toString();
			// button.setText(label);
			Icon icon = null;
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_edit.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_drop.png"));
			button.setIcon(icon);
			isPushed = true;
			this.table = table;
			isDeleteRow = false;
			isUpdateRow = false;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				String id = tableLinea.getValueAt(tableLinea.getSelectedRow(), 0).toString();
				Linea linea = (Linea) coordinador.buscarLinea(new Linea(id, null));
				if (label.equals("edit"))
					coordinador.modificarLineaForm(linea);
				else
					coordinador.borrarLineaForm(linea);
			}
			if (accion == Constantes.BORRAR)
				isDeleteRow = true;
			if (accion == Constantes.MODIFICAR)
				isUpdateRow = true;
			isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();

			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			if (isDeleteRow)
				tableModel.removeRow(table.getSelectedRow());

			if (isUpdateRow) {
				updateRow(table.getSelectedRow());
			}

		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

}
