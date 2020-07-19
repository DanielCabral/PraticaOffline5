package Questao1.Token;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class No {
	
	Thread escuta;
	Socket clientereceived, clientesend, server;
	ServerSocket socketServer=null;
	
	public No (){
		
		 try {

			  //socketServer = new ServerSocket(5000+TokenServer.proximaPorta());
			  server = new Socket("localhost", 12345);
			  System.out.println("500"+TokenServer.getTamanho());
			  //clientesend = new Socket();
		 }catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("Erro: " + e.getMessage());
		 }
		 
		 
		escuta = new Thread(new Runnable() {			
		@Override
			public void run() {
			
			try {
				Object rcv;
							
				while(true){
					
					Socket s = server;
							
					System.out.println("Cliente conectado com servidor");
					ObjectInputStream is = new ObjectInputStream(s.getInputStream());
					rcv = is.readObject();
					if(rcv instanceof ObjetoSocket) {
						ObjetoSocket obj = (ObjetoSocket) rcv;
						System.out.println(obj.getHost());
						System.out.println(obj.getPort());
					}
					System.out.println(rcv);
					
		
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		});
				
		escuta.start();
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {	
		new No();
	}
}
		