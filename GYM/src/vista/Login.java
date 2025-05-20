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

// CLASE "LOGIN" QUE REPRESENTA LA INTERFAZ GRÁFICA PARA EL INICIO DE SESIÓN EN EL SISTEMA.
// PERMITE VALIDAR USUARIOS Y CONTRASEÑAS, ASÍ COMO REGISTRAR NUEVOS USUARIOS.
public class Login {
    /** COMPONENTES DE LA INTERFAZ */
    private JFrame frame;
    private JTextField txtUsuario;        // CAMPO PARA EL USUARIO
    private JPasswordField textField_1;  // CAMPO PARA LA CONTRASEÑA

    /**
     * MÉTODO PRINCIPAL QUE INICIA LA APLICACIÓN
     * CREA Y MUESTRA LA VENTANA DE LOGIN EN EL EVENT DISPATCH THREAD
     */
    // MÉTODO "main":
    // MÉTODO PRINCIPAL QUE INICIA LA APLICACIÓN Y MUESTRA LA VENTANA DE LOGIN.
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    base_datos.UsuariosDB.getInstancia().mostrarUsuariosEnConsola();
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
    // MÉTODO CONSTRUCTOR "Login":
    // INICIALIZA LA INTERFAZ GRÁFICA DEL LOGIN.
    public Login() {
        initialize();
    }

    /*
     * INICIALIZA Y CONFIGURA TODOS LOS COMPONENTES DE LA VENTANA
     * ESTABLECE EL DISEÑO Y LAS PROPIEDADES DE LA INTERFAZ
     */
    // MÉTODO "initialize":
    // CONFIGURA TODOS LOS COMPONENTES DE LA VENTANA, INCLUYENDO CAMPOS DE TEXTO Y BOTONES.
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
        
        // BOTÓN DE REGISTRO
        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
        southPanel.add(btnRegistro);
        
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
	// MÉTODO "aceptar":
    // VALIDA EL USUARIO Y CONTRASEÑA INGRESADOS. SI SON CORRECTOS, ABRE LA VENTANA PRINCIPAL.
	private void aceptar() {
		String usuario = txtUsuario.getText();
		String contrasena = new String(textField_1.getPassword());
		if (usuario.equals("")) {
			JOptionPane.showMessageDialog(null, "Ingresa un Usuario");
			return;
		}
		try {
			if (base_datos.UsuariosDB.getInstancia().validarUsuario(usuario, contrasena)) {
				new Principal();
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al validar usuario: " + ex.getMessage());
		}
	}

	// MÉTODO "registrarUsuario":
    // PERMITE REGISTRAR UN NUEVO USUARIO EN EL SISTEMA. VALIDA QUE LOS CAMPOS NO ESTÉN VACÍOS Y QUE LAS CONTRASEÑAS COINCIDAN.
	private void registrarUsuario() {
		JTextField usuarioField = new JTextField();
		JPasswordField contraField = new JPasswordField();
		JPasswordField contra2Field = new JPasswordField();
		JPanel panel = new JPanel(new java.awt.GridLayout(0,1));
		panel.add(new JLabel("Usuario:")); panel.add(usuarioField);
		panel.add(new JLabel("Contraseña:")); panel.add(contraField);
		panel.add(new JLabel("Repetir contraseña:")); panel.add(contra2Field);
		int result = JOptionPane.showConfirmDialog(frame, panel, "Registro de Usuario", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String usuario = usuarioField.getText();
			String contra = new String(contraField.getPassword());
			String contra2 = new String(contra2Field.getPassword());
			if (usuario.isEmpty() || contra.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Usuario y contraseña no pueden estar vacíos");
				return;
			}
			if (!contra.equals(contra2)) {
				JOptionPane.showMessageDialog(frame, "Las contraseñas no coinciden");
				return;
			}
			try {
				if (base_datos.UsuariosDB.getInstancia().agregarUsuario(usuario, contra)) {
					JOptionPane.showMessageDialog(frame, "Usuario registrado exitosamente");
				} else {
					JOptionPane.showMessageDialog(frame, "El usuario ya existe");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al registrar usuario: " + ex.getMessage());
			}
		}
	}
	
	// MÉTODO "cancelar":
    // CIERRA LA VENTANA DE LOGIN.
	private void cancelar() {
		frame.dispose();
	}
	
	
}
