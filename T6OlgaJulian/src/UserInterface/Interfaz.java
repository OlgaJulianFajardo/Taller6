package UserInterface;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.DatabaseConnection;
import persistence.Incidencia;
import service.Estado;
import service.Logica;

/**
 * Clase para la interaccion con el usuario.
 */
public class Interfaz {

	private Logica logica;
	private Scanner sc = new Scanner(System.in);

	/**
	 * Constructor de la clase.
	 */
	public Interfaz() {
		logica = new Logica();
	}

	/**
	 * Método que imprime bienvenida
	 */
	public static void imprimirBienvenida() {
		System.out.println("*******************************");
		System.out.println("REGISTRO DE INCIDENCIAS");
		System.out.println("*******************************");
		String nombreUsuario = System.getProperty("user.name");
		System.out.println(
				"\n¡Hola "+ nombreUsuario+ "!\nBienvenido/a a la aplicación de registro de incidencias.\nUse las opciones del menú para trabajar.\n");
	}

	/**
	 * Inicia la interacción con el usuario, iniciando y cerrando la conexion con la
	 * base de datos.
	 */
	public void iniciar() {
		DatabaseConnection.abrirConexion();
		imprimirBienvenida();
		int opcion = 0;
		do {
			mostrarMenu();
			String input = sc.nextLine();
			if (!input.isEmpty()) {
				try {
					opcion = Integer.parseInt(input);
					switch (opcion) {
					case 1:
						logica.registrarIncidencia(sc);
						break;
					case 2:
						buscarIncidencia();
						break;
					case 3:
						logica.modificarIncidenciaPendiente(sc);
						break;
					case 4:
						logica.resolverIncidencia(sc);
						break;
					case 5:
						logica.modificarIncidenciaResuelta(sc);
						break;
					case 6:
						logica.devolverIncidenciaPendiente(sc);
						break;
					case 7:
						System.out.print("Ingrese el código de la incidencia a eliminar: ");
						String codigoEliminar = sc.nextLine();
						try {
							logica.eliminarIncidencia(codigoEliminar);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case 8:
						logica.mostrarIncidenciasPorEstado(Estado.PENDIENTE);
						break;
					case 9:
						logica.mostrarIncidenciasPorEstado(Estado.RESUELTA);
						break;
					case 10:
						logica.mostrarIncidenciasEliminadas();
						break;
					case 11:
						System.out.println("Gracias por usar la aplicación.");
						try {
							DatabaseConnection.closeConnection();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					default:
						System.out.println("Opción no válida. Ingrese un número del 1-11.");
						break;
					}
				} catch (NumberFormatException e) {
					System.out.println("Opción no válida. Ingrese un número del 1-11.");
				}
			} else {
				System.out.println("Opción no válida. Ingrese un número del 1-11.");
			}
		} while (opcion != 11);
	}

	/**
	 * Metodo que muestra el menu principal de la aplicacion.
	 */
	private void mostrarMenu() {
		System.out.println("\nSeleccione una opción:");
		System.out.println("1. Registrar nueva incidencia");
		System.out.println("2. Buscar incidencia");
		System.out.println("3. Modificar incidencia pendiente");
		System.out.println("4. Resolver incidencia");
		System.out.println("5. Modificar incidencia resuelta");
		System.out.println("6. Devolver incidencia resuelta a pendiente");
		System.out.println("7. Eliminar incidencia");
		System.out.println("8. Mostrar incidencias pendientes");
		System.out.println("9. Mostrar incidencias resueltas");
		System.out.println("10. Mostrar incidencias eliminadas");
		System.out.println("11. Salir");
	}

	/**
	 * Metodo auxiliar para buscar una incidencia, preguntando al usuario el codigo
	 * de incidencia.
	 */
	private void buscarIncidencia() {
		System.out.println("Ingrese el código de la incidencia a buscar:");
		String codigo = sc.nextLine().trim(); // El trim() elimina los espacios en blanco al principio y al final

		// Una vez que se ha ingresado un código de incidencia válido, continuar con la
		// búsqueda de incidencia
		try {
			Incidencia incidencia = logica.buscarIncidencia(codigo);
			if (incidencia != null) {
				System.out.println("Los detalles de la incidencia son: ");
				System.out.println(incidencia);
			} else {
				System.out.println("Incidencia no encontrada.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void menuOrdenarResueltas() {
		System.out.println("¿Cómo desea ordenar las incidencias resueltas?");
		System.out.println("1. Por código de incidencia.");
		System.out.println("2. Por fecha de resolución.");
	}
	
	
	public static void menuOrdenarEliminadas() {
		System.out.println("¿Cómo desea ordenar las incidencias eliminadas?");
		System.out.println("1. Por código de incidencia.");
		System.out.println("2. Por fecha de eliminación.");
	}

}
