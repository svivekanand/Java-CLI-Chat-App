import java.net.*;
import java.io.*;
import java.util.HashMap;

public class rServer{
    private static HashMap<String,Socket> connected_users = new HashMap<String,Socket>();    
    public static void main(String... ar){
        try(ServerSocket socket = new ServerSocket(1080)){
            while(true){
                Socket client = socket.accept();
                new HandleClient(client).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    static class HandleClient extends Thread{
        private Socket socket;
        private String username;
        public HandleClient(Socket socket){
            this.socket = socket;
        }
        
        public void run(){
            try(OutputStream os = socket.getOutputStream();
               InputStream is  = socket.getInputStream()){
                username = getUserName(os,is);
                synchronized(connected_users){
                    connected_users.put(username,socket);
                }
                String reciever = Reciever(is,os);
                Socket recvsock = connected_users.get(reciever);
                messageHandler(recvsock,is);
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        private String getUserName(OutputStream os, InputStream is){
            try{
                os.write("Enter usrname\t".getBytes());
                os.flush();
                int bytes_read = 0;
                byte[] buffer = new byte[1024];
                bytes_read = is.read(buffer);
                return new String(buffer,0,bytes_read).trim();
            }catch(Exception e){
                e.printStackTrace();
            }
            return "null";
        }
        
        private String Reciever(InputStream is,OutputStream os){
            try{
                os.write("Enter reciever\t".getBytes());
                os.flush();
                byte[] buffer = new byte[1024];
                int bytes_read = 0;
                if((bytes_read = is.read(buffer))!= -1){
                    return new String(buffer,0,bytes_read).trim();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return "null";
            
        }
            
        private void messageHandler(Socket recvsock, InputStream sender_is){
            try(OutputStream receiver_os = recvsock.getOutputStream()){
                
                byte[] buffer = new byte[1024];
                int bytes_read = 0;
                while((bytes_read = sender_is.read(buffer))!= -1){
                    receiver_os.write(buffer);
                    receiver_os.flush();
                    buffer = new byte[1024];
                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        
        
        
    }
    
}