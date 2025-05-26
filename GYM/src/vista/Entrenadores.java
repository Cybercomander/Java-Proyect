package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entidades.Entrenador;
import database.GestionDatos;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.awt.Dimension;

// CLASE "ENTRENADORES" QUE REPRESENTA LA INTERFAZ GRÁFICA PARA GESTIONAR ENTRENADORES EN EL SISTEMA.
// ESTA CLASE HEREDA DE "JPanel" Y UTILIZA COMPONENTES SWING PARA MOSTRAR Y MANIPULAR DATOS DE ENTRENADORES.

public class Entrenadores extends JPanel
{
	@Serial
	private static final long serialVersionUID = 1L;

	// ATRIBUTOS PRIVADOS DE LA CLASE:

	private JTable table;	// TABLA QUE MUESTRA LOS ENTRENADORES EN UNA INTERFAZ GRÁFICA.

	private DefaultTableModel tableModel;	// MODELO DE DATOS PARA LA TABLA, PERMITE MANIPULAR LOS DATOS MOSTRADOS.

	private JButton btnEditar, btnEliminar;	// BOTONES PARA EDITAR Y ELIMINAR ENTRENADORES.

	private JTextField searchField;	// CAMPO DE TEXTO PARA BUSCAR ENTRENADORES POR NOMBRE.

	private JTextArea detallesArea;	// ÁREA DE TEXTO QUE MUESTRA LOS DETALLES DEL ENTRENADOR SELECCIONADO.

	private JPanel panelFormulario;	// PANEL QUE CONTIENE EL FORMULARIO PARA AGREGAR O EDITAR ENTRENADORES.

	private JPanel panelCentral;	// PANEL PRINCIPAL QUE CONTIENE LA TABLA Y EL FORMULARIO.

	private boolean editando = false;	// BANDERA QUE INDICA SI SE ESTÁ EDITANDO UN ENTRENADOR.

	// CONSTRUCTOR DE LA CLASE "ENTRENADORES":
	// CONFIGURA LA INTERFAZ GRÁFICA, INCLUYENDO LA TABLA, BOTONES Y FORMULARIOS.
	// TAMBIÉN CARGA LOS ENTRENADORES DESDE LA BASE DE DATOS AL INICIAR.
	public Entrenadores()
	{
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(245, 250, 255));

		JLabel titulo = new JLabel("Entrenadores", SwingConstants.CENTER);
		titulo.setFont(new Font("Verdana", Font.BOLD, 22));
		titulo.setForeground(new Color(33, 102, 172));
		add(titulo, BorderLayout.NORTH);

		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		searchField = new JTextField();
		searchField.setFont(new Font("Verdana", Font.PLAIN, 11));
		searchField.setToolTipText("Buscar por nombre...");
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Verdana", Font.PLAIN, 11));
		btnBuscar.addActionListener(e -> buscarEntrenadores());
		topPanel.add(searchField, BorderLayout.CENTER);
		topPanel.add(btnBuscar, BorderLayout.EAST);
		add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

