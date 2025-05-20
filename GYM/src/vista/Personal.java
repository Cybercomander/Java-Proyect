package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Dimension;
import java.util.ArrayList;
import entidades.Entrenador;
import base_datos.GestionDatos;
import entidades.Clase;

public class Personal extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextArea detallesArea;
	private JPanel panelFormulario;
	private JPanel panelCentral;
	private boolean editando = false;

	public Personal() {
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(240, 248, 255));

		JLabel titulo = new JLabel("Personal del Gimnasio", SwingConstants.CENTER);
		titulo.setFont(new Font("Tahoma", Font.BOLD, 24));
		titulo.setForeground(new Color(33, 102, 172));
		add(titulo, BorderLayout.NORTH);

		// Panel central para tabla y formulario
		panelCentral = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);
		tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol", "Clase"}, 0) {
			public boolean isCellEditable(int row, int column) { return false; }
		};
		table = new JTable(tableModel);
		table.setRowHeight(24);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
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
		detallesArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
		detallesArea.setLineWrap(true);
		detallesArea.setWrapStyleWord(true);
		JScrollPane detallesScroll = new JScrollPane(detallesArea);
		rightPanel.add(new JLabel("Detalles del Personal", SwingConstants.CENTER), BorderLayout.NORTH);
		rightPanel.add(detallesScroll, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		cargarPersonal();
		table.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
	}

	private void cargarPersonal() {
		tableModel.setRowCount(0);
		ArrayList<Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		System.out.println("Personal cargado desde archivo:");
		for (Entrenador e : entrenadores) {
			System.out.println("ID: " + e.getNumEmpleado() + ", Nombre: " + e.getNombre());
			String clase = (e.getClase() != null) ? String.valueOf(e.getClase().getNumClase()) : "Sin asignar";
			tableModel.addRow(new Object[]{e.getNumEmpleado(), e.getNombre(), "Entrenador", clase});
		}
		// Aquí podrías agregar más tipos de personal si existieran
	}

	private void mostrarDetalles() {
		int row = table.getSelectedRow();
		if (row == -1) {
			detallesArea.setText("");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		if (e != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("ID: ").append(e.getNumEmpleado());
			sb.append("\nNombre: ").append(e.getNombre());
			sb.append("\nFecha Nacimiento: ").append(e.getFechaNacimiento());
			sb.append("\nRol: Entrenador");
			sb.append("\nSalario Diario: $").append(String.format("%.2f", e.getsalarioDiario()));
			sb.append("\nSalario por hora: $").append(String.format("%.2f", e.getSalarioHora()));
			sb.append("\nHoras: ").append(e.gethoras());
			sb.append("\nClase: ").append(e.getClase() != null ? e.getClase().getNumClase() : "Sin asignar");
			detallesArea.setText(sb.toString());
		}
	}

	private Entrenador buscarEntrenadorPorId(int id) {
		ArrayList<Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		for (Entrenador e : entrenadores) {
			if (e.getNumEmpleado() == id) return e;
		}
		return null;
	}

	private void agregarPersonal() {
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
		if (result == JOptionPane.OK_OPTION) {
			try {
				int id = Integer.parseInt(idField.getText());
				String nombre = nombreField.getText();
				String fecha = fechaField.getText();
				float salarioHora = Float.parseFloat(salarioHoraField.getText());
				int horas = Integer.parseInt(horasField.getText());
				// Verificar que no exista el ID
				if (buscarEntrenadorPorId(id) != null) {
					JOptionPane.showMessageDialog(this, "Ya existe un entrenador con ese ID.");
					return;
				}
				Entrenador nuevo = new Entrenador(nombre, fecha, id, salarioHora, horas);
				GestionDatos.getInstancia().getEntrenadores().add(nuevo);
				GestionDatos.getInstancia().guardarDatos();
				cargarPersonal();
				JOptionPane.showMessageDialog(this, "Entrenador agregado exitosamente.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
			}
		}
	}

	private void editarPersonal() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Selecciona un entrenador para editar.");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		if (e == null) return;
		JTextField nombreField = new JTextField(e.getNombre());
		JTextField salarioField = new JTextField(String.valueOf(e.getSalarioHora()));
		JTextField horasField = new JTextField(String.valueOf(e.gethoras()));
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.add(new JLabel("Nombre:")); panel.add(nombreField);
		panel.add(new JLabel("Salario por hora:")); panel.add(salarioField);
		panel.add(new JLabel("Horas:")); panel.add(horasField);
		int result = JOptionPane.showConfirmDialog(this, panel, "Editar Entrenador (Personal)", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			// Editar el objeto en memoria (no crear uno nuevo)
			e.setNombre(nombreField.getText());
			e.setSalarioHora(Float.parseFloat(salarioField.getText()));
			e.setHoras(Integer.parseInt(horasField.getText()));
			try {
				GestionDatos.getInstancia().guardarDatos(); // Guardar en archivo
			} catch (java.io.IOException ex) {
				JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
			}
			cargarPersonal(); // Refrescar tabla
		}
	}

	private void mostrarFormularioAgregar() {
		if (editando) return;
		editando = true;
		if (panelFormulario != null) panelCentral.remove(panelFormulario);
		panelFormulario = crearPanelFormulario(null);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate(); panelCentral.repaint();
	}

	private void mostrarFormularioEditar() {
		int row = table.getSelectedRow();
		if (row == -1 || editando) {
			if (!editando)
				JOptionPane.showMessageDialog(this, "Selecciona un personal para editar.");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		if (e == null) return;
		editando = true;
		if (panelFormulario != null) panelCentral.remove(panelFormulario);
		panelFormulario = crearPanelFormulario(e);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate(); panelCentral.repaint();
	}

private JPanel crearPanelFormulario(Entrenador entrenador) {
    JPanel panel = new JPanel(new GridLayout(0,2,5,5));
    panel.setBorder(BorderFactory.createTitledBorder(entrenador == null ? "Agregar Personal" : "Editar Personal"));
    JTextField idField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.getNumEmpleado()));
    idField.setEnabled(entrenador == null);
    JTextField nombreField = new JTextField(entrenador == null ? "" : entrenador.getNombre());
    JTextField fechaField = new JTextField(entrenador == null ? "" : entrenador.getFechaNacimiento());
    JTextField salarioHoraField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.getSalarioHora()));
    JTextField horasField = new JTextField(entrenador == null ? "" : String.valueOf(entrenador.gethoras()));
    JComboBox<Integer> claseCombo = new JComboBox<>();
    for (Clase c : GestionDatos.getInstancia().getClases()) {
        claseCombo.addItem(c.getNumClase());
    }
    JButton btnNuevaClase = new JButton("Nueva Clase");
    btnNuevaClase.addActionListener(e -> {
        String input = JOptionPane.showInputDialog(this, "Número de nueva clase:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int nuevoNum = Integer.parseInt(input.trim());
                // Verificar que no exista
                boolean existe = false;
                for (Clase c : GestionDatos.getInstancia().getClases()) {
                    if (c.getNumClase() == nuevoNum) { existe = true; break; }
                }
                if (existe) {
                    JOptionPane.showMessageDialog(this, "Ya existe una clase con ese número.");
                } else {
                    Clase nueva = new Clase(nuevoNum);
                    GestionDatos.getInstancia().agregarClase(nueva);
                    claseCombo.addItem(nuevoNum);
                    claseCombo.setSelectedItem(nuevoNum);
                    JOptionPane.showMessageDialog(this, "Clase creada exitosamente.");
                }
            } catch (Exception ex) {
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
    btnGuardar.addActionListener(e -> {
        try {
            int id = Integer.parseInt(idField.getText());
            String nombre = nombreField.getText();
            String fecha = fechaField.getText();
            float salarioHora = Float.parseFloat(salarioHoraField.getText());
            int horas = Integer.parseInt(horasField.getText());
            int numClase = (Integer) claseCombo.getSelectedItem();
            Clase clase = null;
            for (Clase c : GestionDatos.getInstancia().getClases()) {
                if (c.getNumClase() == numClase) {
                    clase = c;
                    break;
                }
            }
            if (entrenador == null) {
                if (buscarEntrenadorPorId(id) != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe un entrenador con ese ID.");
                    return;
                }
                Entrenador nuevo = new Entrenador(nombre, fecha, id, salarioHora, horas);
                nuevo.setClase(clase);
                if (clase != null) clase.setEntrenador(nuevo);
                GestionDatos.getInstancia().guardarDatos();
                JOptionPane.showMessageDialog(this, "Entrenador agregado exitosamente.");
            } else {
                entrenador.setNombre(nombre);
                entrenador.setFechaNacimiento(fecha);
                entrenador.setSalarioHora(salarioHora);
                entrenador.setHoras(horas);
                if (clase != null) entrenador.setClase(clase);
                GestionDatos.getInstancia().guardarDatos();
                JOptionPane.showMessageDialog(this, "Entrenador editado exitosamente.");
            }
            ocultarFormulario();
            cargarPersonal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
        }
    });
    btnCancelar.addActionListener(e -> ocultarFormulario());
    return panel;
}

	private void ocultarFormulario() {
		if (panelFormulario != null) {
			panelCentral.remove(panelFormulario);
			panelFormulario = null;
			editando = false;
			panelCentral.revalidate(); panelCentral.repaint();
		}
	}

	private void eliminarPersonal() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Selecciona un entrenador para eliminar.");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Entrenador e = buscarEntrenadorPorId(id);
		if (e == null) return;
		int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar al entrenador?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			GestionDatos.getInstancia().getEntrenadores().remove(e);
			try {
				GestionDatos.getInstancia().guardarDatos();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
			}
			cargarPersonal();
			detallesArea.setText("");
		}
	}
}
