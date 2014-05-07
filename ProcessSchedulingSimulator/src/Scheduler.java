/**
   COPYRIGHT (C) 2010 Micheal Crawford, Stefan Gloutnikov, Sampriya Chandra. All Rights Reserved.
   Class Scheduler simulates the process scheduler. Handles ready queues and process assigning
   to appropriate queues.
   Solves CS149 Homework Assignment #1
   @author Micheal Crawford
   @author Stefan Gloutnikov
   @author Sampriya Chandra
   @version 1.01 2010/03/09

*/

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.LinkedList;

/**
 * The Class Scheduler.
 */
public class Scheduler {

	/** Q1. */
	ReadyQueue Q1;
	
	/** Q2. */
	ReadyQueue Q2;
	
	/** Q3. */
	ReadyQueue Q3;
	
	/** Q4. */
	ReadyQueue Q4;
	
	/** Disk. */
	IODevice Disk;
	
	/** Printer1. */
	IODevice Printer1;
	
	/** Printer2. */
	IODevice Printer2;
	
	/** Incoming Queue where all loaded from
	 * file processes enter.. */
	Queue<Process> incoming;
	
	/** ArrayList of Processes where all completed
	 * processes enter. */
	ArrayList<Process> completed;
	
	/**
	 * Instantiates a new scheduler.
	 */
	public Scheduler()
	{
		incoming = new LinkedList<Process>();
		Q1 = new ReadyQueue(8);
		Q2 = new ReadyQueue(16);
		Q3 = new ReadyQueue(30);
		Q4 = new ReadyQueue(50);
		Disk = new IODevice(this, "Disk transfer", "Disk");
		Printer1 = new IODevice(this, "Printer1 operation", "Printer1");
		Printer2 = new IODevice(this, "Printer2 operation", "Printer2");
		
		completed = new ArrayList<Process>();
	}
	
	/**
	 * Gets the next process.
	 * 
	 * @return the next process
	 */
	public Process getNextProcess()
	{
		Process ret = Q1.getNext();
		if(ret == null)
			ret = Q2.getNext();
		if(ret == null)
			ret = Q3.getNext();
		if(ret == null)
			ret = Q4.getNext();
		
		return ret;
	}
	
	/**
	 * Gets the more important process for preemption.
	 * 
	 * @param p - Process.
	 * @return the more important process.
	 */
	public Process getMoreImportant(Process p)
	{
		int curp = p.priority;
		Process imp = null;
		switch(curp)
		{
		case 0: case 1: case 2: case 3:
			break; //no process more important.
		
		case 4: case 5: case 6: case 7:
		{
			imp = Q1.getNext();
			break;
		} 
			
		case 8: case 9: case 10: case 11:
		{
			imp = Q1.getNext();
			if(imp == null)
				imp = Q2.getNext();
			break;
		} 
			
		case 12: case 13: case 14: case 15:
		{
			imp = Q1.getNext();
			if(imp == null)
				imp = Q2.getNext();
			if(imp == null)
				imp = Q3.getNext();
			break;
		} 
		default:
			break;
		}
		return imp;
	}
	
