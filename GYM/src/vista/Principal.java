package vista;

import static control.ControlClases.guardar;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import database.GestionDatos;

public class Principal {
	/** VENTANA PRINCIPAL DEL SISTEMA */
	private JFrame frmSistemaDeGestion;
	/** PANEL CENTRAL QUE CONTIENE LOS DIFERENTES PANELES DE LA APLICACION */
	private JPanel centralPanel;
	/** LAYOUT PARA CAMBIAR ENTRE PANELES EN EL PANEL CENTRAL */
	private CardLayout cl_centralPanel;
	/** PANEL DE MIEMBROS DEL GIMNASIO */
	private Miembros panelMiembros;
	/** PANEL DE ENTRENADORES DEL GIMNASIO */
	private Entrenadores panelEntrenadores;
	/** PANEL DE PERSONAL DEL GIMNASIO */
	private Personal panelPersonal;
	/** PANEL DASHBOARD PRINCIPAL */
	private JPanel panelDashboard;
	/** PANEL DE USUARIOS DEL SISTEMA */
	private Usuarios panelUsuarios;
	/** LABEL PARA MOSTRAR LA HORA ACTUAL */
	private JLabel relojLabel;
	/** LABEL PARA MOSTRAR LA FECHA ACTUAL */
	private JLabel fechaLabel;

	/**
	 * CONSTRUCTOR DE LA CLASE PRINCIPAL
	 * INICIALIZA LOS DATOS Y LA INTERFAZ GRAFICA DEL SISTEMA
	 */
	public Principal() {
		GestionDatos.getInstancia().inicializarDatos(); // ASEGURA LA CARGA DE DATOS AL INICIAR
		initialize();
		iniciarReloj();
		frmSistemaDeGestion.setVisible(true);
		frmSistemaDeGestion.setLocationRelativeTo(null);
	}

	/**
	 * METODO QUE INICIALIZA Y CONFIGURA LA VENTANA PRINCIPAL Y SUS COMPONENTES
	 */
	private void initialize() {
		frmSistemaDeGestion = new JFrame();
		// ESTABLECER ICONO DE LA APLICACION
		try {
		    java.awt.Image icon = new javax.swing.ImageIcon(getClass().getResource("/icons/GYMico.png")).getImage();
		    frmSistemaDeGestion.setIconImage(icon);
		} catch (Exception e) {
		    System.out.println("No se pudo cargar el icono de la aplicacion.");
		}
		frmSistemaDeGestion.setFont(new Font("Verdana", Font.PLAIN, 12));
		frmSistemaDeGestion.setTitle("Sistema de Gestion Administrativa de Gimnasio");
		frmSistemaDeGestion.setBounds(100, 100, 1080, 581);
		frmSistemaDeGestion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ---- SOUTH PANEL ----
		JPanel southPanel = new JPanel();
		frmSistemaDeGestion.getContentPane().add(southPanel, BorderLayout.SOUTH);

		// BOTON PARA MOSTRAR EL PANEL PRINCIPAL
		JButton btnPrincipal = new JButton("Principal");
		btnPrincipal.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnPrincipal.setHorizontalAlignment(SwingConstants.LEADING);
		btnPrincipal.addActionListener(e -> mostrarPanel("Dashboard"));
		southPanel.add(btnPrincipal);

		// BOTON PARA MOSTRAR EL PANEL DE ENTRENADORES
		JButton btnEntrenadores = new JButton("Entrenadores");
		btnEntrenadores.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnEntrenadores.addActionListener(e -> mostrarPanel("Entrenadores"));
		
		// BOTON PARA MOSTRAR EL PANEL DE CLASES
		JButton btnClases = new JButton("Clases");
		btnClases.addActionListener(e -> mostrarPanel("Clases"));
		btnClases.setFont(new Font("Verdana", Font.PLAIN, 12));
		southPanel.add(btnClases);
		
				// BOTON PARA MOSTRAR EL PANEL DE MIEMBROS
				JButton btnMiembros = new JButton("Miembros");
				btnMiembros.setFont(new Font("Verdana", Font.PLAIN, 12));
				btnMiembros.addActionListener(e -> mostrarPanel("Miembros"));
				southPanel.add(btnMiembros);
		southPanel.add(btnEntrenadores);

		// BOTON PARA MOSTRAR EL PANEL DE PERSONAL
		JButton btnPersonal = new JButton("Personal");
		btnPersonal.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnPersonal.addActionListener(e -> mostrarPanel("Personal"));
		southPanel.add(btnPersonal);

		// BOTON PARA MOSTRAR EL PANEL DE USUARIOS
		JButton btnUsuarios = new JButton("Usuarios");
		btnUsuarios.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnUsuarios.addActionListener(e -> mostrarPanel("Usuarios"));
		southPanel.add(btnUsuarios);

		// ESPACIO ENTRE BOTONES Y ACCIONES DE GUARDADO/SALIDA
		JLabel lblEspacio = new JLabel("                                        ");
		lblEspacio.setFont(new Font("Verdana", Font.PLAIN, 12));
		southPanel.add(lblEspacio);

		// BOTON PARA GUARDAR CAMBIOS
		JButton btnGuardarCambios = new JButton("Guardar cambios");
		btnGuardarCambios.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnGuardarCambios.setHorizontalAlignment(SwingConstants.RIGHT);
		btnGuardarCambios.addActionListener(e -> guardar());
		southPanel.add(btnGuardarCambios);

		// BOTON PARA SALIR DEL PROGRAMA
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

		// INICIALIZACION DE PANELES PRINCIPALES
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
		centralPanel.add(new Clases(), "Clases");

		// ---- TOP PANEL ----
		JPanel topPanel = new JPanel();
		frmSistemaDeGestion.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut);

