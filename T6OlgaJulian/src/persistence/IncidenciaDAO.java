package persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import service.Estado;

/**
 * Clase DAO (Data Access Object) para gestionar las operaciones de persistencia de la entidad Incidencia en la base de datos.
 */
public class IncidenciaDAO {

	/**
	 * Metodo para insertar una nueva incidencia en la base de datos.
	 * @param incidencia - La incidencia a insertar.
	 * @throws SQLException - Si ocurre un error al ejecutar la sentencia SQL.
	 */
	public void insertar(Incidencia incidencia) throws SQLException {
		String sql = "INSERT INTO incidencias (codigo, puesto, problema, estado) VALUES (?, ?, ?, ?)";

		// Uso try-with-resources para asegurar que el PreparedStatement se cierre automáticamente.
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql)) {
			
			sentencia.setString(1, incidencia.getCodigo());
			sentencia.setInt(2, incidencia.getPuesto());
			sentencia.setString(3, incidencia.getProblema());
			sentencia.setString(4, incidencia.getEstado().name());
			sentencia.executeUpdate();
		}
	}

	
	/**
	 * Metodo para guardar el último número utilizado para una fecha específica en la tabla 'configuracion' de la base de datos. 
	 * @param fecha -La fecha para la cual se guarda el último número.
	 * @param ultimoNumero - El último número utilizado.
	 */
	public void guardarUltimoNumero(LocalDate fecha, int ultimoNumero)  {
		String sqlInsert = "INSERT INTO Configuracion (fecha, ultimo_numero) VALUES (?, ?) ON DUPLICATE KEY UPDATE ultimo_numero = ?";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sqlInsert)) {
			sentencia.setDate(1, Date.valueOf(fecha));
			sentencia.setInt(2, ultimoNumero);
			sentencia.setInt(3, ultimoNumero);
			sentencia.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("Error al guardar los datos.");
			ex.printStackTrace();
		}
	}

	
	/**
	 * Consultar la base de datos para obtener la fecha de la última incidencia registrada
	 * @param fecha - La fecha a verificar.
	 * @return true si es la misma fecha, false en caso contrario.
	 * @throws SQLException - Si ocurre un error al ejecutar la consulta SQL.
	 */
	//  incidencia registrada
	public boolean esMismoDia(LocalDate fecha) throws SQLException {
		boolean mismoDia = false;
	    LocalDate fechaUltimaIncidencia = null;
	    String sql = "SELECT fecha AS ultima_fecha FROM Configuracion ORDER BY fecha DESC LIMIT 1";
	    
	    try (PreparedStatement statement = DatabaseConnection.getConexion().prepareStatement(sql)) {
	    	try (ResultSet resultSet = statement.executeQuery()) {
	    		if (resultSet.next()) {
	    			fechaUltimaIncidencia = resultSet.getDate("ultima_fecha").toLocalDate();
	    		}
	    	}
	    	//Compara las fechas.
	    	if (fechaUltimaIncidencia != null) {
	    		mismoDia = fechaUltimaIncidencia.isEqual(fecha);
	    	}
	    	return mismoDia;
	    }
	}
	
	
	
	/**
	 * Método para obtener el último número utilizado para una fecha específica
	 * @param fecha -La fecha para la cual se obtiene el último número.
	 * @return - El último número utilizado para la fecha especificada.
	 */
	public int obtenerUltimoNumero(LocalDate fecha) {
		int ultimoNumero = 0;
		String sql = "SELECT ultimo_numero FROM Configuracion WHERE fecha = ?";
		try (PreparedStatement statement = DatabaseConnection.getConexion().prepareStatement(sql)) {
			statement.setDate(1, Date.valueOf(fecha));
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					ultimoNumero = resultSet.getInt("ultimo_numero");
				}
			}
		} catch (SQLException ex) {
			System.out.println("Error en la consulta.");
			ex.printStackTrace();
		}
		return ultimoNumero;
	}

	
	/**
	 * Metodo que busca una incidencia en la BBDD segun el codigo proporcionado
	 * @param codigo - El codigo a buscar
	 * @return - La incidencia buscada
	 * @throws SQLException
	 */
	public Incidencia buscarIncidencia(String codigo) throws SQLException {
		Incidencia incidencia = null;
		String sql = "SELECT * FROM incidencias WHERE codigo = ?";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql)) {
			sentencia.setString(1, codigo.trim());

			try (ResultSet rs = sentencia.executeQuery()) {
				if (rs.next()) {
					codigo = rs.getString("codigo");
					Estado estado = Estado.valueOf(rs.getString("estado"));
					int puesto = rs.getInt("puesto");
					String problema = rs.getString("problema");
					LocalDate fechaResolucion = null;
					String resolucion = rs.getString("resolucion");

					if (rs.getDate("fecha_resolucion") != null) {
						fechaResolucion = rs.getDate("fecha_resolucion").toLocalDate(); // Obtener la fecha de resolución
					}
					incidencia = new Incidencia(codigo, puesto, problema);
					incidencia.setEstado(estado);
					incidencia.setFechaResolucion(fechaResolucion);
					incidencia.setResolucion(resolucion);
				} // fin if
			} // fin 2 try
		} // fin 1 try
		return incidencia;

	}

	/**
	 * Metodo que actualiza los atributos de una incidencia en la BBDD.
	 * @param incidencia
	 * @throws SQLException
	 */
	public void update(Incidencia incidencia) throws SQLException {
		String sql = "UPDATE incidencias SET puesto = ?, problema = ?, estado = ?, fecha_resolucion = ?, resolucion = ? WHERE codigo = ?";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql)) {
			sentencia.setInt(1, incidencia.getPuesto());
			sentencia.setString(2, incidencia.getProblema());
			sentencia.setString(3, incidencia.getEstado().name());
			sentencia.setDate(4,
					incidencia.getFechaResolucion() != null ? java.sql.Date.valueOf(incidencia.getFechaResolucion())
							: null);
			sentencia.setString(5, incidencia.getResolucion());
			sentencia.setString(6, incidencia.getCodigo());
			sentencia.executeUpdate();
		}
	}

	/**
	 * Metodo que elimina una incidencia de la BBDD.
	 * @param codigo - Codigo de la incidencia a eliminar 
	 * @throws SQLException
	 */
	public void eliminar(String codigo) throws SQLException {
		String sql = "DELETE FROM incidencias WHERE codigo = ?";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql)) {
			sentencia.setString(1, codigo);
			sentencia.executeUpdate();
		}
	}

	/**
	 * Metodo que busca la incidencias en la BBDD segun el estado de la incidencia
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public List<Incidencia> listarIncidenciasPorEstado(Estado estado) throws SQLException {
		List<Incidencia> incidencias = new ArrayList<>();
		String sql = "SELECT * FROM incidencias WHERE estado = ?";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql)) {
			sentencia.setString(1, estado.name());
			try (ResultSet rs = sentencia.executeQuery()) {
				while (rs.next()) {
					Incidencia incidencia = new Incidencia(rs.getString("codigo"), rs.getInt("puesto"),
							rs.getString("problema"));

					incidencia.setEstado(estado);
					incidencia.setFechaResolucion(
							rs.getDate("fecha_resolucion") != null ? rs.getDate("fecha_resolucion").toLocalDate()
									: null);
					incidencia.setResolucion(rs.getString("resolucion"));
					incidencias.add(incidencia);
				}
			}
			return incidencias;
		}
	}
	
	

	/**
	 * Metodo que permite insertar una incidencia que se quiere eliminar en la tabla IncidenciasEliminadas de la BBDD.
	 * @param incidenciaEliminada
	 * @throws SQLException
	 */
	public void insertarEliminada(IncidenciaEliminada incidenciaEliminada) throws SQLException {
		String sql = "INSERT INTO incidenciasEliminadas (codigo, puesto, problema, estado, fecha_eliminacion, causa_eliminacion, nombreUsuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql)) {
			sentencia.setString(1, incidenciaEliminada.getCodigo());
			sentencia.setInt(2, incidenciaEliminada.getPuesto());
			sentencia.setString(3, incidenciaEliminada.getProblema());
			sentencia.setString(4, incidenciaEliminada.getEstado().name());
			sentencia.setDate(5, Date.valueOf(incidenciaEliminada.getFechaEliminacion()));
			sentencia.setString(6, incidenciaEliminada.getCausaEliminacion());
			sentencia.setString(7, incidenciaEliminada.getNombreUsuario());
			sentencia.executeUpdate();
		}

	}

	/**
	 * Metodo que extrae la información de las incidencias eliminadas en la tabla IncidenciasEliminadas en la BBDD.
	 * @return lista de incidenciasEliminadas.
	 * @throws SQLException
	 */
	public List<IncidenciaEliminada> listarIncidenciasEliminadas() throws SQLException {
		List<IncidenciaEliminada> incidenciasEliminadas = new ArrayList<>();
		String sql = "SELECT * FROM incidenciasEliminadas";
		try (PreparedStatement sentencia = DatabaseConnection.getConexion().prepareStatement(sql);
				ResultSet rs = sentencia.executeQuery()) {
			
			// Iteramos sobre los resultados de la consulta y creamos objetos IncidenciaEliminada.
			while (rs.next()) {
				IncidenciaEliminada incidencia = new IncidenciaEliminada(rs.getString("codigo"), rs.getInt("puesto"),
						rs.getString("problema"), Estado.valueOf(rs.getString("estado")),
						rs.getDate("fecha_eliminacion").toLocalDate(), rs.getString("causa_eliminacion"), rs.getString("nombreUsuario"));

				incidenciasEliminadas.add(incidencia);
			}
		}
		return incidenciasEliminadas;
	}

}
