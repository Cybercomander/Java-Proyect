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

public class Clase implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private int claseID;  // antes numClase
	private ArrayList<Cliente> clientes;
	private Entrenador entrenador;
	private Map<Float, Integer> imcOrd;

	public Clase() {
		this(0);
	}

	public Clase(int claseID) {
		this.claseID = claseID;
		this.clientes = new ArrayList<>();
		this.imcOrd = new HashMap<>();
	}

	// Añade un cliente a la clase
	public void addCliente(Cliente c) {
		clientes.add(c);
	}

	// Elimina un cliente por su ID
	public boolean removeCliente(int idCliente) {
		return clientes.removeIf(cliente -> cliente.getId() == idCliente);
	}

	// Busca clientes por nombre (contiene, case-insensitive)
	public List<Cliente> buscarClientesPorNombre(String nombre) {
		return clientes.stream()
				.filter(cli -> cli.getNombre().toLowerCase()
						.contains(nombre.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Devuelve los clientes ordenados por IMC de mayor a menor
	public List<Cliente> getClientesOrdenadosPorIMC() {
		return clientes.stream()
				.sorted(Comparator.comparing(Cliente::getIMC).reversed())
				.collect(Collectors.toList());
	}

	// Devuelve los nombres de clientes cuyo IMC ≥ imcMinimo
	public List<String> getNombresClientesIMCSuperior(float imcMinimo) {
		return clientes.stream()
				.filter(cli -> cli.getIMC() >= imcMinimo)
				.map(Cliente::getNombre)
				.collect(Collectors.toList());
	}

	// Muestra una tabla de frecuencia de IMC por diálogo
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

	// Getters y setters
	public int getClaseID() {
		return claseID;
	}

	public void setClaseID(int claseID) {
		this.claseID = claseID;
	}

	public int getCantidad() {
		return clientes.size();
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}
}