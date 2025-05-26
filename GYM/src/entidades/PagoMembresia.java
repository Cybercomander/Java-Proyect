package entidades;

import java.io.Serializable;
import java.time.LocalDate;

// CLASE "PagoMembresia":
// REPRESENTA EL REGISTRO DE UN PAGO DE MEMBRESÍA, INCLUYENDO FECHA DE PAGO, VENCIMIENTO Y MONTO.
public class PagoMembresia implements Serializable
{
	private int idCliente;
	private LocalDate fechaPago;
	private LocalDate fechaVencimiento;
	private float monto;

	// MÉTODO CONSTRUCTOR "PagoMembresia":
	// INICIALIZA UNA NUEVA INSTANCIA DE PAGO DE MEMBRESÍA CON LOS DATOS PROPORCIONADOS.
	public PagoMembresia(int idCliente, LocalDate fechaPago, LocalDate fechaVencimiento, float monto)
	{
		this.idCliente = idCliente;
		this.fechaPago = fechaPago;
		this.fechaVencimiento = fechaVencimiento;
		this.monto = monto;
	}

	// MÉTODO "getIdCliente":
	// RETORNA EL ID DEL CLIENTE ASOCIADO A ESTE PAGO.
	public int getIdCliente() { return idCliente; }

	// MÉTODO "getFechaPago":
	// RETORNA LA FECHA EN QUE SE REALIZÓ EL PAGO.
	public LocalDate getFechaPago() { return fechaPago; }

	// MÉTODO "getFechaVencimiento":
	// RETORNA LA FECHA DE VENCIMIENTO DE LA MEMBRESÍA.
	public LocalDate getFechaVencimiento() { return fechaVencimiento; }

	// MÉTODO "getMonto":
	// RETORNA EL MONTO PAGADO POR LA MEMBRESÍA.
	public float getMonto() { return monto; }
}