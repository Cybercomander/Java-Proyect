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

// CLASE "LOGIN" QUE REPRESENTA LA INTERFAZ GRÁFICA PARA EL INICIO DE SESIÓN EN EL SISTEMA.
// PERMITE VALIDAR USUARIOS Y CONTRASEÑAS, ASÍ COMO REGISTRAR NUEVOS USUARIOS.
public class Login
{
	/* COMPONENTES DE LA INTERFAZ */
	private JFrame frame;
	private JTextField txtUsuario;	// CAMPO PARA EL USUARIO
	private JPasswordField txtPassword;	// CAMPO PARA LA CONTRASEÑA

	/**
	 * MÉTODO PRINCIPAL QUE INICIA LA APLICACIÓN
	 * CREA Y MUESTRA LA VENTANA DE LOGIN EN EL EVENT DISPATCH THREAD
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(() ->
		{
			try
			{
				database.UsuariosDB.getInstancia().mostrarUsuariosEnConsola();
				Login ventana = new Login();
				ventana.frame.setVisible(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	/**
	 * CONSTRUCTOR DE LA CLASE
	 * INICIALIZA LA INTERFAZ GRÁFICA
	 */
	public Login()
	{
		initialize();
	}

	/*
	 * INICIALIZA Y CONFIGURA TODOS LOS COMPONENTES DE LA VENTANA
	 * ESTABLECE EL DISEÑO Y LAS PROPIEDADES DE LA INTERFAZ
	 */
	private void initialize()
	{	// CONFIGURA TODOS LOS COMPONENTES DE LA VENTANA, INCLUYENDO CAMPOS DE TEXTO Y BOTONES.
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
		btnAceptar.addActionListener(e -> aceptar());
		southPanel.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> cancelar());
		btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
		southPanel.add(btnCancelar);
		
		// BOTÓN DE REGISTRO
		JButton btnRegistro = new JButton("Registrarse");
		btnRegistro.addActionListener(e -> registrarUsuario());
		southPanel.add(btnRegistro);
		
		// PANEL CENTRAL - CAMPOS DE ENTRADA
		JPanel centralPanel = new JPanel();
		frame.getContentPane().add(centralPanel, BorderLayout.CENTER);
		centralPanel.setLayout(null);
		
		// ETIQUETAS Y CAMPOS DE TEXTO
		JLabel lblNewLabel = new JLabel("Sistema de administración GYM");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 26));
		lblNewLabel.setBounds(283, 25, 500, 72);
		centralPanel.add(lblNewLabel);
		
		JLabel lblUsuario = new JLabel("Usuario :");
		lblUsuario.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblUsuario.setBounds(293, 160, 71, 20);
		centralPanel.add(lblUsuario);
		
		JLabel lblContra = new JLabel("Contraseña :");
		lblContra.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblContra.setBounds(283, 272, 122, 20);
		centralPanel.add(lblContra);
		
		// CAMPO DE TEXTO PARA USUARIO
		txtUsuario = new JTextField();
		txtUsuario.setBounds(385, 154, 398, 37);
		centralPanel.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		// CAMPO DE TEXTO PARA CONTRASEÑA
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(385, 266, 398, 37);
		centralPanel.add(txtPassword);
		
		// PANEL SUPERIOR - TÍTULO DE BIENVENIDA
		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		
		JLabel title = new JLabel("Bienvenido");
		title.setFont(new Font("Verdana", Font.PLAIN, 32));
		topPanel.add(title);
		
		//VENTANA VISIBLE
		frame.setVisible(true);
	}
	
	// METODOS
	
	// VALIDA EL USUARIO Y CONTRASEÑA INGRESADOS. SI SON CORRECTOS, ABRE LA VENTANA PRINCIPAL.
	private void aceptar()
	{
		String usuario = txtUsuario.getText();
		String password = new String(txtPassword.getPassword());
		if (usuario.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Ingresa un Usuario");
			return;
		}
		try
		{
			if (database.UsuariosDB.getInstancia().validarUsuario(usuario, password))
			{
				new Principal();
				frame.dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
			}
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error al validar usuario: " + ex.getMessage());
		}
	}

	// MÉTODO "registrarUsuario":
	// PERMITE REGISTRAR UN NUEVO USUARIO EN EL SISTEMA. VALIDA QUE LOS CAMPOS NO ESTÉN VACÍOS Y QUE LAS CONTRASEÑAS COINCIDAN.
	private void registrarUsuario()
	{
		JTextField usuarioField = new JTextField();
		JPasswordField pass1Field = new JPasswordField();
		JPasswordField pass2Field = new JPasswordField();
		JPanel panel = new JPanel(new java.awt.GridLayout(0,1));
		panel.add(new JLabel("Usuario:")); panel.add(usuarioField);
		panel.add(new JLabel("Contraseña:")); panel.add(pass1Field);
		panel.add(new JLabel("Repetir contraseña:")); panel.add(pass2Field);
		int resultado = JOptionPane.showConfirmDialog(frame, panel, "Registro de Usuario", JOptionPane.OK_CANCEL_OPTION);
		
		if (resultado == JOptionPane.OK_OPTION)
		{
			String usuario = usuarioField.getText();
			String password1 = new String(pass1Field.getPassword());
			String password2 = new String(pass2Field.getPassword());
			if (usuario.isEmpty() || password1.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Usuario y contraseña no pueden estar vacíos");
				return;
			}
			if (!password1.equals(password2))
			{
				JOptionPane.showMessageDialog(frame, "Las contraseñas no coinciden");
				return;
			}
			try
			{
				if (database.UsuariosDB.getInstancia().agregarUsuario(usuario, password1))
				{
					JOptionPane.showMessageDialog(frame, "Usuario registrado exitosamente");
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "El usuario ya existe");
				}
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frame, "Error al registrar usuario: " + ex.getMessage());
			}
		}
	}
	
	// CIERRA LA VENTANA DE LOGIN.
	private void cancelar()
	{
		frame.dispose();
	}
}