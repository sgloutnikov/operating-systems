/**
   COPYRIGHT (C) 2010 Micheal Crawford, Stefan Gloutnikov, Sampriya Chandra. All Rights Reserved.
   Class IODevice to simulate a the IO Queues for Disk and printer.
   Solves CS149 Homework Assignment #1
   @author Micheal Crawford
   @author Stefan Gloutnikov
   @author Sampriya Chandra
   @version 1.01 2010/03/09

*/

import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;

/**
 * The Class IODevice.
 */
public class IODevice 
{
	/** The queue. */
	Queue<Process> queue;
	
	/** The scheduler. */
	Scheduler scheduler;
	
	/** The executing. */
	Process executing;
	
	/** The execution of IOtime. */
	int execiotime;
	
	/** The message. */
	String message;
	
	/** The title. */
	String title;
	
	/** The generator. */
	Random generator;
	
	/**
	 * Instantiates a new IO device.
	 * 
	 * @param scheduler the scheduler
	 * @param message the message
	 * @param title the title
	 */
	public IODevice(Scheduler scheduler, String message, String title)
	{
		queue = new LinkedList<Process>();
		this.scheduler = scheduler;
		generator = new Random();
		this.message = message;
		this.title = title;
	}
	
	/**
	 * Executes cycle.
	 */
	public void executeCycle()
	{
		
		if(executing == null)
		{
			executing = queue.poll();
			if(executing != null)  
			{
				execiotime = executing.IOtime;
			}
		}
		
		if(executing != null)
		{
			if(execiotime > 0)
				execiotime--;
			else
			{
				if(ProcessSchedulingSimulator.trace)
					System.out.println(CPU.getCPUTime() + "\t" + message + 
							" terminated for process " + executing.pid);
				
				scheduler.queueProcess(executing);
				executing = queue.poll();
				if(executing != null)  
					execiotime = executing.IOtime;
			}
				
		}
		
	}
	
	/**
	 * Checks if is executing.
	 * 
	 * @return true, if is executing
	 */
	public boolean isExecuting()
	{
		boolean ret = true;
		if(executing == null && queue.isEmpty())
			ret = false;
		return ret;
	}
	
	/**
	 * Do IO where process added to IO ready queue.
	 * 
	 * @param p the process
	 */
	public void doIO(Process p)
	{
		queue.add(p);
	}
	
	/**
	 * Prints the contents.
	 */
	public void printContents()
	{
	   if (queue.isEmpty() && executing == null) {
         System.out.print(" empty");
      }
      else {
         StringBuffer sb = new StringBuffer();
	     if(executing != null)
		    	 sb.append(" " + executing.pid + "(" + execiotime + "),");
	     
         for(Process p : queue)
         {
            sb.append(" " + p.pid + "(" + p.remainingtime + "),");
         }
         sb.deleteCharAt(sb.length() - 1);
         System.out.print(sb);
         
      }
	   
	   
		   System.out.println();
		   
		   /*if(executing != null)
			   System.out.println("Executing for " + executing.pid + 
					   " with " + execiotime + " remaining.");
					   */
	}
}
