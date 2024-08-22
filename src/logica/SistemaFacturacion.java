package logica;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaFacturacion {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = ConexionBD.getConnection();

        while (true) {
            System.out.println("\nMenú del Sistema de Facturación:");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Registrar producto");
            System.out.println("3. Consultar datos de cliente");
            System.out.println("4. Crear y procesar factura");
            System.out.println("0. Salir");
            System.out.print("Elija una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    registrarCliente(connection, scanner);
                    break;
                case 2:
                    registrarProducto(connection, scanner);
                    break;
                case 3:
                    consultarDatosCliente(connection, scanner);
                    break;
                case 4:
                    procesarFactura(connection, scanner);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }

    private static void registrarCliente(Connection connection, Scanner scanner) {
        try {
            System.out.print("Ingrese el nombre del cliente: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el apellido del cliente: ");
            String apellido = scanner.nextLine();
            System.out.print("Ingrese el estrato del cliente: ");
            int estrato = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            String insertClienteSQL = "INSERT INTO clientes (nombre, apellido, estrato) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertClienteSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setInt(3, estrato);
            statement.executeUpdate();

            // Obtener el ID generado
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idCliente = generatedKeys.getInt(1);
                System.out.println("Cliente registrado exitosamente con ID: " + idCliente);
            } else {
                System.out.println("No se pudo obtener el ID del cliente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void registrarProducto(Connection connection, Scanner scanner) {
        try {
            System.out.print("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el precio del producto: ");
            double precio = scanner.nextDouble();
            scanner.nextLine(); // Limpiar el buffer

            String insertProductoSQL = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertProductoSQL);
            statement.setString(1, nombre);
            statement.setDouble(2, precio);
            statement.executeUpdate();

            System.out.println("Producto registrado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void consultarDatosCliente(Connection connection, Scanner scanner) {
        try {
            System.out.println("Buscar cliente por: ");
            System.out.println("1. ID");
            System.out.println("2. Nombre");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            String query;
            if (opcion == 1) {
                System.out.print("Ingrese el ID del cliente: ");
                int idCliente = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer
                query = "SELECT * FROM clientes WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, idCliente);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    mostrarCliente(resultSet);
                } else {
                    System.out.println("Cliente no encontrado.");
                }
            } else if (opcion == 2) {
                System.out.print("Ingrese el nombre del cliente: ");
                String nombre = scanner.nextLine();
                query = "SELECT * FROM clientes WHERE nombre = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nombre);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    mostrarCliente(resultSet);
                }
            } else {
                System.out.println("Opción no válida.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarCliente(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        int estrato = resultSet.getInt("estrato");
        double totalGastado = resultSet.getDouble("total_gastado");

        System.out.println("\nCliente encontrado:");
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Estrato: " + estrato);
        System.out.println("Total Gastado: " + totalGastado);

        // Mostrar productos comprados
        mostrarProductosComprados(id);
    }

    private static void mostrarProductosComprados(int idCliente) throws SQLException {
        Connection connection = ConexionBD.getConnection();
        String query = "SELECT p.nombre, df.cantidad, df.subtotal " +
                       "FROM detalle_factura df " +
                       "JOIN productos p ON df.id_producto = p.id " +
                       "JOIN facturas f ON df.id_factura = f.id " +
                       "WHERE f.id_cliente = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idCliente);
        ResultSet resultSet = statement.executeQuery();

        System.out.println("\nProductos comprados:");
        while (resultSet.next()) {
            String nombreProducto = resultSet.getString("nombre");
            int cantidad = resultSet.getInt("cantidad");
            double subtotal = resultSet.getDouble("subtotal");

            System.out.println("Producto: " + nombreProducto + ", Cantidad: " + cantidad + ", Subtotal: " + subtotal);
        }
    }

    private static void procesarFactura(Connection connection, Scanner scanner) {
    try {
        System.out.print("Ingrese el ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer después de leer el ID del cliente

        // Crear factura
        String insertFacturaSQL = "INSERT INTO facturas (id_cliente, fecha) VALUES (?, NOW())";
        PreparedStatement statement = connection.prepareStatement(insertFacturaSQL, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, idCliente);
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int idFactura = generatedKeys.getInt(1);
            System.out.println("Factura creada con ID: " + idFactura);

            // Agregar productos a la factura
            while (true) {
                System.out.print("Ingrese el ID del producto (0 para terminar): ");
                int idProducto = scanner.nextInt();
                if (idProducto == 0) break;

                System.out.print("Ingrese la cantidad: ");
                int cantidad = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer después de leer la cantidad

                double precio = obtenerPrecioProducto(connection, idProducto);
                double subtotal = precio * cantidad;

                String insertDetalleSQL = "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
                PreparedStatement detalleStatement = connection.prepareStatement(insertDetalleSQL);
                detalleStatement.setInt(1, idFactura);
                detalleStatement.setInt(2, idProducto);
                detalleStatement.setInt(3, cantidad);
                detalleStatement.setDouble(4, subtotal);
                detalleStatement.executeUpdate();
            }

            // Preguntar si se aplica un descuento
            System.out.print("¿Desea aplicar un descuento? (s/n): ");
            String aplicarDescuento = scanner.nextLine().trim();
            double descuento = 0;
            if (aplicarDescuento.equalsIgnoreCase("s")) {
                System.out.print("Ingrese el porcentaje de descuento: ");
                descuento = scanner.nextDouble();
                scanner.nextLine(); // Limpiar el buffer después de leer el descuento
            }

            double totalFactura = calcularTotalFactura(connection, idFactura, descuento);
            System.out.printf("Total gastado en la factura: %.2f\n", totalFactura);

            // Actualizar total gastado del cliente
            actualizarTotalGastadoCliente(connection, idCliente, totalFactura);

            System.out.println("Gracias por su compra, ¡que tenga un buen día!");
        } else {
            System.out.println("Error al crear la factura.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (InputMismatchException e) {
        System.out.println("Entrada inválida. Por favor ingrese datos válidos.");
        scanner.nextLine(); // Limpiar el buffer en caso de error de entrada
    }
}


    private static double obtenerPrecioProducto(Connection connection, int idProducto) throws SQLException {
        String query = "SELECT precio FROM productos WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idProducto);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getDouble("precio");
        } else {
            System.out.println("Producto no encontrado.");
            return 0;
        }
    }

    private static double calcularTotalFactura(Connection connection, int idFactura, double porcentajeDescuento) throws SQLException {
        String query = "SELECT SUM(subtotal) AS total FROM detalle_factura WHERE id_factura = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idFactura);
        ResultSet resultSet = statement.executeQuery();

        double total = 0;
        if (resultSet.next()) {
            total = resultSet.getDouble("total");
        }

        double descuento = total * (porcentajeDescuento / 100);
        return total - descuento;
    }

    private static void actualizarTotalGastadoCliente(Connection connection, int idCliente, double totalFactura) throws SQLException {
        String query = "UPDATE clientes SET total_gastado = total_gastado + ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, totalFactura);
        statement.setInt(2, idCliente);
        statement.executeUpdate();
    }
}
