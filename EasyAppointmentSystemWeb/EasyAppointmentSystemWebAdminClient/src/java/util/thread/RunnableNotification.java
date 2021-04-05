/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author valenciateh
 */
public class RunnableNotification implements Runnable {
    
    private Thread thread;
    private Future<Boolean> asyncResult;

    
    
    public RunnableNotification() 
    {
    }

    
    
    public RunnableNotification(Future<Boolean> asyncResult)
    {
        this.asyncResult = asyncResult;
    }
    
    
    
    public void run()
    {
       try
       {
           while(true)
           {
               Thread.sleep(1000);

               if(asyncResult != null && asyncResult.isDone()) 
               {
                   Boolean result = asyncResult.get();

                   if(result)
                   {
                       System.out.println("[SERVER] Reminder email actually sent successfully!\n");
                   }
                   else
                   {
                       System.out.println("[SERVER] Reminder email was NOT actually sent!\n");
                   }

                   break;
               }
           }
       }
       catch(InterruptedException | ExecutionException ex)
       {
           System.out.println("[SERVER] Error actually sending reminder email: " + ex.getMessage() + "!\n");
       }
    }
    
    
    
    public void start()
    {
        if (thread == null) 
        {
            thread = new Thread (this, "thread1");
            thread.start();
        }
    }

}
