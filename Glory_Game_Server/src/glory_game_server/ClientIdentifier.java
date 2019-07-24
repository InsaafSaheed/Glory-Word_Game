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


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ClientIdentifier {
    
   
    private static List<Integer> ides = new ArrayList<Integer>();
    private static int in = 0;
    private static final int rng = 10000;
    
    private ClientIdentifier(){
        
    }
    
    static {
        for (int i = 0;i<rng;i++){
            ides.add(i);
        }
        Collections.shuffle(ides);
    }
    
    public static int getIdentifier(){
        if(in > ides.size()-1)
            in = 0;
        
        return ides.get(in++);
    }
    
}
