package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Principal {

	private JFrame frmSistemaDeGestion;

	public Principal() {
		initialize();
		
		frmSistemaDeGestion.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
				cerrar_ventana();	//PARA SALIR DEL PROGRAMA
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
		topPanel.add(btnCancelar);
		
		JPanel centralPanel = new JPanel();
		frmSistemaDeGestion.getContentPane().add(centralPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) southPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		frmSistemaDeGestion.getContentPane().add(southPanel, BorderLayout.NORTH);
		
		JMenuBar menuBar = new JMenuBar();
		frmSistemaDeGestion.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem menuPrincipal = new JMenuItem("Principal");
		mnNewMenu.add(menuPrincipal);
		menuPrincipal.setIcon(null);
		
		JMenuItem menuEntrenadores = new JMenuItem("Entrenadores");
		mnNewMenu.add(menuEntrenadores);
		
		JMenuItem menuMiembros = new JMenuItem("Miembros");
		mnNewMenu.add(menuMiembros);
		
		JMenuItem menuPersonal = new JMenuItem("Personal");
		mnNewMenu.add(menuPersonal);
		
		JMenuItem menuSalir = new JMenuItem("Salir");
		menuSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cerrar_ventana();
			}
		});
		menuSalir.setIcon(new ImageIcon(Principal.class.getResource("/icons/cross.png")));
		mnNewMenu.add(menuSalir);
		
		frmSistemaDeGestion.setVisible(true);
	}
	
	//METODS
	public void aceptar() {
		//GUARDAR TODOS LOS CAMBIOS REALIZADOS EN EL PROGRAMA
		
		
	}
	
	public void cerrar_ventana() {
		System.exit(0);
	}
}
