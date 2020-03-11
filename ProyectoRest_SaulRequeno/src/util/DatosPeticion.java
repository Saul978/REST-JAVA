package util;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DatosPeticion {
	//añadir time stamp , y demas
	private String json;
	private String codigoCliente;
	private Date timeStamp;
	private String Autorizacion;
	private int idCliente;
	
	
	
	public String getAutorizacion() {
		return Autorizacion;
	}
	public void setAutorizacion(String autorizacion) {
		Autorizacion = autorizacion;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	
}
