package entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

// CLASE "CLIENTE" QUE REPRESENTA A UN MIEMBRO DEL GIMNASIO.
// HEREDA DE LA CLASE "Persona" Y CONTIENE INFORMACIÓN ADICIONAL COMO PLAN DE MEMBRESÍA, IMC Y MEDIDAS.

public class Cliente extends Persona implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private int idCliente;
	private String planMembresia;
	private float imc;
	private ArrayList<Float> medidas;  // [peso, altura, edad]
	private Clase suClase;             // Clase asignada

	public Cliente() {
		super();
		this.medidas = new ArrayList<>();
	}

	public Cliente(String nombre, int idCliente, String planMembresia, String fechaNacimiento) {
		super(nombre, fechaNacimiento);
		this.idCliente = idCliente;
		this.planMembresia = planMembresia;
		this.medidas = new ArrayList<>();
	}

	/** Agrega peso, altura y edad, y recalcula IMC */
	public void asignaMedidas(float peso, float altura, float edad) {
		medidas.clear();
		medidas.add(peso);
		medidas.add(altura);
		medidas.add(edad);
		calculaIMC();
	}

	/** Calcula IMC = peso / (altura^2) */
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

	/** Devuelve el IMC */
	public float getIMC() {
		return imc;
	}

	// --- Getters y setters principales ---

	public int getId() {
		return idCliente;
	}

	public void setId(int id) {
		this.idCliente = id;
	}

	public String getTipo() {
		return planMembresia;
	}

	public void setTipo(String tipo) {
		this.planMembresia = tipo;
	}

	@Override
	public String getFechaNacimiento() {
		return super.getFechaNacimiento();
	}

	@Override
	public void setFechaNacimiento(String fechaNacimiento) {
		super.setFechaNacimiento(fechaNacimiento);
	}

	public Clase getClase() {
		return suClase;
	}

	public void setClase(Clase suClase) {
		this.suClase = suClase;
	}

	// --- Medidas individuales ---

	public float getPeso()
	{
		return !medidas.isEmpty() ? medidas.getFirst() : 0f;
	}

	public void setPeso(float peso)
	{
		if (!medidas.isEmpty())
		{
			medidas.set(0, peso);
		}
		else
		{
			// rellenar hasta el índice 0
			while (medidas.isEmpty())
				medidas.add(0f);

			medidas.set(0, peso);
		}
		calculaIMC();
	}

	public float getAltura()
	{
		return medidas.size() > 1 ? medidas.get(1) : 0f;
	}

	public void setAltura(float altura)
	{
		if (medidas.size() > 1)
		{
			medidas.set(1, altura);
		}
		else
		{
			while (medidas.isEmpty())
				medidas.add(0f);

			medidas.add(altura);
		}
		calculaIMC();
	}

	public int getEdad()
	{
		return medidas.size() > 2 ? Math.round(medidas.get(2)) : 0;
	}

	public void setEdad(int edad)
	{
		if (medidas.size() > 2)
		{
			medidas.set(2, (float) edad);
		}
		else
		{
			while (medidas.size() < 2)
				medidas.add(0f);

			medidas.add((float) edad);
		}
	}

	/** Devuelve todas las medidas en orden [peso, altura, edad] */
	public ArrayList<Float> getMedidas() {
		return medidas;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public String getPlanMembresia() {
		return planMembresia;
	}
}
