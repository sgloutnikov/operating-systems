/**
   COPYRIGHT (C) 2010 Micheal Crawford, Stefan Gloutnikov, Sampriya Chandra. All Rights Reserved.
   Class Process to simulate a process control block.
   Solves CS149 Homework Assignment #1
   @author Micheal Crawford
   @author Stefan Gloutnikov
   @author Sampriya Chandra
   @version 1.01 2010/03/09

*/

public class Process 
{	
	/** The pid. */
	int pid;
	
	/** The arrivaltime. */
	int arrivaltime;
	
	/** The completiontime. */
	int completiontime;
	
	/** The remainingtime. */
	int remainingtime;
	
	/** The slicetime. */
	int slicetime;
		
	/** The dprob. */
	double dprob;
	
	/** The p1prob. */
	double p1prob;
	
	/** The p2prob. */
	double p2prob;
	
	/** The avgd. */
	int avgd;
	
	/** The avgp1. */
	int avgp1;
	
	/** The avgp2. */
	int avgp2;
	
	/** The priority. */
	int priority;
	
	/** The first execute. */
	boolean firstExecute;
	
	/** The IO time. */
	int IOtime;
	
	/** The first execute time. */
	int firstExecuteTime;
	
	/** The response time. */
	int responseTime;
	
	/** The turn around time. */
	int turnAroundTime;
	
	/** The waiting time. */
	int waitingTime;
	
	/** The start wait time. */
	int startWaitTime;
	
	/**
	 * Instantiates a new process.
	 * 
	 * @param pid the pid
	 * @param arrivaltime the arrivaltime
	 * @param remainingtime the remainingtime
	 * @param dprob the dprob
	 * @param avgd the avgd
	 * @param p1prob the p1prob
	 * @param avgp1 the avgp1
	 * @param p2prob the p2prob
	 * @param avgp2 the avgp2
	 * @param priority the priority
	 */
	public Process(int pid, int arrivaltime, int remainingtime, 
			double dprob, int avgd, double p1prob, int avgp1, double p2prob, int avgp2, int priority)
	{
		this.pid = pid;
		this.arrivaltime = arrivaltime;
		this.remainingtime = remainingtime;
		this.dprob = dprob;
		this.avgd = avgd;
		this.p1prob = p1prob;
		this.avgp1 = avgp1;
		this.p2prob = p2prob;
		this.avgp2 = avgp2;
		this.priority = priority;
		this.firstExecute = true;
		this.waitingTime = 0;
	}
	
	/**
	 * Sets the time slice.
	 * 
	 * @param timeslice the new time slice
	 */
	public void setTimeSlice(int timeslice)
	{
		slicetime = timeslice;
	}
	
	/**
	 * Promotes a process.
	 */
	public void promote()
	{
		if(priority > 0)
			priority--;
	}
	
	/**
	 * Demotes a process.
	 */
	public void demote()
	{
		if((priority+4) < 16)
			priority+=4;
		else
			priority = 15;
	}
	
	/**
	 * Execute cycle.
	 * 
	 * @return true, if process is done.
	 */
	public boolean executeCycle()
	{
		if(firstExecute)
		{
			firstExecuteTime = CPU.cputime;
			firstExecute = false;
		}
		boolean ret = false;
		if(remainingtime == 0 || slicetime == 0) {
	      // Process demotion due to used up timeslice.
	      demote();
	      ret = true;
	   }
		remainingtime--;
		slicetime--;
	
		return ret;
	}
	
	/**
	 * Calculate metrics for responseTime and turnAroundTime.
	 */
	public void calculateMetrics()
	{
		responseTime = firstExecuteTime - arrivaltime;
		turnAroundTime = completiontime - firstExecuteTime;
	}
	
	/**
	 * Prints the process info.
	 */
	public void printProcessInfo()
	{
		System.out.println("PRINTING PROCESS " + this.pid);
		System.out.println("arrivaltime=" + arrivaltime);
		System.out.println("completiontime=" + completiontime);
		System.out.println("remainingtime=" + remainingtime);
		System.out.println("slicetime=" + slicetime);
		System.out.println("dprob=" + dprob);
		System.out.println("p1prob=" + p1prob);
		System.out.println("p2prob=" + p2prob);
		System.out.println("avgd=" + avgd);
		System.out.println("avgp1=" + avgp1);
		System.out.println("avgp2=" + avgp2);
		System.out.println("priority=" + priority);
		System.out.println("END PRINT PROCESS INFORMATION");
	}
}
