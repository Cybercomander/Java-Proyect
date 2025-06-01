package vista;

// IMPORTACIONES NECESARIAS PARA LA INTERFAZ GRAFICA Y MANEJO DE DATOS
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import database.GestionDatos;
import entidades.Clase;
import entidades.Cliente;

/**
 * CLASE QUE REPRESENTA EL PANEL DE GESTION DE CLASES DEL GIMNASIO.
 * PERMITE VISUALIZAR, BUSCAR, AGREGAR, EDITAR Y ELIMINAR CLASES,
 * ASI COMO MOSTRAR DETALLES DE CADA CLASE SELECCIONADA.
 */
public class Clases extends JPanel {

	private static final long serialVersionUID = 1L;
	// CAMPO DE TEXTO PARA BUSQUEDA DE CLASES POR ID
	private JTextField searchField;
	// AREA DE TEXTO PARA MOSTRAR DETALLES DE LA CLASE SELECCIONADA
	private JTextArea detallesArea;
	// MODELO DE LA TABLA QUE CONTIENE LAS CLASES
	private DefaultTableModel tableModel;
	// TABLA PARA MOSTRAR LAS CLASES
	private JTable table;
	// PANEL PARA EL FORMULARIO DE AGREGAR/EDITAR CLASES
	private JPanel panelFormulario;
	// PANEL CENTRAL QUE CONTIENE LA TABLA Y EL FORMULARIO
	private JPanel panelCentral;
	// BANDERA PARA INDICAR SI SE ESTA EDITANDO/AGREGANDO UNA CLASE
	private boolean editando = false;

	/**
	 * CONSTRUCTOR DEL PANEL CLASES. INICIALIZA LA INTERFAZ GRAFICA Y SUS COMPONENTES.
	 */
	public Clases() {
		setLayout(new BorderLayout(10, 10));
		setBackground(new java.awt.Color(250, 250, 255));

		// TITULO PRINCIPAL
		JLabel titulo = new JLabel("Clases del Gimnasio", SwingConstants.CENTER);
		titulo.setFont(new Font("Verdana", Font.BOLD, 22));
		titulo.setForeground(new java.awt.Color(33, 102, 172));
		add(titulo, BorderLayout.NORTH);

		// PANEL SUPERIOR CON CAMPO DE BUSQUEDA Y BOTON
		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		searchField = new JTextField();
		searchField.setToolTipText("Buscar por numero de clase...");
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> buscarClases());
		topPanel.add(searchField, BorderLayout.CENTER);
		topPanel.add(btnBuscar, BorderLayout.EAST);
		add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

