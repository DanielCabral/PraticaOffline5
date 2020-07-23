package TerceiraTentativa;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Questao1.Token.ObjetoSocket;



public class Servidor implements Runnable{
public Socket cliente;
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
	
 }
 
}