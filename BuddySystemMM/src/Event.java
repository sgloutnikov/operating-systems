
public class Event
{
   int name;
   String action;
   int space;
   int actualSpace;

   public Event(String n, String a, int s)
   {
      this.name = Integer.parseInt(n);
      this.action = a;
      this.space = s;
      this.actualSpace = calcActualSpace(this.space);
   }

   /* Calculate the smallest power of 2 pagest needed
      to allocate s. A process can allocate at maximum of
      4096 pages.
    */

   public int calcActualSpace(int s)
   {
      int aSpace = 0;
      for (int i = 0; i <= 12; i++) {
         aSpace = (int) Math.pow(2, i);
         if (s <= aSpace) {
            break;
         }
      }

      return aSpace;
   }

   void printAllocateTrace(Event e, int eventNum)
   {
      System.out.println(eventNum + "\t\t\t\t" + e.space + "\t\t\t\t" + e.actualSpace + "\t\t\t\t" + "---");
      BuddySystemMM.sb.append(eventNum + "\t\t\t\t" + e.space + "\t\t\t\t" + e.actualSpace + "\t\t\t\t" + "---\n");
   }

   void printDeallocateTrace(Event e, int eventNum, int actPages)
   {
      System.out.println(eventNum + "\t\t\t\t" + "---\t\t\t\t---" + "\t\t\t\t" + actPages + "(" + e.space + ")");
      BuddySystemMM.sb.append(eventNum + "\t\t\t\t" + "---\t\t\t\t---" + "\t\t\t\t" + actPages + "(" + e.space + ")\n");
   }
}
