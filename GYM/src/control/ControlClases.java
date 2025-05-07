package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import entidades.Cliente;
import entidades.Clase;
import entidades.Entrenador;

public class ControlClases {
    // ATRIBUTOS
    private static ArrayList<Clase> clases = null;
        
    // METODOS
    // METODO PARA INICIALIZAR EL ARRAYLIST DE LAS CLASES
    public static void inicializa() {
        clases = new ArrayList<>();
        }
        
    // METODO PARA DAR DE ALTA CLASE
    public static void addClase(Clase c) {
        try {
            // INICIALIZAR ARRAYLIST DE CLASES
            if (clases == null) {
                inicializa();
            }
            
            // VALIDAR NUMERO DE CLASE USANDO STREAMS
            boolean existeClase = clases.stream()
                .anyMatch(clase -> clase.getNumClase() == c.getNumClase());
                
            if (existeClase) {
                JOptionPane.showMessageDialog(null, "Ya existe una clase con ese número");
                return;
            }
            
            clases.add(c);
        } catch(IllegalArgumentException | NullPointerException e) {
            // MENSAJE POR SI SE DESPLIEGA UN ERROR
            JOptionPane.showMessageDialog(null, "Error al agregar clase: " + e.getMessage());
        }
    }
    
    // METODO PARA BUSCAR CLIENTE POR ID USANDO STREAMS
    public static Cliente buscaCliente(int idCliente) {
        // INICIALIZAR CLASES SI NO ESTAN INICIALIZADAS
        if (clases == null) {
            return null;
        }
        
        // UTILIZAR STREAMS PARA BUSCAR
        return clases.stream()
            .flatMap(clase -> clase.getClientes().stream())
            .filter(cliente -> cliente != null && cliente.getIdCliente() == idCliente)
            .findFirst()
            .orElse(null);
    }

    // METODO PARA BUSCAR ENTRENADOR USANDO STREAMS
    public static Entrenador buscaEntrenador(int numEmpleado) {
        // INICIALIZAR CLASES SI NO ESTAN INICIALIZADAS
        if (clases == null) {
            return null;
        }
        
        // SE USAN STREAMS PARA BUSCAR
        return clases.stream()
            .map(Clase::getEntrenador)
            .filter(entrenador -> entrenador != null && entrenador.getNumEmpleado() == numEmpleado)
            .findFirst()
            .orElse(null);
    }    

    // METODO PARA BUSCAR CLASE UTILIZANDO STREAMS
    public static Clase buscaClase(int numClase) {
        // INICIALIZAR CLASES SI NO ESTAN INICIALIZADAS
        if (clases == null) {
            return null;
        }
        
        // SE USAN STREAMS PARA BUSCAR
        return clases.stream()
            .filter(clase -> clase.getNumClase() == numClase)
            .findFirst()
            .orElse(null);
    }    

    // MÉTODO PARA GENERAR TABLA DE FRECUENCIAS POR PLAN DE MEMBRESÍA
    public static void tablaPlanesMembresia() {

        // GENERAR MENSAJE CON LA TABLA DE FRECUENCIAS
        StringBuilder mensaje = new StringBuilder("Frecuencia de Clientes por Plan de Membresía:\n\n");

        // VALIDAR SI HAY CLASES
        if (clases == null) {
            JOptionPane.showMessageDialog(null, "No hay clases registradas");
            return;
        }

        // CREAR EL MAP PARA ALMACENAR FRECUENCIAS POR PLAN
        Map<String, Integer> frecuenciaPlanes = new HashMap<>();

        // RECORRER TODAS LAS CLASES Y OBTENER LOS CLIENTES USANDO STREAMS
        clases.stream()
            .flatMap(clase -> clase.getClientes().stream())
            .forEach(cliente -> {
                // OBTENER EL PLAN DEL CLIENTE
                String plan = cliente.getPlanMembresía();
                
                // INCREMENTAR EL CONTADOR PARA ESE PLAN
                frecuenciaPlanes.put(plan, frecuenciaPlanes.getOrDefault(plan, 0) + 1);
            });

        
        // USAR STREAMS PARA ORDENAR EL MAP POR CLAVE (PLAN) Y GENERAR EL MENSAJE
        frecuenciaPlanes.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String plan = entry.getKey();
                int cantidad = entry.getValue();
                mensaje.append(String.format("Plan: %s || %d cliente(s)\n", plan, cantidad));
            });

        JOptionPane.showMessageDialog(null, mensaje.toString());
    }

    // GET Y SET
    public static ArrayList<Clase> getClases() {
        // INICIALIZAR CLASES SI NO ESTAN INICIALIZADAS
        if (clases == null) {
            inicializa();
        }
        return clases;
    }
    
    public static int getCantidad() {
        if (clases == null) {
            return 0;
        }
        return clases.size();
    }
}