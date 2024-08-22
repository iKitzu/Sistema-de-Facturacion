package logica;

import java.sql.*;
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
            System.out.println("4. Crear factura");
            System.out.println("5. Agregar producto a factura");
            System.out.println("6. Aplicar descuento a factura");
            System.out.println("7. Mostrar total de factura");
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
                    crearFactura(connection, scanner);
                    break;
                case 5:
                    agregarProductoAFactura(connection, scanner);
                    break;
                case 6:
                    aplicarDescuentoAFactura(connection, scanner);
                    break;
                case 7:
                    mostrarTotalFactura(connection, scanner);
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
            PreparedStatement statement = connection.prepareStatement(insertClienteSQL);
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setInt(3, estrato);
            statement.executeUpdate();

            System.out.println("Cliente registrado exitosamente.");
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

    private static void crearFactura(Connection connection, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del cliente: ");
            int idCliente = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer
            System.out.print("Ingrese la fecha de la factura (yyyy-mm-dd): ");
            String fecha = scanner.nextLine();

            String insertFacturaSQL = "INSERT INTO facturas (id_cliente, fecha) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertFacturaSQL);
            statement.setInt(1, idCliente);
            statement.setString(2, fecha);
            statement.executeUpdate();

            System.out.println("Factura creada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void agregarProductoAFactura(Connection connection, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID de la factura: ");
            int idFactura = scanner.nextInt();
            System.out.print("Ingrese el ID del producto: ");
            int idProducto = scanner.nextInt();
            System.out.print("Ingrese la cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            String queryProducto = "SELECT precio FROM productos WHERE id = ?";
            PreparedStatement statementProducto = connection.prepareStatement(queryProducto);
            statementProducto.setInt(1, idProducto);
            ResultSet resultSetProducto = statementProducto.executeQuery();
            double precio = 0;
            if (resultSetProducto.next()) {
                precio = resultSetProducto.getDouble("precio");
            } else {
                System.out.println("Producto no encontrado.");
                return;
            }

            double subtotal = precio * cantidad;
            String insertDetalleSQL = "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement statementDetalle = connection.prepareStatement(insertDetalleSQL);
            statementDetalle.setInt(1, idFactura);
            statementDetalle.setInt(2, idProducto);
            statementDetalle.setInt(3, cantidad);
            statementDetalle.setDouble(4, subtotal);
            statementDetalle.executeUpdate();

            System.out.println("Producto agregado a la factura exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void aplicarDescuentoAFactura(Connection connection, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID de la factura: ");
            int idFactura = scanner.nextInt();
            System.out.print("Ingrese el porcentaje de descuento: ");
            double descuento = scanner.nextDouble();
            scanner.nextLine(); // Limpiar el buffer

            double totalConDescuento = calcularTotalConDescuento(connection, idFactura, descuento);
            System.out.printf("Total con descuento aplicado: %.2f\n", totalConDescuento);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double calcularTotalConDescuento(Connection connection, int idFactura, double porcentajeDescuento) throws SQLException {
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

    private static void mostrarTotalFactura(Connection connection, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID de la factura: ");
            int idFactura = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            double total = calcularTotalFactura(connection, idFactura);
            System.out.printf("Total de la factura: %.2f\n", total);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double calcularTotalFactura(Connection connection, int idFactura) throws SQLException {
        String query = "SELECT SUM(subtotal) AS total FROM detalle_factura WHERE id_factura = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idFactura);
        ResultSet resultSet = statement.executeQuery();

        double total = 0;
        if (resultSet.next()) {
            total = resultSet.getDouble("total");
        }

        return total;
    }
}
