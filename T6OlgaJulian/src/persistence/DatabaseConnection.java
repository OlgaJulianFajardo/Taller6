package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para la conexion con la base de datos.
 */
public class DatabaseConnection {

	private static final String URL = "jdbc:mysql://localhost/gestionIncidencia";
	private static final String USER = "olga";
	private static final String PASSWORD = "123456";

	private static Connection connection;

	/**
	 * Metodo para la conexion con la base de datos
	 * @return connection
	 */
	public static Connection abrirConexion() {
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	/**
	 * Obtiene la conexion con la base de datos
	 * @return connection
	 */
	public static Connection getConexion() {
		if (connection == null) {
			abrirConexion();
		}
		return connection;
	}

	/**
	 * Metodo para cerrar la conexion con la base de datos.
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
