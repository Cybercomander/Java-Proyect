package database;

import entidades.Clase;
import entidades.Cliente;
import entidades.Entrenador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CLASE QUE GESTIONA LOS DATOS PRINCIPALES DEL SISTEMA DE GIMNASIO
 * INCLUYE FUNCIONALIDAD PARA SERIALIZAR Y NOTIFICAR CAMBIOS A LOS OBSERVADORES
 */
public class GestionDatos implements Serializable {
	/** IDENTIFICADOR DE VERSION PARA LA SERIALIZACION */
	@Serial
	private static final long serialVersionUID = 1L;
	/** NOMBRE DEL ARCHIVO DONDE SE GUARDAN LOS DATOS SERIALIZADOS */
	private static final String ARCHIVO_DATOS = "Datos.dat";

	/** LISTA DE CLASES DEL GIMNASIO */
	private ArrayList<Clase> clases;
	/** INSTANCIA UNICA DE GESTIONDATOS (PATRON SINGLETON) */
	private static GestionDatos instancia;

	/**
	 * LISTA DE OBSERVADORES PARA NOTIFICAR CAMBIOS EN LOS DATOS
	 * ES TRANSIENTE PARA NO SERIALIZAR LOS OBSERVADORES
	 */
	private transient List<Runnable> listeners = new CopyOnWriteArrayList<>();

	/**
	 * CONSTRUCTOR PRIVADO PARA IMPEDIR LA CREACION DE MULTIPLES INSTANCIAS
	 * INICIALIZA LA LISTA DE CLASES
	 */
	private GestionDatos() {
		clases = new ArrayList<>();
	}

	/**
	 * OBTIENE LA INSTANCIA UNICA DE GESTIONDATOS (SINGLETON)
	 * @return INSTANCIA DE GESTIONDATOS
	 */
	public static GestionDatos getInstancia() {
		if (instancia == null) {
			instancia = new GestionDatos();
		}
		return instancia;
	}

	/**
	 * AGREGA UN OBSERVADOR PARA SER NOTIFICADO CUANDO HAYA CAMBIOS EN LOS DATOS
	 * @param listener OBJETO QUE IMPLEMENTA RUNNABLE PARA SER NOTIFICADO
	 */
	public void addListener(Runnable listener) {
		if (listeners == null) listeners = new CopyOnWriteArrayList<>();
		listeners.add(listener);
	}

	/**
	 * ELIMINA UN OBSERVADOR DE LA LISTA DE NOTIFICACIONES
	 * @param listener OBSERVADOR A ELIMINAR
	 */
	public void removeListener(Runnable listener) {
		if (listeners != null) listeners.remove(listener);
	}

	/**
	 * NOTIFICA A TODOS LOS OBSERVADORES REGISTRADOS SOBRE UN CAMBIO EN LOS DATOS
	 */
	private void notifyListeners() {
		if (listeners != null) {
			for (Runnable r : listeners) {
				try { r.run(); } catch (Exception ignored) {}
			}
		}
	}

