package subte.gui.aplicacion;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import subte.controlador.Coordinador;

public class DesktopFrameAplicacion extends JFrame {

	private Coordinador coordinador;
	private ResourceBundle rb;
	private JPanel contentPane;

	public DesktopFrameAplicacion() {
		
	}
	
	public void init() {
		rb = coordinador.getResourceBundle();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 600);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu_1 = new JMenu(rb.getString("DesktopFrameApplication_application"));
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem(rb.getString("DesktopFrameApplication_application_exit"));
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(NORMAL);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_1);

		JMenu mnNewMenu = new JMenu(rb.getString("DesktopFrameApplication_data"));
		menuBar.add(mnNewMenu);

		JMenuItem mntmLineas = new JMenuItem(rb.getString("DesktopFrameApplication_data_lines"));
		mntmLineas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				coordinador.mostrarLineaList();
			}
		});
		mnNewMenu.add(mntmLineas);
		
		JMenu mnNewMenu_2 = new JMenu(rb.getString("DesktopFrameApplication_questions"));
		menuBar.add(mnNewMenu_2);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem(rb.getString("DesktopFrameApplication_questions_questions"));
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				coordinador.mostrarConsulta();
			}
		});		
		mnNewMenu_2.add(mntmNewMenuItem_2);

		JMenuItem mntmEstaciones = new JMenuItem(rb.getString("DesktopFrameApplication_data_stations"));
		mnNewMenu.add(mntmEstaciones);
		
		JMenuItem mntmTramos = new JMenuItem(rb.getString("DesktopFrameApplication_data_sections"));
		mnNewMenu.add(mntmTramos);
				
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		setSize(600, 480);
		setTitle(rb.getString("DesktopFrameApplication_company"));
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
}
