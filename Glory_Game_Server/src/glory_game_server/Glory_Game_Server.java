/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glory_game_server;

/**
 *
 * @author Insaaf
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Glory_Game_Server implements Runnable {

    private List<ClientInfo> clnts = new ArrayList<ClientInfo>();
    private int prt;
    private DatagramSocket sockt;
    private boolean rnng = false;
    private Thread run,send, receive;
    private InetAddress ipAdrs;
    static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    private int hprt;
    private int mx = 0;
    ServerSocket srvr;
    
    public Glory_Game_Server(int prt){
        this.prt = prt;
        
        try{
            sockt = new DatagramSocket(prt);
            
        }catch (SocketException e){
            e.printStackTrace();
            return;
            
        }
        run = new Thread(this,"Server");
        run.start();
    }
    
    
    private void receiveData(){
        receive = new Thread("Receive"){
            public void run(){
                while(rnng){
                    byte[] data = new byte[12];
                    DatagramPacket pkt = new DatagramPacket(data,data.length);
                    try{
                        sockt.receive(pkt);
                        int actualSize = pkt.getLength();
                        byte[] actualPacket = new byte[actualSize];
                        System.arraycopy(pkt.getData(), pkt.getOffset(), data, 0, pkt.getLength());
                                
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    
                    process(pkt);
                }
            }
        };
        receive.start();
    }
    
    public void run(){
        rnng = true;
        System.out.println("Server initiated on port : " + prt);
        receiveData();
    
    }
    
    private void sendData(final byte[] data, final InetAddress adres, final int port){
        send = new Thread("Send"){
            public void run(){
                DatagramPacket packet = new DatagramPacket(data,data.length,adres,port);
                try{
                    sockt.send(packet);
                    
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }
    
    private void sendData(String msg, InetAddress address, int port){
        msg+="/e/";
        sendData(msg.getBytes(),address,port);
    }
    
    private void brodcast(String msg){
        try{
            ipAdrs = InetAddress.getByName("localhost");
            
            
            for(int i = 0; i<clnts.size(); i++){
                ClientInfo clnt = clnts.get(i);
                System.out.println(clnt.toString());
                System.out.println(clnt);
                System.out.println(clnt.address);
                System.out.println(clnt.port);
                System.out.println(clnt.name);
                sendData(msg.getBytes(),clnt.address,clnt.port);
            } 
        }catch (UnknownHostException ex){
            Logger.getLogger(Glory_Game_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    private int timrcnt = 10;
    private static int mthdcnt = -1;
    private static int  clntcnt = 0;
    String nam;
    
    private void process(DatagramPacket pkt){
        
        String txt = new String(pkt.getData());
        if(txt.startsWith("/c/")){
            if(timrcnt == 0){
                int id = ClientIdentifier.getIdentifier();
                System.out.println("Identifier : " + id);
                clnts.add(new ClientInfo(txt.substring(3,txt.length()),pkt.getAddress(),pkt.getPort(),id));
                System.out.println(txt.substring(3,txt.length()));
                
                String ID = "/p/" + id;
                sendData(ID,pkt.getAddress(),pkt.getPort());
            }
            else{
                
                clntcnt++;
                int id = ClientIdentifier.getIdentifier();
                System.out.println("Identifier : " + id);
                clnts.add(new ClientInfo(txt.substring(3, txt.length()), pkt.getAddress(), pkt.getPort(), id));
                System.out.println(txt.substring(3,txt.length()));
                
                String ID = "/c/" + id;
                sendData(ID, pkt.getAddress(), pkt.getPort());
                
            }
        }
        else if(txt.startsWith("/m/")){
            brodcast(txt);
        }
        else if (txt.startsWith("/z/")){
            String tmpT = txt.split("z/|/e/")[1];
            System.out.println("Remove name : " + tmpT);
            for(int i = 0; i<clnts.size();i++){
                ClientInfo clnt = clnts.get(i);
                String cName = clnt.name;
                if(cName == null ? tmpT == null : cName.equals(tmpT)){
                    int rPort = clnt.port;
                    map.remove(rPort);
                    String emm = "/y/";
                    sendData(emm.getBytes(), clnt.address, clnt.port);
                    System.out.println("Removing player : " + tmpT);
                    clnts.remove(i);
                    System.out.println("Number of Players : " + clnts.size());
                    clntcnt--;
                    mthdcnt = -1;
                }
                
                if (clnts.size() == 1){
                    String winMsg = "Congratulations " + clnts.get(0).name + "!!!. You are the Winner of this Game";
                    String n = "/w/" + winMsg;
                    brodcast(n);
                }
            }
        }
        else if (txt.startsWith("/v/")){
            close();
            
            System.out.println("Server is Shuting Down");
            System.exit(0);
            
        }
        else if(txt.startsWith("/k/")){
            String tempT = txt.split("k/|/e/")[1];
            for(int i = 0;i<clnts.size();i++){
                ClientInfo clnt = clnts.get(i);
                String cName = clnt.name;
                
                if(cName == null ? tempT == null : cName.equals(tempT)){
                    int rPort = clnt.port;
                    map.remove(rPort);
                    clntcnt--;
                }
                if(clntcnt == 0){
                    close();
                    System.exit(0);
                }
            }
        }
        else {
            mthdcnt++;
            System.out.println("Number of Clients : " + clntcnt);
            if(mthdcnt != clntcnt && (mthdcnt < clntcnt)){
                System.out.println(txt);
                getScore(txt,pkt);
            }
            if(mthdcnt == (clntcnt - 1)){
                
                try{
                    Set set = map.entrySet();
                    Iterator iterator = set.iterator();
                    while(iterator.hasNext()){
                        Map.Entry me = (Map.Entry)iterator.next();
                        System.out.print(me.getKey() +  " : ");
                        System.out.println(me.getValue());
                        
                        int pScore = (Integer) me.getValue();
                        
                        if(mx < pScore){
                            mx = pScore;
                            hprt = (Integer) me.getKey();
                        }
                        
                    }
                    
                    for(int i = 0;i<clnts.size();i++){
                        ClientInfo clnt = clnts.get(i);
                        
                        if(clnt.port == hprt){
                            nam = clnt.name;
                            System.out.println("Name : " + clnt.name);
                        }
                        
                    }
                    
                    ipAdrs = InetAddress.getByName("localhost");
                    String fMsg = "Round Completed";
                    String pMsg = "Congratulations " + nam + ". You earned the best score !!";
                    String hs = "/h/" + pMsg;
                    
                    System.out.println(pMsg);
                    String tmp = "/r/" + fMsg;
                    brodcast(tmp);
                    sendData(hs,ipAdrs,hprt);
                    
                }catch(UnknownHostException ex){
                    Logger.getLogger(Glory_Game_Server.class.getName()).log(Level.SEVERE,null,ex);
                    
                }
            }
        }
    }
    
    
    private int scor;
    String letOne,letTwo;
    boolean alLet, allVov, checkIn;
    String nameF;
    
    public void getScore(String input, DatagramPacket pkt){
        String gSco = input;
        scor = Integer.parseInt(gSco.substring(0,2));
        
        System.out.println("Total Score : " + scor);
        String tm = Integer.toString(scor);
        String xy = "Your Score is : " + tm;
        
        int alPort = pkt.getPort();
        
        for(int i = 0; i<clnts.size(); i++){
            ClientInfo clnt = clnts.get(i);
            if(clnt.port == alPort){
                nameF = clnt.name;
            }
        }
        
        int portNo = pkt.getPort();
        
        if((map.size()-1) < clntcnt){
            if(map.size() < clntcnt){
                map.put(portNo,scor);
                String alTo = nameF + "-" + tm;
                String addToAll = "/a/"+ alTo;
                brodcast(addToAll);
                sendData(xy.getBytes(),pkt.getAddress(),pkt.getPort());
                
;           }
            else{
                Set st = map.entrySet();
                Iterator itr = st.iterator();
                
                while(itr.hasNext()){
                    Map.Entry me = (Map.Entry)itr.next();
                    hprt = (Integer) me.getKey();
                    
                    if(hprt == pkt.getPort()){
                        int pScore = (Integer) me.getValue();
                        int nScore = pScore + scor;
                        String ns = Integer.toString(nScore);
                        map.put(hprt, nScore);
                        String alTo = nameF + "-" + nScore;
                        String addAllTo = "/a/" + alTo;
                        brodcast(addAllTo);
                        sendData(ns.getBytes(),pkt.getAddress(),pkt.getPort());
                    }
                }
                
            }
        }
            
    }
    
  
    
    public void close(){
        new Thread(){
            public void run(){
                synchronized(sockt){
                    sockt.close();
                }
            }
        }.start();
    }
    
    
    public static void main(String[] args) {
        Glory_Game_Server s = new Glory_Game_Server(8080);
        
    }
    
}
