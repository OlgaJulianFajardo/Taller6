package persistence;

import java.time.LocalDate;

import service.Estado;

/**
 * Clase que representa una incidencia eliminada.
 */
public class IncidenciaEliminada {
	/** Código de la incidencia eliminada	 */
	private String codigo;
	/** Número de puesto asociado a la incidencia eliminada */
	private int puesto;
	/** Descripción del problema */
	private String problema;
	/** Estado de la incidencia (debe ser ELIMINADA) */
	private Estado estado;
	/** Fecha de eliminación de la incidencia */
	private LocalDate fechaEliminacion;
	/** Causa de la eliminación */
	private String causaEliminacion;
	/** nombre de usuario actual */
	private String nombreUsuario;

	/**
	 * onstructor para crear una nueva incidencia eliminada.
	 * @param codigo El código de la incidencia
	 * @param puesto El número de puesto asociado a la incidencia.
	 * @param problema La descripción del problema.
	 * @param estado El estado de la incidencia.
	 * @param fechaEliminacion La fecha de eliminación de la incidencia.
	 * @param causaEliminacion La causa de eliminación de la incidencia.
	 */
	public IncidenciaEliminada(String codigo, int puesto, String problema, Estado estado, LocalDate fechaEliminacion,
			String causaEliminacion, String nombreUsuario) {
		this.codigo = codigo;
		this.puesto = puesto;
		this.problema = problema;
		this.estado = estado;
		this.fechaEliminacion = fechaEliminacion;
		this.causaEliminacion = causaEliminacion;
		this.nombreUsuario = nombreUsuario;
	}

	/**  @return codigo  */
	public String getCodigo() {
		return codigo;
	}

	/** @param codigo  */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/** @return puesto  */
	public int getPuesto() {
		return puesto;
	}

	/** @param puesto */
	public void setPuesto(int puesto) {
		this.puesto = puesto;
	}

	/** @return problema */
	public String getProblema() {
		return problema;
	}

	/**  @param problema */
	public void setProblema(String problema) {
		this.problema = problema;
	}

	/** @return estado */
	public Estado getEstado() {
		return estado;
	}

	/** @param estado */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	/** @return fechaEliminacion */
	public LocalDate getFechaEliminacion() {
		return fechaEliminacion;
	}

	/** @param fechaEliminacion  */
	public void setFechaEliminacion(LocalDate fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}

	/** @return causaEliminacion */
	public String getCausaEliminacion() {
		return causaEliminacion;
	}

	/** @param causaEliminacion */
	public void setCausaEliminacion(String causaEliminacion) {
		this.causaEliminacion = causaEliminacion;
	}
	
	public String getNombreUsuario() {
	    return nombreUsuario;
	}
	
	public void setNombreUsuario(String nombreUsuario) {
	    this.nombreUsuario = nombreUsuario;
	}
	
	@Override
    public String toString() {
		StringBuilder cad = new StringBuilder();
		cad.append("\n\nIncidencia:\n") .append("Código: ") .append(codigo)
		.append("\nPuesto: ") .append(puesto)
		.append("\nProblema: ") .append(problema)
		.append("\nEstado: ") .append(estado)
		.append("\nFecha eliminación: ") .append(fechaEliminacion)
		.append("\nCausa Eliminación: ") .append(causaEliminacion)
		.append("\nUsuario que ha eliminado la incidencia: ") .append(nombreUsuario); 
		return cad.toString();
	}

}
