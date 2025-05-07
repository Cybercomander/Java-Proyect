package entidades;

import java.util.ArrayList;

public class Cliente extends Persona {
	// ATRIBUTOS
	private int idCliente;
	private String planMembresía;
	private float imc;

	// CAMBIAMOS LA DECLARACION DE MEDIDAS DE ARRELO A LISTA
	private ArrayList<Float> medidas;

	private Clase suClase; // ATRIBUTO DE RELACION CON LA CLASE

	// CONSTRUCTOR POR DEFAULT
	public Cliente() {
		super();
		medidas = new ArrayList<>(); // CREACION DEL OBJETO ARRAYLIST
	}

	// CONSTRUCTOR ADICIONAL
	public Cliente(String nombre, int idCliente, String planMembresía, String fechaNacimiento) {
		super(nombre, fechaNacimiento);
		this.planMembresía = planMembresía;
		this.idCliente = idCliente;
		medidas = new ArrayList<>(); // CREACION DEL OBJETO ARRAYLIST
	}

	// METODOS
	public void asignaMedidas(float peso, float altura, float grasa) {
		medidas.add(peso);
		medidas.add(altura);
		medidas.add(grasa);

		calculaIMC();
	}

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

	// GET
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

	// SET
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public void setPlanMembresía(String planMembresía) {
		this.planMembresía = planMembresía;
	}

	public void setClase(Clase suClase) {
		this.suClase = suClase;
	}
}