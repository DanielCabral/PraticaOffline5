package Questao1.Token;


import java.io.Serializable;
import java.net.Socket;

public class ObjetoSocket implements Serializable{
 private static final long serialVersionUID = 1L;
 
 String host;
 int port;
 
 public String getHost() {
  return host;
 }

 public void setHost(String host) {
  this.host = host;
 }

 public int getPort() {
  return port;
 }


 public void setPort(int port) {
  this.port = port;
 }
 
 
 public ObjetoSocket (String host,int port) {
  this.host = host;
  this.port = port;
 }
 
}