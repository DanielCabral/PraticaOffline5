package TerceiraTentativa;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javafx.fxml.FXML;
import models.Recurso;

public class No {
	
	Thread escutaServidorRing;
	Thread aguardarConexao;
	Thread escutaVizinho;
	Socket clientereceived, clientesend,server;
	ServerSocket socketServer=null;
	//boolean token = false;
	Boolean token = false;
	String status = "";
	String conteudoDeRecurso = "";
	public No () throws IOException, InterruptedException{
		
		 try {
			  server = new Socket("localhost", 12345);
			  //clientesend = new Socket();
		 }catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("Erro: " + e.getMessage());
		 }
		 
		 
		escutaServidorRing = new Thread(new Runnable() {			
		@Override
			public void run() {
			
			try {
				Object rcv;
							
				while(true){
					
					Socket s = server;
							
					ObjectInputStream is = new ObjectInputStream(s.getInputStream());
					rcv = is.readObject();
					
					if(rcv instanceof String) {
						Thread.sleep(2000);
						System.out.println("Recurso gravado");
						Thread.sleep(2000);
						liberarRecurso();
					}
					
					if(rcv instanceof Recurso) {
						Thread.sleep(2000);
						System.out.println("Recurso recebido");
						System.out.println("Conteudo do recurso: "+((Recurso) rcv).getConteudo());
						Thread.sleep(2000);
						liberarRecurso();
					}
					
					if(rcv instanceof ObjetoSocket) {
						ObjetoSocket obj = (ObjetoSocket) rcv;
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
						System.out.println("Token Recebido pelo servidor");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
						
								
						ObjectInputStream is = new ObjectInputStream(clientereceived.getInputStream());
						rcv = is.readObject();
						if(rcv instanceof String) {
							System.out.println(rcv);
						}			
						
						
						if(rcv instanceof Boolean) {
							token = (Boolean) rcv;							
							System.out.println("Token Recebido...");
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(status.equals("acessarRecurso")) {
								acessarRecurso();
							}else {
								enviarToken();
							}	
							//enviarToken();
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
		

		Scanner teclado = new Scanner(System.in);
		String snd;

		int escolha=-1;
		while(escolha != 0) {
			System.out.println("------Opções de acesso de recurso------");
			System.out.println("0- Sair");
			System.out.println("1- Receber Recurso");
			System.out.println("2- Acessar recurso");
			System.out.print("Escolha uma opção: ");
			escolha = teclado.nextInt();
		
		//Se processo bloqueado, não entra novamente
		if(!status.equals("acessarRecurso")) {
			switch(escolha) {
				case 0: 
					server.close();
					System.exit(0);
					break;
					
				case 1: 
					entrar();
					break;
					
				case 2: 
					Scanner sc =new Scanner(System.in);
					System.out.print("Digite o conteudo a gravar no arquivo: ");
					conteudoDeRecurso = sc.nextLine();
					
					entrar();
					break;
					default: 
						System.out.print("Opção invalida: ");
						break;
			}
		}
		}
		try {
			while(true){
				System.out.println("Digite uma mensagem: ");
				snd = teclado.nextLine();
				if (!snd.equalsIgnoreCase("fim"))
					System.out.println(snd);
				
				//Fluxo de saida para enviar uma mensagem string
				ObjectOutputStream oss=new ObjectOutputStream(clientesend.getOutputStream()); 
				oss.writeObject(snd);
				// flush the stream
		         oss.flush();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void enviarToken() throws IOException {
		ObjectOutputStream oss=new ObjectOutputStream(clientesend.getOutputStream()); 
		oss.writeObject(token);
		System.out.println("Token enviado para vizinho");
		token= !token;
	}
	
	public void liberarRecurso() throws IOException {
		status="";
		System.out.println("Recurso liberado");
		enviarToken();
	}
	
	public void acessarRecurso() throws IOException, InterruptedException{
		System.out.println("Acessando recurso");
		status = "acessarRecurso";
		Thread.sleep(2000);
		ObjectOutputStream oss=new ObjectOutputStream(server.getOutputStream()); 
		if(conteudoDeRecurso.equals("")) {
			oss.writeObject("Receber recurso");
		}else {
			oss.writeObject(new Recurso(conteudoDeRecurso));
		}
	}
	
	@FXML
	public void entrar() throws IOException {
		status = "acessarRecurso";
		System.out.println("Aguardando liberacao de recurso");
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {	
		new No();
	}
}
		