package main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bd.ContactoDAO;
import bd.UsuarioDAO;
import entidades.Contacto;
import entidades.Usuario;
import util.DatosPeticion;
import util.Util;

/**
 * Servlet implementation class contactos
 */
@WebServlet({"/contactos","/contacto/*"})
public class contactos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public contactos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatosPeticion dp = Util.comprobarSeguridad(request, response);
		if (dp == null) {
			return;
		}
			Usuario usuario = UsuarioDAO.carga(dp.getAutorizacion());
		
		
		if (request.getServletPath().contentEquals("/contactos")) {
			
			List<Contacto> contactos = ContactoDAO.lista(usuario.getId());
			for (Contacto contacto : contactos) {
				System.out.println(contacto.getNombre());
			}
			Util.escribirJSON(response, contactos);
		}
		else {
			int id = Integer.parseInt(request.getPathInfo().substring(1));
			
			Contacto u = ContactoDAO.carga(id);
			if (u == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
			else {
				Util.escribirJSON(response, u);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		DatosPeticion dp = Util.comprobarSeguridad(request, response);
		if (dp == null) {
			return;
		}
		Usuario usuario = UsuarioDAO.carga(dp.getAutorizacion());
		
		if (request.getServletPath().contentEquals("/contactos")) {
			Contacto c = Util.leerJSON(dp.getJson(), Contacto.class);
			c.setIdUsuario(usuario.getId());
			c = ContactoDAO.inserta(c);
			Util.escribirJSON(response, c);
		}
		else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatosPeticion dp = Util.comprobarSeguridad(request, response);
		if (dp == null) {
			return;
		}
		
		if (request.getServletPath().contentEquals("/contactos")) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		else {
			int id = Integer.parseInt(request.getPathInfo().substring(1));
			Contacto c = ContactoDAO.carga(id);
			if (c == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
			else {

				Contacto c2 = Util.leerJSON(dp.getJson(), Contacto.class);
				c.setNombre(c2.getNombre());
				c.setTelefono(c2.getTelefono());
				c.setEmail(c2.getEmail());
				//c.setFechaNacimiento(c2.getFechaNacimiento());
				
				c = ContactoDAO.actualiza(c);
				Util.escribirJSON(response, c);
			}
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatosPeticion dp = Util.comprobarSeguridad(request, response);
		if (dp == null) {
			return;
		}
		
		if (request.getServletPath().contentEquals("/contactos")) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		else {
			int id = Integer.parseInt(request.getPathInfo().substring(1));
			Contacto u = ContactoDAO.carga(id);
			if (u == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
			else {
				ContactoDAO.borra(u);
			}
		}
	}
}
