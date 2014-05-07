/**
   COPYRIGHT (C) 2010 Micheal Crawford, Stefan Gloutnikov, Sampriya Chandra. All Rights Reserved.
   Class Ready Queue to simulate the ready queue for the CPU.
   Solves CS149 Homework Assignment #1
   @author Micheal Crawford
   @author Stefan Gloutnikov
   @author Sampriya Chandra
   @version 1.01 2010/03/09

*/
import java.util.Queue;
import java.util.LinkedList;

/**
 * The Class ReadyQueue.
 */
public class ReadyQueue 
{
	/** The queue. */
	Queue<Process> queue;
	
	/** The quantasize. */
	int quantasize;
	
	/**
	 * Gets the next Process.
	 * 
	 * @return the next Process.
	 */
	public Process getNext()
	{
		Process p = queue.poll();
		if(p != null)
		{
			p.setTimeSlice(quantasize);
			p.waitingTime += (CPU.cputime - p.startWaitTime);
		}
		
		return p;
	}
	
	/**
	 * Instantiates a new ready queue.
	 * 
	 * @param quanta the quanta
	 */
	public ReadyQueue(int quanta)
	{
		this.quantasize = quanta;
		this.queue = new LinkedList<Process>();
	}
	
	/**
	 * Adds the process to ready queue.
	 * 
	 * @param p the process
	 */
	public void addProcess(Process p)
	{
		p.startWaitTime = CPU.cputime;
		p.slicetime = quantasize ;
		queue.add(p);
	}
	
	/**
	 * Prints the contents.
	 */
	public void printContents()
	{
	   if (queue.isEmpty()) {
         System.out.print(" empty");
	   }
	   else {
	      StringBuffer sb = new StringBuffer();
	      for(Process p : queue)
	      {
	         sb.append(" " + p.pid + "(" + p.remainingtime + "),");
	      }
	      sb.deleteCharAt(sb.length() - 1);
	      System.out.print(sb);
	      
	   }
	   System.out.println();
	}
	
}
