package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import base_datos.UsuariosDB;
import java.io.RandomAccessFile;

// CLASE "USUARIOS" QUE REPRESENTA LA INTERFAZ GRÁFICA PARA GESTIONAR LOS USUARIOS DEL SISTEMA.
// PERMITE AGREGAR, EDITAR, ELIMINAR Y BUSCAR USUARIOS.
public class Usuarios extends JPanel {
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
    public Usuarios() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(250, 250, 255));

        JLabel titulo = new JLabel("Usuarios del Sistema", SwingConstants.CENTER);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 22));
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
        tableModel = new DefaultTableModel(new Object[]{"Usuario", "Contraseña"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);
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

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
        detallesArea = new JTextArea();
        detallesArea.setEditable(false);
        detallesArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
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

        cargarUsuarios();
    }

    // MÉTODO "cargarUsuarios":
    // CARGA LOS USUARIOS DESDE EL ARCHIVO "Usuarios.dat" Y LOS MUESTRA EN LA TABLA.
    private void cargarUsuarios() {
        tableModel.setRowCount(0);
        usuarios = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile("Usuarios.dat", "r")) {
            int TAM_USUARIO = 20, TAM_CONTRA = 20, TAM_REGISTRO = TAM_USUARIO * 2 + TAM_CONTRA * 2;
            long registros = raf.length() / TAM_REGISTRO;
            for (int i = 0; i < registros; i++) {
                String u = leerCadenaFija(raf, TAM_USUARIO).trim();
                String c = leerCadenaFija(raf, TAM_CONTRA).trim();
                usuarios.add(new UsuarioSimple(u, c));
                tableModel.addRow(new Object[]{u, c});
            }
        } catch (Exception e) {
            // No hay usuarios
        }
    }

    // MÉTODO "buscarUsuarios":
    // FILTRA LOS USUARIOS POR NOMBRE SEGÚN EL TEXTO INGRESADO EN "searchField".
    private void buscarUsuarios() {
        String texto = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        for (UsuarioSimple u : usuarios) {
            if (u.usuario.toLowerCase().contains(texto)) {
                tableModel.addRow(new Object[]{u.usuario, u.contra});
            }
        }
    }

    // MÉTODO "mostrarDetalles":
    // MUESTRA LOS DETALLES DEL USUARIO SELECCIONADO EN LA TABLA EN "detallesArea".
    private void mostrarDetalles() {
        int row = table.getSelectedRow();
        if (row == -1) {
            detallesArea.setText("");
            return;
        }
        String usuario = (String) tableModel.getValueAt(row, 0);
        String contra = (String) tableModel.getValueAt(row, 1);
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: ").append(usuario);
        sb.append("\nContraseña: ").append(contra);
        detallesArea.setText(sb.toString());
    }

    // MÉTODO "mostrarFormularioAgregar":
    // MUESTRA EL FORMULARIO PARA AGREGAR UN NUEVO USUARIO.
    private void mostrarFormularioAgregar() {
        if (editando) return;
        editando = true;
        if (panelFormulario != null) panelCentral.remove(panelFormulario);
        panelFormulario = crearPanelFormulario(null);
        panelCentral.add(panelFormulario, BorderLayout.NORTH);
        panelCentral.revalidate(); panelCentral.repaint();
    }

    // MÉTODO "mostrarFormularioEditar":
    // MUESTRA EL FORMULARIO PARA EDITAR EL USUARIO SELECCIONADO EN LA TABLA.
    private void mostrarFormularioEditar() {
        int row = table.getSelectedRow();
        if (row == -1 || editando) {
            if (!editando)
                JOptionPane.showMessageDialog(this, "Selecciona un usuario para editar.");
            return;
        }
        String usuario = (String) tableModel.getValueAt(row, 0);
        UsuarioSimple u = buscarUsuarioPorNombre(usuario);
        if (u == null) return;
        editando = true;
        if (panelFormulario != null) panelCentral.remove(panelFormulario);
        panelFormulario = crearPanelFormulario(u);
        panelCentral.add(panelFormulario, BorderLayout.NORTH);
        panelCentral.revalidate(); panelCentral.repaint();
    }

    // MÉTODO "crearPanelFormulario":
    // CREA Y CONFIGURA EL FORMULARIO PARA AGREGAR O EDITAR USUARIOS.
    private JPanel crearPanelFormulario(UsuarioSimple usuario) {
        JPanel panel = new JPanel(new GridLayout(0,2,5,5));
        panel.setBorder(BorderFactory.createTitledBorder(usuario == null ? "Agregar Usuario" : "Editar Usuario"));
        JTextField usuarioField = new JTextField(usuario == null ? "" : usuario.usuario);
        usuarioField.setEnabled(usuario == null);
        JTextField contraField = new JTextField(usuario == null ? "" : usuario.contra);
        panel.add(new JLabel("Usuario:")); panel.add(usuarioField);
        panel.add(new JLabel("Contraseña:")); panel.add(contraField);
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panel.add(btnGuardar); panel.add(btnCancelar);
        btnGuardar.addActionListener(e -> {
            try {
                String usuarioTxt = usuarioField.getText().trim();
                String contraTxt = contraField.getText().trim();
                if (usuarioTxt.isEmpty() || contraTxt.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Usuario y contraseña requeridos.");
                    return;
                }
                if (usuario == null) {
                    if (buscarUsuarioPorNombre(usuarioTxt) != null) {
                        JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese nombre.");
                        return;
                    }
                    agregarUsuarioArchivo(usuarioTxt, contraTxt);
                } else {
                    eliminarUsuarioArchivo(usuario.usuario);
                    agregarUsuarioArchivo(usuarioTxt, contraTxt);
                }
                ocultarFormulario();
                cargarUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        btnCancelar.addActionListener(e -> ocultarFormulario());
        return panel;
    }

    // MÉTODO "ocultarFormulario":
    // OCULTA EL FORMULARIO DE AGREGAR O EDITAR USUARIOS Y RESTABLECE EL ESTADO DE EDICIÓN.
    private void ocultarFormulario() {
        if (panelFormulario != null) {
            panelCentral.remove(panelFormulario);
            panelFormulario = null;
            editando = false;
            panelCentral.revalidate(); panelCentral.repaint();
        }
    }

    // MÉTODO "eliminarUsuario":
    // ELIMINA EL USUARIO SELECCIONADO EN LA TABLA DESPUÉS DE CONFIRMAR LA ACCIÓN.
    private void eliminarUsuario() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
            return;
        }
        String usuario = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                eliminarUsuarioArchivo(usuario);
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente");
                cargarUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + ex.getMessage());
            }
        }
    }

    // MÉTODO "eliminarUsuarioArchivo":
    // ELIMINA UN USUARIO DEL ARCHIVO "Usuarios.dat" REESCRIBIENDO EL ARCHIVO SIN ESE USUARIO.
    private void eliminarUsuarioArchivo(String usuario) throws Exception {
        java.io.File archivo = new java.io.File("Usuarios.dat");
        java.io.File temp = new java.io.File("Usuarios_temp.dat");
        int TAM_USUARIO = 20, TAM_CONTRA = 20, TAM_REGISTRO = TAM_USUARIO * 2 + TAM_CONTRA * 2;
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r");
             RandomAccessFile rafTemp = new RandomAccessFile(temp, "rw")) {
            long registros = raf.length() / TAM_REGISTRO;
            for (int i = 0; i < registros; i++) {
                String u = leerCadenaFija(raf, TAM_USUARIO);
                String c = leerCadenaFija(raf, TAM_CONTRA);
                if (!u.trim().equals(usuario)) {
                    escribirCadenaFija(rafTemp, u, TAM_USUARIO);
                    escribirCadenaFija(rafTemp, c, TAM_CONTRA);
                }
            }
        }
        archivo.delete();
        temp.renameTo(archivo);
    }

    // SE AGREGÓ EL MÉTODO "agregarUsuarioArchivo" PARA ESCRIBIR NUEVOS USUARIOS EN EL ARCHIVO "Usuarios.dat".
    private void agregarUsuarioArchivo(String usuario, String contra) throws Exception {
        try (RandomAccessFile raf = new RandomAccessFile("Usuarios.dat", "rw")) {
            raf.seek(raf.length()); // MOVER AL FINAL DEL ARCHIVO
            escribirCadenaFija(raf, usuario, 20);
            escribirCadenaFija(raf, contra, 20);
        }
    }

    private String leerCadenaFija(RandomAccessFile raf, int tam) throws Exception {
        char[] chars = new char[tam];
        for (int i = 0; i < tam; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars);
    }

    private void escribirCadenaFija(RandomAccessFile raf, String s, int tam) throws Exception {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(tam);
        raf.writeChars(sb.toString());
    }

    // SE CORRIGIÓ LA CLASE INTERNA "UsuarioSimple" PARA INCLUIR LOS ATRIBUTOS NECESARIOS.
    private static class UsuarioSimple {
        String usuario;
        String contra;

        UsuarioSimple(String usuario, String contra) {
            this.usuario = usuario;
            this.contra = contra;
        }
    }

    // SE AGREGÓ EL MÉTODO "buscarUsuarioPorNombre" PARA BUSCAR UN USUARIO POR SU NOMBRE EN LA LISTA DE USUARIOS.
    private UsuarioSimple buscarUsuarioPorNombre(String nombreUsuario) {
        for (UsuarioSimple usuario : usuarios) {
            if (usuario.usuario.equals(nombreUsuario)) {
                return usuario;
            }
        }
        return null;
    }
}
