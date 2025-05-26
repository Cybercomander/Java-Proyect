package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.io.Serial;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.GestionDatos;
import entidades.Cliente;

public class Miembros extends JPanel
{
	@Serial
    private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton btnAgregar, btnEditar, btnEliminar;
	private JTextField searchField;
	private JTextArea detallesArea;
	private JPanel panelFormulario;
	private JPanel panelCentral;
	private boolean editando = false;

	@FunctionalInterface
	interface ClienteAction
	{
		void execute(Cliente cliente);
	}

	public Miembros()
	{
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(250, 250, 255));

		JLabel titulo = new JLabel("Miembros del Gimnasio", SwingConstants.CENTER);
		titulo.setFont(new Font("Verdana", Font.BOLD, 22));
		titulo.setForeground(new Color(33, 102, 172));
		add(titulo, BorderLayout.NORTH);

		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		searchField = new JTextField();
		searchField.setToolTipText("Buscar por nombre...");
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> buscarClientes());
		topPanel.add(searchField, BorderLayout.CENTER);
		topPanel.add(btnBuscar, BorderLayout.EAST);
		add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

		// Panel central para tabla y formulario
		panelCentral = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);
		tableModel = new DefaultTableModel(new Object[] {
			"ID",
			"Nombre",
			"Plan",
			"IMC",
			"Fecha Nacimiento",
			"Clase"},
			0)
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
		btnAgregar = new JButton("Agregar");
		btnEditar = new JButton("Editar");
		btnEliminar = new JButton("Eliminar");
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
		rightPanel.add(new JLabel("Detalles del Miembro", SwingConstants.CENTER), BorderLayout.NORTH);
		rightPanel.add(detallesScroll, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		cargarClientes();
		btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
		btnEditar.addActionListener(e -> mostrarFormularioEditar());
		btnEliminar.addActionListener(e -> eliminarCliente(this::eliminarClienteAccion));
		table.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
	}

	private void cargarClientes()
	{
		tableModel.setRowCount(0);
		ArrayList<Cliente> clientes = GestionDatos.getInstancia().getClientes();
		System.out.println("Clientes cargados desde archivo:");
		for (Cliente c : clientes)
		{
			System.out.println("ID: " + c.getIdCliente() + ", Nombre: " + c.getNombre());
			tableModel.addRow(new Object[]
			{
				c.getIdCliente(),
				c.getNombre(),
				c.getPlanMembresia(),
				String.format("%.2f", c.getIMC()),
				c.getFechaNacimiento(),
				(c.getClase() != null ? c.getClase().getClaseID() : "Sin asignar")
			});
		}
	}

	private void buscarClientes()
	{
		String texto = searchField.getText().trim().toLowerCase();
		tableModel.setRowCount(0);
		ArrayList<Cliente> clientes = GestionDatos.getInstancia().getClientes();
		
		for (Cliente c : clientes)
		{
			if (c.getNombre().toLowerCase().contains(texto))
			{
				tableModel.addRow(new Object[]{c.getIdCliente(), c.getNombre(), c.getPlanMembresia(), String.format("%.2f", c.getIMC())});
			}
		}
	}

	private void mostrarDetalles()
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			detallesArea.setText("");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		Cliente c = buscarClientePorId(id);
		if (c != null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("ID: ").append(c.getIdCliente());
			sb.append("\nNombre: ").append(c.getNombre());
			sb.append("\nPlan: ").append(c.getPlanMembresia());
			sb.append("\nFecha Nacimiento: ").append(c.getFechaNacimiento());
			sb.append("\nClase: ").append(c.getClase() != null ? c.getClase().getClaseID() : "Sin asignar");
			sb.append("\nIMC: ").append(String.format("%.2f", c.getIMC()));
			
			if (c.getMedidas() != null && c.getMedidas().size() >= 3)
			{
				sb.append("\nPeso: ").append(String.format("%.2f", c.getMedidas().get(0)));
				sb.append("\nAltura: ").append(String.format("%.2f", c.getMedidas().get(1)));
				sb.append("\nGrasa corporal: ").append(String.format("%.2f", c.getMedidas().get(2)));
			}
			
			detallesArea.setText(sb.toString());
		}
	}

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

	private void mostrarFormularioEditar()
	{
		int row = table.getSelectedRow();
		if (row == -1 || editando)
		{
			if (!editando)
				JOptionPane.showMessageDialog(this, "Selecciona un miembro para editar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		Cliente c = buscarClientePorId(id);
		
		if (c == null)
			return;
		
		editando = true;
		
		if (panelFormulario != null)
			panelCentral.remove(panelFormulario);
		
		panelFormulario = crearPanelFormulario(c);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate(); panelCentral.repaint();
	}

	// SE MODIFICÓ EL MÉTODO "crearPanelFormulario" PARA ASEGURAR QUE LOS MIEMBROS SE AGREGUEN CORRECTAMENTE AL ARCHIVO.
	private JPanel crearPanelFormulario(Cliente cliente)
	{
		JPanel panel = new JPanel(new GridLayout(0,2,5,5));
		panel.setBorder(BorderFactory.createTitledBorder(cliente == null ? "Agregar Miembro" : "Editar Miembro"));
		JTextField idField = new JTextField(cliente == null ? "" : String.valueOf(cliente.getIdCliente()));
		idField.setEnabled(cliente == null);
		JTextField nombreField = new JTextField(cliente == null ? "" : cliente.getNombre());
		JTextField planField = new JTextField(cliente == null ? "" : cliente.getPlanMembresia());
		JTextField fechaField = new JTextField(cliente == null ? "" : cliente.getFechaNacimiento());
		JTextField pesoField = new JTextField();
		JTextField alturaField = new JTextField();
		JTextField grasaField = new JTextField();
		JComboBox<Integer> claseCombo = new JComboBox<>();
		
		for (entidades.Clase c : GestionDatos.getInstancia().getClases())
		{
			claseCombo.addItem(c.getClaseID());
		}
		
		panel.add(new JLabel("ID:")); panel.add(idField);
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Plan Membresía:")); panel.add(planField);
		panel.add(new JLabel("Fecha Nacimiento:")); panel.add(fechaField);
		panel.add(new JLabel("Peso:")); panel.add(pesoField);
		panel.add(new JLabel("Altura:")); panel.add(alturaField);
		panel.add(new JLabel("Grasa corporal:")); panel.add(grasaField);
		panel.add(new JLabel("Clase:")); panel.add(claseCombo);
		JButton btnGuardar = new JButton("Guardar");
		JButton btnCancelar = new JButton("Cancelar");
		panel.add(btnGuardar); panel.add(btnCancelar);
		btnGuardar.addActionListener(e ->
		{
			try
			{
				int id = Integer.parseInt(idField.getText());
				String nombre = nombreField.getText();
				String plan = planField.getText();
				String fecha = fechaField.getText();
				float peso = Float.parseFloat(pesoField.getText());
				float altura = Float.parseFloat(alturaField.getText());
				float grasa = Float.parseFloat(grasaField.getText());
				int numClase = (Integer) claseCombo.getSelectedItem();
				entidades.Clase clase = null;
				
				for (entidades.Clase c : GestionDatos.getInstancia().getClases())
				{
					if (c.getClaseID() == numClase)
					{
						clase = c;
						break;
					}
				}
				
				if (cliente == null)
				{
					// Verifica que no exista el ID
					for (Cliente cli : GestionDatos.getInstancia().getClientes())
					{
						if (cli.getIdCliente() == id)
						{
							JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese ID.");
							return;
						}
					}
					Cliente nuevo = new Cliente(nombre, id, plan, fecha);
					nuevo.asignaMedidas(peso, altura, grasa);
					nuevo.setClase(clase);
					
					if (clase != null)
						clase.addCliente(nuevo);
					
					GestionDatos.getInstancia().guardarDatos();
					JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
				}
				else
				{
					cliente.setNombre(nombre);
					cliente.setTipo(plan);
					cliente.setFechaNacimiento(fecha);
					cliente.asignaMedidas(peso, altura, grasa);
					
					if (clase != null)
						cliente.setClase(clase);
					
					GestionDatos.getInstancia().guardarDatos();
					JOptionPane.showMessageDialog(this, "Cliente editado exitosamente.");
				}
				ocultarFormulario();
				cargarClientes();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
			}
		});
		btnCancelar.addActionListener(e -> ocultarFormulario());
		return panel;
	}

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

	private void agregarCliente(ClienteAction action)
	{
		JTextField nombreField = new JTextField();
		JTextField planField = new JTextField();
		JTextField fechaField = new JTextField();
		JTextField idField = new JTextField();
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.add(new JLabel("ID:")); panel.add(idField);
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Plan Membresía:")); panel.add(planField);
		panel.add(new JLabel("Fecha Nacimiento:")); panel.add(fechaField);
		int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION)
		{
			try
			{
				int id = Integer.parseInt(idField.getText());
				
				// Validar que el ID no exista
				if (buscarClientePorId(id) != null)
				{
					JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese ID.");
					return;
				}
				
				Cliente nuevo = new Cliente(nombreField.getText(), id, planField.getText(), fechaField.getText());
				action.execute(nuevo);
				System.out.println("Cliente agregado: ID = " + nuevo.getIdCliente() + ", Nombre = " + nuevo.getNombre());
				GestionDatos.getInstancia().guardarDatos(); // Guardar en archivo
				cargarClientes(); // Refrescar tabla
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
			}
		}
	}

	private void agregarClienteAccion(Cliente cliente)
	{
		// Validación extra para evitar duplicados en la lista
		if (buscarClientePorId(cliente.getIdCliente()) == null)
		{
			GestionDatos.getInstancia().getClientes().add(cliente);
		}
	}

	private void editarCliente()
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selecciona un cliente para editar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		Cliente c = buscarClientePorId(id);
		
		if (c == null)
			return;
		
		JTextField nombreField = new JTextField(c.getNombre());
		JTextField planField = new JTextField(c.getPlanMembresia());
		JTextField fechaField = new JTextField(c.getFechaNacimiento());
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Plan Membresía:")); panel.add(planField);
		panel.add(new JLabel("Fecha Nacimiento:")); panel.add(fechaField);
		int result = JOptionPane.showConfirmDialog(this, panel, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION)
		{
			for (entidades.Clase clase : GestionDatos.getInstancia().getClases())
			{
				ArrayList<Cliente> clientes = clase.getClientes();
				for (Cliente cli : clientes)
				{
					if (cli.getIdCliente() == id)
					{
						cli.setNombre(nombreField.getText());
						cli.setTipo(planField.getText());
						cli.setFechaNacimiento(fechaField.getText());
						break;
					}
				}
			}
			try
			{
				GestionDatos.getInstancia().guardarDatos(); // Guardar en archivo
			}
			catch (java.io.IOException ex)
			{
				JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
			}
			cargarClientes(); // Refrescar tabla
		}
	}

	private void eliminarCliente(ClienteAction action)
	{
		int row = table.getSelectedRow();
		
		if (row == -1)
		{
			JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar.");
			return;
		}
		
		int id = (int) tableModel.getValueAt(row, 0);
		int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION)
		{
			Cliente c = buscarClientePorId(id);
			if (c != null)
			{
				action.execute(c);
				try
				{
					GestionDatos.getInstancia().guardarDatos(); // Guardar en archivo
				}
				catch (java.io.IOException ex)
				{
					JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
				}
				cargarClientes(); // Refrescar tabla
			}
			else
			{
				JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.");
			}
		}
	}

	private void eliminarClienteAccion(Cliente cliente)
	{
		ArrayList<entidades.Clase> clases = GestionDatos.getInstancia().getClases();
		
		for (entidades.Clase clase : clases)
		{
			ArrayList<Cliente> clientes = clase.getClientes();
			clientes.removeIf(c -> c.getIdCliente() == cliente.getIdCliente());
		}
		
		GestionDatos.getInstancia().getClientes().removeIf(c -> c.getIdCliente() == cliente.getIdCliente());
	}

	private Cliente buscarClientePorId(int id)
	{
		ArrayList<Cliente> clientes = GestionDatos.getInstancia().getClientes();
		
		for (Cliente c : clientes)
		{
			if (c.getIdCliente() == id)
				return c;
		}
		
		return null;
	}
}