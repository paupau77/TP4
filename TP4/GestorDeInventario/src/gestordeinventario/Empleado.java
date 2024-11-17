package gestordeinventario;

import java.sql.SQLException;

// Subclase de Usuario que representa al Empleado
public class Empleado extends Usuario {

    // Constructor de la clase Empleado
    public Empleado(String nombre, String email, String clave) {
        super(nombre, email, clave);
    }

    // Mostrar las opciones del menú para el Empleado
    @Override
    public void mostrarOpciones() {
        System.out.println("\nMenú Empleado:");
        System.out.println("1. Registrar Entrada");
        System.out.println("2. Registrar Salida");
        System.out.println("3. Visualizar Inventario");
        System.out.println("4. Cerrar Sesión");
    }

    // Implementación para registrar entrada
    public void registrarEntrada(int idProducto, int cantidad) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.registrarEntrada(idProducto, cantidad);
    }

    // Implementación para registrar salida
    public void registrarSalida(int idProducto, int cantidad) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.registrarSalida(idProducto, cantidad);
    }

    // Implementación para visualizar el inventario
    @Override
    public void visualizarInventario() throws SQLException {
        Producto.obtenerProductos();  // Llamamos al método que obtiene todos los productos
    }
    
        // Implementación vacía para el método generarReporte, ya que el Empleado no tiene permiso para generar reportes
    @Override
    public void generarReporte(String fechaInicio, String fechaFin) throws SQLException {
        System.out.println("Los empleados no tienen permiso para generar reportes.");
    }

}