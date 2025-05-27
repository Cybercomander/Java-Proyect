package entidades;

import java.io.Serializable;
import java.time.LocalDate;

// CLASE "ASISTENCIA" QUE REPRESENTA EL REGISTRO DE ASISTENCIA DE UN CLIENTE EN UNA FECHA ESPECÍFICA.
// IMPLEMENTA LA INTERFAZ "Serializable" PARA PERMITIR SU SERIALIZACIÓN.
public class Asistencia implements Serializable
{
	private int idCliente;
	private LocalDate fecha;

	// MÉTODO CONSTRUCTOR "Asistencia":
	// INICIALIZA UNA NUEVA INSTANCIA DE ASISTENCIA CON EL ID DEL CLIENTE Y LA FECHA.
	public Asistencia(int idCliente, LocalDate fecha)
	{
		this.idCliente = idCliente;
		this.fecha = fecha;
	}

	// MÉTODO "getIdCliente":
	// RETORNA EL ID DEL CLIENTE ASOCIADO A ESTA ASISTENCIA.
	public int getIdCliente() { return idCliente; }

	// MÉTODO "getFecha":
	// RETORNA LA FECHA EN QUE SE REGISTRÓ LA ASISTENCIA.
	public LocalDate getFecha() { return fecha; }
}