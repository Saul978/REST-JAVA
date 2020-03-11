package bd;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entidades.Contacto;

public class ContactoDAO {

	public static Contacto carga(int id) {
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("select * from contactos where id=?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			Contacto u = null;
			if (rs.next()) {
				u = new Contacto();
				u.setId(rs.getInt("id"));
				u.setIdUsuario(rs.getInt("idUsuario"));
				u.setNombre(rs.getString("nombre"));
				u.setEmail(rs.getString("email"));
				u.setTelefono(rs.getString("telefono"));
				u.setFechaNacimiento(format.parse(rs.getString("fechaNacimiento")));
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
	public static List<Contacto> lista(int usuario) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement(
					"select * from contactos  where idUsuario =? order by nombre");
			st.setInt(1, usuario);
			ResultSet rs = st.executeQuery();
			List<Contacto> contactos = new ArrayList<Contacto>();
			while (rs.next()) {
				Contacto u = new Contacto();
				u.setId(rs.getInt("id"));
				u.setIdUsuario(rs.getInt("idUsuario"));
				u.setNombre(rs.getString("nombre"));
				u.setEmail(rs.getString("email"));
				u.setTelefono(rs.getString("telefono"));
				u.setFechaNacimiento(rs.getDate("fechaNacimiento"));
				contactos.add(u);
			}
			rs.close();
			st.close();
			c.close();
			return contactos;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Contacto inserta(Contacto u) {
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement(
					"insert into contactos(idUsuario,nombre,email,telefono,fechaNacimiento) values(?,?,?,?,now())", 
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, u.getIdUsuario());
			st.setString(2, u.getNombre());
			st.setString(3, u.getEmail());
			st.setString(4, u.getTelefono());
			
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
	
	public static Contacto actualiza(Contacto u) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement(
					"update contactos set nombre=?, email=?, telefono=?,fechaNacimiento=? where id=?");
			st.setInt(5, u.getId());
			st.setString(1, u.getNombre());
			st.setString(2, u.getEmail());
			st.setString(3, u.getTelefono());
			st.setString(4, "2000-12-12");
			st.executeUpdate();
			st.close();
			c.close();
			return u;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void borra(Contacto u) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("delete from contactos where id=?");
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
