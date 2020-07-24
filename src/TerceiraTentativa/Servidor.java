package TerceiraTentativa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import models.Recurso;




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
							
							ObjectOutputStream oss=new ObjectOutputStream(cliente.getOutputStream()); 
							
							if(rcv instanceof String) {
								TokenServer.recurso.setConteudo(lerArquivo());
								oss.writeObject(TokenServer.recurso);
							}
							
							if(rcv instanceof Recurso) {
								Recurso re = (Recurso) rcv;
								System.out.println("Conteudo: "+re.getConteudo());
								gravarArquivo(re.getConteudo());
								oss.writeObject("Arquivo gravado");
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
		escutaNo.start();
 }
 
 public void gravarArquivo(String conteudo) {
	 FileOutputStream arquivoEscrever;
		try {
			arquivoEscrever = new FileOutputStream("src/dados/Arquivo.txt");
			PrintWriter pr = new PrintWriter(arquivoEscrever);
			pr.print(conteudo);
			System.out.println("Arquivo criado");
			pr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 }
 
 public String lerArquivo() {
	 File myFile = new File("src/dados/Arquivo.txt");
	 try {	    
	     FileReader arq=new FileReader(myFile);
	     BufferedReader bufferedReader=new BufferedReader(arq);    
	     String entrada="";
	     String linha = bufferedReader.readLine(); 
	     while(linha != null) {
	    	 entrada+=linha;
	    	 linha = bufferedReader.readLine(); 
	     }
	     arq.close();
	     bufferedReader.close();
	     return entrada;
	     
	 }
	 catch(IOException e) {
		 System.out.println(e+" caminho: "+myFile.getAbsolutePath());
	 }
	 return null;
 }
 
}