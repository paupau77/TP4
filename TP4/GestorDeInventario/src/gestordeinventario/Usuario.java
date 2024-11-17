package gestordeinventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Clase abstracta que representa a un usuario
public abstract class Usuario {
    protected String nombre;
    protected String email;
    protected String clave;

    // Constructor de la clase Usuario
    public Usuario(String nombre, String email, String clave) {
        this.nombre = nombre;
        this.email = email;
        this.clave = clave;
    }

    // Método abstracto que será implementado en cada subclase para mostrar el menú
    public abstract void mostrarOpciones();

    // Método abstracto para visualizar el inventario (lo implementarán las subclases)
    public abstract void visualizarInventario() throws SQLException;


    // Método abstracto para generar reporte con rango de fechas (solo para Administrador y Gerente)
    public abstract void generarReporte(String fechaInicio, String fechaFin) throws SQLException;


    // Método para iniciar sesión y devolver el tipo de usuario
    public static Usuario iniciarSesion(String email, String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM Usuarios WHERE email = ? AND clave = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                String nombre = rs.getString("nombre");
                if (rol.equals("Administrador")) {
                    return new Administrador(nombre, email, password);
                } else if (rol.equals("Empleado")) {
                    return new Empleado(nombre, email, password);
                } else if (rol.equals("Gerente")) {
                    return new Gerente(nombre, email, password);
                }
            }
        }
        return null;  // Devuelve null si el usuario no se encuentra o las credenciales son incorrectas
    }
}