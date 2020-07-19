package Questao1.Token;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;



public class Servidor implements Runnable{
public Socket cliente;
public static int cont = 0;

public Servidor(Socket cliente) throws IOException{
	this.cliente = cliente;
	if(TokenServer.listaDeNos.size() > 1) {
		enviarParaAnteriorEnderecoEPorta();		
	}
}

public void enviarParaAnteriorEnderecoEPorta() throws IOException {
	int tamanho = TokenServer.listaDeNos.size();
	Socket NoAnterior = TokenServer.listaDeNos.get(tamanho-2);
	Socket NoAtual = TokenServer.listaDeNos.get(tamanho-1);
	
	InetAddress addr = NoAtual.getInetAddress();
	int         port = NoAtual.getPort();
	
	
	String host = addr.getHostAddress();
	ObjetoSocket dadosDeEnderecoEPorta = new ObjetoSocket(host , port);
	
	ObjectOutputStream oss=new ObjectOutputStream(NoAnterior.getOutputStream());
 	
	 oss.writeObject(dadosDeEnderecoEPorta);
}

public Socket getCliente() {
	return cliente;
}


 public void run(){
	try {
		Scanner s = null;
		s = new Scanner(this.cliente.getInputStream());
		String rcv;
	    //Exibe mensagem no console
		while(s.hasNextLine()){
			rcv = s.nextLine();
			if (rcv.equalsIgnoreCase("fim"))
				break;
			else
				System.out.println(rcv);
			//enviar(rcv);
	
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
}
 
}