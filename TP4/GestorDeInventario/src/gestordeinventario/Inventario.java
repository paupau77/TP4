package gestordeinventario;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Clase que gestiona el inventario
public class Inventario {


    // Método para registrar la entrada de productos con fecha automática
    public void registrarEntrada(int idProducto, int cantidad) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        // Registrar la entrada en la tabla Entradas
        String queryEntrada = "INSERT INTO Entradas (idProducto, cantidad, fecha) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmtEntrada = conn.prepareStatement(queryEntrada)) {
            stmtEntrada.setInt(1, idProducto);
            stmtEntrada.setInt(2, cantidad);
            stmtEntrada.executeUpdate();
            System.out.println("Entrada registrada exitosamente.");
        }

        // Actualizar la cantidad total en la tabla Productos
        String queryActualizarProducto = "UPDATE Productos SET cantidad = cantidad + ? WHERE idProducto = ?";
        try (PreparedStatement stmtActualizar = conn.prepareStatement(queryActualizarProducto)) {
            stmtActualizar.setInt(1, cantidad);
            stmtActualizar.setInt(2, idProducto);
            stmtActualizar.executeUpdate();
            System.out.println("Cantidad actualizada correctamente.");
        }
    }

    // Método para registrar la salida de productos y actualizar la cantidad en Productos
    public void registrarSalida(int idProducto, int cantidad) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        // Registrar la salida en la tabla Salidas
        String querySalida = "INSERT INTO Salidas (idProducto, cantidad, fecha) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmtSalida = conn.prepareStatement(querySalida)) {
            stmtSalida.setInt(1, idProducto);
            stmtSalida.setInt(2, cantidad);
            stmtSalida.executeUpdate();
            System.out.println("Salida registrada exitosamente.");
        }

        // Actualizar la cantidad total en la tabla Productos, restando la cantidad
        String queryActualizarProducto = "UPDATE Productos SET cantidad = cantidad - ? WHERE idProducto = ? AND cantidad >= ?";
        try (PreparedStatement stmtActualizar = conn.prepareStatement(queryActualizarProducto)) {
            stmtActualizar.setInt(1, cantidad);
            stmtActualizar.setInt(2, idProducto);
            stmtActualizar.setInt(3, cantidad);
            int rowsUpdated = stmtActualizar.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Cantidad actualizada correctament.");
            } else {
                System.out.println("No hay suficiente stock para registrar la salida.");
            }
        }
    }
}