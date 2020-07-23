package Questao1.Nos;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class No {
	
	Thread escutaServidorRing;
	Socket  server;
	//boolean token = false;
	Boolean token = false;
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
						//System.out.println("Conectar com o vizinho");
						//System.out.println(obj.getHost());
						//System.out.println(obj.getPort());
						server = new Socket(obj.getHost(), obj.getPort());
						System.out.println(server);
					}					
					
					
					if(rcv instanceof Boolean) {
						token = (Boolean) rcv;						
						System.out.println("Token Recebido...");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ObjectOutputStream oss=new ObjectOutputStream(server.getOutputStream()); 
						oss.writeObject(token);

						token= !token;
					}
					
					
					
		
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
		
		
		Scanner teclado = new Scanner(System.in);
		String snd;
		try {
			while(true){
				System.out.println("Digite uma mensagem: ");
				snd = teclado.nextLine();
				if (!snd.equalsIgnoreCase("fim"))
					System.out.println(snd);
				
				//Fluxo de saida para enviar uma mensagem string
				ObjectOutputStream oss=new ObjectOutputStream(server.getOutputStream()); 
				oss.writeObject(snd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {	
		new No();
	}
}
		