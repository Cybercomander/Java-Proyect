package entidades;

import java.util.ArrayList;

import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class Clase
{
	// ATRIBUTOS
	private int numClase;
	
	// ATRIBUTO DE LA RELACION CON CLIENTE CAMBIADO DE ARREGLO A LISTA
	private ArrayList<Cliente> clientes;
	
	// ATRIBUTO DE RELACION CON ENTRENADOR
	private Entrenador entrenador;
	
	//ATRIBUTO PARA CLIENTES POR IMC
	Map<Float, Integer> imcOrd;

	
	// CONSTRUCTORES
	public Clase()
	{
		clientes = new ArrayList<>(); //INICIALIZAR LISTA
		imcOrd = new HashMap<Float,Integer>();
	}
	
	public Clase(int numClase)
	{
		clientes = new ArrayList<>(); // INICIALIZAR LISTA
		imcOrd = new HashMap<Float,Integer>();
		this.numClase = numClase;
	}

	// METODOS
	// METODO PARA AGREGAR CLIENTE A LA CLASE
	public void addCliente(Cliente c)
	{
		clientes.add(c); //USAR METODO PARA AGREGAR A LA LISTA
	}
	
	// METODO PARA ELIMINAR CLIENTE DE LA CLASE
	public boolean removeCliente(int idCliente)
	{
		boolean removed = clientes.removeIf(cliente -> cliente.getIdCliente() == idCliente);
		return removed;
	}
	
	// BUSCAR CLIENTE POR NOMBRE USANDO STREAMS Y FILTER
	public List<Cliente> buscarClientesPorNombre(String nombre)
	{
		return clientes
			.stream()
			.filter(cliente -> cliente.getNombre().toLowerCase().contains(nombre.toLowerCase()))
			.collect(Collectors.toList());
	}
	
	// ORDENAR CLIENTES POR IMC USANDO STREAMS Y SORTED
	public List<Cliente> getClientesOrdenadosPorIMC()
	{
		return clientes
			.stream()
			.sorted(Comparator.comparing(Cliente::getIMC).reversed())
			.collect(Collectors.toList());
	}
	
	// OBTENER CLIENTES CON IMC SUPERIOR USANDO STREAMS, FILTER Y MAP
	public List<String> getNombresClientesIMCSuperior(float imcMinimo)
	{
		return clientes
			.stream()
			.filter(cliente -> cliente.getIMC() >= imcMinimo)
			.map(Cliente::getNombre)
			.collect(Collectors.toList());
	}
	
	//METODO PARA GENERAR FRECUENCIA DE CLIENTES POR IMC
	public void tablaIMC()
	{
		// VALIDAR SI HAY CLIENTES EN LA CLASE
		if (clientes.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "No hay clientes en la clase");
			return;
		}

		// USAR STREAMS PARA VALIDAR SI HAY CLIENTES POR IMC EXACTO
		Map<Float, Long> frecuenciaIMC = clientes
			.stream()
			.collect(Collectors.groupingBy(
				Cliente::getIMC, 
				Collectors.counting()
			));

		// CONSTRUIR UN MENSAJE DE TEXTO FORMATEADO
		StringBuilder mensaje = new StringBuilder("Frecuencia de Clientes por IMC:\n\n");
		
		frecuenciaIMC.entrySet()
			.stream()
			.sorted(Map.Entry.<Float, Long>comparingByKey())
			.forEach(entry -> 
				mensaje.append(String.format("IMC %.1f || %d cliente(s)\n",
					entry.getKey(),
					entry.getValue()
					)
				)
			);

		JOptionPane.showMessageDialog(null, mensaje.toString());
	}
	
	// GET
	public int getCantidad() { return clientes.size(); }
	
	public int getNumClase() { return numClase; }
	
	public Entrenador getEntrenador() { return entrenador; }
	
	public ArrayList<Cliente> getClientes() { return clientes; }
	
	// GETTERS Y SETTERS ADICIONALES PARA ACCESO ALEATORIO
	public int getId() { return numClase; }
	
	public void setId(int id) { this.numClase = id; }

	// SETS
	public void setNumClase(int numClase) { this.numClase = numClase; }
	
	public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }
}