package es.izquierdofp.modelos;

import java.util.Date;

public class Contacto {
	private int id;
	private int idUsuario;
	private String nombre;
	private String email;
	private String telefono;
	private Date fechaNacimiento;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date date) {
		this.fechaNacimiento = date;
	}
	@Override
	public String toString() {
		return  nombre + " - " + email ;
	}
	
	
	
}
