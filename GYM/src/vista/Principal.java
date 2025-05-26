package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import database.GestionDatos;

import static control.ControlClases.guardar;
import javax.swing.border.TitledBorder;

public class Principal {
	private JFrame frmSistemaDeGestion;
	private JPanel centralPanel;
	private CardLayout cl_centralPanel;
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
		frmSistemaDeGestion.setLocationRelativeTo(null);
		frmSistemaDeGestion.setResizable(false);
	}

	private void initialize() {
		frmSistemaDeGestion = new JFrame();
		frmSistemaDeGestion.setFont(new Font("Verdana", Font.PLAIN, 12));
		frmSistemaDeGestion.setTitle("Sistema de Gestión Administrativa de Gimnasio");
		frmSistemaDeGestion.setBounds(100, 100, 1080, 581);
		frmSistemaDeGestion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ---- SOUTH PANEL ----
		JPanel southPanel = new JPanel();
		frmSistemaDeGestion.getContentPane().add(southPanel, BorderLayout.SOUTH);

		JButton btnPrincipal = new JButton("Principal");
		btnPrincipal.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnPrincipal.setHorizontalAlignment(SwingConstants.LEADING);
		btnPrincipal.addActionListener(e -> mostrarPanel("Dashboard"));
		southPanel.add(btnPrincipal);

		JButton btnEntrenadores = new JButton("Entrenadores");
		btnEntrenadores.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnEntrenadores.addActionListener(e -> mostrarPanel("Entrenadores"));
		southPanel.add(btnEntrenadores);

		JButton btnMiembros = new JButton("Miembros");
		btnMiembros.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnMiembros.addActionListener(e -> mostrarPanel("Miembros"));
		southPanel.add(btnMiembros);

		JButton btnPersonal = new JButton("Personal");
		btnPersonal.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnPersonal.addActionListener(e -> mostrarPanel("Personal"));
		southPanel.add(btnPersonal);

		JButton btnUsuarios = new JButton("Usuarios");
		btnUsuarios.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnUsuarios.addActionListener(e -> mostrarPanel("Usuarios"));
		southPanel.add(btnUsuarios);

		JLabel lblEspacio = new JLabel("                                        ");
		lblEspacio.setFont(new Font("Verdana", Font.PLAIN, 12));
		southPanel.add(lblEspacio);

		JButton btnGuardarCambios = new JButton("Guardar cambios");
		btnGuardarCambios.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnGuardarCambios.setHorizontalAlignment(SwingConstants.RIGHT);
		btnGuardarCambios.addActionListener(e -> guardar());
		southPanel.add(btnGuardarCambios);

		JButton btnSalir = new JButton("Salir del programa");
		btnSalir.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnSalir.setHorizontalAlignment(SwingConstants.RIGHT);
		btnSalir.addActionListener(e -> cerrarVentana());
		southPanel.add(btnSalir);

		// ---- CENTRAL PANEL ----
		cl_centralPanel = new CardLayout();
		centralPanel = new JPanel(cl_centralPanel);
		centralPanel.setBackground(Color.WHITE);
		frmSistemaDeGestion.getContentPane().add(centralPanel, BorderLayout.CENTER);

		panelDashboard   = crearPanelDashboard();
		panelMiembros    = new Miembros();
		panelEntrenadores= new Entrenadores();
		panelPersonal    = new Personal();
		panelUsuarios    = new Usuarios();

		centralPanel.add(panelDashboard,    "Dashboard");
		centralPanel.add(panelMiembros,     "Miembros");
		centralPanel.add(panelEntrenadores, "Entrenadores");
		centralPanel.add(panelPersonal,     "Personal");
		centralPanel.add(panelUsuarios,     "Usuarios");

		// ---- TOP PANEL ----
		JPanel topPanel = new JPanel();
		FlowLayout fl_topPanel = (FlowLayout) topPanel.getLayout();
		fl_topPanel.setAlignment(FlowLayout.LEADING);
		frmSistemaDeGestion.getContentPane().add(topPanel, BorderLayout.NORTH);

		relojLabel = new JLabel();
		relojLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		relojLabel.setHorizontalAlignment(SwingConstants.CENTER);
		relojLabel.setForeground(new Color(33, 102, 172));
		topPanel.add(relojLabel);

		frmSistemaDeGestion.setVisible(true);
		mostrarPanel("Dashboard");
	}

	private JPanel crearPanelDashboard() {
		ImagePanel panelPrincipal = new ImagePanel("/images/modern-home-gym.jpg");
		panelPrincipal.setBackground(new Color(245, 245, 245));  // solo sirve si la imagen falla

		JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Gestión del Gimnasio", SwingConstants.CENTER);
		lblTitulo.setBackground(Color.DARK_GRAY);
		lblTitulo.setOpaque(true);
		lblTitulo.setFont(new Font("Verdana", Font.BOLD, 28));
		lblTitulo.setForeground(Color.WHITE);
		panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

		JLabel lblDesc = new JLabel("Seleccione una opción para comenzar", SwingConstants.CENTER);
		lblDesc.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDesc.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDesc.setFont(new Font("Verdana", Font.PLAIN, 18));
		panelPrincipal.add(lblDesc, BorderLayout.CENTER);

		return panelPrincipal;
	}


	private void mostrarPanel(String nombre) {
		cl_centralPanel.show(centralPanel, nombre);
	}

	// CERRAR VENTANA con confirmación y guardado de datos
	private void cerrarVentana() {
		int opcion = JOptionPane.showConfirmDialog(
				frmSistemaDeGestion,
				"¿Estás seguro de que quieres salir?",
				"Confirmar salida",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);
		if (opcion == JOptionPane.YES_OPTION) {
			frmSistemaDeGestion.dispose(); // Cerrar la ventana
		}
	}

	private void iniciarReloj() {
		Thread hiloReloj = new Thread(() -> {
			while (true) {
				java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
				java.time.format.DateTimeFormatter formatter =
						java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String texto = ahora.format(formatter);
				javax.swing.SwingUtilities.invokeLater(() -> relojLabel.setText(texto));
				try { Thread.sleep(1000); }
				catch (InterruptedException e) { break; }
			}
		});
		hiloReloj.setDaemon(true);
		hiloReloj.start();
	}
}
