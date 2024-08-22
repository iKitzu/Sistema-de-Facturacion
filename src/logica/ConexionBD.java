package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://bofi9zvykt97phksfooe-mysql.services.clever-cloud.com:3306/bofi9zvykt97phksfooe";
    private static final String USER = "utfyhfmhumyevfnk";
    private static final String PASSWORD = "qoVBvGe7ubDPiYeDu5in";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
