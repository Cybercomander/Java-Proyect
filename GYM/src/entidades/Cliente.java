package entidades;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * CLASE QUE REPRESENTA A UN CLIENTE DEL GIMNASIO
 * HEREDA DE LA CLASE PERSONA Y CONTIENE INFORMACION ADICIONAL COMO PLAN DE MEMBRESIA, IMC Y MEDIDAS
 */
public class Cliente extends Persona implements Serializable {
	/** IDENTIFICADOR DE VERSION PARA SERIALIZACION */
	private static final long serialVersionUID = 1L;

	/** ID UNICO DEL CLIENTE */
	private int idCliente;
	/** PLAN DE MEMBRESIA DEL CLIENTE */
	private String planMembresia;
	/** INDICE DE MASA CORPORAL DEL CLIENTE */
	private float imc;
	/** LISTA DE MEDIDAS: [PESO, ALTURA, EDAD] */
	private ArrayList<Float> medidas;  // [peso, altura, edad]
	/** CLASE ASIGNADA AL CLIENTE */
	private Clase suClase;             // Clase asignada

	/**
	 * CONSTRUCTOR POR DEFECTO QUE INICIALIZA LAS MEDIDAS
	 */
	public Cliente() {
		super();
		this.medidas = new ArrayList<>();
	}

	/**
	 * CONSTRUCTOR QUE INICIALIZA EL CLIENTE CON NOMBRE, ID, PLAN Y FECHA DE NACIMIENTO
	 * @param nombre NOMBRE DEL CLIENTE
	 * @param idCliente ID UNICO DEL CLIENTE
	 * @param planMembresia PLAN DE MEMBRESIA
	 * @param fechaNacimiento FECHA DE NACIMIENTO
	 */
	public Cliente(String nombre, int idCliente, String planMembresia, String fechaNacimiento) {
		super(nombre, fechaNacimiento);
		this.idCliente = idCliente;
		this.planMembresia = planMembresia;
		this.medidas = new ArrayList<>();
	}

	/**
	 * AGREGA PESO, ALTURA Y EDAD, Y RECALCULA IMC
	 * @param peso PESO DEL CLIENTE
	 * @param altura ALTURA DEL CLIENTE
	 * @param edad EDAD DEL CLIENTE
	 */
	public void asignaMedidas(float peso, float altura, float edad) {
		medidas.clear();
		medidas.add(peso);
		medidas.add(altura);
		medidas.add(edad);
		calculaIMC();
	}

	/**
	 * CALCULA EL IMC = PESO / (ALTURA^2)
	 */
	public void calculaIMC() {
		if (medidas.size() < 2) {
			imc = 0;
		} else {
			float peso = medidas.get(0);
			float altura = medidas.get(1);
			if (altura > 0) {
				imc = peso / (altura * altura);
			} else {
				imc = 0;
			}
		}
	}

	/**
	 * DEVUELVE EL IMC DEL CLIENTE
	 * @return IMC
	 */
	public float getIMC() {
		return imc;
	}

	// --- GETTERS Y SETTERS PRINCIPALES ---

	/**
	 * OBTIENE EL ID DEL CLIENTE
	 * @return ID DEL CLIENTE
	 */
	public int getId() {
		return idCliente;
	}

	/**
	 * ESTABLECE EL ID DEL CLIENTE
	 * @param id NUEVO ID
	 */
	public void setId(int id) {
		this.idCliente = id;
	}

	/**
	 * OBTIENE EL TIPO DE PLAN DE MEMBRESIA
	 * @return PLAN DE MEMBRESIA
	 */
	public String getTipo() {
		return planMembresia;
	}

	/**
	 * ESTABLECE EL TIPO DE PLAN DE MEMBRESIA
	 * @param tipo NUEVO PLAN DE MEMBRESIA
	 */
	public void setTipo(String tipo) {
		this.planMembresia = tipo;
	}

	/**
	 * OBTIENE LA FECHA DE NACIMIENTO DEL CLIENTE
	 * @return FECHA DE NACIMIENTO
	 */
	@Override
	public String getFechaNacimiento() {
		return super.getFechaNacimiento();
	}

	/**
	 * ESTABLECE LA FECHA DE NACIMIENTO DEL CLIENTE
	 * @param fechaNacimiento FECHA DE NACIMIENTO
	 */
	@Override
	public void setFechaNacimiento(String fechaNacimiento) {
		super.setFechaNacimiento(fechaNacimiento);
	}

	/**
	 * OBTIENE LA CLASE ASIGNADA AL CLIENTE
	 * @return CLASE ASIGNADA
	 */
	public Clase getClase() {
		return suClase;
	}

	/**
	 * ASIGNA UNA CLASE AL CLIENTE
	 * @param suClase CLASE A ASIGNAR
	 */
	public void setClase(Clase suClase) {
		this.suClase = suClase;
	}

	// --- MEDIDAS INDIVIDUALES ---

	/**
	 * OBTIENE EL PESO DEL CLIENTE
	 * @return PESO
	 */
	public float getPeso() {
		return medidas.size() > 0 ? medidas.get(0) : 0f;
	}

	/**
	 * ESTABLECE EL PESO DEL CLIENTE Y RECALCULA IMC
	 * @param peso NUEVO PESO
	 */
	public void setPeso(float peso) {
		if (medidas.size() > 0) {
			medidas.set(0, peso);
		} else {
			// RELLENAR HASTA EL INDICE 0
			while (medidas.size() < 1) medidas.add(0f);
			medidas.set(0, peso);
		}
		calculaIMC();
	}

	/**
	 * OBTIENE LA ALTURA DEL CLIENTE
	 * @return ALTURA
	 */
	public float getAltura() {
		return medidas.size() > 1 ? medidas.get(1) : 0f;
	}

	/**
	 * ESTABLECE LA ALTURA DEL CLIENTE Y RECALCULA IMC
	 * @param altura NUEVA ALTURA
	 */
	public void setAltura(float altura) {
		if (medidas.size() > 1) {
			medidas.set(1, altura);
		} else {
			while (medidas.size() < 1) medidas.add(0f);
			medidas.add(altura);
		}
		calculaIMC();
	}

	/**
	 * OBTIENE LA EDAD DEL CLIENTE
	 * @return EDAD
	 */
	public int getEdad() {
		return medidas.size() > 2 ? Math.round(medidas.get(2)) : 0;
	}

	/**
	 * ESTABLECE LA EDAD DEL CLIENTE
	 * @param edad NUEVA EDAD
	 */
	public void setEdad(int edad) {
		if (medidas.size() > 2) {
			medidas.set(2, (float) edad);
		} else {
			while (medidas.size() < 2) medidas.add(0f);
			medidas.add((float) edad);
		}
	}

	/**
	 * DEVUELVE TODAS LAS MEDIDAS EN ORDEN [PESO, ALTURA, EDAD]
	 * @return LISTA DE MEDIDAS
	 */
	public ArrayList<Float> getMedidas() {
		return medidas;
	}

	/**
	 * OBTIENE EL ID DEL CLIENTE (COMPATIBILIDAD)
	 * @return ID DEL CLIENTE
	 */
	public int getIdCliente() {
		return idCliente;
	}

	/**
	 * OBTIENE EL PLAN DE MEMBRESIA DEL CLIENTE
	 * @return PLAN DE MEMBRESIA
	 */
	public String getPlanMembresia() {
		return planMembresia;
	}
}
