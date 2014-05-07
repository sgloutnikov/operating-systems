import java.util.ArrayList;

public class Memory
{
   int memorySize; // max memory in node
   int usedSpace;  // current used space
   int freeSpace;  // available space in node.
   int eventId;    // event currently occupying space
   String marker;  // used to mark memory; o - free, x - used
   int power;
   ArrayList<Memory> lastLevelNodes = null;
   Memory root = null;
   Memory left = null;
   Memory right = null;

   public Memory()
   {
   }

   public Memory(int p)
   {
      // Base Node Creation
      this.power = p;
      this.memorySize = (int) Math.pow(2, p);
      this.freeSpace = (int) Math.pow(2, p);
      this.usedSpace = 0;
      this.eventId = -1;
      this.root = null;
      lastLevelNodes = new ArrayList<Memory>();
   }

   void createMemory(Memory root)
   {
      ArrayList<Memory> nodeList = new ArrayList<Memory>();
      ArrayList<Memory> addNodeList = new ArrayList<Memory>();
      nodeList.add(root);
      int n = root.power;

      for (int i = 0; i < root.power; i++) {
         n--;
         int nodeMemorySize = (int) Math.pow(2, n);

         for (Memory mem : nodeList) {
            // Create Left
            mem.left = new Memory();
            mem.left.root = mem;
            mem.left.memorySize = nodeMemorySize;
            if (nodeMemorySize == 1) {
               root.lastLevelNodes.add(mem.left);
               mem.left.marker = "o";
            }
            mem.left.freeSpace = nodeMemorySize;
            mem.left.usedSpace = 0;
            mem.left.eventId = -1;
            addNodeList.add(mem.left);

            // Create Right
            mem.right = new Memory();
            mem.right.root = mem;
            mem.right.memorySize = nodeMemorySize;
            if (mem.right.memorySize == 1) {
               root.lastLevelNodes.add(mem.right);
               mem.right.marker = "o";
            }
            mem.right.freeSpace = nodeMemorySize;
            mem.right.usedSpace = 0;
            mem.right.eventId = -1;
            addNodeList.add(mem.right);
         }
         nodeList.clear();
         for (Memory mem : addNodeList) {
            nodeList.add(mem);
         }
         addNodeList.clear();
      }
   }


   void allocate(Memory mem, int id, int space, boolean p)
   {
      Memory fillNode = null;
      Memory currentNode = mem;
      int spaceCopy = space;
      if ((space > mem.freeSpace) && (p == true)) {
         System.out.println("+++ ALLOCATION FAILED! +++ \t Not enough space for " + space + "pages");
         BuddySystemMM.sb.append("+++ ALLOCATION FAILED! +++ \t Not enough space for " + space + "pages\n");
         return;
      }
      else if ((space > mem.freeSpace) && (p == false)) {
         return;
      }

      /* In the case that requested allocation = max size and is free */
      if ((space == currentNode.memorySize) && (space == currentNode.freeSpace)) {
         currentNode.eventId = id;
         currentNode.freeSpace = 0;
         currentNode.usedSpace = space;
         for (Memory m : currentNode.lastLevelNodes) {
            m.freeSpace = 0;
            m.usedSpace = 1;
            m.eventId = id;
            m.marker = "x";
         }
         return;
      }

      // Find the node which will hold the pages
      while ((currentNode.memorySize != space) && (currentNode.freeSpace >= space)) {
         //Go Left
         if (currentNode.left.freeSpace >= space) {
            currentNode = currentNode.left;
            fillNode = currentNode;
         }
         //Go Right
         else if (currentNode.right.freeSpace >= space) {
            currentNode = currentNode.right;
            fillNode = currentNode;
         }
         // Not Enough Space
         else {
            System.out.println("+++ ALLOCATION FAILED! +++ \t Not enough space for " + space + "pages");
            BuddySystemMM.sb.append("+++ ALLOCATION FAILED! +++ \t Not enough space for " + space + "pages\n");
            return;
         }


      }

      //Fill information about allocation TO ROOT
      //Here currentNode is equal to fillNode
      while (currentNode.root != null) {
         currentNode.usedSpace = currentNode.usedSpace + space;
         currentNode.freeSpace = currentNode.freeSpace - space;
         if (currentNode.memorySize == space) {
            currentNode.eventId = id;
         }
         currentNode = currentNode.root;
      }
      // Fill information about allocation TO ACTUAL ROOT
      // currentNode is equal to the ACTUAL ROOT
      currentNode.usedSpace = currentNode.usedSpace + space;
      currentNode.freeSpace = currentNode.freeSpace - space;

      //Fill information about allocation TO BOTTOM
      //Here currentNode != fillNode, use fillNode

      // fillNode is actually the last node. Allocation was 1 page.
      if ((fillNode.left == null) && (fillNode.right == null)) {
         fillNode.eventId = id;
         fillNode.marker = "x";
         fillNode.usedSpace = 1;
         fillNode.freeSpace = 0;
      }
      // fillNode is above the last nodes.
      else {
         ArrayList<Memory> nodeList = new ArrayList<Memory>();
         ArrayList<Memory> addNodeList = new ArrayList<Memory>();
         nodeList.add(fillNode);
         while (spaceCopy != 1) {
            for (Memory memo : nodeList) {
               addNodeList.add(memo.left);
               addNodeList.add(memo.right);
            }
            nodeList.clear();
            spaceCopy = spaceCopy / 2;
            for (Memory memo : addNodeList) {
               nodeList.add(memo);
            }
            addNodeList.clear();

         }
         for (Memory memo : nodeList) {
            memo.usedSpace = 1;
            memo.freeSpace = 0;
            memo.eventId = id;
            memo.marker = "x";
         }

      }

   }

   void deAllocate(Memory mem, int id, int space)
   {
      Memory currentNode = mem;
      Memory fillNode = null;


      ArrayList<Memory> nodeList = new ArrayList<Memory>();
      ArrayList<Memory> addNodeList = new ArrayList<Memory>();
      nodeList.add(currentNode);

      // Deallocate Root Node
      currentNode.usedSpace = currentNode.usedSpace - space;
      currentNode.freeSpace = currentNode.freeSpace + space;


      // Find the fill node
      while (fillNode == null) {
         for (Memory memo : nodeList) {
            if (memo.eventId == id) {
               fillNode = memo;
               break;
            }
            else {
               addNodeList.add(memo.left);
               addNodeList.add(memo.right);
            }
         }
         nodeList.clear();
         for (Memory memo : addNodeList) {
            nodeList.add(memo);
         }
         addNodeList.clear();
      }


      currentNode = fillNode;

      for (Memory memo : mem.lastLevelNodes) {
         if (memo.eventId == id) {
            memo.marker = "o";
         }

      }

      // Clear spaces fillNode TO ROOT
      while (currentNode.root != null) {
         if (currentNode.eventId == id) {
            currentNode.eventId = -1;
         }
         currentNode.usedSpace = currentNode.usedSpace - space;
         currentNode.freeSpace = currentNode.freeSpace + space;
         currentNode = currentNode.root;
      }


   }


}
