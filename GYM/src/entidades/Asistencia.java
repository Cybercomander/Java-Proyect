package entidades;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * CLASE QUE REPRESENTA EL REGISTRO DE ASISTENCIA DE UN CLIENTE EN UNA FECHA ESPECIFICA
 * IMPLEMENTA LA INTERFAZ SERIALIZABLE PARA PERMITIR SU SERIALIZACION
 */
public class Asistencia implements Serializable
{
	/** ID DEL CLIENTE QUE REALIZA LA ASISTENCIA */
	private int idCliente;
	/** FECHA EN QUE SE REGISTRA LA ASISTENCIA */
	private LocalDate fecha;

	/**
	 * CONSTRUCTOR DE LA CLASE ASISTENCIA
	 * INICIALIZA UNA NUEVA INSTANCIA DE ASISTENCIA CON EL ID DEL CLIENTE Y LA FECHA
	 * @param idCliente ID DEL CLIENTE
	 * @param fecha FECHA DE LA ASISTENCIA
	 */
	public Asistencia(int idCliente, LocalDate fecha)
	{
		this.idCliente = idCliente;
		this.fecha = fecha;
	}

	/**
	 * OBTIENE EL ID DEL CLIENTE ASOCIADO A ESTA ASISTENCIA
	 * @return ID DEL CLIENTE
	 */
	public int getIdCliente() { return idCliente; }

	/**
	 * OBTIENE LA FECHA EN QUE SE REGISTRO LA ASISTENCIA
	 * @return FECHA DE LA ASISTENCIA
	 */
	public LocalDate getFecha() { return fecha; }
}