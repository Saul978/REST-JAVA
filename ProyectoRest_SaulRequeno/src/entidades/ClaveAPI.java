package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ClaveAPI {
	private int id;
	private String codigo;
	private String clave;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
}
