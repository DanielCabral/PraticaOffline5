package Questao1.Token;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;



public class Servidor implements Runnable{
public Socket cliente;

public Servidor(Socket cliente) throws IOException{
	this.cliente = cliente;
	System.out.println("No servidor: "+TokenServer.listaDeNos.size());
	if(TokenServer.listaDeNos.size() > 1) {
		enviarParaAnteriorEnderecoEPorta();		
	}else {
		iniciarServidorZero();
	}
}

public void enviarParaAnteriorEnderecoEPorta() throws IOException {
	int tamanho = TokenServer.listaDeNos.size();
	Socket NoZero = TokenServer.listaDeNos.get(0);
	//size = 4
	//0 3 4
	Socket NoAnterior = TokenServer.listaDeNos.get(tamanho-2);
	Socket NoAtual = TokenServer.listaDeNos.get(tamanho-1);
	
	//Fluxo de envio para o NO n
	ObjectOutputStream oss=new ObjectOutputStream(NoAtual.getOutputStream());
	//Enviar para o NO n, a porta que ele deve criar o servidor 
	oss.writeObject(5000+((Integer) tamanho));
	
	
	//Pegar Dados do NO n
	InetAddress addr = NoAtual.getInetAddress();
	String host = addr.getHostAddress();
	
	//Criar um object socket com endereço e porta do NO n
	//Esse endereco e porta, serao enviados pra o NO n-1
	ObjetoSocket dadosDeEnderecoEPorta = new ObjetoSocket(host , 5000+tamanho);
		
	//Fluxo de envio para o NO n-1
	oss=new ObjectOutputStream(NoAnterior.getOutputStream());
 	//Enviar para NO n-1, os dados com endereco e porta do servidor que o no seguinte criou
	oss.writeObject(dadosDeEnderecoEPorta);
	
	
	//Pegar Dados do NO 0
	addr = NoZero.getInetAddress();
	host = addr.getHostAddress();
	
	dadosDeEnderecoEPorta = new ObjetoSocket(host , 5001);
	System.out.println();
	//Fluxo de envio para o no n
	oss=new ObjectOutputStream(NoAtual.getOutputStream());
	//Enviar para NO n, os dados com endereco e porta do servidor do NO 0
	oss.writeObject(dadosDeEnderecoEPorta);
	
	if(tamanho==2) {
		/*Boolean token = true;
		ObjectOutputStream oss2=new ObjectOutputStream(NoZero.getOutputStream());
		oss2.writeObject(token);*/		
	}
}

public void iniciarServidorZero() throws IOException {
	ObjectOutputStream oss=new ObjectOutputStream(TokenServer.listaDeNos.get(0).getOutputStream());
	//Enviar para o NO 0 a porta que ele deve criar o servidor 
	oss.writeObject(5001);
}

public Socket getCliente() {
	return cliente;
}


 public void run(){
	
 }
 
}