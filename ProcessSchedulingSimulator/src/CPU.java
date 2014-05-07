/**
   COPYRIGHT (C) 2010 Micheal Crawford, Stefan Gloutnikov, Sampriya Chandra. All Rights Reserved.
   Class CPU used to drive the scheduler.
   Solves CS149 Homework Assignment #1
   @author Micheal Crawford
   @author Stefan Gloutnikov
   @author Sampriya Chandra
   @version 1.01 2010/03/09

*/

import java.util.Random;

/**
 * The Class CPU.
 */
public class CPU {

	/** The current process. */
	Process current;
	
	/** The scheduler. */
	Scheduler scheduler;
	
	/** Current cpu time in the simulator. */
	static int cputime;
	
	/** The generator. */
	Random generator;
	
	/**
	 * Instantiates a new cPU.
	 */
	public CPU()
	{
	   scheduler = new Scheduler();
	   cputime = 0;
	   generator = new Random();
	   
	}
	
	/**
	 * Gets the CPU time.
	 * 
	 * @return the CPU time
	 */
	public static String getCPUTime()
	{
		return String.valueOf(cputime + ".0");
	}
		
	/**
	 * Process io.
	 * 
	 * @return true, if IO needed.
	 */
	private boolean processIO()
	{
		boolean ret = false; //didnt add to queue
		
		if(generator.nextDouble() <= current.dprob)
		{
			current.IOtime = (int) (2 * current.avgd * generator.nextDouble());
			if(ProcessSchedulingSimulator.trace)
			{
				System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " requests Disk operation (" + current.IOtime + ")");
				System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " added to Disk Queue");
			}
			scheduler.queueDisk(current);
			ret = true;
		}
		else if(generator.nextDouble() <= current.p1prob)
		{
			current.IOtime = (int) (2 * current.avgp1 * generator.nextDouble());
			if(ProcessSchedulingSimulator.trace)
			{
				System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " requests Printer1 operation (" + current.IOtime + ")");
				System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " added to Printer1 Queue");
			}
			scheduler.queuePrinter1(current);
			ret = true;
		}
		else if(generator.nextDouble() <= current.p2prob)
		{
			current.IOtime = (int) (2 * current.avgp2 * generator.nextDouble());
			if(ProcessSchedulingSimulator.trace)
			{
				System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " requests Printer2 operation (" + current.IOtime + ")");
				System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " added to Printer2 Queue");
			}
			scheduler.queuePrinter2(current);
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * Excecute cycle of the CPU.
	 * 
	 * @return true, if sumulation has completed.
	 */
	public boolean excecuteCycle()
	{
		boolean ret = false;
		scheduler.queueIncomingProcess(cputime);
		
		if(current == null)
		{
			assignProcess(scheduler.getNextProcess());
				
		}
		
		if(current != null)
		{
			Process imp = scheduler.getMoreImportant(current);
			if(imp != null)
			{
				scheduler.queueProcess(current);
				assignProcess(imp);
			}
			
			boolean iocall = processIO();
			if(!iocall)
			{
				boolean finished = current.executeCycle();
				if(finished)
				{
					scheduler.queueProcess(current);
					assignProcess(scheduler.getNextProcess());
				}
			}
			else
				assignProcess(scheduler.getNextProcess());
		}
		
		if(current == null && 
				!scheduler.Disk.isExecuting() && 
					!scheduler.Printer1.isExecuting() &&
						!scheduler.Printer2.isExecuting() &&
							scheduler.incoming.isEmpty())
			ret = true;
		
		if(!ret)
		{
		cputime++;
		scheduler.Disk.executeCycle();
		scheduler.Printer1.executeCycle();
		scheduler.Printer2.executeCycle();
		}
		
		return ret;
	}
	
	/**
	 * Assign a process.
	 * 
	 * @param p the process.
	 */
	private void assignProcess(Process p)
	{
		current = p;
		if(current != null && ProcessSchedulingSimulator.trace)
			System.out.println(CPU.getCPUTime() + "\tProcess " + current.pid + " assigned to the CPU");
	}
	
}
