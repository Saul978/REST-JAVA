package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.mysql.cj.protocol.Resultset;

import entidades.Contacto;
import entidades.LoginEntidad;
import entidades.Usuario;
import util.Cripto;

public class LoginDao {
	public static LoginEntidad Login(String login,String password) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("select * from usuarios where login=? ");
			st.setString(1, login);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				LoginEntidad loginentidad = new LoginEntidad();
				loginentidad.setIdUsuario(rs.getInt("id"));
				if (Cripto.comprobarClave(password, rs.getString("pwd"))) {
					loginentidad.setLogin(true);
					return loginentidad;
				}else {
					loginentidad.setLogin(false);
					return loginentidad;
				}
			}

			
			
			rs.close();
			st.close();
			c.close();
			return null;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	public static String Autorizacion(int id,String clave) {
		try {

			Connection c  = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("select autorizaciones.codigo from autorizaciones inner join clavesapi on clavesapi.id=autorizaciones.idClave where idUsuario=? and clavesapi.codigo=?");
			st.setInt(1, id);
			st.setString(2, clave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}else {
				UUID random =UUID.randomUUID();
				String random2 = random.toString();
				System.out.println(random2);
				crearAutorizacion(id, clave, random2);
				return random2;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static void crearAutorizacion(int id,String clave,String random) {
		try {
			Connection c  = BD.abrirBD();
			int idclave=0;
			PreparedStatement st = c.prepareStatement("Select id from clavesapi where codigo=?");
			st.setString(1,clave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				idclave=rs.getInt("id");
				rs.close();
				st.close();
			}
			PreparedStatement st2 = c.prepareStatement("INSERT INTO autorizaciones(idUsuario, idClave, codigo) VALUES (?,?,?)");
			st2.setInt(1, id);
			st2.setInt(2, idclave);
			st2.setString(3, random);
			st2.executeUpdate();
			
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
}
