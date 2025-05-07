package entidades;

public class Persona {
	// ATRIBUTOS
	private String nombre;
	private String fechaNacimiento;

	//CONSTRUCTORES
	public Persona() {
		
	}
	
	public Persona(String nombre, String fechaNacimiento) {
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	// GET
	public String getNombre() {
		return nombre;
	}
	
	public String getfechaNacimiento() {
		return fechaNacimiento;
	}
	
	
	//SETTER
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

}