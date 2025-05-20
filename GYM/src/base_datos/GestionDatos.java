package base_datos;

import entidades.*;
import java.io.*;
import java.util.ArrayList;

public class GestionDatos {
    private static final String ARCHIVO_DATOS = "Datos.dat";
    private static final int MAX_CLIENTES = 1000;
    private static final int MAX_ENTRENADORES = 20;
    private static final int MAX_CLASES = 10;
    // Tamaños fijos para los campos (en bytes)
    private static final int TAM_NOMBRE = 30;
    private static final int TAM_TIPO = 15;
    private static final int TAM_FECHA = 15;
    // Tamaño de un registro Cliente
    private static final int TAM_CLIENTE = TAM_NOMBRE + 4 + TAM_TIPO + TAM_FECHA + 4 + 4 + 4 + 4;
    // Tamaño de un registro Entrenador
    private static final int TAM_ENTRENADOR = TAM_NOMBRE + TAM_FECHA + 4 + 4 + 4 + 4;
    // Tamaño de un registro Clase (solo id y referencia a entrenador)
    private static final int TAM_CLASE = 4 + 4;

    private ArrayList<Clase> clases;
    private static GestionDatos instancia;

    public static GestionDatos getInstancia() {
        if (instancia == null) {
            instancia = new GestionDatos();
        }
        return instancia;
    }

    public GestionDatos() {
        clases = new ArrayList<>();
    }

