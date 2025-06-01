package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import entidades.Cliente;
import entidades.Clase;
import entidades.Entrenador;
import database.GestionDatos;

/**
 * CLASE CONTROLCLASES QUE GESTIONA LAS OPERACIONES RELACIONADAS CON LAS CLASES DEL GIMNASIO.
 * UTILIZA LA CLASE GESTIONDATOS PARA ACCEDER Y MODIFICAR LOS DATOS PERSISTENTES.
 */
public class ControlClases
{
	/**
	 * INSTANCIA ESTATICA DE GESTIONDATOS PARA ACCESO A DATOS PERSISTENTES.
	 */
	private static GestionDatos datos = GestionDatos.getInstancia();
	
	/**
	 * INICIALIZA LOS DATOS DEL SISTEMA DESDE UN ARCHIVO O GENERA DATOS DE PRUEBA SI NO EXISTEN.
	 */
	public static void inicializa()
	{
		datos.inicializarDatos();
	}
	
	/**
	 * AGREGA UNA NUEVA CLASE AL SISTEMA SI NO EXISTE UNA CON EL MISMO NUMERO DE ID.
	 * @param c OBJETO CLASE A AGREGAR
	 */
	public static void addClase(Clase c)
	{
		ArrayList<Clase> clases = datos.getClases();
		boolean existeClase = clases
			.stream()
			.anyMatch(clase -> clase.getClaseID() == c.getClaseID());
		if (existeClase)
		{
			JOptionPane.showMessageDialog(null, "Ya existe una clase con ese número");
			return;
		}
		clases.add(c);
		guardar();
	}

	/**
	 * BUSCA Y RETORNA UN CLIENTE POR SU ID. SI NO SE ENCUENTRA, RETORNA NULL.
	 * @param idCliente ID DEL CLIENTE A BUSCAR
	 * @return OBJETO CLIENTE SI SE ENCUENTRA, NULL EN CASO CONTRARIO
	 */
	public static Cliente buscaCliente(int idCliente)
	{
		return datos.getClientes()
			.stream()
			.filter(cliente -> cliente != null && cliente.getIdCliente() == idCliente)
			.findFirst()
			.orElse(null);
	}

	/**
	 * BUSCA Y RETORNA UN ENTRENADOR POR SU NUMERO DE EMPLEADO. SI NO SE ENCUENTRA, RETORNA NULL.
	 * @param numEmpleado NUMERO DE EMPLEADO DEL ENTRENADOR
	 * @return OBJETO ENTRENADOR SI SE ENCUENTRA, NULL EN CASO CONTRARIO
	 */
	public static Entrenador buscaEntrenador(int numEmpleado)
	{
		return datos.getEntrenadores()
			.stream()
			.filter(entrenador -> entrenador != null && entrenador.getNumEmpleado() == numEmpleado)
			.findFirst()
			.orElse(null);
	}

	/**
	 * BUSCA Y RETORNA UNA CLASE POR SU NUMERO DE ID. SI NO SE ENCUENTRA, RETORNA NULL.
	 * @param numClase NUMERO DE ID DE LA CLASE
	 * @return OBJETO CLASE SI SE ENCUENTRA, NULL EN CASO CONTRARIO
	 */
	public static Clase buscaClase(int numClase)
	{
		return datos.getClases()
			.stream()
			.filter(clase -> clase.getClaseID() == numClase)
			.findFirst()
			.orElse(null);
	}

	/**
	 * ELIMINA UNA CLASE DEL SISTEMA POR SU NUMERO DE ID Y GUARDA LOS CAMBIOS EN LOS DATOS PERSISTENTES.
	 * @param numClase NUMERO DE ID DE LA CLASE A ELIMINAR
	 * @return TRUE SI LA CLASE FUE ELIMINADA, FALSE EN CASO CONTRARIO
	 */
	public static boolean eliminarClase(int numClase)
	{
		ArrayList<Clase> clases = datos.getClases();
		boolean removed = clases.removeIf(clase -> clase.getClaseID() == numClase);
		if (removed)
			guardar();
		return removed;
	}

	/**
	 * GENERA UNA TABLA DE FRECUENCIA DE CLIENTES POR PLAN DE MEMBRESIA Y LA MUESTRA EN UN DIALOGO.
	 */
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

	/**
	 * GUARDA LOS DATOS DEL SISTEMA EN UN ARCHIVO PARA MANTENER LA PERSISTENCIA.
	 */
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

	/**
	 * RETORNA LA LISTA DE CLASES REGISTRADAS EN EL SISTEMA.
	 * @return LISTA DE OBJETOS CLASE
	 */
	public static ArrayList<Clase> getClases()
	{
		return datos.getClases();
	}

	/**
	 * RETORNA LA CANTIDAD TOTAL DE CLASES REGISTRADAS.
	 * @return NUMERO ENTERO DE CLASES
	 */
	public static int getCantidad()
	{
		return datos.getClases().size();
	}
}