		// Panel central para tabla y formulario
		panelCentral = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);

		tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Salario Diario", "Horas", "Clase"}, 0)
		{
			@Serial
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
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
		btnAgregar.setFont(new Font("Verdana", Font.PLAIN, 11));
		btnEditar = new JButton("Editar");
		btnEditar.setFont(new Font("Verdana", Font.PLAIN, 11));
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setFont(new Font("Verdana", Font.PLAIN, 11));
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
		JLabel lblDetalles = new JLabel("Detalles del Entrenador", SwingConstants.CENTER);
		lblDetalles.setFont(new Font("Verdana", Font.PLAIN, 11));
		rightPanel.add(lblDetalles, BorderLayout.NORTH);
		rightPanel.add(detallesScroll, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		cargarEntrenadores();
		btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
		btnEditar.addActionListener(e -> mostrarFormularioEditar());
		btnEliminar.addActionListener(e -> eliminarEntrenador());
		table.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
	}

	// MÉTODO "cargarEntrenadores":
	// CARGA LOS ENTRENADORES DESDE LA BASE DE DATOS Y LOS MUESTRA EN LA TABLA.
	// LIMPIA EL MODELO DE LA TABLA ANTES DE AGREGAR LOS NUEVOS DATOS.
	private void cargarEntrenadores()
	{
		tableModel.setRowCount(0); // Limpiar la tabla
		ArrayList<Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		System.out.println("Cargando entrenadores desde la base de datos...");
		for (Entrenador e : entrenadores)
		{
			System.out.println("ID: " + e.getNumEmpleado() + ", Nombre: " + e.getNombre());
			String clase = (e.getClase() != null) ? String.valueOf(e.getClase().getNumClase()) : "Sin asignar";
			tableModel.addRow(new Object[]
				{
					e.getNumEmpleado(),
					e.getNombre(),
					String.format("%.2f", e.getsalarioDiario()),
					e.gethoras(),
					clase
				}
			);
		}
		System.out.println("Total entrenadores cargados: " + entrenadores.size());
	}

	// MÉTODO "buscarEntrenadores":
	// FILTRA LOS ENTRENADORES POR NOMBRE SEGÚN EL TEXTO INGRESADO EN "searchField".
	// ACTUALIZA LA TABLA CON LOS RESULTADOS DE LA BÚSQUEDA.
	private void buscarEntrenadores()
	{
		String texto = searchField.getText().trim().toLowerCase();
		tableModel.setRowCount(0);
		ArrayList<Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		for (Entrenador e : entrenadores)
		{
			if (e.getNombre().toLowerCase().contains(texto))
			{
				String clase = (e.getClase() != null) ? String.valueOf(e.getClase().getNumClase()) : "";
				tableModel.addRow(new Object[]
				{
					e.getNumEmpleado(),
					e.getNombre(),
					String.format("%.2f", e.getsalarioDiario()), e.gethoras(), clase
				});
			}
		}
	}

	// MÉTODO "mostrarDetalles":
	// MUESTRA LOS DETALLES DEL ENTRENADOR SELECCIONADO EN LA TABLA EN "detallesArea".
	// SI NO HAY NINGÚN ENTRENADOR SELECCIONADO, LIMPIA EL ÁREA DE DETALLES.
	private void mostrarDetalles()
	{
		int row = table.getSelectedRow();
		if (row == -1)
		{
			detallesArea.setText("");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		if (e != null)
		{
			String sb = "ID: " + e.getNumEmpleado() +
					"\nNombre: " + e.getNombre() +
					"\nFecha Nacimiento: " + e.getFechaNacimiento() +
					"\nSalario Diario: $" + String.format("%.2f", e.getsalarioDiario()) +
					"\nSalario por hora: $" + String.format("%.2f", e.getSalarioHora()) +
					"\nHoras: " + e.gethoras() +
					"\nClase: " + (e.getClase() != null ? e.getClase().getNumClase() : "Sin asignar");
			detallesArea.setText(sb);
		}
	}

	// MÉTODO "mostrarFormularioAgregar":
	// MUESTRA EL FORMULARIO PARA AGREGAR UN NUEVO ENTRENADOR.
	// SI YA SE ESTÁ EDITANDO, NO PERMITE ABRIR OTRO FORMULARIO.
	private void mostrarFormularioAgregar()
	{
		if (editando)
			return;
		
		editando = true;
		
		if (panelFormulario != null)
			panelCentral.remove(panelFormulario);
		
		panelFormulario = crearPanelFormulario(null);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate();
		panelCentral.repaint();
	}

	// MÉTODO "mostrarFormularioEditar":
	// MUESTRA EL FORMULARIO PARA EDITAR EL ENTRENADOR SELECCIONADO EN LA TABLA.
	// SI NO HAY NINGÚN ENTRENADOR SELECCIONADO O YA SE ESTÁ EDITANDO, NO HACE NADA.
	private void mostrarFormularioEditar()
	{
		int row = table.getSelectedRow();
		
		if (row == -1 || editando)
		{
			if (!editando)
				JOptionPane.showMessageDialog(this, "Selecciona un entrenador para editar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		
		if (e == null)
			return;
		
		editando = true;
		
		if (panelFormulario != null)
			panelCentral.remove(panelFormulario);
		
		panelFormulario = crearPanelFormulario(e);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate();
		panelCentral.repaint();
	}

	// MÉTODO "crearPanelFormulario":
	// CREA Y CONFIGURA EL FORMULARIO PARA AGREGAR O EDITAR ENTRENADORES.
	// INCLUYE CAMPOS PARA ID, NOMBRE, FECHA DE NACIMIENTO, SALARIO POR HORA Y HORAS.
	// TAMBIÉN INCLUYE BOTONES PARA GUARDAR O CANCELAR LOS CAMBIOS.
	private JPanel crearPanelFormulario(Entrenador entrenador)
	{
		JPanel panel = new JPanel(new GridLayout(0,2,5,5));
		panel.setBorder(BorderFactory.createTitledBorder(entrenador == null ? "Agregar Entrenador" : "Editar Entrenador"));
		JTextField idField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.getNumEmpleado()));
		idField.setEnabled(entrenador == null);
		JTextField nombreField = new JTextField(entrenador == null ? "" : entrenador.getNombre());
		JTextField fechaField = new JTextField(entrenador == null ? "" : entrenador.getFechaNacimiento());
		JTextField salarioHoraField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.getSalarioHora()));
		JTextField horasField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.gethoras()));
		JComboBox<Integer> claseCombo = new JComboBox<>();
		
		for (entidades.Clase c : GestionDatos.getInstancia().getClases())
		{
			claseCombo.addItem(c.getNumClase());
		}
		
		JButton btnNuevaClase = new JButton("Nueva Clase");
		btnNuevaClase.addActionListener(e ->
		{
			String input = JOptionPane.showInputDialog(this, "Número de nueva clase:");
			if (input != null && !input.trim().isEmpty())
			{
				try
				{
					int nuevoNum = Integer.parseInt(input.trim());
					boolean existe = false;
					
					for (entidades.Clase c : GestionDatos.getInstancia().getClases())
					{
						if (c.getNumClase() == nuevoNum)
						{
							existe = true;
							break;
						}
					}
					
					if (existe)
					{
						JOptionPane.showMessageDialog(this, "Ya existe una clase con ese número.");
					}
					else
					{
						entidades.Clase nueva = new entidades.Clase(nuevoNum);
						GestionDatos.getInstancia().agregarClase(nueva);
						claseCombo.addItem(nuevoNum);
						claseCombo.setSelectedItem(nuevoNum);
						JOptionPane.showMessageDialog(this, "Clase creada exitosamente.");
					}
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(this, "Número inválido.");
				}
			}
		});
		panel.add(new JLabel("ID:")); panel.add(idField);
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Fecha Nacimiento:")); panel.add(fechaField);
		panel.add(new JLabel("Salario por hora:")); panel.add(salarioHoraField);
		panel.add(new JLabel("Horas:")); panel.add(horasField);
		panel.add(new JLabel("Clase:"));
		JPanel clasePanel = new JPanel(new BorderLayout());
		clasePanel.add(claseCombo, BorderLayout.CENTER);
		clasePanel.add(btnNuevaClase, BorderLayout.EAST);
		panel.add(clasePanel);
		JButton btnGuardar = new JButton("Guardar");
		JButton btnCancelar = new JButton("Cancelar");
		panel.add(btnGuardar); panel.add(btnCancelar);
		btnGuardar.addActionListener(e ->
		{
			try
			{
				int id = Integer.parseInt(idField.getText());
				String nombre = nombreField.getText();
				String fecha = fechaField.getText();
				float salarioHora = Float.parseFloat(salarioHoraField.getText());
				int horas = Integer.parseInt(horasField.getText());
				int numClase = (Integer) claseCombo.getSelectedItem();
				entidades.Clase clase = null;
				
				for (entidades.Clase c : GestionDatos.getInstancia().getClases())
				{
					if (c.getNumClase() == numClase)
					{
						clase = c;
						break;
					}
				}
				
				if (entrenador == null)
				{
					if (buscarEntrenadorPorId(id) != null)
					{
						JOptionPane.showMessageDialog(this, "Ya existe un entrenador con ese ID.");
						return;
					}
					
					Entrenador nuevo = new Entrenador(nombre, fecha, id, salarioHora, horas);
					nuevo.setClase(clase);
					
					if (clase != null)
						clase.setEntrenador(nuevo);
					
					GestionDatos.getInstancia().guardarDatos();
					JOptionPane.showMessageDialog(this, "Entrenador agregado exitosamente.");
				}
				else
				{
					entrenador.setNombre(nombre);
					entrenador.setFechaNacimiento(fecha);
					entrenador.setSalarioHora(salarioHora);
					entrenador.setHoras(horas);
					if (clase != null)
						entrenador.setClase(clase);
					
					GestionDatos.getInstancia().guardarDatos();
					JOptionPane.showMessageDialog(this, "Entrenador editado exitosamente.");
				}
				ocultarFormulario();
				cargarEntrenadores();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
			}
		});
		btnCancelar.addActionListener(e -> ocultarFormulario());
		return panel;
	}

	// MÉTODO "ocultarFormulario":
	// OCULTA EL FORMULARIO DE AGREGAR O EDITAR ENTRENADORES Y RESTABLECE EL ESTADO DE EDICIÓN.
	private void ocultarFormulario()
	{
		if (panelFormulario != null)
		{
			panelCentral.remove(panelFormulario);
			panelFormulario = null;
			editando = false;
			panelCentral.revalidate();
			panelCentral.repaint();
		}
	}

	// MÉTODO "eliminarEntrenador":
	// ELIMINA EL ENTRENADOR SELECCIONADO EN LA TABLA DESPUÉS DE CONFIRMAR LA ACCIÓN.
	// TAMBIÉN ACTUALIZA LA BASE DE DATOS Y REFRESCA LA TABLA.
	private void eliminarEntrenador()
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selecciona un entrenador para eliminar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este entrenador?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION)
		{
			if (eliminarEntrenadorPorId(id))
			{
				try
				{
					GestionDatos.getInstancia().guardarDatos(); // Guardar en archivo
				}
				catch (java.io.IOException ex)
				{
					JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
				}
				cargarEntrenadores(); // Refrescar tabla
			}
			else
			{
				JOptionPane.showMessageDialog(this, "No se pudo eliminar el entrenador.");
			}
		}
	}

	// MÉTODO "buscarEntrenadorPorId":
	// BUSCA UN ENTRENADOR POR SU ID EN LA LISTA DE ENTRENADORES CARGADA DESDE LA BASE DE DATOS.
	private Entrenador buscarEntrenadorPorId(int id)
	{
		ArrayList<Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		
		for (Entrenador e : entrenadores)
		{
			if (e.getNumEmpleado() == id)
				return e;
		}
		
		return null;
	}

	// MÉTODO "eliminarEntrenadorPorId":
	// ELIMINA LA RELACIÓN DEL ENTRENADOR CON SU CLASE Y LO ELIMINA DE LA BASE DE DATOS.
	private boolean eliminarEntrenadorPorId(int id)
	{
		ArrayList<entidades.Clase> clases = GestionDatos.getInstancia().getClases();
		for (entidades.Clase clase : clases)
		{
			if (clase.getEntrenador() != null && clase.getEntrenador().getNumEmpleado() == id)
			{
				clase.setEntrenador(null);
				return true;
			}
		}
		return false;
	}
}