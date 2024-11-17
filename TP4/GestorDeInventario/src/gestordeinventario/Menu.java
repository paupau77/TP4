package gestordeinventario;

import java.sql.SQLException;
import java.util.Scanner;

// Clase que gestiona el menú principal y las interacciones del usuario
public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        try {
            // Crear tablas y usuarios predeterminados al iniciar la aplicación
            DatabaseConnection.crearTablasYUsuarios();
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas o usuarios predeterminados.");
        }

        Usuario usuarioActual = null;

        while (!salir) {
            System.out.println("\nPANTALLA DE INICIO DE SESIÓN:");
            System.out.print("Usuario: ");
            String email = scanner.nextLine();
            System.out.print("Contraseña: ");
            String password = scanner.nextLine();

                      try {
                usuarioActual = Usuario.iniciarSesion(email, password);
                if (usuarioActual != null) {
                    boolean cerrarSesion = false;
                    while (!cerrarSesion) {
                        usuarioActual.mostrarOpciones();
                        System.out.print("\nSeleccione una opción: ");
                        int opcion = scanner.nextInt();
                        scanner.nextLine(); // Consumir nueva línea

                        if (usuarioActual instanceof Administrador) {
                            switch (opcion) {
                                case 1:  // Registrar producto
                                    registrarProducto(scanner, (Administrador) usuarioActual);
                                    break;
                                case 2:  // Editar producto
                                    editarProducto(scanner, (Administrador) usuarioActual);
                                    break;
                                case 3:  // Eliminar producto
                                    eliminarProducto(scanner, (Administrador) usuarioActual);
                                    break;
                                case 4:  // Registrar entrada
                                    registrarEntradaSalida(scanner, (Administrador) usuarioActual, "entrada");
                                    break;
                                case 5:  // Registrar salida
                                    registrarEntradaSalida(scanner, (Administrador) usuarioActual, "salida");
                                    break;
                                case 6:  // Visualizar inventario
                                    visualizarInventario(usuarioActual);
                                    break;
                                case 7:  // Generar reporte
                                    generarReporte(scanner, usuarioActual);
                                    break;
                                case 8:  // Cerrar sesión
                                    cerrarSesion = true;
                                    System.out.println("Sesión cerrada.");
                                    break;
                                default:
                                    System.out.println("Opción no válida.");
                            }
                        } else if (usuarioActual instanceof Empleado) {
                            switch (opcion) {
                                case 1:  // Registrar entrada
                                    registrarEntradaSalida(scanner, (Empleado) usuarioActual, "entrada");
                                    break;
                                case 2:  // Registrar salida
                                    registrarEntradaSalida(scanner, (Empleado) usuarioActual, "salida");
                                    break;
                                case 3:  // Visualizar inventario
                                    visualizarInventario(usuarioActual);
                                    break;
                                case 4:  // Cerrar sesión
                                    cerrarSesion = true;
                                    System.out.println("Sesión cerrada.");
                                    break;
                                default:
                                    System.out.println("Opción no válida.");
                            }
                        } else if (usuarioActual instanceof Gerente) {
                            switch (opcion) {
                                case 1:  // Visualizar inventario
                                    visualizarInventario(usuarioActual);
                                    break;
                                case 2:  // Generar reporte
                                    generarReporte(scanner, usuarioActual);
                                    break;
                                case 3:  // Cerrar sesión
                                    cerrarSesion = true;
                                    System.out.println("Sesión cerrada.");
                                    break;
                                default:
                                    System.out.println("Opción no válida.");
                            }
                        }
                    }
                } else {
                    System.out.println("Credenciales incorrectas.");
                }
            } catch (SQLException e) {
                System.out.println("Error al intentar iniciar sesión.");
            }

            System.out.println("\n¿Desea salir del programa? (s/n)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                salir = true;
            }
        }

        DatabaseConnection.cerrarConexion();
        scanner.close();
    }

    // Métodos auxiliares para las distintas operaciones (registrar, editar, etc.)
    // Método para registrar un producto sin pedir cantidad inicial
    private static void registrarProducto(Scanner scanner, Administrador admin) throws SQLException {
        System.out.println("Ingrese los detalles del nuevo producto:");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        System.out.print("Proveedor: ");
        String proveedor = scanner.nextLine();

        admin.registrarProducto(nombre, categoria, proveedor);
    }

    // Método para editar un producto
    private static void editarProducto(Scanner scanner, Administrador admin) throws SQLException {
        System.out.print("Ingrese el ID del producto a editar: ");
        int idProducto = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea
        System.out.print("Nuevo nombre del producto: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Nueva categoría del producto: ");
        String nuevaCategoria = scanner.nextLine();
        System.out.print("Nuevo proveedor del producto: ");
        String nuevoProveedor = scanner.nextLine();
        System.out.print("Nueva cantidad del producto: ");
        int nuevaCantidad = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        admin.editarProducto(idProducto, nuevoNombre, nuevaCategoria, nuevoProveedor, nuevaCantidad);
    }
    
        // Método para eliminar un producto
    private static void eliminarProducto(Scanner scanner, Administrador admin) throws SQLException {
        System.out.print("Ingrese el ID del producto a eliminar: ");
        int idProducto = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        admin.eliminarProducto(idProducto);
    }

    // Método para registrar entrada o salida
    private static void registrarEntradaSalida(Scanner scanner, Usuario usuario, String tipo) throws SQLException {
        System.out.print("Ingrese el ID del producto: ");
        int idProducto = scanner.nextInt();
        System.out.print("Ingrese la cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        if (tipo.equals("entrada")) {
            if (usuario instanceof Administrador) {
                ((Administrador) usuario).registrarEntrada(idProducto, cantidad);
            } else if (usuario instanceof Empleado) {
                ((Empleado) usuario).registrarEntrada(idProducto, cantidad);
            }
        } else if (tipo.equals("salida")) {
            if (usuario instanceof Administrador) {
                ((Administrador) usuario).registrarSalida(idProducto, cantidad);
            } else if (usuario instanceof Empleado) {
                ((Empleado) usuario).registrarSalida(idProducto, cantidad);
            }
        } else {
            System.out.println("Opción no válida.");
        }
    }

    // Método para visualizar inventario
    private static void visualizarInventario(Usuario usuario) throws SQLException {
        usuario.visualizarInventario();
    }

    // Método para generar un reporte con rango de fechas
    private static void generarReporte(Scanner scanner, Usuario usuario) throws SQLException {
        System.out.println("\n--- Generar Reporte ---");

        // Solicitar el rango de fechas para el reporte
        System.out.print("Ingrese la fecha de inicio (YYYY-MM-DD): ");
        String fechaInicio = scanner.nextLine();
        System.out.print("Ingrese la fecha de fin (YYYY-MM-DD): ");
        String fechaFin = scanner.nextLine();

        // Generar y mostrar el reporte
        usuario.generarReporte(fechaInicio, fechaFin);
    }
}