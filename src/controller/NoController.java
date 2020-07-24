package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.webkit.ThemeClientImpl;

import TerceiraTentativa.ObjetoSocket;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.Recurso;

public class NoController implements Initializable {

	@FXML Label statusLabel;
	@FXML Label  tokenstatus;
	Thread escutaServidorRing;
	Thread aguardarConexao;
	Thread escutaVizinho;
	Socket clientereceived, clientesend,server;
	ServerSocket socketServer=null;
	Boolean token = false;
	String status = "";
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			  server = new Socket("localhost", 12345);
			  //clientesend = new Socket();
		 }catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("Erro: " + e.getMessage());
		 }
		 
		Runnable r;
		escutaServidorRing = new Thread(r =() -> {

			try {
				Object rcv;
							
				while(true){
					
					Socket s = server;
							
					ObjectInputStream is = new ObjectInputStream(s.getInputStream());
					rcv = is.readObject();
					System.out.println(rcv);
					if(rcv instanceof Recurso) {
						System.out.println("Recurso recebido");
						liberarRecurso();
					}
					
					if(rcv instanceof ObjetoSocket) {
						ObjetoSocket obj = (ObjetoSocket) rcv;
						System.out.println(obj);
						clientesend = new Socket(obj.getHost(), obj.getPort());
						System.out.println(clientesend);
					}
					
					if(rcv instanceof Integer) {											
						socketServer = null;
						socketServer = new ServerSocket((Integer) rcv);
						aguardarConexao.start();
					}
					
					//Inicializar token
					if(rcv instanceof Boolean) {
						token = (Boolean) rcv;						
						System.out.println("Token Recebido pelo servidor na porta ");
						//tokenstatus.setText("Disponivel");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//tokenstatus.setText("Indisponivel");
						if(status.equals("acessarRecurso")) {
							acessarRecurso();
						}else {
							enviarToken();
						}	
					}
					
					
					
		
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		);
		 

				
		escutaServidorRing.start();
		
		aguardarConexao = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Socket s = socketServer.accept();
							clientereceived = null;
							clientereceived = s;
							System.out.println("Cliente recebido "+clientereceived);
							if(!escutaVizinho.isAlive()) 
								escutaVizinho.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		 });
		
		
		escutaVizinho = new Thread(new Runnable() {			
			@Override
				public void run() {				
				try {
					Object rcv;
								
					while(true){
						
						//Socket s = clientereceived;
								
						ObjectInputStream is = new ObjectInputStream(clientereceived.getInputStream());
						rcv = is.readObject();
						System.out.println("rcv ");
						if(rcv instanceof String) {
							System.out.println(rcv);
						}			
						
						
						if(rcv instanceof Boolean) {
							token = (Boolean) rcv;							
							System.out.println("Token Recebido pelo servidor na porta "+clientereceived.getPort());
							//tokenstatus.setText("Disponivel");
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//tokenstatus.setText("Indisponivel");
							if(status.equals("acessarRecurso")) {
								acessarRecurso();
							}else {
								enviarToken();
							}							
						}
						
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			});
	}	
	
	public void enviarToken() throws IOException {
		ObjectOutputStream oss=new ObjectOutputStream(clientesend.getOutputStream()); 
		oss.writeObject(token);
		System.out.println("Token enviado para vizinho na porta "+clientesend.getPort());
		token= !token;
	}
	
	public void liberarRecurso() throws IOException {
		status="";
		statusLabel.setText("Recurso solicitado");
		enviarToken();
	}
	
	public void acessarRecurso() throws IOException, InterruptedException{
		statusLabel.setText("Acessando recurso");
		Thread.sleep(2000);
		ObjectOutputStream oss=new ObjectOutputStream(server.getOutputStream()); 
		oss.writeObject("Receber recurso");
	}
	
	@FXML
	public void entrar() throws IOException {
		status = "acessarRecurso";
		statusLabel.setText("Aguardando liberacao de recurso");
	}
	
	@FXML
	public void sair() {
		System.exit(0);
	}
}
