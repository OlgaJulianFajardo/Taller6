package controller;

import UserInterface.Interfaz;

/**
 * Clase principal de la aplicacion
 */
public class Main {

	/**
	 * Metodo principal de la aplicacion
	 * @param args
	 */
	public static void main(String[] args) {

		// Crea una nueva instancia de la interfaz y la inicia.
		Interfaz interfaz = new Interfaz();
		interfaz.iniciar();
	}
}
