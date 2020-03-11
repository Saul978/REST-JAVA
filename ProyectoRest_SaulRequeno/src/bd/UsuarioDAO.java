package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entidades.Usuario;


public class UsuarioDAO {
	
	public static Usuario carga(int id) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("select * from usuarios where id=?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			Usuario u = null;
			if (rs.next()) {
				u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNombre(rs.getString("nombre"));
				u.setLogin(rs.getString("login"));
			}
			rs.close();
			st.close();
			c.close();
			return u;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	public static Usuario carga(String codigo) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("SELECT usuarios.* FROM usuarios inner join autorizaciones on autorizaciones.idUsuario=usuarios.id WHERE autorizaciones.codigo=?");
			st.setString(1, codigo);
			ResultSet rs = st.executeQuery();
			Usuario u = null;
			if (rs.next()) {
				u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNombre(rs.getString("nombre"));
				u.setLogin(rs.getString("login"));
			}
			rs.close();
			st.close();
			c.close();
			return u;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	public static List<Usuario> lista() {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement(
					"select * from usuarios order by login");
			ResultSet rs = st.executeQuery();
			List<Usuario> usuarios = new ArrayList<Usuario>();
			while (rs.next()) {
				Usuario u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNombre(rs.getString("nombre"));
				u.setLogin(rs.getString("login"));
				usuarios.add(u);
			}
			rs.close();
			st.close();
			c.close();
			return usuarios;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Usuario inserta(Usuario u) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement(
					"insert into usuarios(nombre,login,pwd) values(?,?,?)", 
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, u.getNombre());
			st.setString(2, u.getLogin());
			st.setString(3, u.getPwd());
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				u.setId(rs.getInt(1));
			}
			rs.close();
			st.close();
			c.close();
			return u;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Usuario actualiza(Usuario u) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement(
					"update usuarios set nombre=?, login=?, pwd=? where id=?");
			st.setString(1, u.getNombre());
			st.setString(2, u.getLogin());
			st.setString(3, u.getPwd());
			st.setInt(4, u.getId());
			st.executeUpdate();
			st.close();
			c.close();
			return u;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void borra(Usuario u) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("delete from usuarios where id=?");
			st.setInt(1, u.getId());
			st.executeUpdate();
			st.close();
			c.close();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