    public void inicializarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists() && archivo.length() > 0) {
            try {
                cargarDatos();
            } catch (Exception e) {
                generarDatosPrueba();
                try { guardarDatos(); } catch (IOException ex) { ex.printStackTrace(); }
            }
        } else {
            generarDatosPrueba();
            try { guardarDatos(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void generarDatosPrueba() {
        clases.clear();
        Clase c1 = new Clase(101);
        Clase c2 = new Clase(102);
        Clase c3 = new Clase(103);
        Clase c4 = new Clase(104);
        clases.add(c1);
        clases.add(c2);
        clases.add(c3);
        clases.add(c4);

        Entrenador e1 = new Entrenador("Luis", "15 Agosto 1985", 1000, 60, 4);
        e1.setClase(c1);
        c1.setEntrenador(e1);
        Entrenador e2 = new Entrenador("Guillermo", "21 Marzo 1990", 1001, 65, 5);
        e2.setClase(c2);
        c2.setEntrenador(e2);
        Entrenador e3 = new Entrenador("Alejandro", "03 Agosto 1988", 1002, 70, 6);
        e3.setClase(c3);
        c3.setEntrenador(e3);
        Entrenador e4 = new Entrenador("Mario", "18 Enero 1992", 1003, 75, 4);
        e4.setClase(c4);
        c4.setEntrenador(e4);

        Cliente c;
        c = new Cliente("Carlos", 20001, "Premium", "15-03-1995");
        c.asignaMedidas(80, 1.75f, 20);
        c.setClase(c1);
        c1.addCliente(c);

        c = new Cliente("Pepe", 20002, "Básico", "23-08-1990");
        c.asignaMedidas(75, 1.68f, 18);
        c.setClase(c1);
        c1.addCliente(c);

        c = new Cliente("Kevin", 20003, "Premium", "07-11-1988");
        c.asignaMedidas(90, 1.80f, 22);
        c.setClase(c2);
        c2.addCliente(c);

        c = new Cliente("Kenay", 20004, "Premium Plus", "30-05-1992");
        c.asignaMedidas(85, 1.78f, 19);
        c.setClase(c2);
        c2.addCliente(c);

        c = new Cliente("Manuel", 20005, "Básico", "12-02-1985");
        c.asignaMedidas(95, 1.85f, 25);
        c.setClase(c3);
        c3.addCliente(c);

        c = new Cliente("Roberto", 20006, "Premium", "28-09-1987");
        c.asignaMedidas(70, 1.70f, 16);
        c.setClase(c3);
        c3.addCliente(c);

        c = new Cliente("Max", 20007, "Premium Plus", "04-07-1995");
        c.asignaMedidas(78, 1.73f, 17);
        c.setClase(c3);
        c3.addCliente(c);

        c = new Cliente("Alex", 20008, "Básico", "19-01-1998");
        c.asignaMedidas(82, 1.76f, 19);
        c.setClase(c4);
        c4.addCliente(c);

        c = new Cliente("Yax", 20009, "Premium", "25-06-1982");
        c.asignaMedidas(88, 1.82f, 21);
        c.setClase(c4);
        c4.addCliente(c);

        c = new Cliente("Angel", 20010, "Premium Plus", "11-04-1993");
        c.asignaMedidas(72, 1.65f, 18);
        c.setClase(c4);
        c4.addCliente(c);
    }

    // Métodos de acceso aleatorio
    public void guardarDatos() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(ARCHIVO_DATOS, "rw")) {
            raf.setLength(0); // Limpiar archivo
            System.out.println("Guardando datos en el archivo...");
            // Guardar clases
            for (Clase clase : clases) {
                guardarClase(raf, clase);
                // Guardar entrenador de la clase
                if (clase.getEntrenador() != null) {
                    guardarEntrenador(raf, clase.getEntrenador());
                }
                // Guardar clientes de la clase
                for (Cliente cliente : clase.getClientes()) {
                    guardarCliente(raf, cliente);
                }
            }
            System.out.println("Datos guardados correctamente en el archivo.");
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
            throw e;
        }
    }

    public void cargarDatos() throws IOException {
        clases.clear();
        try (RandomAccessFile raf = new RandomAccessFile(ARCHIVO_DATOS, "r")) {
            long length = raf.length();
            raf.seek(0);
            // Leer clases, entrenadores y clientes
            while (raf.getFilePointer() < length) {
                int tipo = raf.readInt();
                if (tipo == 1) { // Clase
                    Clase clase = leerClase(raf);
                    clases.add(clase);
                } else if (tipo == 2) { // Entrenador
                    Entrenador e = leerEntrenador(raf);
                    // Asignar a la clase correspondiente
                    for (Clase c : clases) {
                        if (c.getId() == e.getClase().getId()) {
                            c.setEntrenador(e);
                            e.setClase(c);
                        }
                    }
                } else if (tipo == 3) { // Cliente
                    Cliente cli = leerCliente(raf);
                    // Asignar a la clase correspondiente
                    for (Clase c : clases) {
                        if (c.getId() == cli.getClase().getId()) {
                            c.addCliente(cli);
                            cli.setClase(c);
                        }
                    }
                } else {
                    // Saltar registro desconocido
                    raf.skipBytes(TAM_CLASE);
                }
            }
        }
    }

    // Métodos para guardar y leer registros
    private void guardarClase(RandomAccessFile raf, Clase clase) throws IOException {
        raf.writeInt(1); // Tipo 1 = Clase
        raf.writeInt(clase.getId());
        raf.writeInt(clase.getEntrenador() != null ? clase.getEntrenador().getId() : -1);
    }

    private Clase leerClase(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        int idEntrenador = raf.readInt();
        Clase c = new Clase(id);
        // El entrenador se asigna después
        return c;
    }

    private void guardarEntrenador(RandomAccessFile raf, Entrenador e) throws IOException {
        raf.writeInt(2); // Tipo 2 = Entrenador
        writeFixedString(raf, e.getNombre(), TAM_NOMBRE);
        writeFixedString(raf, e.getFechaNacimiento(), TAM_FECHA);
        raf.writeInt(e.getId());
        raf.writeInt(e.getPeso());
        raf.writeInt(e.getExperiencia());
        raf.writeInt(e.getClase() != null ? e.getClase().getId() : -1);
    }

    private Entrenador leerEntrenador(RandomAccessFile raf) throws IOException {
        String nombre = readFixedString(raf, TAM_NOMBRE);
        String fecha = readFixedString(raf, TAM_FECHA);
        int id = raf.readInt();
        int peso = raf.readInt();
        int exp = raf.readInt();
        int idClase = raf.readInt();
        Entrenador e = new Entrenador(nombre.trim(), fecha.trim(), id, peso, exp);
        e.setClase(new Clase(idClase));
        return e;
    }

    private void guardarCliente(RandomAccessFile raf, Cliente c) throws IOException {
        raf.writeInt(3); // Tipo 3 = Cliente
        writeFixedString(raf, c.getNombre(), TAM_NOMBRE);
        raf.writeInt(c.getId());
        writeFixedString(raf, c.getTipo(), TAM_TIPO);
        writeFixedString(raf, c.getFechaNacimiento(), TAM_FECHA);
        raf.writeInt(c.getPeso());
        raf.writeFloat(c.getAltura());
        raf.writeInt(c.getEdad());
        raf.writeInt(c.getClase() != null ? c.getClase().getId() : -1);
    }

    private Cliente leerCliente(RandomAccessFile raf) throws IOException {
        String nombre = readFixedString(raf, TAM_NOMBRE);
        int id = raf.readInt();
        String tipo = readFixedString(raf, TAM_TIPO);
        String fecha = readFixedString(raf, TAM_FECHA);
        int peso = raf.readInt();
        float altura = raf.readFloat();
        int edad = raf.readInt();
        int idClase = raf.readInt();
        Cliente c = new Cliente(nombre.trim(), id, tipo.trim(), fecha.trim());
        c.asignaMedidas(peso, altura, edad);
        c.setClase(new Clase(idClase));
        return c;
    }

    // Utilidades para strings fijos
    private void writeFixedString(RandomAccessFile raf, String s, int length) throws IOException {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(length);
        raf.writeChars(sb.toString());
    }

    private String readFixedString(RandomAccessFile raf, int length) throws IOException {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars);
    }

    // Métodos auxiliares para cadenas de longitud fija (como en UsuariosDB)
    private void escribirCadenaFija(RandomAccessFile raf, String s, int tam) throws IOException {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(tam);
        raf.writeChars(sb.toString());
    }

    private String leerCadenaFija(RandomAccessFile raf, int tam) throws IOException {
        char[] chars = new char[tam];
        for (int i = 0; i < tam; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars);
    }

    // Agregar una clase: agregar a la lista, guardar y recargar desde archivo
    public void agregarClase(Clase clase) throws IOException {
        clases.add(clase);
        guardarDatos();
        cargarDatos(); // Recarga la lista en memoria para reflejar el cambio
    }

    // Agregar un entrenador: buscar la clase, asignar, guardar y recargar
    public void agregarEntrenador(Entrenador e) throws IOException {
        for (Clase c : clases) {
            if (c.getId() == (e.getClase() != null ? e.getClase().getId() : -1)) {
                c.setEntrenador(e);
                e.setClase(c);
                break;
            }
        }
        guardarDatos();
        cargarDatos();
    }

    // Agregar un cliente: buscar la clase, agregar, guardar y recargar
    public void agregarCliente(Cliente c) throws IOException {
        for (Clase clase : clases) {
            if (clase.getId() == (c.getClase() != null ? c.getClase().getId() : -1)) {
                clase.addCliente(c);
                c.setClase(clase);
                break;
            }
        }
        guardarDatos();
        cargarDatos();
    }

    // Para editar/eliminar: leer todos los registros, modificar en memoria y reescribir el archivo completo (como en UsuariosDB)
    // Puedes crear metodos eliminarClase, eliminarEntrenador, eliminarCliente siguiendo el ejemplo de Usuarios.java

    // Getters para acceder a los datos
    public ArrayList<Clase> getClases() {
        return clases;
    }

    public ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        for (Clase c : clases) {
            if (c.getClientes() != null) {
                clientes.addAll(c.getClientes());
            }
        }
        return clientes;
    }

    public ArrayList<Entrenador> getEntrenadores() {
        ArrayList<Entrenador> entrenadores = new ArrayList<>();
        for (Clase c : clases) {
            if (c.getEntrenador() != null) {
                entrenadores.add(c.getEntrenador());
            }
        }
        return entrenadores;
    }
}
