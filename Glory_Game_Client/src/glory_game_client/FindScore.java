/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glory_game_client;
import java.net.DatagramPacket;
/**
 *
 * @author NCCS
 */
public class FindScore {
    private int timeused,nonlet;
    private int sco,timebonus,letbonus,vletbonus,letpen;
    String contain1,contain2;
    boolean alllet,allvow,checkini;
    String nameff;
    String scoo;
    String sc,newscore;
    public String calscore(String input,int time,String ini11,String ini12)
    {
        timeused=time;
        int points=0;
        String data=input;
        String word[]=explode(data);
        
        String initiallet1=ini11;
        String initiallet2=ini12;
        
        int noofletters=0;
        int remaininglet=0;
        noofletters=data.length();
        System.out.println(data);
        remaininglet=12-noofletters;
        
        for(int i=0;i<word.length;i++)
        {
            if(word[i].equals(initiallet1))
            {
                contain1=word[i];
            }
            if(word[i].equals(initiallet2))
            {
                contain2=word[i];
            }
        }
        for(int i=0;i<word.length;i++)
        {
            String lt=word[i];
            if("A".equals(lt) || "E".equals(lt) || "I".equals(lt) || "O".equals(lt) || "U".equals(lt) || "L".equals(lt) || "N".equals(lt) || "R".equals(lt) || "S".equals(lt) || "T".equals(lt))
                points=points+1;
            else if("D".equals(lt) || "G".equals(lt))
                points=points+2;
            else if("B".equals(lt) || "C".equals(lt) || "M".equals(lt) || "P".equals(lt))
                points=points+3;
            else if("F".equals(lt) || "H".equals(lt) || "V".equals(lt) || "W".equals(lt) || "Y".equals(lt))
                points=points+4;
            else if("K".equals(lt))
                points=points+5;
            else if("J".equals(lt))
                points=points+8;
            else if("Q".equals(lt))
                points=points+10;
            
        }
        System.out.println("Your points for letters ="+points);
        
        if(timeused<15)
            timebonus=20;
        else if(timeused<30)
            timebonus=15;
        else
            timebonus=10;
        
        if(word.length==11)
            letbonus=25;
        else if(word.length>10)
            letbonus=20;
        else if(word.length >8)
            letbonus=15;
        else if(word.length>6)
            letbonus=10;
        else if(word.length<6)
            letpen=10;
        
        if(contain1 !=null)
        {
            if(contain2 !=null)
            {
                sco=points + ((timebonus+letbonus)+ noofletters-remaininglet)-letpen;
                scoo=Integer.toString(sco);
            }
        }
        else
        {
            scoo="00";
        }
        if(scoo.length()==1 || scoo.length()==0)
        {
            newscore="0"+scoo+"0";
        }else
        {
            newscore=scoo;
        }
        return newscore;
    }
    public String[] explode(String s){
        String[] arr= new String[s.length()];
        for(int i=0;i<s.length();i++)
        {
            arr[i]=String.valueOf(s.charAt(i));
        }
        return arr;
    }
    String calscore(String cc,int timeu,String text,String text0,String text1){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
