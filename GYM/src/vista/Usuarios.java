package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import database.GestionDatos;

// CLASE "USUARIOS" QUE REPRESENTA LA INTERFAZ GRÁFICA PARA GESTIONAR LOS USUARIOS DEL SISTEMA.
// PERMITE AGREGAR, EDITAR, ELIMINAR Y BUSCAR USUARIOS.
public class Usuarios extends JPanel
{
	@Serial
    private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField searchField;
	private JTextArea detallesArea;
	private java.util.List<UsuarioSimple> usuarios;
	private JPanel panelFormulario;
	private JPanel panelCentral;
	private boolean editando = false;

	// MÉTODO CONSTRUCTOR "Usuarios":
	// CONFIGURA LA INTERFAZ GRÁFICA, INCLUYENDO LA TABLA, BOTONES Y FORMULARIOS.
	public Usuarios()
	{
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(250, 250, 255));

		JLabel titulo = new JLabel("Usuarios del Sistema", SwingConstants.CENTER);
		titulo.setFont(new Font("Verdana", Font.BOLD, 22));
		titulo.setForeground(new Color(33, 102, 172));
		add(titulo, BorderLayout.NORTH);

		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		searchField = new JTextField();
		searchField.setToolTipText("Buscar por usuario...");
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(event -> buscarUsuarios());
		topPanel.add(searchField, BorderLayout.CENTER);
		topPanel.add(btnBuscar, BorderLayout.EAST);
		add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

