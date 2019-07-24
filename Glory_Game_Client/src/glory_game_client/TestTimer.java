/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glory_game_client;

/**
 *
 * @author NCCS
 */
public class TestTimer {
    static Thread td=new Thread();
    public static void main(String args[]) throws InterruptedException
    {
        TestTimer tt=new TestTimer();
        tt.timer();
    }
    public void timer() throws InterruptedException
    {
        for(int i=60;i>=0;i--)
        {
            td.sleep(1000);
            System.out.println(i);
        }
    }
}
