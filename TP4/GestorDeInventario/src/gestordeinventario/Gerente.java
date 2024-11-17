package gestordeinventario;

import java.sql.SQLException;

// Subclase de Usuario que representa al Gerente
public class Gerente extends Usuario {

    // Constructor de la clase Gerente
    public Gerente(String nombre, String email, String clave) {
        super(nombre, email, clave);
    }

    // Mostrar las opciones del menú para el Gerente
    @Override
    public void mostrarOpciones() {
        System.out.println("\nMenú Gerente:");
        System.out.println("1. Visualizar Inventario");
        System.out.println("2. Generar Reporte");
        System.out.println("3. Cerrar Sesión");
    }

    // Implementación para visualizar el inventario
    @Override
    public void visualizarInventario() throws SQLException {
        Producto.obtenerProductos();  // Llamamos al método que obtiene todos los productos
    }

    // Implementación de generarReporte con rango de fechas
    @Override
    public void generarReporte(String fechaInicio, String fechaFin) throws SQLException {
        System.out.println("\n--- Reporte de Inventario ---");
        Producto.mostrarReporteInventario(fechaInicio, fechaFin);
        System.out.println("\n--- Fin del Reporte ---");
    }
}