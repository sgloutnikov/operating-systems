/**
   COPYRIGHT (C) 2010 Micheal Crawford, Stefan Gloutnikov, Sampriya Chandra. All Rights Reserved.
   Class ProcessSchedulingSimulator to simulate the entire Process Scheduling System.
   Solves CS149 Homework Assignment #1
   @author Micheal Crawford
   @author Stefan Gloutnikov
   @author Sampriya Chandra
   @version 1.01 2010/03/09
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * The Class ProcessSchedulingSimulator.
 */
public class ProcessSchedulingSimulator {

	/** The cpu. */
	CPU cpu;
	
	/** The trace. */
	static boolean trace = false;
	
	/** The aging. */
	boolean aging = false;
	
	/** The displayqueues. */
	boolean displayqueues = false;
	
	/** The pause. */
	boolean pause = false;
	
	/** The agetime. */
	int agetime = 100;
	
	/** The pauseat. */
	int pauseat = 0;
	
	/** The displayqueueinterval. */
	int displayqueueinterval = 0;
	
	/**
	 * Instantiates a new process scheduling simulator.
	 * 
	 * @param agetime the frequency of how often to be performed.
	 */
	public ProcessSchedulingSimulator(int agetime)
	{
		cpu = new CPU();
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main (String [] args)
	{
	   String input;
	   ProcessSchedulingSimulator pss = new ProcessSchedulingSimulator(100);
	   
	   System.out.println("Welcome to Process Scheduling Simulator");
	   System.out.println("Please specify the following options before we begin:");
	   Scanner in = new Scanner(System.in);
	   // Read Aging
	   System.out.print("Specify Aging [on/off]: ");
	   input = in.next();
	   if(input.equalsIgnoreCase("on")) {
	      pss.aging = true;
	   }
	   else {
	      pss.aging = false;
	   }
	   // Read Trace
	   System.out.print("Specify Trace [on/off]: ");
	   input = in.next();
	   if(input.equalsIgnoreCase("on"))
	      pss.trace = true;
	   // Read How often to display queues.
	   System.out.print("Specify how often to display queues in msec [0 to disable]: ");
	   pss.displayqueueinterval = in.nextInt();
	   if(pss.displayqueueinterval > 0)
		   pss.displayqueues = true;
	   // Read When to pause.
	   System.out.print("Specify when to pause in msec (0 to omit pausing): ");
	   pss.pauseat = in.nextInt();
	   if(pss.pauseat > 0)
		   pss.pause = true;
	   // Read Input File and Process Incoming.
	   System.out.print("Specify input file: ");
	   String infile = in.next();
	   //pss.displayqueueinterval = 50;
	   //pss.pauseat = -1;
	   //pss.aging = false;
	   //pss.trace = true;
	   
	   
	   File file = new File(infile);
	   pss.cpu.scheduler.processIncoming(file);

	   //pss.cpu.scheduler.printIncomingProcesses();
	   
	   // Printing if Trace is ON
	   if(pss.trace == true)
	   {
	      System.out.println("Aging: " + pss.aging + "\t\t Trace: true");
	      System.out.println("Display queues every: \t " + pss.displayqueueinterval);
	      System.out.println("Pause at: \t\t " + pss.pauseat);
	      System.out.println("\t\tTrace");
	      System.out.println("Time (msec)\t Event");
	   }
	   
	   pss.runSimulator();
		
	}
	
	/**
	 * Calculating the Average metrics for each values.
	 */
	public void averageMetrics()
	{
		DecimalFormat df = new DecimalFormat("#.##");

		int totalResponseTime = 0;
		int totalTurnAroundTime = 0;
		int totalWaitingTime = 0;
	
		ArrayList<Process> completed = cpu.scheduler.completed;
		int len = completed.size();
		//System.out.println("len=" + len);
		for(int i = 0; i < len; i++)
		{
			Process p = completed.get(i);
			p.calculateMetrics();
			//System.out.println("p = " + p.pid + ", responsetime = " + p.responseTime + 
			//		", turnaround = " + p.turnAroundTime + ", waittime = " + p.waitingTime);
			totalResponseTime += p.responseTime;
			totalTurnAroundTime += p.turnAroundTime;
			totalWaitingTime += p.waitingTime;
		}
		
		double avgResponseTime = (totalResponseTime / len);
		double avgTurnAroundTime = (totalTurnAroundTime / len);
		double avgWaitingTime = (totalWaitingTime / len);
		
		System.out.println("--- Statistics ---");
		System.out.println();
		System.out.println("Total CPU Time:\t\t\t" + CPU.getCPUTime());
		System.out.println("Average Wait time:\t\t" + df.format(avgWaitingTime));
		System.out.println("Average Turnaround time:\t" + df.format(avgTurnAroundTime));
		System.out.println("Average Response time:\t\t" + df.format(avgResponseTime));
		
	}
	
	/**
	 * Run the simulator.
	 */
	public void runSimulator()
	{
		while(!cpu.excecuteCycle())
		{
			int cputime = cpu.cputime;
			// Aging
			if(aging && (cputime % agetime == 0))
			{
			   cpu.scheduler.promoteAllProcesses();
			}
			// Check for pausing.
			if(pause && (cputime == pauseat))
			{
			   Scanner s = new Scanner(System.in);
			   System.out.print(CPU.getCPUTime() + "\tPause. Continue (Y/N)? ");
			   String input = s.next();
			   if(input.equalsIgnoreCase("N"))
			   {
			      System.out.print("Do you want to display the queuesw now? (Y/N) ");
			      input = s.next();
			      if(input.equalsIgnoreCase("Y"))
			      {
			         cpu.scheduler.printQueues();
			      }
			      System.out.print("Do you want to pause again? (Y/N) ");
			      input = s.next();
			      if(input.equalsIgnoreCase("Y"))
			      {
			         System.out.print("Enter the time at which the simulator should pause: ");
			         {
			            pauseat = s.nextInt();
			         }
			      }
			   }
			}
			if(displayqueues && (cputime % displayqueueinterval == 0))
			{
			   cpu.scheduler.printQueues();
			}
		}
		//compute metrics
		averageMetrics();
	}
	
}
