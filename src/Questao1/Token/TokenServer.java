package Questao1.Token;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class TokenServer {
	static int cont=0;
	static LinkedList<Socket> listaDeNos = new LinkedList<Socket>();

	public static void main(String [] args) throws IOException {
		//ServerSocket ouvindo a porta 12345, aceitando novos clientes
		ServerSocket token;
		token = new ServerSocket(12345);
		System.out.println("Servidor ouvindo a porta 12345\n\n");
		while(true) {
			// o método accept() bloqueia aO execução até que o servidor receba 3 pedido de conexão			
		    Socket cliente = token.accept();
		    System.out.println("Cliente conectado\n\n");
		    listaDeNos.add(cliente);		    
		    Servidor s= new Servidor(cliente);
		    Thread t=new Thread(s);
		    t.start();
		}
	}
	public static int getTamanho() {
		cont= listaDeNos.size();
		return cont;
	}
	
	public int proximaPorta() {
		return listaDeNos.size();
	}
}
