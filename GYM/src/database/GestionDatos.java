package database;

import entidades.Clase;
import entidades.Cliente;
import entidades.Entrenador;

import java.io.*;
import java.util.ArrayList;

public class GestionDatos implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private static final String ARCHIVO_DATOS = "Datos.dat";

	private ArrayList<Clase> clases;
	private static GestionDatos instancia;

	private GestionDatos() {
		clases = new ArrayList<>();
	}

	public static GestionDatos getInstancia() {
		if (instancia == null) {
			instancia = new GestionDatos();
		}
		return instancia;
	}

	/**
	 * Inicializa: si existe el fichero con datos, los carga;
	 * si no, genera datos de prueba y los guarda.
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

	/** Serializa el ArrayList<Clase> en el fichero Datos.dat */
	public void guardarDatos() throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS)))
		{
			oos.writeObject(clases);
			System.out.println("Datos guardados correctamente.");
		}
	}

	/** Población inicial de prueba (idéntica a tu versión original) */
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

		cliente = new Cliente("Pepe", 20002, "Básico", "23-08-1990");
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

		cliente = new Cliente("Manuel", 20005, "Básico", "12-02-1985");
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

		cliente = new Cliente("Alex", 20008, "Básico", "19-01-1998");
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

	/** Devuelve la lista de todas las clases */
	public ArrayList<Clase> getClases() {
		return clases;
	}

	/** Devuelve la lista de todos los clientes */
	public ArrayList<Cliente> getClientes() {
		ArrayList<Cliente> clientes = new ArrayList<>();
		for (Clase c : clases) {
			clientes.addAll(c.getClientes());
		}
		return clientes;
	}

	/** Devuelve la lista de todos los entrenadores */
	public ArrayList<Entrenador> getEntrenadores() {
		ArrayList<Entrenador> entrenadores = new ArrayList<>();
		for (Clase c : clases) {
			if (c.getEntrenador() != null) {
				entrenadores.add(c.getEntrenador());
			}
		}
		return entrenadores;
	}

	/** Agrega una nueva clase y persiste inmediatamente */
	public void agregarClase(Clase clase) throws IOException {
		clases.add(clase);
		guardarDatos();
	}

	/** Agrega un entrenador a su clase y persiste */
	public void agregarEntrenador(Entrenador entrenador) throws IOException {
		for (Clase c : clases) {
			if (c.getClaseID() == entrenador.getClase().getClaseID()) {
				c.setEntrenador(entrenador);
				entrenador.setClase(c);
				break;
			}
		}
		guardarDatos();
	}

	/** Agrega un cliente a su clase y persiste */
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