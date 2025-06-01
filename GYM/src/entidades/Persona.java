package entidades;

import java.io.Serializable;

/**
 * CLASE QUE REPRESENTA UNA PERSONA GENERICA DEL SISTEMA
 * IMPLEMENTA SERIALIZABLE PARA PERMITIR SU SERIALIZACION
 */
public class Persona implements Serializable
{
	// ATRIBUTOS PRINCIPALES DE UNA PERSONA
	private String nombre;
	private String fechaNacimiento;

	/**
	 * CONSTRUCTOR POR DEFECTO
	 * INICIALIZA UNA PERSONA SIN NOMBRE NI FECHA DE NACIMIENTO
	 */
	public Persona() {}
	
	/**
	 * CONSTRUCTOR QUE INICIALIZA UNA PERSONA CON NOMBRE Y FECHA DE NACIMIENTO
	 * @param nombre NOMBRE DE LA PERSONA
	 * @param fechaNacimiento FECHA DE NACIMIENTO DE LA PERSONA
	 */
	public Persona(String nombre, String fechaNacimiento)
	{
		// ASIGNA EL NOMBRE Y LA FECHA DE NACIMIENTO A LOS ATRIBUTOS
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
	}
	
	/**
	 * OBTIENE EL NOMBRE DE LA PERSONA
	 * @return NOMBRE DE LA PERSONA
	 */
	public String getNombre() {
		// RETORNA EL NOMBRE ALMACENADO
		return nombre;
	}
	
	/**
	 * OBTIENE LA FECHA DE NACIMIENTO DE LA PERSONA
	 * @return FECHA DE NACIMIENTO
	 */
	public String getFechaNacimiento() {
		// RETORNA LA FECHA DE NACIMIENTO ALMACENADA
		return fechaNacimiento;
	}
	
	/**
	 * ESTABLECE EL NOMBRE DE LA PERSONA
	 * @param nombre NUEVO NOMBRE
	 */
	public void setNombre(String nombre) {
		// ASIGNA EL NUEVO NOMBRE AL ATRIBUTO
		this.nombre = nombre;
	}
	
	/**
	 * ESTABLECE LA FECHA DE NACIMIENTO DE LA PERSONA
	 * @param fechaNacimiento NUEVA FECHA DE NACIMIENTO
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		// ASIGNA LA NUEVA FECHA DE NACIMIENTO AL ATRIBUTO
		this.fechaNacimiento = fechaNacimiento;
	}
}