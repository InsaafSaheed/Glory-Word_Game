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

import java.net.InetAddress;

public class ClientInfo {
    
    public String name;
    public InetAddress address;
    public int port;
    private final int id;
    public int attempt = 0;
    
    
    public ClientInfo(String name, InetAddress address, int port, final int id){
        this.name = name;
        this.address = address;
        this.port = port;
        this.id = id;
    }
    
    
    public int getID(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
}
