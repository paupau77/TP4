package gestordeinventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Clase que representa un producto del inventario
public class Producto {

    private final String nombre;
    private final String categoria;
    private final String proveedor;

    // Constructor sin cantidad inicial
    public Producto(String nombre, String categoria, String proveedor) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
    }

    // Método para registrar un nuevo producto sin cantidad inicial
    public void registrarProducto() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO Productos (nombre, categoria, proveedor) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, categoria);
            stmt.setString(3, proveedor);
            stmt.executeUpdate();
            System.out.println("Producto registrado correctamente.");
        }
    }

    // Método para eliminar un producto
    public static void eliminarProducto(int idProducto) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "DELETE FROM Productos WHERE idProducto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idProducto);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Producto eliminado correctamente.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        }
    }

    // Método para editar un producto existente
    public static void editarProducto(int idProducto, String nuevoNombre, String nuevaCategoria, String nuevoProveedor, int nuevaCantidad) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "UPDATE Productos SET nombre = ?, categoria = ?, proveedor = ?, cantidad = ? WHERE idProducto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevaCategoria);
            stmt.setString(3, nuevoProveedor);
            stmt.setInt(4, nuevaCantidad);
            stmt.setInt(5, idProducto);
            stmt.executeUpdate();
            System.out.println("Producto editado correctamente.");
        }
    }

    // Método para mostrar el inventario con las columnas específicas
public static void obtenerProductos() throws SQLException {
    Connection conn = DatabaseConnection.getConnection();
    String query = "SELECT idProducto, nombre, cantidad, proveedor, categoria FROM Productos";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        ResultSet rs = stmt.executeQuery();
        
        // Cabecera de la tabla, ahora incluye la columna "ID"
        System.out.printf("%-5s %-20s %-10s %-20s %-15s\n", "ID", "Nombre", "Cantidad", "Proveedor", "Categoría");

        while (rs.next()) {
            int id = rs.getInt("idProducto");
            String nombre = rs.getString("nombre");
            int cantidad = rs.getInt("cantidad");
            String proveedor = rs.getString("proveedor");
            String categoria = rs.getString("categoria");

            System.out.printf("%-5d %-20s %-10d %-20s %-15s\n", id, nombre, cantidad, proveedor, categoria);
        }
    }
}
    
   // Método para mostrar el reporte del inventario en el rango de fechas
    public static void mostrarReporteInventario(String fechaInicio, String fechaFin) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        // Consulta para obtener los productos con sus salidas en el rango de fechas especificado usando LEFT JOIN
        String query = "SELECT p.idProducto, p.nombre, p.categoria, p.proveedor, p.cantidad AS stock, " +
                       "IFNULL(SUM(s.cantidad), 0) AS salidas " +
                       "FROM Productos p " +
                       "LEFT JOIN Salidas s ON p.idProducto = s.idProducto " +
                       "AND s.fecha BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY) - INTERVAL 1 SECOND " +
                       "GROUP BY p.idProducto, p.nombre, p.categoria, p.proveedor, p.cantidad";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            ResultSet rs = stmt.executeQuery();

            // Cabecera de la tabla con el ID del producto incluido
            System.out.printf("%-5s %-20s %-15s %-20s %-10s %-10s %-20s\n",
                    "ID", "NombreProducto", "Categoría", "Proveedor", "Stock", "Salidas", "Periodo");

            // Periodo del reporte
            String periodo = fechaInicio + " a " + fechaFin;

            // Mostrar cada producto con sus datos en el periodo especificado
            while (rs.next()) {
                int id = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String proveedor = rs.getString("proveedor");
                int stock = rs.getInt("stock");
                int salidas = rs.getInt("salidas");

                System.out.printf("%-5d %-20s %-15s %-20s %-10d %-10d %-20s\n",
                        id, nombre, categoria, proveedor, stock, salidas, periodo);
            }
        }
    }
}
