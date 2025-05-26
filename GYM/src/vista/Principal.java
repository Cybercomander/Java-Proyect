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
import java.util.Objects;

import database.GestionDatos;

public class Principal
{
	private JFrame frmSistemaDeGestion;
	private JPanel centralPanel;
	private CardLayout cl_centralPanel;
	private Miembros panelMiembros;
	private Entrenadores panelEntrenadores;
	private Personal panelPersonal;
	private JPanel panelDashboard;
	private Usuarios panelUsuarios;
	private JLabel relojLabel;

	public Principal()
	{
		GestionDatos.getInstancia().inicializarDatos(); // Asegura la carga de datos al iniciar
		initialize();
		iniciarReloj();
		frmSistemaDeGestion.setVisible(true);
	}

	private void initialize()
	{
		frmSistemaDeGestion = new JFrame();
		frmSistemaDeGestion.setFont(new Font("Verdana", Font.PLAIN, 12));
		frmSistemaDeGestion.setTitle("SISTEMA DE GESTION ADMINISTRATIVA DE GIMNASIO");
		frmSistemaDeGestion.setBounds(100, 100, 1080, 581);
		frmSistemaDeGestion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel southPanel = new JPanel();
		frmSistemaDeGestion.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		JButton btnPrincipal = new JButton("Principal");
		btnPrincipal.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnPrincipal.setHorizontalAlignment(SwingConstants.LEADING);
		southPanel.add(btnPrincipal);
		
		JButton btnEntrenadores = new JButton("Entrenadores");
		southPanel.add(btnEntrenadores);
		
		JButton btnMiembros = new JButton("Miembros");
		southPanel.add(btnMiembros);
		
		JButton btnPersonal = new JButton("Personal");
		southPanel.add(btnPersonal);
		
		JButton btnUsuarios = new JButton("Usuarios");
		southPanel.add(btnUsuarios);
		
		JLabel lblEspacio = new JLabel("                                        ");
		southPanel.add(lblEspacio);

		JButton btnGuardarCambios = new JButton("Guardar cambios");
		btnGuardarCambios.setHorizontalAlignment(SwingConstants.RIGHT);
		btnGuardarCambios.setFont(new Font("Verdana", Font.PLAIN, 12));
		southPanel.add(btnGuardarCambios);

		JButton btnSalir = new JButton("Salir del programa");
		btnSalir.addActionListener(e -> cerrarVentana());
		btnSalir.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnSalir.setHorizontalAlignment(SwingConstants.RIGHT);
		southPanel.add(btnSalir);

		// Panel central con CardLayout para cambiar entre vistas
		cl_centralPanel = new CardLayout();
		centralPanel = new JPanel(cl_centralPanel);
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

		JPanel topPanel = new JPanel();
		FlowLayout fl_topPanel = (FlowLayout) topPanel.getLayout();
		fl_topPanel.setAlignment(FlowLayout.LEADING);
		frmSistemaDeGestion.getContentPane().add(topPanel, BorderLayout.NORTH);

		relojLabel = new JLabel();
		relojLabel.setHorizontalAlignment(SwingConstants.CENTER);
		relojLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		relojLabel.setForeground(new Color(33, 102, 172));
		topPanel.add(relojLabel);

		JMenuBar menuBar = new JMenuBar();
		frmSistemaDeGestion.setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menú");
		mnMenu.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(mnMenu);

		JMenuItem menuPrincipal = new JMenuItem("Principal");
		mnMenu.add(menuPrincipal);
		menuPrincipal.addActionListener(e -> mostrarPanel("Dashboard"));

		JMenuItem menuEntrenadores = new JMenuItem("Entrenadores");
		mnMenu.add(menuEntrenadores);
		menuEntrenadores.addActionListener(e -> mostrarPanel("Entrenadores"));

		JMenuItem menuMiembros = new JMenuItem("Miembros");
		mnMenu.add(menuMiembros);
		menuMiembros.addActionListener(e -> mostrarPanel("Miembros"));

		JMenuItem menuPersonal = new JMenuItem("Personal");
		mnMenu.add(menuPersonal);
		menuPersonal.addActionListener(e -> mostrarPanel("Personal"));

		JMenuItem menuUsuarios = new JMenuItem("Usuarios");
		mnMenu.add(menuUsuarios);
		menuUsuarios.addActionListener(e -> mostrarPanel("Usuarios"));

		JMenuItem menuSalir = new JMenuItem("Salir");
		menuSalir.addActionListener(e -> cerrarVentana());
		menuSalir.setIcon(new ImageIcon(Objects.requireNonNull(Principal.class.getResource("/icons/cross.png"))));
		mnMenu.add(menuSalir);

		frmSistemaDeGestion.setVisible(true);
		mostrarPanel("Dashboard");
	}

	private JPanel crearPanelDashboard()
	{
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setBackground(new Color(245, 245, 245));
		panelPrincipal.setLayout(new BorderLayout());
		JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Gestión del Gimnasio", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Verdana", Font.BOLD, 28));
		lblTitulo.setForeground(new Color(33, 102, 172));
		panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
		JLabel lblDesc = new JLabel("Seleccione una opción para comenzar", SwingConstants.CENTER);
		lblDesc.setFont(new Font("Verdana", Font.PLAIN, 18));
		panelPrincipal.add(lblDesc, BorderLayout.CENTER);
		return panelPrincipal;
	}

	private void mostrarPanel(String nombre)
	{
		cl_centralPanel.show(centralPanel, nombre);
	}

	//METODS
	public void aceptar()
	{
		//TODO: GUARDAR TODOS LOS CAMBIOS REALIZADOS EN EL PROGRAMA
	}

	//CERRAR VENTANA
	public void cerrarVentana()
	{
		System.exit(0);
	}

	private void iniciarReloj()
	{
		Thread hiloReloj = new Thread(() ->
		{
			while (true)
			{
				java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
				java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String texto = ahora.format(formatter);
				javax.swing.SwingUtilities.invokeLater(() -> relojLabel.setText(texto));
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					break;
				}
			}
		});
		hiloReloj.setDaemon(true);
		hiloReloj.start();
	}
}