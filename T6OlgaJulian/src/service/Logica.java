package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import UserInterface.Interfaz;
import persistence.Incidencia;
import persistence.IncidenciaDAO;
import persistence.IncidenciaEliminada;

/**
 * Clase que contiene la lógica de negocio para la gestión de incidencias.
 */
public class Logica {

	private IncidenciaDAO incidenciaDao;
	Scanner sc = new Scanner(System.in);

	/**
	 * Constructor de la clase Logica.
	 */
	public Logica() {
		this.incidenciaDao = new IncidenciaDAO();
	}

	/**
	 * Metodo que genera un codigo unico para la incidencia.Da formato al codigo y llama a los metodos
	 * obtenerUltimoNumero y esMismoDia para comprobar qué número entero es el que le tiene que dar
	 * al codigo de incidencia.
	 * 
	 * @return codigo generado.
	 */
	private String generarCodigo() {
		LocalDate ahora = LocalDate.now();
		LocalTime horaActual = LocalTime.now();
		String codigoFechaHora = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-"
				+ horaActual.format(DateTimeFormatter.ofPattern("HH:mm"));

		// Llamada al codigo para comprobar el numero entero necesario para el codigo de
		// incidencia
		int ultimoNumero = incidenciaDao.obtenerUltimoNumero(ahora);
		int nuevoNumero = 1;

		try {
			// Llamada al metodo para comprobar si es el mismo dia que el dia de la ultima
			// incidencia registrada
			// (He creado una nueva tabla para tener el control de esto)
			if (ultimoNumero == 0 || !incidenciaDao.esMismoDia(ahora)) {
				ultimoNumero = 1;
			} else {
				nuevoNumero = ultimoNumero + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Llamada al metodo para guardar la fecha y el numero de la incidencia generada
		// (sirve para llevar el control del numero entero del codigo de las
		// incidencias)
		incidenciaDao.guardarUltimoNumero(ahora, nuevoNumero);

		return codigoFechaHora + "-" + nuevoNumero;
	}

	/**
	 * Metodo para registrar una nueva incidencia. 
	 * 
	 * @param sc el scanner para leer la entrada del usuario.
	 * @return true si la incidencia se registró con éxito, false en caso contrario.
	 */
	public boolean registrarIncidencia(Scanner sc) {
		boolean registro = false;
		String codigo = generarCodigo();
		int puesto = 0;

		puesto = verificarPuesto(codigo);
		System.out.println("Ingrese la descripción del problema:");
		String problema = sc.nextLine();
		Incidencia incidencia = new Incidencia(codigo, puesto, problema);
		try {
			incidenciaDao.insertar(incidencia);
			System.out.println("Incidencia generada con éxito:");
			System.out.println(incidencia);
			registro = true;
		} catch (SQLException e) {
			e.printStackTrace();
			registro = false;
		}
		return registro;
	}

	/**
	 * Metodo para verificar que la entrada del usuario es un numero entero.
	 * 
	 * @param codigo
	 * @return
	 */
	public int verificarPuesto(String codigo) {
		int puesto = 0;
		boolean entradaValida = false; // Variable para controlar la validez de la entrada del usuario
		do {
			System.out.println("\nIngrese el número del puesto:");
			try {
				puesto = Integer.parseInt(sc.nextLine());
				entradaValida = true;
			} catch (NumberFormatException e) {
				System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
			}
		} while (!entradaValida);
		return puesto;
	}

	/**
	 * Metodo que permite buscar una incidencia.
	 * 
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public Incidencia buscarIncidencia(String codigo) throws SQLException {
		return incidenciaDao.buscarIncidencia(codigo);
	}

	

	/**
	 * Meetodo que permite modificar una incidencia pendiente. Se modifica el puesto
	 * y la descripcion.
	 * 
	 * @param sc el scanner para leer la entrada del usuario.
	 * @return true si la incidencia se modifica con éxito, false en caso contrario.
	 */
	public boolean modificarIncidenciaPendiente(Scanner sc) {
		System.out.println("Ingrese el código de la incidencia a modificar:");
		String codigo = sc.nextLine();
		boolean modificada = false;
		try {
			// Llamada al metodo para buscar la incidencia. A continuación se comprueba si
			// el estado de la
			// incidencia encontrada es 'pendiente'. Si lo es, permite modificar los
			// atributos.
			Incidencia incidencia = incidenciaDao.buscarIncidencia(codigo);
			if (incidencia == null || incidencia.getEstado() != Estado.PENDIENTE) {
				System.out.println("\nLa incidencia no se ha encontrado o no está pendiente.");
				modificada = false;
			}

			// Si se encuentra y está pendiente, permite modificar los atributos
			System.out.println("\nLos detalles actuales de la incidencia son:");
			System.out.println(incidencia);

			int puesto = verificarPuesto(codigo);
			System.out.println("Ingrese la nueva descripción del problema:");
			String problema = sc.nextLine();

			incidencia.setPuesto(puesto);
			incidencia.setProblema(problema);
			incidenciaDao.update(incidencia);

			System.out.println("Incidencia modificada: ");
			System.out.println(incidencia);
			modificada = true;
		} catch (SQLException e) {
			e.printStackTrace();
			modificada = false;
		}
		return modificada;
	}

	/**
	 * Metodo que permite resolver una incidencia.
	 * 
	 * @param sc el scanner para leer la entrada del usuario.
	 * @return true si la incidencia se modifica con éxito, false en caso contrario.
	 */
	public boolean resolverIncidencia(Scanner sc) {
		System.out.println("Ingrese el código de la incidencia a resolver:");
		String codigo = sc.nextLine();
		try {
			// Llamada al metodo de buscar incidencia(por codigo). Se comprueba si el estado
			// de la incidencia
			// es 'pendiente', si lo es, permite resolver la incidencia.
			Incidencia incidencia = incidenciaDao.buscarIncidencia(codigo);
			if (incidencia == null || incidencia.getEstado() != Estado.PENDIENTE) {
				System.out.println("Incidencia no encontrada o no está pendiente.");
				return false;
			}
			System.out.println("Ingrese la descripción de la resolución:");
			String resolucion = sc.nextLine();

			LocalDate fechaResolucion = solicitarFechaValida(sc);

			incidencia.setResolucion(resolucion);
			incidencia.setFechaResolucion(fechaResolucion);
			incidencia.setEstado(Estado.RESUELTA);
			incidenciaDao.update(incidencia);
			System.out.println("Incidencia resuelta: ");
			System.out.println(incidencia);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo que permite modificar una incidencia ya resuelta.
	 * 
	 * @param sc el scanner para leer la entrada del usuario.
	 * @return true si la incidencia se modifica con éxito, false en caso contrario.
	 */
	public boolean modificarIncidenciaResuelta(Scanner sc) {
		System.out.println("Ingrese el código de la incidencia a modificar:");
		String codigo = sc.nextLine();
		try {
			// Busca la incidencia y comprueba si el estado es 'resuelta'.
			Incidencia incidencia = incidenciaDao.buscarIncidencia(codigo);
			if (incidencia == null || incidencia.getEstado() != Estado.RESUELTA) {
				System.out.println("Incidencia no encontrada o no está resuelta.");
				return false;
			}
			System.out.println("Los detalles actuales de la incidencia son: ");
			System.out.println(incidencia);
			System.out.println("Ingrese la nueva descripción de la resolución:");
			String resolucion = sc.nextLine();

			LocalDate fechaResolucion = solicitarFechaValida(sc);

			incidencia.setResolucion(resolucion);
			incidencia.setFechaResolucion(fechaResolucion);
			incidenciaDao.update(incidencia);
			System.out.println("Incidencia modificada: ");
			System.out.println(incidencia);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo para verificar que el formato de fecha introducido es válido.
	 * 
	 * @param sc
	 * @return
	 */
	private LocalDate solicitarFechaValida(Scanner sc) {
		LocalDate fecha = null;
		boolean fechaValida;
		do {
			System.out.println("Ingrese la fecha (dd/MM/yyyy):");
			String fechaStr = sc.nextLine();

			try {
				fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				fechaValida = true; // Si la conversión no lanza una excepción, la fecha es válida

			} catch (DateTimeParseException e) {
				System.out.println("Formato de fecha incorrecto. Inténtelo de nuevo.");
				fechaValida = false;
			}
		} while (!fechaValida);

		return fecha;
	}

	/**
	 * Metodo que permite devolver una incidencia ya resuelta al estado de
	 * 'pendiente' de nuevo.
	 * 
	 * @param sc el scanner para leer la entrada del usuario.
	 * @return true si la incidencia se modifica con éxito, false en caso contrario.
	 */
	public boolean devolverIncidenciaPendiente(Scanner sc) {
		System.out.println("Ingrese el código de la incidencia a devolver a pendiente:");
		String codigo = sc.nextLine();
		try {
			// Comrpueba si el estado es 'resuelta'.
			Incidencia incidencia = incidenciaDao.buscarIncidencia(codigo);
			if (incidencia == null || incidencia.getEstado() != Estado.RESUELTA) {
				System.out.println("Incidencia no encontrada o no está resuelta.");
				return false;
			}
			System.out.println("Los detalles actuales de la incidencia son: ");
			System.out.println(incidencia);
			incidencia.setResolucion(null);
			incidencia.setFechaResolucion(null);
			incidencia.setEstado(Estado.PENDIENTE);
			incidenciaDao.update(incidencia);
			System.out.println("Incidencia devuelta a 'pendiente' exitosamente. ");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo que permite eliminar una incidencia. Las incidencias que son
	 * eliminadas se buscan en la base de datos, si el estado está como 'pendiente'
	 * se pasa a 'eliminada', además, se añade esta incidencia a una nueva tabla de
	 * la base de datos (incidenciasEliminadas) y se elimina de la tabla 'original'.
	 * 
	 * @param codigo
	 * @throws SQLException
	 */
	// Con esto se consigue practicar la extracción de datos, inserción y
	// eliminación en la base de datos.
	// Además, se consigue mantener un histórico de las incidencias que han sido
	// eliminadas.
	public void eliminarIncidencia(String codigo) throws SQLException {
		Incidencia incidencia = incidenciaDao.buscarIncidencia(codigo);
		if (incidencia != null && incidencia.getEstado() == Estado.PENDIENTE) {
			System.out.println("Los detalles actuales de la incidencia son: ");
			System.out.println(incidencia);
			LocalDate fechaEliminacion = LocalDate.now();
			System.out.println("\nIngrese la causa de eliminación: ");
			String causaEliminacion = sc.nextLine();
			incidencia.setEstado(Estado.ELIMINADA);
			String nombreUsuario = obtenerNombreUsuario();

			IncidenciaEliminada incidenciaEliminada = new IncidenciaEliminada(incidencia.getCodigo(),
					incidencia.getPuesto(), incidencia.getProblema(), incidencia.getEstado(), fechaEliminacion,
					causaEliminacion, nombreUsuario);

			incidenciaDao.insertarEliminada(incidenciaEliminada);
			incidenciaDao.eliminar(codigo);
			System.out.println("Incidencia eliminada con éxito. ");
		} else {
			System.out.println("No se encontró la incidencia o la incidencia no está pendiente.");
		}
	}

	/**
	 * Metodo que permite mostrar un listado de las incidencias dependiendo del
	 * estado.
	 * 
	 * @param estado
	 */
	public void mostrarIncidenciasPorEstado(Estado estado) {
		try {
			List<Incidencia> incidencias = incidenciaDao.listarIncidenciasPorEstado(estado);
			if (incidencias.isEmpty()) {
				System.out.println("No hay incidencias con el estado " + estado);
			} else {
				if (estado == Estado.RESUELTA) {
					Interfaz.menuOrdenarResueltas();
					int opcion = Integer.parseInt(sc.nextLine());
					switch (opcion) {
					case 1:
						incidencias.sort(Comparator.comparing(Incidencia::getCodigo));
						break;
					case 2:
						incidencias.sort(Comparator.comparing(Incidencia::getFechaResolucion));
						break;
					default:
						System.out.println("Opción no válida. Mostrando con el orden por defecto.");
						break;
					}
				}
				for (Incidencia incidencia : incidencias) {
					System.out.println(incidencia);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que busca en la tabla de incidencias eliminadas y muestra el listado
	 * de las incidencias
	 */
	public void mostrarIncidenciasEliminadas() {
		try {
			List<IncidenciaEliminada> incidenciasEliminadas = incidenciaDao.listarIncidenciasEliminadas();
			if (incidenciasEliminadas.isEmpty()) {
				System.out.println("No hay incidencias eliminadas.");
			} else {
				Interfaz.menuOrdenarEliminadas();
				int opcion = Integer.parseInt(sc.nextLine());
				switch (opcion) {
				case 1:
					incidenciasEliminadas.sort(Comparator.comparing(IncidenciaEliminada::getCodigo));
					break;
				case 2:
					incidenciasEliminadas.sort(Comparator.comparing(IncidenciaEliminada::getFechaEliminacion));
					// si usara Collections.sort (de la clase Collections, y se utiliza para ordenar
					// cualquier lista que implemente la interfaz List. No es necesario tener acceso
					// al ArrayList en sí para utilizar este método), se implementaría:
					// Collections.sort(incidenciasEliminadas, Comparator.comparing(IncidenciaEliminada::getCodigo));
					break;
				default:
					System.out.println("Opción no válida. Se mostrarán las incidencias con el orden por defecto.");
					break;
				}
				for (IncidenciaEliminada incidencia : incidenciasEliminadas) {
					System.out.println(incidencia);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Metodo para obtener el nombre de usuario actual(del sistema operativo)
	 * @return nombre de usuario
	 */
	private String obtenerNombreUsuario() {
		String nombreUsuario = System.getProperty("user.name");
		return nombreUsuario;
	}

}
