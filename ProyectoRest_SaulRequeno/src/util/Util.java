package util;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bd.AutorizacionDAO;
import bd.ClaveApiDAO;
import bd.LoginDao;
import entidades.Autorizacion;
import entidades.ClaveAPI;

public class Util {

	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private static final int CINCO_MINUTOS = 5 * 60 * 1000;
	public static <T> T leerJSON(String json, Class<T> c) {
		return gson.fromJson(json, c);
	}
    
	public static <T> T leerJSON(String json, Type t) {
		return gson.fromJson(json, t);
	}
    
	public static void escribirJSON(HttpServletResponse response, Object obj) throws IOException {
		String json = gson.toJson(obj);
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	public static DatosPeticion comprobarSeguridad(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatosPeticion dp = new DatosPeticion();
		dp.setCodigoCliente(request.getHeader("X-CodigoCliente"));
		String signature = request.getHeader("X-Signature");
		
		long current = System.currentTimeMillis();
		
		
		String timeStamp = request.getHeader("X-TimeStamp");
		String autorizacion = request.getHeader("X-Autorizacion");
		
		
		if (dp.getCodigoCliente() == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		ClaveAPI cliente = ClaveApiDAO.carga(dp.getCodigoCliente());
		if (cliente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;			
		}
		
		if (autorizacion== null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}else {
			Autorizacion autor = AutorizacionDAO.carga(autorizacion);
			if (autor.getIdClave()==cliente.getId()) {
				dp.setAutorizacion(autor.getCodigo());
			}else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		
		if(timeStamp==null) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			return null;
		}else {
			Long diff = current-Long.valueOf(timeStamp);
			System.out.println(diff);
			if (diff>CINCO_MINUTOS) {
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				return null;
			}
		}
		dp.setIdCliente(cliente.getId());

		// Construir mensaje para calcular firma
		StringBuilder sb = new StringBuilder();
		// Linea 1 - Metodo + URL
		sb.append(request.getMethod()).append(" ");
		sb.append(request.getServletPath());
		if (request.getPathInfo() != null) {
			sb.append(request.getPathInfo());
		}
		sb.append("\n");
		// Linea 2 - Codigo Cliente
		sb.append(dp.getCodigoCliente()).append("\n");
		//Linea  2.2 -autorizacion y timestamp
		sb.append(autorizacion).append("\n");
		sb.append(timeStamp).append("\n");
		// Linea 3 - Contenido petición
		if ("POST".contentEquals(request.getMethod()) ||
			"PUT".contentEquals(request.getMethod())) {
			dp.setJson(leerReader(request.getReader()));
			sb.append(dp.getJson()).append("\n");
		}
		
		String firma = Cripto.calcularMac(sb.toString(), cliente.getClave());
		if (!signature.equals(firma)) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			return null;	
		}
				
		return dp;
	}
	
	public static DatosPeticion comprobarSeguridadLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatosPeticion dp = new DatosPeticion();
		dp.setCodigoCliente(request.getHeader("X-CodigoCliente"));
		String signature = request.getHeader("X-Signature");
		String timeStamp = request.getHeader("X-TimeStamp");
		

		long current = System.currentTimeMillis();
		
		
		if (dp.getCodigoCliente() == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		ClaveAPI cliente = ClaveApiDAO.carga(dp.getCodigoCliente());
		if (cliente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;			
		}
		
		if(timeStamp==null) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			return null;
		}else {
			Long diff = current-Long.valueOf(timeStamp);
			System.out.println(diff);
			if (diff>CINCO_MINUTOS) {
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				return null;
			}
		}
		
		dp.setIdCliente(cliente.getId());

		// Construir mensaje para calcular firma
		StringBuilder sb = new StringBuilder();
		// Linea 1 - Metodo + URL
		sb.append(request.getMethod()).append(" ");
		sb.append(request.getServletPath());
		if (request.getPathInfo() != null) {
			sb.append(request.getPathInfo());
		}
		sb.append("\n");
		// Linea 2 - Codigo Cliente
		sb.append(dp.getCodigoCliente()).append("\n");
		sb.append(timeStamp).append("\n");
		// Linea 3 - Contenido petición
		if ("POST".contentEquals(request.getMethod()) ||
			"PUT".contentEquals(request.getMethod())) {
			dp.setJson(leerReader(request.getReader()));
			sb.append(dp.getJson()).append("\n");
		}
		
		String firma = Cripto.calcularMac(sb.toString(), cliente.getClave());
		if (!signature.equals(firma)) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			return null;	
		}
				
		return dp;
	}
	public static String leerReader(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		char[] chars = new char[1024];
		int n;
		while ((n = reader.read(chars)) > 0) {
			sb.append(chars, 0, n);
		}
		return sb.toString();
	}

	

}
