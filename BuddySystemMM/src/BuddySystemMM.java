/**
 * User: Stefan
 * Date: Apr 16, 2010
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class BuddySystemMM
{
   boolean printTrace = false;
   public int n = 0;
   Memory m;
   ArrayList<Event> events = new ArrayList<Event>();

   public static StringBuffer sb;

   public static void main(String args[])
   {
      BuddySystemMM bsmm = new BuddySystemMM();
      sb = new StringBuffer();


      Scanner in = new Scanner(System.in);
      System.out.print("Enter the input file name: ");
      String fileStr = in.next();
      String outFileStr = fileStr + "_out.txt";
      File outputFile = new File(outFileStr);


      File inputFile = new File(fileStr);
      bsmm.processFile(inputFile);

      bsmm.m.createMemory(bsmm.m);

      if (bsmm.n < 6) {
         bsmm.printTrace = true;
      }

      if (bsmm.printTrace) {

         bsmm.printTrace();

         bsmm.printMemoryTrace(bsmm.m);

         System.out.println();
         sb.append("\n");
         System.out.println("Event # \t Allocate \t Actual Allocation \t Deallocate (Event #)");
         sb.append("Event # \t Allocate \t Actual Allocation \t Deallocate (Event #)\n");
      }


      int eventNum = 1;


      // Control loop
      for (Event e : bsmm.events) {

         if (e.action.equalsIgnoreCase("a")) {
            if (bsmm.printTrace) {
               e.printAllocateTrace(e, eventNum);
               bsmm.m.allocate(bsmm.m, e.name, e.actualSpace, bsmm.printTrace);
               bsmm.printMemoryTrace(bsmm.m);
               eventNum++;
            }
            else {
               bsmm.m.allocate(bsmm.m, e.name, e.actualSpace, bsmm.printTrace);
            }
         }

         if (e.action.equalsIgnoreCase("d")) {
            if (bsmm.printTrace) {
               e.printDeallocateTrace(e, eventNum, bsmm.events.get(e.space - 1).actualSpace);
               bsmm.m.deAllocate(bsmm.m, e.space, bsmm.events.get(e.space - 1).actualSpace);
               bsmm.printMemoryTrace(bsmm.m);
               eventNum++;
            }

         }
      }

      /*
      try {
         // Create file
         FileWriter fstream = new FileWriter(outputFile);
         BufferedWriter out = new BufferedWriter(fstream);
         out.write("Hello Java");
         //Close the output stream
         out.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      */

      System.out.println("\n\nSimulator Successfully Terminated!");

      try {
         if (bsmm.n < 6) {
            PrintStream ps = new PrintStream(outputFile);
            ps.append(sb);
         }

      }
      catch (Exception ex) {
         ex.printStackTrace();
      }


   }

   public void processFile(File f)
   {

      try {
         FileReader fr = new FileReader(f);
         BufferedReader br = new BufferedReader(fr);
         String line = null;

         while ((line = br.readLine()) != null) {

            if (!((line.trim()).equals(""))) {

               String[] fields = line.split("\\s+");

               if (fields.length == 2) {
                  n = Integer.parseInt(fields[1]);
                  m = new Memory(n);
               }
               if (fields.length == 3) {
                  String n = fields[0];
                  String a = fields[1];
                  int s = Integer.parseInt(fields[2]);

                  Event ev = new Event(n, a, s);
                  events.add(ev);
               }
            }
         }

      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   void printTrace()
   {
      System.out.println("Value of n = " + n + " (total number of pages: " + (int) Math.pow(2, n) + ")");
      sb.append("Value of n = " + n + " (total number of pages: " + (int) Math.pow(2, n) + ")\n");
   }

   void printMemoryTrace(Memory root)
   {
      int n = 0;
      for (Memory mem : root.lastLevelNodes) {
         n++;
         if (n % 4 == 0 && root.power > 2) {
            System.out.print(mem.marker + " ");
            sb.append(mem.marker + " ");
         }
         else {
            System.out.print(mem.marker);
            sb.append(mem.marker);

         }

      }
      System.out.println();
      sb.append("\n");

   }

}
