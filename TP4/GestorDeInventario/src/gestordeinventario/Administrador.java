package gestordeinventario;

import java.sql.SQLException;

// Subclase de Usuario que representa al Administrador
public class Administrador extends Usuario {

    // Constructor de la clase Administrador
    public Administrador(String nombre, String email, String clave) {
        super(nombre, email, clave);
    }

    // Mostrar las opciones del menú para el Administrador
    @Override
    public void mostrarOpciones() {
        System.out.println("\nMenú Administrador:");
        System.out.println("1. Registrar Producto");
        System.out.println("2. Editar Producto");
        System.out.println("3. Eliminar Producto");
        System.out.println("4. Registrar Entrada");
        System.out.println("5. Registrar Salida");
        System.out.println("6. Visualizar Inventario");
        System.out.println("7. Generar Reporte");
        System.out.println("8. Cerrar Sesión");
    }

    // Método para registrar un producto sin cantidad inicial
    public void registrarProducto(String nombre, String categoria, String proveedor) throws SQLException {
        Producto nuevoProducto = new Producto(nombre, categoria, proveedor);
        nuevoProducto.registrarProducto();
    }
    
        // Método para eliminar un producto
    public void eliminarProducto(int idProducto) throws SQLException {
        Producto.eliminarProducto(idProducto);
    }

    // Implementación para editar un producto
    public void editarProducto(int idProducto, String nuevoNombre, String nuevaCategoria, String nuevoProveedor, int nuevaCantidad) throws SQLException {
        Producto.editarProducto(idProducto, nuevoNombre, nuevaCategoria, nuevoProveedor, nuevaCantidad);
    }

    // Implementación para registrar entrada o salida
    public void registrarEntrada(int idProducto, int cantidad) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.registrarEntrada(idProducto, cantidad);
    }

    public void registrarSalida(int idProducto, int cantidad) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.registrarSalida(idProducto, cantidad);
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