package es.izquierdofp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpResponse;

import com.google.gson.reflect.TypeToken;

import es.izquierdofp.modelos.Contacto;
import es.izquierdofp.modelos.Usuario;
import es.izquierdofp.util.RestUtil;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class Contactos extends JFrame {

	private JPanel contentPane;
	private String autorizacion;
	private JList<Contacto> list;
	private JLabel usuariolbl;

	/**
	 * Create the frame.
	 */
	public Contactos(String autorizacion) {
		this.autorizacion=autorizacion;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 534, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		 list = new JList();
		list.setBounds(10, 11, 220, 341);
		contentPane.add(list);
		
		JButton btnNuevo = new JButton("Nuevo");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addContacto add = new addContacto(autorizacion);
				add.setModal(true);
				add.setVisible(true);
				if (add.getCierraOK()==true) {
					new ListadoWorker().execute();
				}
			}
		});
		btnNuevo.setBounds(240, 30, 268, 23);
		contentPane.add(btnNuevo);
		
		JButton btnNewButton = new JButton("Modificar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue()!=null) {
					modContacto mod = new modContacto(autorizacion,list.getSelectedValue().getId());
					mod.setModal(true);
					mod.setVisible(true);
					if (mod.getCierraOK()==true) {
						new ListadoWorker().execute();
					}
				}else {

					JOptionPane.showMessageDialog(Contactos.this, "Selecciona un contacto");
				}
				
			}
		});
		btnNewButton.setBounds(240, 88, 268, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Eliminar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BorrarWorker().execute();
			}
		});
		btnNewButton_1.setBounds(240, 143, 268, 23);
		contentPane.add(btnNewButton_1);
		
		 usuariolbl = new JLabel("");
		usuariolbl.setFont(new Font("Tahoma", Font.PLAIN, 17));
		usuariolbl.setBounds(242, 179, 262, 50);
		contentPane.add(usuariolbl);
		
		new ListadoWorker().execute();
	}
	
	private class ListadoWorker extends SwingWorker<List<Contacto>,Void> {
		@Override
		protected List<Contacto> doInBackground() throws Exception {
			HttpResponse res = RestUtil.get("/contactos",autorizacion);
			List<Contacto> contactos = RestUtil.leerJSON(res, new TypeToken<List<Contacto>>() {}.getType());
			return contactos;
		}
		
		@Override
		protected void done() {
			try {
				List<Contacto> contactos = get();
				DefaultListModel<Contacto> model = new DefaultListModel<Contacto>();
				for (Contacto c : contactos) {
					model.addElement(c);
				}
				list.setModel(model);
				list.clearSelection();
				new UsuarioWorker().execute();
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Contactos.this, "Error cargando contactos");
			}
		}
	}
	private class UsuarioWorker extends SwingWorker	<Usuario,Void> {
		@Override
		protected Usuario doInBackground() throws Exception {
			HttpResponse res = RestUtil.get("/Usuarios",autorizacion);
			Usuario usuario = RestUtil.leerJSON(res, Usuario.class);
			return usuario;
		}
		
		@Override
		protected void done() {
			try {
				Usuario usuario = get();
				usuariolbl.setText("Contactos de: "+usuario.getNombre());
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Contactos.this, "Error cargando usuario");
			}
		}
	}
	private class BorrarWorker extends SwingWorker<Void,Void> {
		@Override
		protected Void doInBackground() throws Exception {
			if (list.getSelectedValue() != null) {
				int id = list.getSelectedValue().getId();
				RestUtil.delete("/contacto/" + id,autorizacion);
			}
			return null;
		}
		
		@Override
		protected void done() {
			try {
				get();
				new ListadoWorker().execute();
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Contactos.this, "Error borrando usuario");
			}
		}
	}
}
