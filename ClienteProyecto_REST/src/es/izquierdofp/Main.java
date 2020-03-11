package es.izquierdofp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.google.gson.reflect.TypeToken;

import es.izquierdofp.modelos.Usuario;
import es.izquierdofp.util.RestUtil;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(44, 25, 97, 23);
		contentPane.add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a");
		lblContrasea.setBounds(44, 81, 97, 23);
		contentPane.add(lblContrasea);
		
		textField = new JTextField();
		textField.setBounds(151, 26, 219, 23);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(151, 81, 219, 23);
		contentPane.add(passwordField);
		
		JButton btnIniciarSesion = new JButton("Iniciar Sesion");
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginWorker().execute();
				
			}
		});
		btnIniciarSesion.setBounds(44, 157, 156, 23);
		contentPane.add(btnIniciarSesion);
		
	}
	
	
	
	private class LoginWorker extends SwingWorker<String,Void> {
		@Override
		protected String doInBackground() throws Exception {
			Usuario u = new Usuario();
			u.setLogin(textField.getText());
			u.setPwd(new String(passwordField.getPassword()));
			HttpResponse res;
			
			res = RestUtil.post("/LoginServlet", u);
			Header header =  res.getFirstHeader("X-Autorizacion");
			
			return header.getValue();
		}
		
		@Override
		protected void done() {
			try {
				String header = get();
				if (header !=null) {
					System.out.println(header);
					Contactos contactos = new Contactos(header);
					contactos.setVisible(true);
					Main.this.dispose();
				}else {
					
					JOptionPane.showMessageDialog(Main.this, "Error al logear");
				}
			}catch (Exception e) {
			}
		
	}
	}
	
	
}
