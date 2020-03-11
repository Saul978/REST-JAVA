package es.izquierdofp.util;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestUtil {
	//tabla claves api
	private static final String CODIGO_CLIENTE = "cliente1-4f45-11ea-9559-c48e8ff4fa4a";
	private static final String CLAVE_FIRMA = "4232525f-d7de-4963-8833-3f5f868551eb";
	private static final String BASE_URL = "http://localhost:8080/ProyectoRest_SaulRequeno";
	
	private static String authorization;
	
	private static HttpClient client = HttpClientBuilder.create().build();
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	
	public static HttpResponse get(String url,String Autorizacion) throws Exception {
		return call("GET", url, null,Autorizacion);
	}
	
	public static HttpResponse post(String url, Object obj,String Autorizacion) throws Exception {
		return call("POST", url, obj,Autorizacion);
	}
	public static HttpResponse post(String url, Object obj) throws Exception {
		return call("POST", url, obj);
	}
	public static HttpResponse put(String url, Object obj,String Autorizacion) throws Exception {
		return call("PUT", url, obj,Autorizacion);
	}
	
	public static HttpResponse delete(String url,String Autorizacion) throws Exception {
		return call("DELETE", url, null,Autorizacion);
	}
	
	private static HttpResponse call(String method, String url, Object contenido,String aut) throws Exception {
		String json = (contenido == null) ? null : gson.toJson(contenido);
		HttpRequestBase request = null;
		switch (method) {
		case "GET":
			request = new HttpGet(BASE_URL + url);
			break;
		case "POST":
			HttpPost post = new HttpPost(BASE_URL + url);
			post.setEntity(new StringEntity(json));
			request = post;
			break;
		case "PUT":
			HttpPut put = new HttpPut(BASE_URL + url);
			put.setEntity(new StringEntity(json));
			request = put;
			break;
		case "DELETE":
			request = new HttpDelete(BASE_URL + url);
			break;
		}
		long tiempo = System.currentTimeMillis();
		
		request.addHeader("X-CodigoCliente", CODIGO_CLIENTE);
		request.addHeader("X-Autorizacion",aut);
		request.addHeader("X-TimeStamp",String.valueOf(tiempo));
		StringBuilder sb = new StringBuilder();
		sb.append(method).append(" ");
		sb.append(url).append("\n");
		sb.append(CODIGO_CLIENTE).append("\n");
		sb.append(aut).append("\n");
		sb.append(tiempo).append("\n");
		if (json != null) {
			sb.append(json).append("\n");
		}

		request.addHeader("X-Signature", calcularFirma(sb.toString()));
		
		return client.execute(request);
	}
	
	private static HttpResponse call(String method, String url, Object contenido) throws Exception {
		String json = (contenido == null) ? null : gson.toJson(contenido);
		HttpRequestBase request = null;
		switch (method) {
		case "GET":
			request = new HttpGet(BASE_URL + url);
			break;
		case "POST":
			HttpPost post = new HttpPost(BASE_URL + url);
			post.setEntity(new StringEntity(json));
			request = post;
			break;
		case "PUT":
			HttpPut put = new HttpPut(BASE_URL + url);
			put.setEntity(new StringEntity(json));
			request = put;
			break;
		case "DELETE":
			request = new HttpDelete(BASE_URL + url);
			break;
		}
		long tiempo = System.currentTimeMillis();
		request.addHeader("X-CodigoCliente", CODIGO_CLIENTE);
		request.addHeader("X-TimeStamp",String.valueOf(tiempo));
		StringBuilder sb = new StringBuilder();
		sb.append(method).append(" ");
		sb.append(url).append("\n");
		sb.append(CODIGO_CLIENTE).append("\n");
		sb.append(tiempo).append("\n");
		if (json != null) {
			sb.append(json).append("\n");
		}

		request.addHeader("X-Signature", calcularFirma(sb.toString()));
		
		return client.execute(request);
	}
	
	private static String calcularFirma(String msg) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(CLAVE_FIRMA.getBytes(), "HmacSHA256"));
			byte[] hash = mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
			return Hex.hexString(hash);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static <T> T leerJSON(HttpResponse res, Class<T> class1) {
		try {
			String json = EntityUtils.toString(res.getEntity());
			return gson.fromJson(json, class1);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static <T> T leerJSON(HttpResponse res, Type t) {
		try {
			if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception("ERROR " + res.getStatusLine().getStatusCode());
			}
			String json = EntityUtils.toString(res.getEntity());
			return gson.fromJson(json, t);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