		// Panel central para tabla y formulario
		panelCentral = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);
		tableModel = new DefaultTableModel(new Object[]{"Usuario", "Contraseña"}, 0)
		{
			@Serial
            private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int fila, int columna)
			{
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setRowHeight(22);
		table.setFont(new Font("Verdana", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(table);
		panelCentral.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnAgregar = new JButton("Agregar");
		JButton btnEditar = new JButton("Editar");
		JButton btnEliminar = new JButton("Eliminar");
		buttonPanel.add(btnAgregar);
		buttonPanel.add(btnEditar);
		buttonPanel.add(btnEliminar);
		add(buttonPanel, BorderLayout.SOUTH);

		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setPreferredSize(new Dimension(300, 0));
		detallesArea = new JTextArea();
		detallesArea.setEditable(false);
		detallesArea.setFont(new Font("Verdana", Font.PLAIN, 14));
		detallesArea.setLineWrap(true);
		detallesArea.setWrapStyleWord(true);
		JScrollPane detallesScroll = new JScrollPane(detallesArea);
		rightPanel.add(new JLabel("Detalles del Usuario", SwingConstants.CENTER), BorderLayout.NORTH);
		rightPanel.add(detallesScroll, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		btnAgregar.addActionListener(event -> mostrarFormularioAgregar());
		btnEditar.addActionListener(event -> mostrarFormularioEditar());
		btnEliminar.addActionListener(event -> eliminarUsuario());
		table.getSelectionModel().addListSelectionListener(event -> mostrarDetalles());

		GestionDatos.getInstancia().addListener(this::cargarUsuarios);

		cargarUsuarios();
	}

	// MÉTODO "cargarUsuarios":
	// CARGA LOS USUARIOS DESDE EL ARCHIVO "Usuarios.dat" Y LOS MUESTRA EN LA TABLA.
	private void cargarUsuarios()
	{
		tableModel.setRowCount(0);
		usuarios = new ArrayList<>();
		try (RandomAccessFile archivo = new RandomAccessFile("Usuarios.dat", "r"))
		{
			int SIZE_USUARIO = 20, SIZE_PASSWORD = 20, SIZE_REGISTRO = SIZE_USUARIO * 2 + SIZE_PASSWORD * 2;
			long registros = archivo.length() / SIZE_REGISTRO;
			for (int i = 0; i < registros; i++)
			{
				String usuarios = leerCadenaFija(archivo, SIZE_USUARIO).trim();
				String passwords = leerCadenaFija(archivo, SIZE_PASSWORD).trim();
				this.usuarios.add(new UsuarioSimple(usuarios, passwords));
				tableModel.addRow(new Object[]{usuarios, passwords});
			}
		}
		catch (Exception e)
		{
			// No hay usuarios
		}
	}

	// METODO "buscarUsuarios":
	// FILTRA LOS USUARIOS POR NOMBRE SEGUN EL TEXTO INGRESADO EN "searchField".
	private void buscarUsuarios()
	{
		// OBTIENE EL TEXTO INGRESADO EN EL CAMPO DE BUSQUEDA Y LO CONVIERTE A MINUSCULAS
		String texto = searchField.getText().trim().toLowerCase();
		// LIMPIA LA TABLA ANTES DE AGREGAR LOS RESULTADOS FILTRADOS
		tableModel.setRowCount(0);
		// RECORRE LA LISTA DE USUARIOS Y AGREGA SOLO LOS QUE COINCIDEN CON EL FILTRO
		for (UsuarioSimple user : usuarios)
		{
			if (user.usuario.toLowerCase().contains(texto))
			{
				tableModel.addRow(new Object[]{user.usuario, user.password});
			}
		}
	}

	// METODO "mostrarDetalles":
	// MUESTRA LOS DETALLES DEL USUARIO SELECCIONADO EN LA TABLA EN "detallesArea".
	private void mostrarDetalles()
	{
		// OBTIENE LA FILA SELECCIONADA EN LA TABLA
		int fila = table.getSelectedRow();
		// SI NO HAY FILA SELECCIONADA, LIMPIA EL AREA DE DETALLES Y SALE
		if (fila == -1)
		{
			detallesArea.setText("");
			return;
		}
		// OBTIENE EL USUARIO Y CONTRASENA DE LA FILA SELECCIONADA
		String usuario = (String) tableModel.getValueAt(fila, 0);
		String contra = (String) tableModel.getValueAt(fila, 1);
		// CONSTRUYE EL TEXTO DE DETALLES Y LO MUESTRA EN EL AREA
		String string = "Usuario: " + usuario +
				"\nContraseña: " + contra;
		detallesArea.setText(string);
	}

	// MÉTODO "mostrarFormularioAgregar":
	// MUESTRA EL FORMULARIO PARA AGREGAR UN NUEVO USUARIO.
	private void mostrarFormularioAgregar()
	{
		if (editando)
			return;
		
		editando = true;
		
		if (panelFormulario != null)
			panelCentral.remove(panelFormulario);
		
		panelFormulario = crearPanelFormulario(null);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate(); panelCentral.repaint();
	}

	// MÉTODO "mostrarFormularioEditar":
	// MUESTRA EL FORMULARIO PARA EDITAR EL USUARIO SELECCIONADO EN LA TABLA.
	private void mostrarFormularioEditar()
	{
		int fila = table.getSelectedRow();
		
		if (fila == -1 || editando)
		{
			if (!editando)
				JOptionPane.showMessageDialog(this, "Selecciona un usuario para editar.");
			return;
		}
		
		String usuario = (String) tableModel.getValueAt(fila, 0);
		UsuarioSimple user = buscarUsuarioPorNombre(usuario);
		
		if (user == null)
			return;
		
		editando = true;
		
		if (panelFormulario != null)
			panelCentral.remove(panelFormulario);
		
		panelFormulario = crearPanelFormulario(user);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate(); panelCentral.repaint();
	}

	// MÉTODO "crearPanelFormulario":
	// CREA Y CONFIGURA EL FORMULARIO PARA AGREGAR O EDITAR USUARIOS.
	private JPanel crearPanelFormulario(UsuarioSimple usuario)
	{
		JPanel panel = new JPanel(new GridLayout(0,2,5,5));
		panel.setBorder(BorderFactory.createTitledBorder(usuario == null ? "Agregar Usuario" : "Editar Usuario"));
		JTextField usuarioField = new JTextField(usuario == null ? "" : usuario.usuario);
		usuarioField.setEnabled(usuario == null);
		JTextField contraField = new JTextField(usuario == null ? "" : usuario.password);
		panel.add(new JLabel("Usuario:")); panel.add(usuarioField);
		panel.add(new JLabel("Contraseña:")); panel.add(contraField);
		JButton btnGuardar = new JButton("Guardar");
		JButton btnCancelar = new JButton("Cancelar");
		panel.add(btnGuardar); panel.add(btnCancelar);
		btnGuardar.addActionListener(e ->
		{
			try
			{
				String usuarioTxt = usuarioField.getText().trim();
				String contraTxt = contraField.getText().trim();
				if (usuarioTxt.isEmpty() || contraTxt.isEmpty())
				{
					JOptionPane.showMessageDialog(this, "Usuario y contraseña requeridos.");
					return;
				}
				if (usuario == null)
				{
					if (buscarUsuarioPorNombre(usuarioTxt) != null)
					{
						JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese nombre.");
						return;
					}
					agregarUsuarioArchivo(usuarioTxt, contraTxt);
				}
				else
				{
					eliminarUsuarioArchivo(usuario.usuario);
					agregarUsuarioArchivo(usuarioTxt, contraTxt);
				}
				ocultarFormulario();
				cargarUsuarios();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			}
		});
		btnCancelar.addActionListener(e -> ocultarFormulario());
		return panel;
	}

	// MÉTODO "ocultarFormulario":
	// OCULTA EL FORMULARIO DE AGREGAR O EDITAR USUARIOS Y RESTABLECE EL ESTADO DE EDICIÓN.
	private void ocultarFormulario()
	{
		if (panelFormulario != null)
		{
			panelCentral.remove(panelFormulario);
			panelFormulario = null;
			editando = false;
			panelCentral.revalidate(); panelCentral.repaint();
		}
	}

	// MÉTODO "eliminarUsuario":
	// ELIMINA EL USUARIO SELECCIONADO EN LA TABLA DESPUÉS DE CONFIRMAR LA ACCIÓN.
	private void eliminarUsuario()
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
			return;
		}
		
		String usuario = (String) tableModel.getValueAt(row, 0);
		int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION)
		{
			try
			{
				eliminarUsuarioArchivo(usuario);
				JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente");
				cargarUsuarios();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + ex.getMessage());
			}
		}
	}

	// MÉTODO "eliminarUsuarioArchivo":
	// ELIMINA UN USUARIO DEL ARCHIVO "Usuarios.dat" REESCRIBIENDO EL ARCHIVO SIN ESE USUARIO.
	private void eliminarUsuarioArchivo(String usuario) throws Exception
	{
		java.io.File file = new java.io.File("Usuarios.dat");
		java.io.File temp = new java.io.File("Usuarios_temp.dat");
		int SIZE_USUARIO = 20, SIZE_PASSWORD = 20, SIZE_REGISTRO = SIZE_USUARIO * 2 + SIZE_PASSWORD * 2;

        try (
			RandomAccessFile archivo = new RandomAccessFile(file, "r");
			RandomAccessFile archivoTemp = new RandomAccessFile(temp, "rw")
			)
		{
			long registros = archivo.length() / SIZE_REGISTRO;
			for (int i = 0; i < registros; i++)
			{
				String usuarios = leerCadenaFija(archivo, SIZE_USUARIO);
				String passwords = leerCadenaFija(archivo, SIZE_PASSWORD);
				
				if (!usuarios.trim().equals(usuario))
				{
					escribirCadenaFija(archivoTemp, usuarios, SIZE_USUARIO);
					escribirCadenaFija(archivoTemp, passwords, SIZE_PASSWORD);
				}
			}
		}
		file.delete();
		temp.renameTo(file);
	}

	// SE AGREGÓ EL MÉTODO "agregarUsuarioArchivo" PARA ESCRIBIR NUEVOS USUARIOS EN EL ARCHIVO "Usuarios.dat".
	private void agregarUsuarioArchivo(String usuario, String password) throws Exception
	{
		try (RandomAccessFile archivo = new RandomAccessFile("Usuarios.dat", "rw"))
		{
			archivo.seek(archivo.length()); // MOVER AL FINAL DEL ARCHIVO
			escribirCadenaFija(archivo, usuario, 20);
			escribirCadenaFija(archivo, password, 20);
		}
	}

	private String leerCadenaFija(RandomAccessFile archivo, int size) throws Exception
	{
		char[] chars = new char[size];
		for (int i = 0; i < size; i++)
		{
			chars[i] = archivo.readChar();
		}
		return new String(chars);
	}

	private void escribirCadenaFija(RandomAccessFile archivo, String string, int size) throws Exception
	{
		StringBuilder stringBuilder = new StringBuilder(string);
		stringBuilder.setLength(size);
		archivo.writeChars(stringBuilder.toString());
	}

	// SE CORRIGIÓ LA CLASE INTERNA "UsuarioSimple" PARA INCLUIR LOS ATRIBUTOS NECESARIOS.
	private static class UsuarioSimple
	{
		String usuario;
		String password;

		UsuarioSimple(String usuario, String password)
		{
			this.usuario = usuario;
			this.password = password;
		}
	}

	// SE AGREGÓ EL MÉTODO "buscarUsuarioPorNombre" PARA BUSCAR UN USUARIO POR SU NOMBRE EN LA LISTA DE USUARIOS.
	private UsuarioSimple buscarUsuarioPorNombre(String nombreUsuario)
	{
		for (UsuarioSimple usuario : usuarios)
		{
			if (usuario.usuario.equals(nombreUsuario))
			{
				return usuario;
			}
		}
		return null;
	}
}