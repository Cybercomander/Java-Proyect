package vista;

import control.ControlClases;
import entidades.*;
import java.util.List;
import javax.swing.JOptionPane;

@Deprecated	// This class is deprecated and should not be used in new code.

public class Interfaz {
	public static void main(String[] args) {
		// INICIALIZAR CONTROL DE CLASES
		ControlClases.inicializa();

		Interfaz i = new Interfaz();
		i.inicio();
	}

	// SECUENCIA DEL PROGRAMA
	private void inicio() {
		datosPrueba();
		menu();
	}
	
	private void datosPrueba() {
		Clase c1 = new Clase(101);
		Clase c2 = new Clase(102);
		Clase c3 = new Clase(103);
		Clase c4 = new Clase(104);
		ControlClases.addClase(c1);
		ControlClases.addClase(c2);
		ControlClases.addClase(c3);
		ControlClases.addClase(c4);
		
		Entrenador e1, e2, e3, e4;
		
		e1 = new Entrenador("Luis", "15 Agosto 1985", 1000, 60, 4);
		e1.setClase(c1);
		c1.setEntrenador(e1);

		
		e2 = new Entrenador("Guillermo", "21 Marzo 1990", 1001, 65, 5);
		e2.setClase(c2);
		c2.setEntrenador(e2);

		e3 = new Entrenador("Alejandro", "03 Agosto 1988", 1002, 70, 6);
		e3.setClase(c3);
		c3.setEntrenador(e3);
		
		e4 = new Entrenador("Mario", "18 Enero 1992", 1003, 75, 4);
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

	// METODOS
	private void menu() {
		int opc = 0;
		while (opc != 10) {
			opc = Integer.parseInt(JOptionPane.showInputDialog("-- Sistema de Gestión de Gimnasio -- \n"+ 
					"1) Agregar Cliente a Clase\n" + 
					"2) Agregar Entrenador \n" + 
					"3) Crear Clase \n"+ 
					"4) Buscar Clase \n" + 
					"5) Mostrar Cliente \n" + 
					"6) Mostrar Clases \n" + 
					"7) Mostrar Entrenador \n"+ 
					"8) Tabla de frecuencias de clientes por IMC \n"+
					"9) Tabla de frecuencias de clientes por plan \n"+
					"10) Finalizar programa \n" + 
					"Opcion: "));

			switch (opc) {
			case 1:
				asignarClienteClase();
				break;
			case 2:
				addEntrenador();
				break;
			case 3:
				crearClase();
				break;
			case 4:
				buscarClase();
				break;
			case 5:
				muestraClientes();
				break;
			case 6:
				muestraClases();
				break;
			case 7:
				muestraEntrenador();
				break;
			case 8:
				frecuenciaIMC();
				break;
			case 9:
				frecuenciaPlanes();
				break;
			case 10:
				JOptionPane.showMessageDialog(null, "Finalizando programa...");
			break;
				
			default:
				JOptionPane.showMessageDialog(null, "Opción inválida. Intente de nuevo.");
				break;
			}
		}
	}

	// INGRESAR CLIENTES
	private Cliente ingresaCliente() {
		// INGRESA CLIENTE
		String nombre = JOptionPane.showInputDialog("Ingresa el nombre: ");
		String fechaNacimiento = JOptionPane.showInputDialog("Ingresa la fecha de nacimiento (DD-MM-AAAA): ");
		String planMembresia = JOptionPane.showInputDialog("Ingresa el plan de membresía: ");
		int idCliente = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el ID del cliente: "));
		float peso = Float.parseFloat(JOptionPane.showInputDialog("Ingresa el peso (kg): "));
		float altura = Float.parseFloat(JOptionPane.showInputDialog("Ingresa la altura (m): "));
		float grasa = Float.parseFloat(JOptionPane.showInputDialog("Ingresa el porcentaje de grasa corporal: "));

		// CREAR OBJETO CLIENTE
		Cliente c = new Cliente(nombre, idCliente, planMembresia, fechaNacimiento);
		c.asignaMedidas(peso, altura, grasa);

		JOptionPane.showMessageDialog(null, "Cliente creado correctamente");
		return c;
	}

	// CREAR CLASE
	private void crearClase() {
		int numClase = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el número de clase: "));
		Clase c = new Clase(numClase);
		ControlClases.addClase(c);
		JOptionPane.showMessageDialog(null, "Clase " + numClase + " creada correctamente");
	}

	// ASIGNAR CLIENTE A CLASE
	private void asignarClienteClase() {
		int numClase = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el número de clase: "));
		Clase c = ControlClases.buscaClase(numClase);

		if (c == null) {
			JOptionPane.showMessageDialog(null, "La clase no existe");
			return;
		}

		// CREAR CLIENTE
		Cliente cliente = ingresaCliente();

		// ASIGNAR CLIENTE A LA CLASE
		c.addCliente(cliente);
		cliente.setClase(c);

		JOptionPane.showMessageDialog(null, "Cliente asignado a la clase " + numClase + " correctamente");
	}

	// AGREGAR ENTRENADOR A LA CLASE
	private void addEntrenador() {
		int numClase = Integer
				.parseInt(JOptionPane.showInputDialog("Ingresa el número de clase a la que asignar el entrenador: "));
		Clase c = ControlClases.buscaClase(numClase);

		if (c == null) {
			JOptionPane.showMessageDialog(null, "La clase no existe");
			return;
		}

		String nombre = JOptionPane.showInputDialog("Ingresa el nombre del entrenador: ");
		String fechaNacimiento = JOptionPane.showInputDialog("Ingresa la fecha de nacimiento (DD-MM-AAAA): ");
		int numEmpleado = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el número de empleado: "));
		float salarioHora = Float.parseFloat(JOptionPane.showInputDialog("Ingresa el salario por hora: "));
		int horas = Integer.parseInt(JOptionPane.showInputDialog("Ingresa las horas de trabajo: "));

		Entrenador e = new Entrenador(nombre, fechaNacimiento, numEmpleado, salarioHora, horas);
		c.setEntrenador(e);
		e.setClase(c);

		JOptionPane.showMessageDialog(null, "Entrenador asignado a la clase " + numClase + " correctamente");
	}

	// METODO PARA BUSCAR CLASES
	private Clase buscarClase() {
		int numClase = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el número de clase a buscar: "));
		Clase c = ControlClases.buscaClase(numClase);

		if (c == null) {
			JOptionPane.showMessageDialog(null, "La clase no existe");
			return c;
		}

		String mensaje = "Información de la Clase " + c.getNumClase() + "\n";
		mensaje += "Cantidad de clientes: " + c.getCantidad() + "\n";

		if (c.getEntrenador() != null) {
			mensaje += "Entrenador: " + c.getEntrenador().getNombre() + "\n";
		} else {
			mensaje += "No tiene entrenador asignado\n";
		}

		JOptionPane.showMessageDialog(null, mensaje);
		return c;
	}
	
	private void frecuenciaIMC() {
		Clase c = buscarClase();
		if (c != null) {
			c.tablaIMC();
		}
	}
	
	private void frecuenciaPlanes() {
		ControlClases.tablaPlanesMembresia();
	}

	// GETS
	private void muestraClientes() {
		int idCliente = Integer.parseInt(JOptionPane.showInputDialog("Inserte el ID del cliente a buscar: "));

		Cliente clienteEncontrado = ControlClases.buscaCliente(idCliente);

		if (clienteEncontrado != null) {
			JOptionPane.showMessageDialog(null,
					"Nombre: " + clienteEncontrado.getNombre() + "\n" + "ID Cliente: " + clienteEncontrado.getIdCliente()
							+ "\n" + "Fecha de nacimiento: " + clienteEncontrado.getfechaNacimiento() + "\n"
							+ "Plan de Membresía: " + clienteEncontrado.getPlanMembresia() + "\n" + "IMC: "
							+ clienteEncontrado.getIMC() + "\n" + "Clase: "
							+ (clienteEncontrado.getClase() != null ? clienteEncontrado.getClase().getNumClase()
									: "No asignado"));
		} else {
			JOptionPane.showMessageDialog(null, "No se encontró cliente con ese ID");
		}
	}

	private void muestraEntrenador() {
        int numEmpleado = Integer.parseInt(JOptionPane.showInputDialog("Inserte el número de empleado del entrenador: "));

        Entrenador entrenadorEncontrado = ControlClases.buscaEntrenador(numEmpleado);

        if (entrenadorEncontrado != null) {
            JOptionPane.showMessageDialog(null,
                "Nombre: " + entrenadorEncontrado.getNombre() + "\n" +
                "Número de empleado: " + entrenadorEncontrado.getNumEmpleado() + "\n" +
				"Fecha de nacimiento: " + entrenadorEncontrado.getfechaNacimiento() + "\n" +
                "Salario por hora: " + entrenadorEncontrado.getSalarioHora() + "\n" +
                "Horas trabajadas: " + entrenadorEncontrado.gethoras() + "\n" +
                "Clase asignada: " + (entrenadorEncontrado.getClase() != null ? 
                    entrenadorEncontrado.getClase().getNumClase() : "No asignado"));
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró entrenador con ese número de empleado");
        }
    }

    private void muestraClases() {
        List<Clase> clases = ControlClases.getClases();
        if (clases.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clases registradas");
            return;
        }

        StringBuilder mensaje = new StringBuilder("Lista de Clases:\n\n");
        for (Clase c : clases) {
            mensaje.append("Clase ").append(c.getNumClase()).append("\n");
            mensaje.append("Cantidad de clientes: ").append(c.getCantidad()).append("\n");
            if (c.getEntrenador() != null) {
                mensaje.append("Entrenador: ").append(c.getEntrenador().getNombre()).append("\n");
            } else {
                mensaje.append("Sin entrenador asignado\n");
            }
            mensaje.append("-------------------------\n");
        }
        
        JOptionPane.showMessageDialog(null, mensaje.toString());
    }
}