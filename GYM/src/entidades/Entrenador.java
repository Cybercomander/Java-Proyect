package entidades;

// CLASE "ENTRENADOR" QUE REPRESENTA A UN ENTRENADOR DEL GIMNASIO.
// HEREDA DE LA CLASE "Persona" Y CONTIENE INFORMACIÓN ADICIONAL COMO NÚMERO DE EMPLEADO, SALARIO Y HORAS TRABAJADAS.

public class Entrenador extends Persona{
	//ATRIBUTOS
	private int numEmpleado;
	private float salarioDiario;
	private float salarioHora;
	private int horas;
	
	private Clase suClase;		//ATRIBUTO DE RELACION CON LA CLASE
	
	//CONSTRUCTOR POR DEFAULT:
	// INICIALIZA UN ENTRENADOR CON VALORES POR DEFECTO.
	public Entrenador() {
		super();
	}
	
	// CONSTRUCTOR ADICIONAL:
	// INICIALIZA UN ENTRENADOR CON LOS DATOS PROPORCIONADOS COMO NOMBRE, FECHA DE NACIMIENTO, NÚMERO DE EMPLEADO, SALARIO POR HORA Y HORAS TRABAJADAS.
	// TAMBIÉN CALCULA EL SALARIO DIARIO.
	public Entrenador(String nombre, String fechaNacimiento, int numEmpleado, float salarioHora, int horas) {
		super(nombre, fechaNacimiento);
		this.numEmpleado = numEmpleado;
		this.salarioHora = salarioHora;
		this.horas = horas;
		
		calcularSalarioDiario();  // CALCULAR EL SALARIO DIARIO
		
	}
	
	// MÉTODO "calcularSalarioDiario":
	// CALCULA EL SALARIO DIARIO DEL ENTRENADOR BASADO EN SU SALARIO POR HORA Y HORAS TRABAJADAS.
	public void calcularSalarioDiario() {
		salarioDiario = salarioHora * horas;
	}
		
	// MÉTODOS GETTERS:
	// PERMITEN OBTENER EL NÚMERO DE EMPLEADO, SALARIO DIARIO, SALARIO POR HORA, HORAS TRABAJADAS Y LA CLASE ASIGNADA.
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
	
	// MÉTODOS SETTERS:
	// PERMITEN MODIFICAR EL NÚMERO DE EMPLEADO, SALARIO POR HORA, HORAS TRABAJADAS Y LA CLASE ASIGNADA.
	// TAMBIÉN ACTUALIZAN EL SALARIO DIARIO CUANDO SE MODIFICAN EL SALARIO POR HORA O LAS HORAS TRABAJADAS.
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

    // GETTERS Y SETTERS ADICIONALES PARA ACCESO ALEATORIO
    public int getId() { return numEmpleado; }
    public void setId(int id) { this.numEmpleado = id; }
    public String getTipo() { return "Entrenador"; }
    public void setTipo(String tipo) { /* No aplica */ }
    public String getFechaNacimiento() { return getfechaNacimiento(); }
    public void setFechaNacimiento(String fecha) { super.setFechaNacimiento(fecha); }
    public int getPeso() { return Math.round(salarioHora); } // Usar salarioHora como "peso" para compatibilidad
    public void setPeso(int peso) { this.salarioHora = peso; calcularSalarioDiario(); }
    public float getAltura() { return 0; }
    public void setAltura(float altura) { /* No aplica */ }
    public int getEdad() { return horas; }
    public void setEdad(int edad) { this.horas = edad; calcularSalarioDiario(); }
    public int getExperiencia() { return horas; }
    public void setExperiencia(int exp) { this.horas = exp; calcularSalarioDiario(); }
}