		// PANEL CENTRAL CON LA TABLA DE CLASES
		panelCentral = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);
		tableModel = new DefaultTableModel(new Object[] { "ID", "Horario", "Entrenador", "#Miembros" }, 0) {
			// HACE QUE LAS CELDAS NO SEAN EDITABLES DIRECTAMENTE
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setRowHeight(22);
		table.setFont(new Font("Verdana", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(table);
		panelCentral.add(scrollPane, BorderLayout.CENTER);

		// PANEL INFERIOR CON BOTONES DE ACCION
		JPanel buttonPanel = new JPanel();
		JButton btnAgregar = new JButton("Agregar");
		JButton btnEditar = new JButton("Editar");
		JButton btnEliminar = new JButton("Eliminar");
		buttonPanel.add(btnAgregar);
		buttonPanel.add(btnEditar);
		buttonPanel.add(btnEliminar);
		add(buttonPanel, BorderLayout.SOUTH);

		// PANEL DERECHO PARA MOSTRAR DETALLES DE LA CLASE SELECCIONADA
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setPreferredSize(new Dimension(300, 0));
		detallesArea = new JTextArea();
		detallesArea.setEditable(false);
		detallesArea.setFont(new Font("Verdana", Font.PLAIN, 14));
		detallesArea.setLineWrap(true);
		detallesArea.setWrapStyleWord(true);
		JScrollPane detallesScroll = new JScrollPane(detallesArea);
		rightPanel.add(new JLabel("Detalles de la Clase", SwingConstants.CENTER), BorderLayout.NORTH);
		rightPanel.add(detallesScroll, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		// CARGA INICIAL DE LAS CLASES EN LA TABLA
		cargarClases();
		// ASIGNACION DE ACCIONES A LOS BOTONES
		btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
		btnEditar.addActionListener(e -> mostrarFormularioEditar());
		btnEliminar.addActionListener(e -> eliminarClase());
		// LISTENER PARA MOSTRAR DETALLES AL SELECCIONAR UNA FILA
		table.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());
		// Suscribirse a cambios de datos para refrescar la tabla automáticamente
		GestionDatos.getInstancia().addListener(this::cargarClases);
	}

	// ===================== METODOS =====================

	/**
	 * CARGA TODAS LAS CLASES DESDE LA BASE DE DATOS Y LAS MUESTRA EN LA TABLA.
	 */
	private void cargarClases() {
		tableModel.setRowCount(0);
		ArrayList<Clase> clases = GestionDatos.getInstancia().getClases();
		for (Clase c : clases) {
			String entrenador = (c.getEntrenador() != null) ? c.getEntrenador().getNombre() : "Sin asignar";
			tableModel.addRow(new Object[] { c.getClaseID(), c.getHorario(), entrenador, c.getClientes().size() });
		}
	}

	/**
	 * BUSCA CLASES CUYO ID CONTENGA EL TEXTO INGRESADO EN EL CAMPO DE BUSQUEDA.
	 * ACTUALIZA LA TABLA Y EL AREA DE DETALLES CON LOS RESULTADOS.
	 */
	private void buscarClases() {
		String texto = searchField.getText().trim().toLowerCase();
		tableModel.setRowCount(0);
		ArrayList<Clase> clases = GestionDatos.getInstancia().getClases();
		for (Clase c : clases) {
			if (String.valueOf(c.getClaseID()).toLowerCase().contains(texto)) {
				String entrenador = (c.getEntrenador() != null) ? c.getEntrenador().getNombre() : "Sin asignar";
				tableModel.addRow(new Object[] { c.getClaseID(), c.getHorario(), entrenador, c.getClientes().size() });
			}
		}
		if (tableModel.getRowCount() == 0) {
			detallesArea.setText("No se encontraron clases con ese numero.");
		} else {
			detallesArea.setText("Clases encontradas: " + tableModel.getRowCount());
		}
	}

	/**
	 * MUESTRA LOS DETALLES DE LA CLASE SELECCIONADA EN LA TABLA EN EL AREA DE DETALLES.
	 */
	private void mostrarDetalles() {
		int row = table.getSelectedRow();
		if (row == -1) {
			detallesArea.setText("");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Clase c = buscarClasePorId(id);
		if (c != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("ID: ").append(c.getClaseID());
			sb.append("\nHorario: ").append(c.getHorario());
			sb.append("\nEntrenador: ")
				.append(c.getEntrenador() != null ? c.getEntrenador().getNombre() : "Sin asignar");
			sb.append("\n#Miembros: ").append(c.getClientes().size());
			if (!c.getClientes().isEmpty()) {
				sb.append("\nMiembros:");
				for (Cliente cli : c.getClientes()) {
					sb.append("\n - ").append(cli.getNombre()).append(" (ID: ").append(cli.getIdCliente()).append(")");
				}
			}
			detallesArea.setText(sb.toString());
		}
	}

	/**
	 * MUESTRA EL FORMULARIO PARA AGREGAR UNA NUEVA CLASE.
	 * SI YA SE ESTA EDITANDO/AGREGANDO, NO HACE NADA.
	 */
	private void mostrarFormularioAgregar() {
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

	/**
	 * MUESTRA EL FORMULARIO PARA EDITAR LA CLASE SELECCIONADA.
	 * SI NO HAY SELECCION O YA SE ESTA EDITANDO/AGREGANDO, MUESTRA UN MENSAJE O NO HACE NADA.
	 */
	private void mostrarFormularioEditar() {
		int row = table.getSelectedRow();
		if (row == -1 || editando) {
			if (!editando)
				JOptionPane.showMessageDialog(this, "Selecciona una clase para editar.");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		Clase c = buscarClasePorId(id);
		if (c == null)
			return;
		editando = true;
		if (panelFormulario != null)
			panelCentral.remove(panelFormulario);
		panelFormulario = crearPanelFormulario(c);
		panelCentral.add(panelFormulario, BorderLayout.NORTH);
		panelCentral.revalidate();
		panelCentral.repaint();
	}

	/**
	 * CREA Y RETORNA UN PANEL DE FORMULARIO PARA AGREGAR O EDITAR UNA CLASE.
	 * @param clase CLASE A EDITAR, O NULL SI ES PARA AGREGAR
	 * @return JPANEL CON LOS CAMPOS Y BOTONES CORRESPONDIENTES
	 */
	private JPanel crearPanelFormulario(Clase clase) {
		JPanel panel = new JPanel(new java.awt.GridLayout(0, 2, 5, 5));
		panel.setBorder(javax.swing.BorderFactory.createTitledBorder(clase == null ? "Agregar Clase" : "Editar Clase"));
		JTextField idField = new JTextField(clase == null ? "" : String.valueOf(clase.getClaseID()));
		idField.setEnabled(clase == null);
		JTextField horarioField = new JTextField(clase == null ? "" : clase.getHorario());
		JComboBox<String> entrenadorCombo = new JComboBox<>();
		ArrayList<entidades.Entrenador> entrenadores = GestionDatos.getInstancia().getEntrenadores();
		entrenadorCombo.addItem("Sin asignar");
		for (entidades.Entrenador e : entrenadores) {
			// Solo mostrar entrenadores no asignados o el actual
			if (clase == null || clase.getEntrenador() == null || e == clase.getEntrenador() || (e.getClase() == null)) {
				entrenadorCombo.addItem(e.getNombre() + " (" + e.getNumEmpleado() + ")");
			}
		}
		if (clase != null && clase.getEntrenador() != null) {
			String nombre = clase.getEntrenador().getNombre() + " (" + clase.getEntrenador().getNumEmpleado() + ")";
			entrenadorCombo.setSelectedItem(nombre);
		}
		panel.add(new JLabel("ID:"));
		panel.add(idField);
		panel.add(new JLabel("Horario:"));
		panel.add(horarioField);
		panel.add(new JLabel("Entrenador:"));
		panel.add(entrenadorCombo);
		JButton btnGuardar = new JButton("Guardar");
		JButton btnCancelar = new JButton("Cancelar");
		panel.add(btnGuardar);
		panel.add(btnCancelar);
		// ACCION AL PRESIONAR GUARDAR
		btnGuardar.addActionListener(e -> {
			try {
				int id = Integer.parseInt(idField.getText());
				String horario = horarioField.getText();
				entidades.Entrenador entrenador = null;
				if (entrenadorCombo.getSelectedIndex() > 0) {
					String selected = (String) entrenadorCombo.getSelectedItem();
					int idx1 = selected.lastIndexOf('(');
					int idx2 = selected.lastIndexOf(')');
					if (idx1 != -1 && idx2 != -1) {
						int numEmpleado = Integer.parseInt(selected.substring(idx1 + 1, idx2));
						for (entidades.Entrenador e2 : entrenadores) {
							if (e2.getNumEmpleado() == numEmpleado) {
								entrenador = e2;
								break;
							}
						}
					}
				}
				if (clase == null) {
					// VALIDACION PARA EVITAR IDS DUPLICADOS
					for (Clase c : GestionDatos.getInstancia().getClases()) {
						if (c.getClaseID() == id) {
							JOptionPane.showMessageDialog(this, "Ya existe una clase con ese ID.");
							return;
						}
					}
					Clase nueva = new Clase(id);
					nueva.setHorario(horario);
					if (entrenador != null) {
						try {
							GestionDatos.getInstancia().agregarEntrenador(entrenador);
							nueva.setEntrenador(entrenador);
							entrenador.setClase(nueva);
						} catch (IllegalStateException ex) {
							JOptionPane.showMessageDialog(this, ex.getMessage());
							return;
						}
					}
					GestionDatos.getInstancia().agregarClase(nueva);
					JOptionPane.showMessageDialog(this, "Clase agregada exitosamente.");
				} else {
					// EDICION DE CLASE EXISTENTE
					clase.setHorario(horario);
					if (entrenador != null) {
						if (clase.getEntrenador() != null && clase.getEntrenador() != entrenador) {
							JOptionPane.showMessageDialog(this, "Ya existe un entrenador asignado a esta clase.");
							return;
						}
						clase.setEntrenador(entrenador);
						entrenador.setClase(clase);
					} else {
						clase.setEntrenador(null);
					}
					GestionDatos.getInstancia().guardarDatos();
					JOptionPane.showMessageDialog(this, "Clase editada exitosamente.");
				}
				ocultarFormulario();
				//cargarClases(); // Ya se actualiza por listener
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Datos invalidos: " + ex.getMessage());
			}
		});
		// ACCION AL PRESIONAR CANCELAR
		btnCancelar.addActionListener(e -> ocultarFormulario());
		return panel;
	}

	/**
	 * OCULTA EL FORMULARIO DE AGREGAR/EDITAR CLASE Y ACTUALIZA LA INTERFAZ.
	 */
	private void ocultarFormulario() {
		if (panelFormulario != null) {
			panelCentral.remove(panelFormulario);
			panelFormulario = null;
			editando = false;
			panelCentral.revalidate();
			panelCentral.repaint();
		}
	}

	/**
	 * ELIMINA LA CLASE SELECCIONADA DE LA TABLA Y DE LA BASE DE DATOS CON
	 * PREVIA CONFIRMACION DEL USUARIO.
	 */
	private void eliminarClase() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Selecciona una clase para eliminar.");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar esta clase?", "Confirmar",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			ArrayList<Clase> clases = GestionDatos.getInstancia().getClases();
			clases.removeIf(c -> c.getClaseID() == id);
			try {
				GestionDatos.getInstancia().guardarDatos();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
			}
			cargarClases();
			detallesArea.setText("");
		}
	}

	/**
	 * BUSCA Y RETORNA UNA CLASE POR SU ID.
	 * @param id ID DE LA CLASE A BUSCAR
	 * @return CLASE ENCONTRADA O NULL SI NO EXISTE
	 */
	private Clase buscarClasePorId(int id) {
		ArrayList<Clase> clases = GestionDatos.getInstancia().getClases();
		for (Clase c : clases) {
			if (c.getClaseID() == id)
				return c;
		}
		return null;
	}

}
