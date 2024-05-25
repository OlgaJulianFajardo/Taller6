package persistence;

import java.time.LocalDate;
import java.util.Date;

import service.Estado;

/**
 * Clase que representa una incidencia.
 */
public class Incidencia {
	
	/** Codigo de la incidencia.  */
	private String codigo;
	/** Numero de puesto asociado a la incidencia.*/
    private int puesto;
    /** Descripcion del problema. */
    private String problema;
    /** Estado de la incidencia. */
    private Estado estado;
    /** Fecha de la resolucion de la incidencia. */
    private LocalDate fechaResolucion;
    /** Descripcion de la resolucion. */
    private String resolucion;
    /** Fecha de eliminacion de una incidencia.  */
    private Date fechaEliminacion;
    /** Causa de la eliminacion */
    private String causaEliminacion;
    
    
    /**
     * Constructor para crear una nueva incidencia. Inicialmente la incidencia se crea con estado 'pendiente'.
     * @param codigo  El código de la incidencia.
     * @param puesto  El número de puesto asociado a la incidencia.
     * @param problema La descripción del problema.
     */
    public Incidencia(String codigo, int puesto, String problema) {
        this.codigo = codigo;
        this.puesto = puesto;
        this.problema = problema;
        this.estado = Estado.PENDIENTE;
    }
    
    
	/**  @return codigo */
	public String getCodigo() {
		return codigo;
	}
	/**  @param codigo */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**  @return puesto  */
	public int getPuesto() {
		return puesto;
	}
	/** @param puesto */
	public void setPuesto(int puesto) {
		this.puesto = puesto;
	}
	/** @return problema  */
	public String getProblema() {
		return problema;
	}
	/** @param problema  */
	public void setProblema(String problema) {
		this.problema = problema;
	}
	/** @return estado  */
	public Estado getEstado() {
		return estado;
	}
	/** @param estado  */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	/** @return fechaResolucion  */
	public LocalDate getFechaResolucion() {
		return fechaResolucion;
	}
	/** @param fechaResolucion  */
	public void setFechaResolucion(LocalDate fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}
	/** @return resolucion  */
	public String getResolucion() {
		return resolucion;
	}
	/** @param resolucion  */
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	/** @return fechaEliminacion */
	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}
	/** @param fechaEliminacion */
	public void setFechaEliminacion(Date fechaEliminacion) {
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
    
	@Override
    public String toString() {
		StringBuilder cad = new StringBuilder();
		cad.append("\n\nIncidencia:\n") .append("Código: ") .append(codigo)
		.append("\nPuesto: ") .append(puesto)
		.append("\nProblema: ") .append(problema)
		.append("\nEstado: ") .append(estado);
		if (estado == Estado.RESUELTA) {
			cad.append("\nFecha resolución: ") .append(fechaResolucion)
			.append("\nResolución: ") .append(resolucion);
		}
		return cad.toString();
	}

}
