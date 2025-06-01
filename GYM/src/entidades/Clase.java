package entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

/**
 * CLASE QUE REPRESENTA UNA CLASE DEL GIMNASIO
 * CONTIENE INFORMACION SOBRE CLIENTES, ENTRENADOR, HORARIO Y FUNCIONES DE GESTION
 */
public class Clase implements Serializable {
	/** IDENTIFICADOR UNICO DE LA CLASE */
	@Serial
	private static final long serialVersionUID = 1L;

	/** ID DE LA CLASE */
	private int claseID;  // ANTES NUMCLASE
	/** LISTA DE CLIENTES ASIGNADOS A LA CLASE */
	private ArrayList<Cliente> clientes;
	/** ENTRENADOR ASIGNADO A LA CLASE */
	private Entrenador entrenador;
	/** MAPA PARA ORDENAR IMC DE CLIENTES (NO USADO DIRECTAMENTE EN METODOS) */
	private Map<Float, Integer> imcOrd;
	/** HORARIO DE LA CLASE */
	private String horario; // NUEVO ATRIBUTO

	/**
	 * CONSTRUCTOR POR DEFECTO QUE INICIALIZA LA CLASE CON ID 0
	 */
	public Clase() {
		this(0);
	}

	/**
	 * CONSTRUCTOR QUE INICIALIZA LA CLASE CON UN ID ESPECIFICO
	 * @param claseID ID DE LA CLASE
	 */
	public Clase(int claseID) {
		this.claseID = claseID;
		this.clientes = new ArrayList<>();
		this.imcOrd = new HashMap<>();
		this.horario = "";
	}

	/**
	 * AGREGA UN CLIENTE A LA LISTA DE CLIENTES DE LA CLASE
	 * @param c CLIENTE A AGREGAR
	 */
	public void addCliente(Cliente c) {
		clientes.add(c);
	}

	/**
	 * ELIMINA UN CLIENTE DE LA CLASE POR SU ID
	 * @param idCliente ID DEL CLIENTE A ELIMINAR
	 * @return TRUE SI SE ELIMINO, FALSE SI NO SE ENCONTRO
	 */
	public boolean removeCliente(int idCliente) {
		return clientes.removeIf(cliente -> cliente.getId() == idCliente);
	}

	/**
	 * BUSCA CLIENTES POR NOMBRE (CONTENIDO, CASE-INSENSITIVE)
	 * @param nombre NOMBRE O PARTE DEL NOMBRE A BUSCAR
	 * @return LISTA DE CLIENTES QUE COINCIDEN
	 */
	public List<Cliente> buscarClientesPorNombre(String nombre) {
		return clientes.stream()
				.filter(cli -> cli.getNombre().toLowerCase()
						.contains(nombre.toLowerCase()))
				.collect(Collectors.toList());
	}

	/**
	 * DEVUELVE LOS CLIENTES ORDENADOS POR IMC DE MAYOR A MENOR
	 * @return LISTA DE CLIENTES ORDENADA POR IMC DESCENDENTE
	 */
	public List<Cliente> getClientesOrdenadosPorIMC() {
		return clientes.stream()
				.sorted(Comparator.comparing(Cliente::getIMC).reversed())
				.collect(Collectors.toList());
	}

	/**
	 * DEVUELVE LOS NOMBRES DE CLIENTES CUYO IMC ES MAYOR O IGUAL AL MINIMO DADO
	 * @param imcMinimo VALOR MINIMO DE IMC
	 * @return LISTA DE NOMBRES DE CLIENTES
	 */
	public List<String> getNombresClientesIMCSuperior(float imcMinimo) {
		return clientes.stream()
				.filter(cli -> cli.getIMC() >= imcMinimo)
				.map(Cliente::getNombre)
				.collect(Collectors.toList());
	}

	/**
	 * MUESTRA UNA TABLA DE FRECUENCIA DE IMC DE LOS CLIENTES EN UN DIALOGO
	 */
	public void tablaIMC() {
		if (clientes.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay clientes en la clase");
			return;
		}
		Map<Float, Long> frecuenciaIMC = clientes.stream()
				.collect(Collectors.groupingBy(
						Cliente::getIMC,
						Collectors.counting()
				));

		StringBuilder mensaje = new StringBuilder("Frecuencia de Clientes por IMC:\n\n");
		frecuenciaIMC.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.forEach(entry ->
					mensaje.append(String.format("IMC %.1f || %d cliente(s)\n",
							entry.getKey(), entry.getValue()))
				);
		JOptionPane.showMessageDialog(null, mensaje.toString());
	}

	/**
	 * OBTIENE EL ID DE LA CLASE
	 * @return ID DE LA CLASE
	 */
	public int getClaseID() {
		return claseID;
	}

	/**
	 * ESTABLECE EL ID DE LA CLASE
	 * @param claseID NUEVO ID DE LA CLASE
	 */
	public void setClaseID(int claseID) {
		this.claseID = claseID;
	}

	/**
	 * OBTIENE LA CANTIDAD DE CLIENTES EN LA CLASE
	 * @return NUMERO DE CLIENTES
	 */
	public int getCantidad() {
		return clientes.size();
	}

	/**
	 * OBTIENE EL ENTRENADOR ASIGNADO A LA CLASE
	 * @return ENTRENADOR DE LA CLASE
	 */
	public Entrenador getEntrenador() {
		return entrenador;
	}

	/**
	 * ASIGNA UN ENTRENADOR A LA CLASE
	 * @param entrenador ENTRENADOR A ASIGNAR
	 */
	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	/**
	 * OBTIENE LA LISTA DE CLIENTES DE LA CLASE
	 * @return LISTA DE CLIENTES
	 */
	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	/**
	 * OBTIENE EL HORARIO DE LA CLASE
	 * @return HORARIO DE LA CLASE
	 */
	public String getHorario() {
		return horario;
	}

	/**
	 * ESTABLECE EL HORARIO DE LA CLASE
	 * @param horario NUEVO HORARIO
	 */
	public void setHorario(String horario) {
		this.horario = horario;
	}
}