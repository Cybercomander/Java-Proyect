package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Dimension;
import java.io.Serial;
import java.util.ArrayList;
import entidades.Entrenador;
import database.GestionDatos;
import entidades.Clase;

/**
 * CLASE PERSONAL QUE REPRESENTA LA INTERFAZ GRAFICA PARA GESTIONAR EL PERSONAL DEL GIMNASIO
 * HEREDA DE JPANEL Y UTILIZA COMPONENTES SWING PARA MOSTRAR Y MANIPULAR DATOS DE ENTRENADORES
 */
public class Personal extends JPanel
{
	/** IDENTIFICADOR DE VERSION PARA SERIALIZACION */
	@Serial
	private static final long serialVersionUID = 1L;
	/** TABLA QUE MUESTRA EL PERSONAL EN UNA INTERFAZ GRAFICA */
	private JTable table;
	/** MODELO DE DATOS PARA LA TABLA, PERMITE MANIPULAR LOS DATOS MOSTRADOS */
	private DefaultTableModel tableModel;
	/** AREA DE TEXTO QUE MUESTRA LOS DETALLES DEL PERSONAL SELECCIONADO */
	private JTextArea detallesArea;
	/** PANEL QUE CONTIENE EL FORMULARIO PARA AGREGAR O EDITAR PERSONAL */
	private JPanel panelFormulario;
	/** PANEL PRINCIPAL QUE CONTIENE LA TABLA Y EL FORMULARIO */
	private JPanel panelCentral;
	/** BANDERA QUE INDICA SI SE ESTA EDITANDO UN PERSONAL */
	private boolean editando = false;

	/**
	 * CONSTRUCTOR DE LA CLASE PERSONAL
	 * CONFIGURA LA INTERFAZ GRAFICA, INCLUYENDO LA TABLA, BOTONES Y FORMULARIOS
	 * TAMBIEN CARGA EL PERSONAL DESDE LA BASE DE DATOS AL INICIAR
	 */
	public Personal()
	{
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(240, 248, 255));

		JLabel titulo = new JLabel("Personal del Gimnasio", SwingConstants.CENTER);
		titulo.setFont(new Font("Verdana", Font.BOLD, 24));
		titulo.setForeground(new Color(33, 102, 172));
		add(titulo, BorderLayout.NORTH);

