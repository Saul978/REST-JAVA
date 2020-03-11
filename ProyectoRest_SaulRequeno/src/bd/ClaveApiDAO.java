package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entidades.ClaveAPI;


public class ClaveApiDAO {
	public static ClaveAPI carga(String codigo) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("select * from clavesapi where codigo=?");
			st.setString(1, codigo);
			ResultSet rs = st.executeQuery();
			ClaveAPI ca = null;
			if (rs.next()) {
				ca = new ClaveAPI();
				ca.setId(rs.getInt("id"));
				ca.setCodigo(rs.getString("codigo"));
				ca.setClave(rs.getString("clave"));
			}
			rs.close();
			st.close();
			c.close();
			return ca;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}}
