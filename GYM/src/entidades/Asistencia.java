package entidades;

import java.io.Serializable;
import java.time.LocalDate;

// CLASE "ASISTENCIA" QUE REPRESENTA EL REGISTRO DE ASISTENCIA DE UN CLIENTE EN UNA FECHA ESPECÍFICA.
// IMPLEMENTA LA INTERFAZ "Serializable" PARA PERMITIR SU SERIALIZACIÓN.
public class Asistencia implements Serializable {
    private int idCliente;
    private LocalDate fecha;

    // MÉTODO CONSTRUCTOR "Asistencia":
    // INICIALIZA UNA NUEVA INSTANCIA DE ASISTENCIA CON EL ID DEL CLIENTE Y LA FECHA.
    public Asistencia(int idCliente, LocalDate fecha) {
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

// CLASE "PagoMembresia":
// REPRESENTA EL REGISTRO DE UN PAGO DE MEMBRESÍA, INCLUYENDO FECHA DE PAGO, VENCIMIENTO Y MONTO.
class PagoMembresia implements Serializable {
    private int idCliente;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;
    private float monto;

    // MÉTODO CONSTRUCTOR "PagoMembresia":
    // INICIALIZA UNA NUEVA INSTANCIA DE PAGO DE MEMBRESÍA CON LOS DATOS PROPORCIONADOS.
    public PagoMembresia(int idCliente, LocalDate fechaPago, LocalDate fechaVencimiento, float monto) {
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
