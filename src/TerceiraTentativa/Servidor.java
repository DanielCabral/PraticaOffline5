package TerceiraTentativa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;




public class Servidor implements Runnable{
public Socket cliente;
Thread escutaNo;
int index;
public Servidor(Socket cliente, int index) throws IOException{
	this.index = index;
	this.cliente = cliente;
	System.out.println("No servidor: "+TokenServer.listaDeNos.size());
	iniciarServidor();
}

public void iniciarServidor() throws IOException {
	ObjectOutputStream oss=new ObjectOutputStream(TokenServer.listaDeNos.get(index-1).getOutputStream());
	//Enviar para o NO 0 a porta que ele deve criar o servidor 
	oss.writeObject(5000+index);
}

public Socket getCliente() {
	return cliente;
}


 public void run(){
	 escutaNo = new Thread(new Runnable() {			
			@Override
			public void run() {				
				try {
					Object rcv;
					while(true){
						
							ObjectInputStream is = new ObjectInputStream(cliente.getInputStream());
							rcv = is.readObject();
							
							
							
							System.out.println(rcv);
							ObjectOutputStream oss=new ObjectOutputStream(cliente.getOutputStream()); 
							oss.writeObject(TokenServer.recurso);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		escutaNo.start();
 }
 
}