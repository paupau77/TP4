package gestordeinventario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Clase que maneja la conexión a la base de datos MySQL
public class DatabaseConnection {
    private static Connection conexion;

    private static final String URL = "jdbc:mysql://localhost:3306/inventario_db";
    private static final String USUARIO = "root";
    private static final String CLAVE = "root";

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
                System.out.println("Conexión establecida.");
            } catch (SQLException e) {
                System.out.println("Error al conectar con la base de datos.");
                throw e;
            }
        }
        return conexion;
    }

    // Método para cerrar la conexión con la base de datos
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión.");
        }
    }

    // Método para crear las tablas necesarias y usuarios predeterminados
    public static void crearTablasYUsuarios() throws SQLException {
        Connection conn = getConnection();

        // Crear la tabla de productos
        String sqlProductos = "CREATE TABLE IF NOT EXISTS Productos (" +
            "idProducto INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(255)," +
            "categoria VARCHAR(255)," +
            "cantidad INT DEFAULT 0," +
            "proveedor VARCHAR(255)," +
            "fechaAdquisicion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";

    // Crear la tabla de usuarios
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios (" +
                "idUsuario INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(255)," +
                "email VARCHAR(255) UNIQUE," +
                "clave VARCHAR(255)," +
                "rol ENUM('Administrador', 'Empleado', 'Gerente')" +
                ")";

         // Crear la tabla de entradas
         String sqlEntradas = "CREATE TABLE IF NOT EXISTS Entradas (" +
            "idEntrada INT AUTO_INCREMENT PRIMARY KEY," +
            "idProducto INT," +
            "cantidad INT NOT NULL," +
            "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (idProducto) REFERENCES Productos(idProducto) ON DELETE CASCADE" +
            ")";

         // Crear la tabla de salidas
         String sqlSalidas = "CREATE TABLE IF NOT EXISTS Salidas (" +
            "idSalida INT AUTO_INCREMENT PRIMARY KEY," +
            "idProducto INT," +
            "cantidad INT NOT NULL," +
            "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (idProducto) REFERENCES Productos(idProducto) ON DELETE CASCADE" +
            ")";

        // Ejecutar las consultas para crear tablas
        try (PreparedStatement stmtProductos = conn.prepareStatement(sqlProductos);
             PreparedStatement stmtUsuarios = conn.prepareStatement(sqlUsuarios);
             PreparedStatement stmtEntradas = conn.prepareStatement(sqlEntradas);
             PreparedStatement stmtSalidas = conn.prepareStatement(sqlSalidas)) {

            stmtProductos.executeUpdate();
            stmtUsuarios.executeUpdate();
            stmtEntradas.executeUpdate();
            stmtSalidas.executeUpdate();
            System.out.println("Tablas creadas correctamente.");
        }

        // Insertar usuarios predeterminados para pruebas
        String sqlInsertarUsuario = "INSERT IGNORE INTO Usuarios (nombre, email, clave, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmtInsertar = conn.prepareStatement(sqlInsertarUsuario)) {
            stmtInsertar.setString(1, "Admin");
            stmtInsertar.setString(2, "admin@admin.com");
            stmtInsertar.setString(3, "admin123");
            stmtInsertar.setString(4, "Administrador");
            stmtInsertar.executeUpdate();

            stmtInsertar.setString(1, "Empleado1");
            stmtInsertar.setString(2, "empleado1@empresa.com");
            stmtInsertar.setString(3, "empleado123");
            stmtInsertar.setString(4, "Empleado");
            stmtInsertar.executeUpdate();

            stmtInsertar.setString(1, "Gerente1");
            stmtInsertar.setString(2, "gerente1@empresa.com");
            stmtInsertar.setString(3, "gerente123");
            stmtInsertar.setString(4, "Gerente");
            stmtInsertar.executeUpdate();

            System.out.println("Usuarios predeterminados agregados.");
        }
    }
}