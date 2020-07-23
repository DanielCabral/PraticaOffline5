package TerceiraTentativa;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class No {
	
	Thread escutaServidorRing;
	Thread aguardarConexao;
	Thread escutaVizinho;
	Socket clientereceived, clientesend,server;
	ServerSocket socketServer=null;
	//boolean token = false;
	Boolean token = false;
	Integer porta;
	Integer portaVizinho;
	public No (){
		
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
					System.out.println(rcv);
					if(rcv instanceof ObjetoSocket) {
						ObjetoSocket obj = (ObjetoSocket) rcv;
						portaVizinho = obj.getPort();
						System.out.println("Porta: "+portaVizinho);
						clientesend = new Socket(obj.getHost(), obj.getPort());
						System.out.println(clientesend);
					}
					
					if(rcv instanceof Integer) {						
						porta = (Integer) rcv;
						socketServer = null;
						socketServer = new ServerSocket((Integer) rcv);
						aguardarConexao.start();
					}
					
					/*
					if(rcv instanceof Boolean) {
						token = (Boolean) rcv;						
						System.out.println("Token Recebido pelo servidor na porta "+porta);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ObjectOutputStream oss=new ObjectOutputStream(clientesend.getOutputStream()); 
						oss.writeObject(token);
						System.out.println("Token enviado para vizinho na porta "+clientesend.getPort());
						token= !token;
					}*/
					
					
					
		
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
						
						//Socket s = clientereceived;
								
						ObjectInputStream is = new ObjectInputStream(clientereceived.getInputStream());
						rcv = is.readObject();
						System.out.println("rcv ");
						if(rcv instanceof String) {
							System.out.println(rcv);
						}			
						
						/*
						if(rcv instanceof Boolean) {
							token = (Boolean) rcv;							
							System.out.println("Token Recebido pelo servidor na porta "+clientereceived.getPort());
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ObjectOutputStream oss=new ObjectOutputStream(clientesend.getOutputStream()); 
							oss.writeObject(token);
							System.out.println("Token enviado para vizinho na porta "+clientesend.getPort());
							token= !token;
						}*/
						
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			});
		

		Scanner teclado = new Scanner(System.in);
		String snd;
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
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {	
		new No();
	}
}
		