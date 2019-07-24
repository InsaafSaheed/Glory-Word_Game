/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glory_game_client;
import java.util.Random;

/**
 *
 * @author NCCS
 */

public class RandomLetterGen {
    private String CHAR_LIST="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int RANDOM_STRING_LENGTH = 3;
    private String CHAR_LIST_Con="BCEFGHJKLMNPQRSTVWXYZ";
    private String CHAR_LIST_Vowel="AEIOU";
   
    
    public String generateRandomString(){
        int letcount = CHAR_LIST.length();
        StringBuffer randStr=new StringBuffer();
        for(int i=0;i<RANDOM_STRING_LENGTH;i++){
            int number=getRandomNumber(letcount);
            char ch=CHAR_LIST.charAt(number);
            randStr.append(ch);
            
        }
        return randStr.toString();
    }
    public String generateRandomStringCon(int amount){
        int letcount=amount;
        StringBuffer randStr=new StringBuffer();
        for(int i=0;i<letcount;i++){
            int number = getRandomNumber(CHAR_LIST_Con.length());
            char ch=CHAR_LIST_Con.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }
    public String generateRandomStringVowel(int amount){
        int letcount=amount;
        StringBuffer randStr=new StringBuffer();
        for(int i=0;i<letcount;i++){
            int number = getRandomNumber(CHAR_LIST_Vowel.length());
            char ch=CHAR_LIST_Vowel.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }
    private int getRandomNumber(int val){
        int value=val;
        int randomInt=0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(value);
        if(randomInt-1==-1){
            return randomInt;
            
        }else {
            return randomInt-1;
        }
    }
}
