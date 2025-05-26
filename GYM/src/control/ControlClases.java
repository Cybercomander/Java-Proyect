package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import entidades.Cliente;
import entidades.Clase;
import entidades.Entrenador;
import database.GestionDatos;

// CLASE "CONTROLCLASES" QUE GESTIONA LAS OPERACIONES RELACIONADAS CON LAS CLASES DEL GIMNASIO.
// UTILIZA LA CLASE "GestionDatos" PARA ACCEDER Y MODIFICAR LOS DATOS PERSISTENTES.
public class ControlClases
{
	// Todas las operaciones usan GestionDatos
	private static GestionDatos datos = GestionDatos.getInstancia();
	
	// MÉTODO "inicializa":
	// INICIALIZA LOS DATOS DEL SISTEMA DESDE UN ARCHIVO O GENERA DATOS DE PRUEBA SI NO EXISTEN.
	public static void inicializa()
	{
		datos.inicializarDatos();
	}
	
	// MÉTODO "addClase":
	// AGREGA UNA NUEVA CLASE AL SISTEMA SI NO EXISTE UNA CON EL MISMO NÚMERO.
	// GUARDA LOS CAMBIOS EN LOS DATOS PERSISTENTES.
	public static void addClase(Clase c)
	{
		ArrayList<Clase> clases = datos.getClases();
		boolean existeClase = clases
			.stream()
			.anyMatch(clase -> clase.getNumClase() == c.getNumClase());
		if (existeClase)
		{
			JOptionPane.showMessageDialog(null, "Ya existe una clase con ese número");
			return;
		}
		clases.add(c);
		guardar();
	}

	// MÉTODO "buscaCliente":
	// BUSCA Y RETORNA UN CLIENTE POR SU ID. SI NO SE ENCUENTRA, RETORNA NULL.
	public static Cliente buscaCliente(int idCliente)
	{
		return datos.getClientes()
			.stream()
			.filter(cliente -> cliente != null && cliente.getIdCliente() == idCliente)
			.findFirst()
			.orElse(null);
	}

	// MÉTODO "buscaEntrenador":
	// BUSCA Y RETORNA UN ENTRENADOR POR SU NÚMERO DE EMPLEADO. SI NO SE ENCUENTRA, RETORNA NULL.
	public static Entrenador buscaEntrenador(int numEmpleado)
	{
		return datos.getEntrenadores()
			.stream()
			.filter(entrenador -> entrenador != null && entrenador.getNumEmpleado() == numEmpleado)
			.findFirst()
			.orElse(null);
	}

	// MÉTODO "buscaClase":
	// BUSCA Y RETORNA UNA CLASE POR SU NÚMERO. SI NO SE ENCUENTRA, RETORNA NULL.
	public static Clase buscaClase(int numClase)
	{
		return datos.getClases()
			.stream()
			.filter(clase -> clase.getNumClase() == numClase)
			.findFirst()
			.orElse(null);
	}

	// MÉTODO "eliminarClase":
	// ELIMINA UNA CLASE DEL SISTEMA POR SU NÚMERO Y GUARDA LOS CAMBIOS EN LOS DATOS PERSISTENTES.
	// RETORNA TRUE SI LA CLASE FUE ELIMINADA, FALSE EN CASO CONTRARIO.
	public static boolean eliminarClase(int numClase)
	{
		ArrayList<Clase> clases = datos.getClases();
		boolean removed = clases.removeIf(clase -> clase.getNumClase() == numClase);
		if (removed)
			guardar();
		return removed;
	}

	// MÉTODO "tablaPlanesMembresia":
	// GENERA UNA TABLA DE FRECUENCIA DE CLIENTES POR PLAN DE MEMBRESÍA Y LA MUESTRA EN UN DIÁLOGO.
	public static void tablaPlanesMembresia()
	{
		ArrayList<Clase> clases = datos.getClases();
		StringBuilder mensaje = new StringBuilder("Frecuencia de Clientes por Plan de Membresía:\n\n");
		
		if (clases == null || clases.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "No hay clases registradas");
			return;
		}
		
		Map<String, Integer> frecuenciaPlanes = new HashMap<>();
		
		clases
			.stream()
			.flatMap(clase -> clase.getClientes().stream())
			.forEach(cliente ->
			{
				String plan = cliente.getPlanMembresia();
				frecuenciaPlanes.put(plan, frecuenciaPlanes.getOrDefault(plan, 0) + 1);
			});
		
		frecuenciaPlanes
			.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(entry ->
			{
				String plan = entry.getKey();
				int cantidad = entry.getValue();
				mensaje.append(String.format("Plan: %s || %d cliente(s)\n", plan, cantidad));
			});
			
		JOptionPane.showMessageDialog(null, mensaje.toString());
	}

	// MÉTODO "guardar":
	// GUARDA LOS DATOS DEL SISTEMA EN UN ARCHIVO PARA MANTENER LA PERSISTENCIA.
	public static void guardar()
	{
		try
		{
			datos.guardarDatos();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error al guardar datos: " + e.getMessage());
		}
	}

	// MÉTODO "getClases":
	// RETORNA LA LISTA DE CLASES REGISTRADAS EN EL SISTEMA.
	public static ArrayList<Clase> getClases()
	{
		return datos.getClases();
	}

	// MÉTODO "getCantidad":
	// RETORNA LA CANTIDAD TOTAL DE CLASES REGISTRADAS.
	public static int getCantidad()
	{
		return datos.getClases().size();
	}
}