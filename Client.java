import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
    public static void main(String... ar){
        try(Socket socket = new Socket("16.170.219.93",1080)){
            ReadData rd = new ReadData(socket);
            WriteData wd = new WriteData(socket);
            
            rd.start();
            wd.start();
            
            rd.join();
            wd.join();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class ReadData extends Thread{
    private Socket socket;
    public ReadData(Socket socket){
        this.socket = socket;
    }
    
    public void run(){
        try(InputStream is = socket.getInputStream()){
            byte[] buffer = new byte[1024];
            int readData = 0;
            while((readData = is.read(buffer))!=-1){
                System.out.println("Receieved: "+new String(buffer));
                buffer = new byte[1024];
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}

class WriteData extends Thread{
    private Scanner sc = new Scanner(System.in);
    private Socket socket;
    public WriteData(Socket socket){
        this.socket = socket;
    }
    
    public void run(){
        try(OutputStream os = socket.getOutputStream()){
            while(true){
                String s = sc.nextLine()+"\n";
                os.write(s.getBytes());
                os.flush();
            }   
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}