	/**
	 * Process incoming processes from file.
	 * 
	 * @param f the file.
	 */
	public void processIncoming(File f)
	{
		try
		{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			while((line = br.readLine()) != null)
			{
				if(!((line.trim()).equals("")))
				{
					String[] fields = line.split("\\s+");
					if(fields.length == 10)
					{
					int pid = Integer.parseInt(fields[0]);
					int arrivaltime = Integer.parseInt(fields[1]);
					int remainingtime = Integer.parseInt(fields[2]);
					double dprob = Double.parseDouble(fields[3]);
					int avgd = Integer.parseInt(fields[4]);
					double p1prob = Double.parseDouble(fields[5]);
					int avgp1 = Integer.parseInt(fields[6]);
					double p2prob = Double.parseDouble(fields[7]);
					int avgp2 = Integer.parseInt(fields[8]);
					int priority = Integer.parseInt(fields[9]);
					
					Process cur = new Process(pid, arrivaltime, remainingtime, dprob, 
							avgd, p1prob, avgp1, p2prob, avgp2, priority);
					
					incoming.add(cur);
					}
					
				}
			}
			
			
			br.close();
			fr.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	   
	}
	
	/**
	 * Queue process in appropriate queue.
	 * 
	 * @param p the process.
	 */
	public void queueProcess(Process p)
	{
		if(p.remainingtime > 0)
		{
			if(ProcessSchedulingSimulator.trace)
				System.out.print(CPU.getCPUTime() + "\tProcess " + p.pid + " enters ");
			switch(p.priority)
			{
			case 0: case 1: case 2: case 3:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q1");
				Q1.addProcess(p);
				break;
			case 4: case 5: case 6: case 7:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q2");
				Q2.addProcess(p);
				break;
			case 8: case 9: case 10: case 11:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q3");
				Q3.addProcess(p);
				break;
			case 12: case 13: case 14: case 15:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q4");
				Q4.addProcess(p);
				break;
			default:
				break;
			}
		}
		else
		{
			if(ProcessSchedulingSimulator.trace)
				System.out.println(CPU.getCPUTime() + "\tProcess " + p.pid + " terminates");
			p.completiontime = CPU.cputime;
			completed.add(p);
		}
	}
	
	/**
	 * Queues to disk queue.
	 * 
	 * @param p process that needs the disk.
	 */
	public void queueDisk(Process p)
	{
		Disk.doIO(p);
	}
	
	/**
	 * Queues to printer1 queue.
	 * 
	 * @param p process that needs printer1.
	 */
	public void queuePrinter1(Process p)
	{
		Printer1.doIO(p);
	}
	
	/**
	 * Queues to printer2 queue.
	 * 
	 * @param p process that needs printer2.
	 */
	public void queuePrinter2(Process p)
	{
		Printer2.doIO(p);
	}
	
	/**
	 * Promotes all processes if aging is enabled.
	 */
	public void promoteAllProcesses()
	{
		Process next = null;
		ArrayList<Process> allprocs = new ArrayList<Process>();
		while((next = Q1.getNext()) != null)
		{
			next.promote();
			allprocs.add(next);
		}
		while((next = Q2.getNext()) != null)
		{
			next.promote();
			allprocs.add(next);
		}
		while((next = Q3.getNext()) != null)
		{
			next.promote();
			allprocs.add(next);
		}
		while((next = Q4.getNext()) != null)
		{
			next.promote();
			allprocs.add(next);
		}
		
		Process[] procs = allprocs.toArray(new Process[allprocs.size()]);
		for(int i = 0; i < procs.length; i++)
			queueProcess(procs[i]);
	}
	
	/**
	 * Queues an incoming process.
	 * 
	 * @param cputime the current CPU time.
	 */
	public void queueIncomingProcess(int cputime)
	{
		Process first = incoming.peek();
		if(first != null && (first.arrivaltime == cputime))
		{
			if(ProcessSchedulingSimulator.trace)
				System.out.print(CPU.getCPUTime() + "\tNew Process (" + first.pid + ") enters ");
			first = incoming.poll();
			switch(first.priority)
			{
			case 0: case 1: case 2: case 3:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q1");
				Q1.addProcess(first);
				break;
			case 4: case 5: case 6: case 7:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q2");
				Q2.addProcess(first);
				break;
			case 8: case 9: case 10: case 11:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q3");
				Q3.addProcess(first);
				break;
			case 12: case 13: case 14: case 15:
				if(ProcessSchedulingSimulator.trace)
					System.out.println("Q4");
				Q4.addProcess(first);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Prints the queues. Used when user specifies print queues
	 * every x msec.
	 */
	public void printQueues()
	{
	   String cputime = CPU.getCPUTime();
	   StringBuilder sb = new StringBuilder();
	   int len = cputime.length();
	   for(int i = 0; i < len; i++)
		   sb.append(" ");
		
	   String tab = sb.toString() + "\t";	
	   System.out.print(cputime + "\tQ1:");
	   Q1.printContents();
	   System.out.print(tab + "Q2:");
	   Q2.printContents();
	   System.out.print(tab + "Q3:");
	   Q3.printContents();
	   System.out.print(tab + "Q4:");
	   Q4.printContents();
	   System.out.print(tab + "Disk Q:");
	   Disk.printContents();
	   System.out.print(tab + "Printer1 Q:");
	   Printer1.printContents();
	   System.out.print(tab + "Printer2 Q:");
	   Printer2.printContents();  
	}
	
	
	/**
	 * Prints the incoming processes from file if needed.
	 */
	public void printIncomingProcesses()
	{
		int len = incoming.size();
		for(int i = 0; i < len; i++)
		{
			Process p = incoming.poll();
			if(p != null)
			{
				p.printProcessInfo();
			incoming.add(p);
			}
		}
	}
	
}
