package es.izquierdofp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpResponse;

import com.google.gson.reflect.TypeToken;

import es.izquierdofp.modelos.Contacto;
import es.izquierdofp.util.RestUtil;

public class modContacto extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String autorizacion;
public boolean cierraOK=false;
private JPanel contentPane;
private JTextField Nombretxt;
private JTextField emailtxt;
private JTextField Telefonotxt;
private JTextField fechatxt;
	private int id;
	public boolean getCierraOK() {
		return cierraOK;
	}
	
	public modContacto(String autorizacion,int id) {
		this.autorizacion=autorizacion;
		this.id= id;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre: ");
		lblNombre.setBounds(31, 34, 68, 14);
		contentPane.add(lblNombre);
		
		Nombretxt = new JTextField();
		Nombretxt.setBounds(125, 31, 167, 20);
		contentPane.add(Nombretxt);
		Nombretxt.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email: ");
		lblEmail.setBounds(31, 95, 68, 14);
		contentPane.add(lblEmail);
		
		emailtxt = new JTextField();
		emailtxt.setBounds(124, 92, 168, 20);
		contentPane.add(emailtxt);
		emailtxt.setColumns(10);
		
		JLabel lblTelefono = new JLabel("Telefono: ");
		lblTelefono.setBounds(31, 160, 68, 14);
		contentPane.add(lblTelefono);
		
		Telefonotxt = new JTextField();
		Telefonotxt.setBounds(125, 157, 167, 20);
		contentPane.add(Telefonotxt);
		Telefonotxt.setColumns(10);
		
		JLabel lblFechaNacimiento = new JLabel("Fecha Nacimiento:");
		lblFechaNacimiento.setBounds(10, 214, 89, 14);
		contentPane.add(lblFechaNacimiento);
		
		fechatxt = new JTextField();
		fechatxt.setBounds(125, 208, 167, 20);
		contentPane.add(fechatxt);
		fechatxt.setColumns(10);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new GuardarWorker().execute();
			}
		});
		btnAceptar.setBounds(335, 227, 89, 23);
		contentPane.add(btnAceptar);
		new CargarWorker().execute();
	}
	
	private class CargarWorker extends SwingWorker<Contacto,Void> {
		@Override
		protected Contacto doInBackground() throws Exception {
			System.out.println(id);
			HttpResponse res = RestUtil.get("/contacto/" + id,autorizacion);
			Contacto contacto = RestUtil.leerJSON(res, Contacto.class);
			return contacto;
		}
		
		@Override
		protected void done() {
			try {
				 Contacto contacto = get();
				 Nombretxt.setText(contacto.getNombre());
				 emailtxt.setText(contacto.getEmail());
				 fechatxt.setText(contacto.getFechaNacimiento().toString());
				 Telefonotxt.setText(contacto.getTelefono());
				 
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(modContacto.this, "Error cargando contacto");
			}
		}
	}
	
	
	private class GuardarWorker extends SwingWorker<Contacto,Void> {
		@Override
		protected Contacto doInBackground() throws Exception {
			//SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd");
			if (fechatxt.getText().trim().isEmpty()||
				Nombretxt.getText().trim().isEmpty()||
				Telefonotxt.getText().trim().isEmpty()||
				emailtxt.getText().trim().isEmpty()) {

				JOptionPane.showMessageDialog(modContacto.this, "Rellena todos los campos");
				return null;
			}else {

				Contacto c = new Contacto();
				c.setId(id);
				c.setNombre(Nombretxt.getText());
				c.setEmail(emailtxt.getText());
				c.setTelefono(Telefonotxt.getText());
				//kaka
				HttpResponse res;
				res = RestUtil.put("/contacto/" + id, c,autorizacion);
				
				c = RestUtil.leerJSON(res, Contacto.class);
				return c;
			}
		}
		
		@Override
		protected void done() {
			try {
				Contacto c = get();
				if (c!=null) {
					cierraOK=true;
					modContacto.this.dispose();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(modContacto.this, "Error guardando el contacto");
			}
		}
	}
}