	/**
	 * INICIALIZA LOS DATOS DEL SISTEMA:
	 * SI EXISTE EL ARCHIVO DE DATOS, LOS CARGA;
	 * SI NO, GENERA DATOS DE PRUEBA Y LOS GUARDA
	 */
	@SuppressWarnings("unchecked")
	public void inicializarDatos()
	{
		File archivo = new File(ARCHIVO_DATOS);
		if (archivo.exists() && archivo.length() > 0)
		{
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo)))
			{
				clases = (ArrayList<Clase>) ois.readObject();
				System.out.println("Datos cargados correctamente.");
			}
			catch (Exception e)
			{
				System.err.println("Error al leer datos: " + e.getMessage());
				generarDatosPrueba();
				try
				{
					guardarDatos();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		else
		{
			generarDatosPrueba();
			try
			{
				guardarDatos();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * GUARDA LA LISTA DE CLASES EN EL ARCHIVO DE DATOS USANDO SERIALIZACION
	 * @throws IOException SI OCURRE UN ERROR AL ESCRIBIR EL ARCHIVO
	 */
	public void guardarDatos() throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS)))
		{
			oos.writeObject(clases);
			System.out.println("Datos guardados correctamente.");
		}
		notifyListeners(); // NOTIFICAR A LOS PANELES
	}

	/**
	 * GENERA UNA POBLACION INICIAL DE DATOS DE PRUEBA PARA EL SISTEMA
	 */
	public void generarDatosPrueba()
	{
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

		Cliente cliente;
		cliente = new Cliente("Carlos", 20001, "Premium", "15-03-1995");
		cliente.asignaMedidas(80, 1.75f, 20);
		cliente.setClase(c1);
		c1.addCliente(cliente);

		cliente = new Cliente("Pepe", 20002, "Basico", "23-08-1990");
		cliente.asignaMedidas(75, 1.68f, 18);
		cliente.setClase(c1);
		c1.addCliente(cliente);

		cliente = new Cliente("Kevin", 20003, "Premium", "07-11-1988");
		cliente.asignaMedidas(90, 1.80f, 22);
		cliente.setClase(c2);
		c2.addCliente(cliente);

		cliente = new Cliente("Kenay", 20004, "Premium Plus", "30-05-1992");
		cliente.asignaMedidas(85, 1.78f, 19);
		cliente.setClase(c2);
		c2.addCliente(cliente);

		cliente = new Cliente("Manuel", 20005, "Basico", "12-02-1985");
		cliente.asignaMedidas(95, 1.85f, 25);
		cliente.setClase(c3);
		c3.addCliente(cliente);

		cliente = new Cliente("Roberto", 20006, "Premium", "28-09-1987");
		cliente.asignaMedidas(70, 1.70f, 16);
		cliente.setClase(c3);
		c3.addCliente(cliente);

		cliente = new Cliente("Max", 20007, "Premium Plus", "04-07-1995");
		cliente.asignaMedidas(78, 1.73f, 17);
		cliente.setClase(c3);
		c3.addCliente(cliente);

		cliente = new Cliente("Alex", 20008, "Basico", "19-01-1998");
		cliente.asignaMedidas(82, 1.76f, 19);
		cliente.setClase(c4);
		c4.addCliente(cliente);

		cliente = new Cliente("Yax", 20009, "Premium", "25-06-1982");
		cliente.asignaMedidas(88, 1.82f, 21);
		cliente.setClase(c4);
		c4.addCliente(cliente);

		cliente = new Cliente("Angel", 20010, "Premium Plus", "11-04-1993");
		cliente.asignaMedidas(72, 1.65f, 18);
		cliente.setClase(c4);
		c4.addCliente(cliente);
	}

	/**
	 * DEVUELVE LA LISTA DE TODAS LAS CLASES DEL GIMNASIO
	 * @return LISTA DE CLASES
	 */
	public ArrayList<Clase> getClases() {
		return clases;
	}

	/**
	 * DEVUELVE LA LISTA DE TODOS LOS CLIENTES DEL GIMNASIO
	 * @return LISTA DE CLIENTES
	 */
	public ArrayList<Cliente> getClientes() {
		ArrayList<Cliente> clientes = new ArrayList<>();
		for (Clase c : clases) {
			clientes.addAll(c.getClientes());
		}
		return clientes;
	}

	/**
	 * DEVUELVE LA LISTA DE TODOS LOS ENTRENADORES DEL GIMNASIO
	 * @return LISTA DE ENTRENADORES
	 */
	public ArrayList<Entrenador> getEntrenadores() {
		ArrayList<Entrenador> entrenadores = new ArrayList<>();
		for (Clase c : clases) {
			if (c.getEntrenador() != null) {
				entrenadores.add(c.getEntrenador());
			}
		}
		return entrenadores;
	}

	/**
	 * AGREGA UNA NUEVA CLASE AL SISTEMA Y GUARDA LOS DATOS INMEDIATAMENTE
	 * @param clase CLASE A AGREGAR
	 * @throws IOException SI OCURRE UN ERROR AL GUARDAR LOS DATOS
	 */
	public void agregarClase(Clase clase) throws IOException {
		clases.add(clase);
		guardarDatos();
	}

	/**
	 * AGREGA UN ENTRENADOR A SU CLASE CORRESPONDIENTE Y GUARDA LOS DATOS
	 * @param entrenador ENTRENADOR A AGREGAR
	 * @throws IOException SI OCURRE UN ERROR AL GUARDAR LOS DATOS
	 */
	public void agregarEntrenador(Entrenador entrenador) throws IOException {
		for (Clase c : clases) {
			if (c.getClaseID() == entrenador.getClase().getClaseID()) {
				if (c.getEntrenador() != null) {
					throw new IllegalStateException("Ya existe un entrenador asignado a esta clase.");
				}
				c.setEntrenador(entrenador);
				entrenador.setClase(c);
				break;
			}
		}
		guardarDatos();
	}

	/**
	 * AGREGA UN CLIENTE A SU CLASE CORRESPONDIENTE Y GUARDA LOS DATOS
	 * @param cliente CLIENTE A AGREGAR
	 * @throws IOException SI OCURRE UN ERROR AL GUARDAR LOS DATOS
	 */
	public void agregarCliente(Cliente cliente) throws IOException {
		for (Clase c : clases) {
			if (c.getClaseID() == cliente.getClase().getClaseID()) {
				c.addCliente(cliente);
				cliente.setClase(c);
				break;
			}
		}
		guardarDatos();
	}
}