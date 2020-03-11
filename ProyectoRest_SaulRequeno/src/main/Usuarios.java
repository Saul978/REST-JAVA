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
 * Servlet implementation class Usuarios
 */
@WebServlet({"/Usuarios","/Usuario/*"})
public class Usuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Usuarios() {
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
		
		
		if (request.getServletPath().contentEquals("/Usuarios")) {

			Usuario usuario = UsuarioDAO.carga(dp.getAutorizacion());

			Util.escribirJSON(response, usuario);
		}
		else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

}
