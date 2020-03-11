package es.izquierdofp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpResponse;
import es.izquierdofp.modelos.Contacto;
import es.izquierdofp.modelos.Usuario;
import es.izquierdofp.util.RestUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class addContacto extends JDialog {

	private JPanel contentPane;
	private String autorizacion;
	private JTextField Nombretxt;
	private JTextField emailtxt;
	private JTextField Telefonotxt;
	private JTextField fechatxt;
	public boolean cierraOK=false;
	
	public boolean getCierraOK() {
		return cierraOK;
	}
	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public addContacto(String autorizacion) {
		this.autorizacion=autorizacion;
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
	}
	private class GuardarWorker extends SwingWorker<Contacto,Void> {
		@Override
		protected Contacto doInBackground() throws Exception {
			//SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd");
			if (fechatxt.getText().trim().isEmpty()||
					Nombretxt.getText().trim().isEmpty()||
					Telefonotxt.getText().trim().isEmpty()||
					emailtxt.getText().trim().isEmpty()) {

				JOptionPane.showMessageDialog(addContacto.this, "Rellena todos los campos");
				return null;
			}else {
				DateFormat format = new SimpleDateFormat(
			            "yyyy-mm-dd", Locale.GERMAN);
				Contacto c = new Contacto();
				c.setNombre(Nombretxt.getText());
				c.setEmail(emailtxt.getText());
				c.setTelefono(Telefonotxt.getText());
				//kaka
				System.out.println("hola");
				System.out.println(format.parse("2000-11-11"));
				HttpResponse res;
				
					res = RestUtil.post("/contactos", c,autorizacion);
				
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
					addContacto.this.dispose();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(addContacto.this, "Error guardando el contacto");
			}
		}
	}
}