		// Panel central para tabla y formulario
		panelCentral = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);
		tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol", "Clase"}, 0)
		{
			@Serial
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setRowHeight(24);
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

		btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
		btnEditar.addActionListener(e -> mostrarFormularioEditar());
		btnEliminar.addActionListener(e -> eliminarPersonal());

		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setPreferredSize(new Dimension(300, 0));
		detallesArea = new JTextArea();
		detallesArea.setEditable(false);
		detallesArea.setFont(new Font("Verdana", Font.PLAIN, 14));
		detallesArea.setLineWrap(true);
		detallesArea.setWrapStyleWord(true);
		JScrollPane detallesScroll = new JScrollPane(detallesArea);
		rightPanel.add(new JLabel("Detalles del Personal", SwingConstants.CENTER), BorderLayout.NORTH);
		rightPanel.add(detallesScroll, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		GestionDatos.getInstancia().addListener(this::cargarPersonal);
		cargarPersonal();
		table.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
	}

	/**
	 * METODO cargarPersonal
	 * CARGA EL PERSONAL DESDE LA BASE DE DATOS Y LOS MUESTRA EN LA TABLA
	 * LIMPIA EL MODELO DE LA TABLA ANTES DE AGREGAR LOS NUEVOS DATOS
	 */
	private void cargarPersonal()
	{
		tableModel.setRowCount(0);
		ArrayList<Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		System.out.println("Personal cargado desde archivo:");
		
		for (Entrenador e : entrenadores)
		{
			System.out.println("ID: " + e.getNumEmpleado() + ", Nombre: " + e.getNombre());
			String clase = (e.getClase() != null) ? String.valueOf(e.getClase().getClaseID()) : "Sin asignar";
			tableModel.addRow(new Object[]{e.getNumEmpleado(), e.getNombre(), "Entrenador", clase});
		}
		// Aquí podrías agregar más tipos de personal si existieran
	}

	/**
	 * METODO mostrarDetalles
	 * MUESTRA LOS DETALLES DEL PERSONAL SELECCIONADO EN LA TABLA EN detallesArea
	 * SI NO HAY NINGUN PERSONAL SELECCIONADO, LIMPIA EL AREA DE DETALLES
	 */
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
				"\nRol: Entrenador" +
				"\nSalario Diario: $" + String.format("%.2f", e.getsalarioDiario()) +
				"\nSalario por hora: $" + String.format("%.2f", e.getSalarioHora()) +
				"\nHoras: " + e.gethoras() +
				"\nClase: " + (e.getClase() != null ? e.getClase().getClaseID() : "Sin asignar");
			detallesArea.setText(sb);
		}
	}

	/**
	 * METODO buscarEntrenadorPorId
	 * BUSCA UN ENTRENADOR POR SU ID EN LA LISTA DE ENTRENADORES CARGADA DESDE LA BASE DE DATOS
	 * @param id ID DEL ENTRENADOR A BUSCAR
	 * @return ENTRENADOR ENCONTRADO O NULL SI NO EXISTE
	 */
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

	/**
	 * METODO agregarPersonal
	 * MUESTRA UN DIALOGO PARA AGREGAR UN NUEVO ENTRENADOR Y LO AGREGA A LA LISTA
	 */
	private void agregarPersonal()
	{
		JTextField idField = new JTextField();
		JTextField nombreField = new JTextField();
		JTextField fechaField = new JTextField();
		JTextField salarioHoraField = new JTextField();
		JTextField horasField = new JTextField();
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.add(new JLabel("ID:")); panel.add(idField);
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Fecha Nacimiento:")); panel.add(fechaField);
		panel.add(new JLabel("Salario por hora:")); panel.add(salarioHoraField);
		panel.add(new JLabel("Horas:")); panel.add(horasField);
		int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Entrenador (Personal)", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION)
		{
			try
			{
				int id = Integer.parseInt(idField.getText());
				String nombre = nombreField.getText();
				String fecha = fechaField.getText();
				float salarioHora = Float.parseFloat(salarioHoraField.getText());
				int horas = Integer.parseInt(horasField.getText());
				
				// Verificar que no exista el ID
				if (buscarEntrenadorPorId(id) != null)
				{
					JOptionPane.showMessageDialog(this, "Ya existe un entrenador con ese ID.");
					return;
				}
				
				Entrenador nuevo = new Entrenador(nombre, fecha, id, salarioHora, horas);
				GestionDatos.getInstancia().getEntrenadores().add(nuevo);
				GestionDatos.getInstancia().guardarDatos();
				cargarPersonal();
				JOptionPane.showMessageDialog(this, "Entrenador agregado exitosamente.");
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
			}
		}
	}

	/**
	 * METODO editarPersonal
	 * MUESTRA UN DIALOGO PARA EDITAR LOS DATOS DEL ENTRENADOR SELECCIONADO
	 */
	private void editarPersonal()
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selecciona un entrenador para editar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		
		if (e == null)
			return;
		
		JTextField nombreField = new JTextField(e.getNombre());
		JTextField salarioField = new JTextField(String.valueOf(e.getSalarioHora()));
		JTextField horasField = new JTextField(String.valueOf(e.gethoras()));
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Salario por hora:")); panel.add(salarioField);
		panel.add(new JLabel("Horas:")); panel.add(horasField);
		int result = JOptionPane.showConfirmDialog(this, panel, "Editar Entrenador (Personal)", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION)
		{
			// Editar el objeto en memoria (no crear uno nuevo)
			e.setNombre(nombreField.getText());
			e.setSalarioHora(Float.parseFloat(salarioField.getText()));
			e.setHoras(Integer.parseInt(horasField.getText()));
			try
			{
				GestionDatos.getInstancia().guardarDatos(); // Guardar en archivo
			}
			catch (java.io.IOException ex)
			{
				JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
			}
			cargarPersonal(); // Refrescar tabla
		}
	}

	/**
	 * METODO mostrarFormularioAgregar
	 * MUESTRA EL FORMULARIO PARA AGREGAR UN NUEVO PERSONAL
	 * SI YA SE ESTA EDITANDO, NO PERMITE ABRIR OTRO FORMULARIO
	 */
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

	/**
	 * METODO mostrarFormularioEditar
	 * MUESTRA EL FORMULARIO PARA EDITAR EL PERSONAL SELECCIONADO EN LA TABLA
	 * SI NO HAY NINGUN PERSONAL SELECCIONADO O YA SE ESTA EDITANDO, NO HACE NADA
	 */
	private void mostrarFormularioEditar()
	{
		int row = table.getSelectedRow();
		
		if (row == -1 || editando)
		{
			if (!editando)
				JOptionPane.showMessageDialog(this, "Selecciona un personal para editar.");
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
		panelCentral.revalidate(); panelCentral.repaint();
	}

	/**
	 * METODO crearPanelFormulario
	 * CREA Y CONFIGURA EL FORMULARIO PARA AGREGAR O EDITAR PERSONAL
	 * INCLUYE CAMPOS PARA ID, NOMBRE, FECHA DE NACIMIENTO, SALARIO POR HORA, HORAS Y CLASE
	 * TAMBIEN INCLUYE BOTONES PARA GUARDAR O CANCELAR LOS CAMBIOS
	 * @param entrenador ENTRENADOR A EDITAR O NULL PARA AGREGAR NUEVO
	 * @return PANEL CON EL FORMULARIO
	 */
	private JPanel crearPanelFormulario(Entrenador entrenador)
	{
	JPanel panel = new JPanel(new GridLayout(0,2,5,5));
	panel.setBorder(BorderFactory.createTitledBorder(entrenador == null ? "Agregar Personal" : "Editar Personal"));
	JTextField idField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.getNumEmpleado()));
	idField.setEnabled(entrenador == null);
	JTextField nombreField = new JTextField(entrenador == null ? "" : entrenador.getNombre());
	JTextField fechaField = new JTextField(entrenador == null ? "" : entrenador.getFechaNacimiento());
	JTextField salarioHoraField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.getSalarioHora()));
	JTextField horasField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.gethoras()));
	JComboBox<Integer> claseCombo = new JComboBox<>();
	
	for (Clase c : GestionDatos.getInstancia().getClases())
	{
		claseCombo.addItem(c.getClaseID());
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
				
				// Verificar que no exista
				boolean existe = false;
				
				for (Clase c : GestionDatos.getInstancia().getClases())
				{
					if (c.getClaseID() == nuevoNum)
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
					Clase nueva = new Clase(nuevoNum);
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
			Clase clase = null;
			
			for (Clase c : GestionDatos.getInstancia().getClases())
			{
				if (c.getClaseID() == numClase)
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
			cargarPersonal();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
		}
	});
	btnCancelar.addActionListener(e -> ocultarFormulario());
	return panel;
}

	/**
	 * METODO ocultarFormulario
	 * OCULTA EL FORMULARIO DE AGREGAR O EDITAR PERSONAL Y RESTABLECE EL ESTADO DE EDICION
	 */
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

	/**
	 * METODO eliminarPersonal
	 * ELIMINA EL PERSONAL SELECCIONADO EN LA TABLA DESPUES DE CONFIRMAR LA ACCION
	 * TAMBIEN ACTUALIZA LA BASE DE DATOS Y REFRESCA LA TABLA
	 */
	private void eliminarPersonal()
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selecciona un entrenador para eliminar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		
		if (e == null)
			return;
		
		int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar al entrenador?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
		
		if (confirm == JOptionPane.YES_OPTION)
		{
			GestionDatos.getInstancia().getEntrenadores().remove(e);
			try
			{
				GestionDatos.getInstancia().guardarDatos();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
			}
			cargarPersonal();
			detallesArea.setText("");
		}
	}
}