		fechaLabel = new JLabel();
		fechaLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		fechaLabel.setHorizontalAlignment(SwingConstants.LEFT);
		fechaLabel.setForeground(Color.ORANGE);
		topPanel.add(fechaLabel);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut_1);

		relojLabel = new JLabel();
		relojLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		relojLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		relojLabel.setForeground(Color.ORANGE);
		topPanel.add(relojLabel);

		frmSistemaDeGestion.setVisible(true);
		mostrarPanel("Dashboard");
	}

	/**
	 * CREA Y CONFIGURA EL PANEL DASHBOARD PRINCIPAL CON IMAGEN Y MENSAJES DE BIENVENIDA
	 * @return PANEL PRINCIPAL DE DASHBOARD
	 */
	private JPanel crearPanelDashboard() {
		ImagePanel panelPrincipal = new ImagePanel("/icons/Gimnasio.jpg");
		panelPrincipal.setBackground(new Color(245, 245, 245));  // SOLO SIRVE SI LA IMAGEN NO SE CARGA

		JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Gestion del Gimnasio", SwingConstants.CENTER);
		lblTitulo.setBackground(Color.DARK_GRAY);
		lblTitulo.setOpaque(true);
		lblTitulo.setFont(new Font("Verdana", Font.BOLD, 28));
		lblTitulo.setForeground(Color.WHITE);
		panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

		JLabel lblDesc = new JLabel("Seleccione una opcion para comenzar", SwingConstants.CENTER);
		lblDesc.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDesc.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDesc.setFont(new Font("Verdana", Font.PLAIN, 18));
		panelPrincipal.add(lblDesc, BorderLayout.CENTER);

		return panelPrincipal;
	}

	/**
	 * MUESTRA EL PANEL CON EL NOMBRE ESPECIFICADO EN EL PANEL CENTRAL
	 * @param nombre NOMBRE DEL PANEL A MOSTRAR
	 */
	private void mostrarPanel(String nombre) {
		cl_centralPanel.show(centralPanel, nombre);
	}

	/**
	 * CIERRA LA VENTANA PRINCIPAL CON CONFIRMACION Y GUARDA LOS DATOS SI SE CONFIRMA
	 */
	private void cerrarVentana() {
		// MUESTRA UN DIALOGO DE CONFIRMACION PARA SALIR DEL SISTEMA
		int opcion = JOptionPane.showConfirmDialog(
				frmSistemaDeGestion,
				"¿Estas seguro de que quieres salir?",
				"Confirmar salida",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);
		// SI EL USUARIO CONFIRMA, SE CIERRA LA VENTANA PRINCIPAL
		if (opcion == JOptionPane.YES_OPTION) {
			frmSistemaDeGestion.dispose(); // CERRAR LA VENTANA
		}
	}

	/**
	 * INICIA UN HILO QUE ACTUALIZA LA HORA Y FECHA EN LA INTERFAZ CADA SEGUNDO
	 */
	private void iniciarReloj() {
		// CREA UN NUEVO HILO PARA ACTUALIZAR LA HORA Y FECHA CADA SEGUNDO
		Thread hiloReloj = new Thread(() -> {
			while (true) {
				// OBTIENE LA FECHA Y HORA ACTUAL DEL SISTEMA
				java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
				java.time.format.DateTimeFormatter formatterFecha =
						java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
				java.time.format.DateTimeFormatter formatterHora =
						java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
				String textoFecha = ahora.format(formatterFecha);
				String textoHora = ahora.format(formatterHora);
				// ACTUALIZA LOS LABELS DE FECHA Y HORA EN LA INTERFAZ GRAFICA
				javax.swing.SwingUtilities.invokeLater(() -> {
					relojLabel.setText(textoHora);
					fechaLabel.setText(textoFecha);
				});
				// ESPERA UN SEGUNDO ANTES DE ACTUALIZAR DE NUEVO
				try { Thread.sleep(1000); }
				catch (InterruptedException e) { break; }
			}
		});
		// ESTABLECE EL HILO COMO DAEMON PARA QUE NO IMPIDA EL CIERRE DE LA APLICACION
		hiloReloj.setDaemon(true);
		hiloReloj.start();
	}
}
