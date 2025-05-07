package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login {
    /** COMPONENTES DE LA INTERFAZ */
    private JFrame frame;
    private JTextField txtUsuario;        // CAMPO PARA EL USUARIO
    private JPasswordField textField_1;  // CAMPO PARA LA CONTRASEÑA

    /**
     * MÉTODO PRINCIPAL QUE INICIA LA APLICACIÓN
     * CREA Y MUESTRA LA VENTANA DE LOGIN EN EL EVENT DISPATCH THREAD
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login window = new Login();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * CONSTRUCTOR DE LA CLASE
     * INICIALIZA LA INTERFAZ GRÁFICA
     */
    public Login() {
        initialize();
    }

    /**
     * INICIALIZA Y CONFIGURA TODOS LOS COMPONENTES DE LA VENTANA
     * ESTABLECE EL DISEÑO Y LAS PROPIEDADES DE LA INTERFAZ
     */
    private void initialize() {
        // CONFIGURACIÓN DEL FRAME PRINCIPAL
        frame = new JFrame();
        frame.setBounds(100, 100, 1080, 581);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // PANEL SUR - BOTONES DE ACCIÓN
        JPanel southPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) southPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        // BOTONES DE ACEPTAR Y CANCELAR
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// ACCIÓN AL HACER CLIC EN ACEPTAR
				aceptar();
        	}
        });
        southPanel.add(btnAceptar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// ACCIÓN AL HACER CLIC EN CANCELAR
        		cancelar();
        	}
        });
        btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
        southPanel.add(btnCancelar);
        
        // PANEL CENTRAL - CAMPOS DE ENTRADA
        JPanel centralPanel = new JPanel();
        frame.getContentPane().add(centralPanel, BorderLayout.CENTER);
        centralPanel.setLayout(null);
        
        // ETIQUETAS Y CAMPOS DE TEXTO
        JLabel lblNewLabel = new JLabel("Sistema de administración GYM");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        lblNewLabel.setBounds(283, 25, 500, 72);
        centralPanel.add(lblNewLabel);
        
        JLabel lblUsuario = new JLabel("Usuario :");
        lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsuario.setBounds(293, 160, 71, 20);
        centralPanel.add(lblUsuario);
        
        JLabel lblContra = new JLabel("Contraseña :");
        lblContra.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblContra.setBounds(283, 272, 122, 20);
        centralPanel.add(lblContra);
        
        // CAMPO DE TEXTO PARA USUARIO
        txtUsuario = new JTextField();
        txtUsuario.setBounds(385, 154, 398, 37);
        centralPanel.add(txtUsuario);
        txtUsuario.setColumns(10);
        
        // CAMPO DE TEXTO PARA CONTRASEÑA
        textField_1 = new JPasswordField();
        textField_1.setColumns(10);
        textField_1.setBounds(385, 266, 398, 37);
        centralPanel.add(textField_1);
        
        // PANEL SUPERIOR - TÍTULO DE BIENVENIDA
        JPanel topPanel = new JPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        
        JLabel title = new JLabel("Bienvenido");
        title.setFont(new Font("Tahoma", Font.PLAIN, 32));
        topPanel.add(title);
        
        //VENTANA VISIBLE
        frame.setVisible(true);
    }
    
	//METODOS
	private void aceptar() {
		//VALIDAR USUARIO
		if (txtUsuario.getText().equals(""))
			JOptionPane.showMessageDialog(null, "Ingresa un Usuario");
		else {
			new Principal(); //CREAR OBJETO DE VENTANA MENU
			frame.dispose(); //PARA DESAPARECER EL LOGIN
			
			/* 
			 * APLICAR VISIBILIDAD AL MENU CUANDO NO PONGO METODO 
			 * DE VISIBILIDAD DENTRO DEL CONSTRUCTOR 
			 *
			 * Menu m = new Menu();
			 * m.setVisible(true);
			 */
		}
	}
	
	private void cancelar() {
		frame.dispose();
	}
	
	
}
