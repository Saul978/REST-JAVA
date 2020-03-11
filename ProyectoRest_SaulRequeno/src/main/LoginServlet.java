package main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bd.LoginDao;
import bd.UsuarioDAO;
import entidades.LoginEntidad;
import entidades.Usuario;
import util.Cripto;
import util.DatosPeticion;
import util.Util;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatosPeticion dp = Util.comprobarSeguridadLogin(request, response);
		if (dp == null) {
			return;
		}
		
		if (request.getServletPath().contentEquals("/LoginServlet")) {
			Usuario u = Util.leerJSON(dp.getJson(), Usuario.class);
			 LoginEntidad login = LoginDao.Login(u.getLogin(),u.getPwd());
			if (login.isLogin()==true) {

				String Autorizacion = LoginDao.Autorizacion(login.getIdUsuario(),request.getHeader("X-CodigoCliente"));
				response.addHeader("X-Autorizacion", Autorizacion);
				
			}else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

}
