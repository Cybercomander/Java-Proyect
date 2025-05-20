package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.CardLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import base_datos.GestionDatos;

public class Principal {

	private JFrame frmSistemaDeGestion;
	private JPanel centralPanel;
	private CardLayout cardLayout;
	private Miembros panelMiembros;
	private Entrenadores panelEntrenadores;
	private Personal panelPersonal;
	private JPanel panelDashboard;
	private Usuarios panelUsuarios;
	private JLabel relojLabel;

	public Principal() {
		GestionDatos.getInstancia().inicializarDatos(); // Asegura la carga de datos al iniciar
		initialize();
		iniciarReloj();
		frmSistemaDeGestion.setVisible(true);
	}

	private void initialize() {
		frmSistemaDeGestion = new JFrame();
		frmSistemaDeGestion.setTitle("SISTEMA DE GESTION ADMINISTRATIVA DE GIMNASIO");
		frmSistemaDeGestion.setBounds(100, 100, 1080, 581);
		frmSistemaDeGestion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) topPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.TRAILING);
		frmSistemaDeGestion.getContentPane().add(topPanel, BorderLayout.SOUTH);

		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setHorizontalAlignment(SwingConstants.RIGHT);
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		topPanel.add(btnAceptar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrar_ventana();
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
		topPanel.add(btnCancelar);

		// Panel central con CardLayout para cambiar entre vistas
		cardLayout = new CardLayout();
		centralPanel = new JPanel(cardLayout);
		centralPanel.setBackground(Color.WHITE);
		frmSistemaDeGestion.getContentPane().add(centralPanel, BorderLayout.CENTER);

		// Paneles de cada sección
		panelDashboard = crearPanelDashboard();
		panelMiembros = new Miembros();
		panelEntrenadores = new Entrenadores();
		panelPersonal = new Personal();
		panelUsuarios = new Usuarios();

		centralPanel.add(panelDashboard, "Dashboard");
		centralPanel.add(panelMiembros, "Miembros");
		centralPanel.add(panelEntrenadores, "Entrenadores");
		centralPanel.add(panelPersonal, "Personal");
		centralPanel.add(panelUsuarios, "Usuarios");

		JPanel southPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) southPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		frmSistemaDeGestion.getContentPane().add(southPanel, BorderLayout.NORTH);

		relojLabel = new JLabel();
		relojLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		relojLabel.setForeground(new Color(33, 102, 172));
		southPanel.add(relojLabel);

		JMenuBar menuBar = new JMenuBar();
		frmSistemaDeGestion.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Menú");
		menuBar.add(mnNewMenu);

		JMenuItem menuPrincipal = new JMenuItem("Principal");
		mnNewMenu.add(menuPrincipal);
		menuPrincipal.addActionListener(e -> mostrarPanel("Dashboard"));

		JMenuItem menuEntrenadores = new JMenuItem("Entrenadores");
		mnNewMenu.add(menuEntrenadores);
		menuEntrenadores.addActionListener(e -> mostrarPanel("Entrenadores"));

		JMenuItem menuMiembros = new JMenuItem("Miembros");
		mnNewMenu.add(menuMiembros);
		menuMiembros.addActionListener(e -> mostrarPanel("Miembros"));

		JMenuItem menuPersonal = new JMenuItem("Personal");
		mnNewMenu.add(menuPersonal);
		menuPersonal.addActionListener(e -> mostrarPanel("Personal"));

		JMenuItem menuUsuarios = new JMenuItem("Usuarios");
		mnNewMenu.add(menuUsuarios);
		menuUsuarios.addActionListener(e -> mostrarPanel("Usuarios"));

		JMenuItem menuSalir = new JMenuItem("Salir");
		menuSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrar_ventana();
			}
		});
		menuSalir.setIcon(new ImageIcon(Principal.class.getResource("/icons/cross.png")));
		mnNewMenu.add(menuSalir);

		frmSistemaDeGestion.setVisible(true);
		mostrarPanel("Dashboard");
	}

	private JPanel crearPanelDashboard() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 245, 245));
		panel.setLayout(new BorderLayout());
		JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Gestión del Gimnasio", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitulo.setForeground(new Color(33, 102, 172));
		panel.add(lblTitulo, BorderLayout.NORTH);
		JLabel lblDesc = new JLabel("Seleccione una opción del menú para comenzar", SwingConstants.CENTER);
		lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel.add(lblDesc, BorderLayout.CENTER);
		return panel;
	}

	private void mostrarPanel(String nombre) {
		cardLayout.show(centralPanel, nombre);
	}

	//METODS
	public void aceptar() {
		//GUARDAR TODOS LOS CAMBIOS REALIZADOS EN EL PROGRAMA
	}

	//CERRAR VENTANA
	public void cerrar_ventana() {
		System.exit(0);
	}

	private void iniciarReloj() {
		Thread hiloReloj = new Thread(() -> {
			while (true) {
				java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
				java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String texto = ahora.format(formatter);
				javax.swing.SwingUtilities.invokeLater(() -> relojLabel.setText(texto));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		});
		hiloReloj.setDaemon(true);
		hiloReloj.start();
	}
}
