package entidades;

public class Entrenador extends Persona{
	//ATRIBUTOS
	private int numEmpleado;
	private float salarioDiario;
	private float salarioHora;
	private int horas;
	
	private Clase suClase;		//ATRIBUTO DE RELACION CON LA CLASE
	
	//CONSTRUCTORES
	public Entrenador() {
		super();
	}
	
	public Entrenador(String nombre, String fechaNacimiento, int numEmpleado, float salarioHora, int horas) {
		super(nombre, fechaNacimiento);
		this.numEmpleado = numEmpleado;
		this.salarioHora = salarioHora;
		this.horas = horas;
		
		calcularSalarioDiario();  // CALCULAR EL SALARIO DIARIO
		
	}
	
	//METODOS
	public void calcularSalarioDiario() {
		salarioDiario = salarioHora * horas;
	}
		
	//GETS
	public int getNumEmpleado() {
		return numEmpleado;
	}
	
	public float getsalarioDiario() {
		calcularSalarioDiario();
		return salarioDiario;
	}
	
	public float getSalarioHora() {
		return salarioHora;
	}
	
	public int gethoras() {
		return horas;
	}
	
	public Clase getClase() {
		return suClase;
	}
	
	//SETS
	public void setNumEmpleado(int numEmpleado) {
		this.numEmpleado = numEmpleado;
	}
	
	public void setSalarioHora(float salarioHora) {
		this.salarioHora = salarioHora;
		calcularSalarioDiario();  // ACTUALIZAR EL SALARIO DIARIO AL CAMBIAR EL SALARIO POR HORA
	}
	
	public void setHoras(int horas) {
		this.horas = horas;
		calcularSalarioDiario();  // ACTUALIZAR EL SALARIO DIARIO AL CAMBIAR LAS HORAS TRABAJADAS
	}
	
	public void setClase(Clase suClase) {
		this.suClase = suClase;
	}
}