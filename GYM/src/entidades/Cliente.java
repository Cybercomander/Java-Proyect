package entidades;

import java.util.ArrayList;

// CLASE "CLIENTE" QUE REPRESENTA A UN MIEMBRO DEL GIMNASIO.
// HEREDA DE LA CLASE "Persona" Y CONTIENE INFORMACIÓN ADICIONAL COMO PLAN DE MEMBRESÍA, IMC Y MEDIDAS.

public class Cliente extends Persona {
	// ATRIBUTOS
	private int idCliente;
	private String planMembresia;	// Bien
	private float imc;

	// CAMBIAMOS LA DECLARACION DE MEDIDAS DE ARRELO A LISTA
	private ArrayList<Float> medidas;

	private Clase suClase; // ATRIBUTO DE RELACION CON LA CLASE

	// CONSTRUCTOR POR DEFAULT
	// INICIALIZA UN CLIENTE CON VALORES POR DEFECTO Y UNA LISTA VACÍA DE MEDIDAS.
	public Cliente() {
		super();
		medidas = new ArrayList<>(); // CREACION DEL OBJETO ARRAYLIST
	}

	// CONSTRUCTOR ADICIONAL
	// INICIALIZA UN CLIENTE CON LOS DATOS PROPORCIONADOS COMO NOMBRE, ID, PLAN DE MEMBRESÍA Y FECHA DE NACIMIENTO.
	public Cliente(String nombre, int idCliente, String planMembresía, String fechaNacimiento) {
		super(nombre, fechaNacimiento);
		this.planMembresía = planMembresía;
		this.idCliente = idCliente;
		medidas = new ArrayList<>(); // CREACION DEL OBJETO ARRAYLIST
	}

	// METODOS
	// MÉTODO "asignaMedidas":
	// AGREGA PESO, ALTURA Y GRASA CORPORAL A LA LISTA DE MEDIDAS DEL CLIENTE Y CALCULA SU IMC.
	public void asignaMedidas(float peso, float altura, float grasa) {
		medidas.add(peso);
		medidas.add(altura);
		medidas.add(grasa);

		calculaIMC();
	}

	// MÉTODO "calculaIMC":
	// CALCULA EL ÍNDICE DE MASA CORPORAL (IMC) DEL CLIENTE BASADO EN SU PESO Y ALTURA.
	public void calculaIMC() {
		if (medidas.isEmpty() || medidas.size() < 2) {
			imc = 0;
			return;
		}

		// CALCULAR IMC (PESO / ALTURA^2)
		float peso = medidas.get(0);
		float altura = medidas.get(1);
		imc = peso / (altura * altura);
	}

	// GETTERS
	// MÉTODOS GETTERS:
	// PERMITEN OBTENER EL ID DEL CLIENTE, SU PLAN DE MEMBRESÍA, IMC, CLASE ASIGNADA Y MEDIDAS.
	public int getIdCliente() {
		return idCliente;
	}

	public String getPlanMembresía() {
		return planMembresía;
	}

	public float getIMC() {
		return imc;
	}

	public Clase getClase() {
		return suClase;
	}

	public ArrayList<Float> getMedidas() {
		return medidas;
	}

	// SETTERS
	// MÉTODOS SETTERS:
	// PERMITEN MODIFICAR EL ID DEL CLIENTE, SU PLAN DE MEMBRESÍA Y LA CLASE ASIGNADA.
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	@Override
	public void setFechaNacimiento(String fechaNacimiento) {
		super.setFechaNacimiento(fechaNacimiento);
	}

	public void setPlanMembresía(String planMembresía) {
		this.planMembresía = planMembresía;
	}

	public void setClase(Clase suClase) {
		this.suClase = suClase;
	}

	// GETTERS Y SETTERS ADICIONALES PARA ACCESO ALEATORIO
	public int getId() { return idCliente; }
	public void setId(int id) { this.idCliente = id; }
	public String getTipo() { return planMembresía; }
	public void setTipo(String tipo) { this.planMembresía = tipo; }
	public String getFechaNacimiento() { return getfechaNacimiento(); }
	public int getPeso() { return (medidas.size() > 0) ? Math.round(medidas.get(0)) : 0; }
	public void setPeso(int peso) { if (medidas.size() > 0) medidas.set(0, (float)peso); else medidas.add((float)peso); calculaIMC(); }
	public float getAltura() { return (medidas.size() > 1) ? medidas.get(1) : 0; }
	public void setAltura(float altura) { if (medidas.size() > 1) medidas.set(1, altura); else if (medidas.size() == 1) medidas.add(altura); else { medidas.add(0f); medidas.add(altura); } calculaIMC(); }
	public int getEdad() { return (medidas.size() > 2) ? Math.round(medidas.get(2)) : 0; }
	public void setEdad(int edad) { if (medidas.size() > 2) medidas.set(2, (float)edad); else { while (medidas.size() < 2) medidas.add(0f); medidas.add((float)edad); } }
	public int getExperiencia() { return 0; } // No aplica a Cliente
	public void setExperiencia(int exp) { /* No aplica a Cliente */ }
}
