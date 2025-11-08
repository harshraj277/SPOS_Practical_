package Memory_replacement_strategies;

//Assignment No. 6
//Program to simulate Memory placement strategies:
//Best Fit, First Fit, Next Fit, Worst Fit

import java.util.Scanner;

public class MemoryAllocation {

 // Method for First Fit allocation
 static void firstFit(int blockSize[], int m, int processSize[], int n) {
     int allocation[] = new int[n];

     // Initially no block is assigned
     for (int i = 0; i < n; i++)
         allocation[i] = -1;

     // Pick each process and find suitable block
     for (int i = 0; i < n; i++) {
         for (int j = 0; j < m; j++) {
             if (blockSize[j] >= processSize[i]) {
                 allocation[i] = j;
                 blockSize[j] -= processSize[i];
                 break;
             }
         }
     }

     System.out.println("\nFirst Fit Allocation:");
     printAllocation(allocation, processSize);
 }

 // Method for Best Fit allocation
 static void bestFit(int blockSize[], int m, int processSize[], int n) {
     int allocation[] = new int[n];

     for (int i = 0; i < n; i++)
         allocation[i] = -1;

     for (int i = 0; i < n; i++) {
         int bestIdx = -1;
         for (int j = 0; j < m; j++) {
             if (blockSize[j] >= processSize[i]) {
                 if (bestIdx == -1 || blockSize[j] < blockSize[bestIdx])
                     bestIdx = j;
             }
         }

         if (bestIdx != -1) {
             allocation[i] = bestIdx;
             blockSize[bestIdx] -= processSize[i];
         }
     }

     System.out.println("\nBest Fit Allocation:");
     printAllocation(allocation, processSize);
 }

 // Method for Worst Fit allocation
 static void worstFit(int blockSize[], int m, int processSize[], int n) {
     int allocation[] = new int[n];

     for (int i = 0; i < n; i++)
         allocation[i] = -1;

     for (int i = 0; i < n; i++) {
         int worstIdx = -1;
         for (int j = 0; j < m; j++) {
             if (blockSize[j] >= processSize[i]) {
                 if (worstIdx == -1 || blockSize[j] > blockSize[worstIdx])
                     worstIdx = j;
             }
         }

         if (worstIdx != -1) {
             allocation[i] = worstIdx;
             blockSize[worstIdx] -= processSize[i];
         }
     }

     System.out.println("\nWorst Fit Allocation:");
     printAllocation(allocation, processSize);
 }

 // Method for Next Fit allocation
 static void nextFit(int blockSize[], int m, int processSize[], int n) {
     int allocation[] = new int[n];
     for (int i = 0; i < n; i++)
         allocation[i] = -1;

     int j = 0;
     for (int i = 0; i < n; i++) {
         int count = 0;
         while (count < m) {
             if (blockSize[j] >= processSize[i]) {
                 allocation[i] = j;
                 blockSize[j] -= processSize[i];
                 break;
             }
             j = (j + 1) % m;
             count++;
         }
     }

     System.out.println("\nNext Fit Allocation:");
     printAllocation(allocation, processSize);
 }

 // Helper method to print results
 static void printAllocation(int allocation[], int processSize[]) {
     System.out.println("Process No.\tProcess Size\tBlock No.");
     for (int i = 0; i < processSize.length; i++) {
         System.out.print("   " + (i + 1) + "\t\t" + processSize[i] + "\t\t");
         if (allocation[i] != -1)
             System.out.println(allocation[i] + 1);
         else
             System.out.println("Not Allocated");
     }
 }

 public static void main(String[] args) {
     Scanner sc = new Scanner(System.in);

     System.out.print("Enter number of memory blocks: ");
     int m = sc.nextInt();
     int blockSize[] = new int[m];
     System.out.println("Enter size of each memory block:");
     for (int i = 0; i < m; i++)
         blockSize[i] = sc.nextInt();

     System.out.print("Enter number of processes: ");
     int n = sc.nextInt();
     int processSize[] = new int[n];
     System.out.println("Enter size of each process:");
     for (int i = 0; i < n; i++)
         processSize[i] = sc.nextInt();

     // Copy of block sizes for each strategy
     firstFit(blockSize.clone(), m, processSize.clone(), n);
     bestFit(blockSize.clone(), m, processSize.clone(), n);
     nextFit(blockSize.clone(), m, processSize.clone(), n);
     worstFit(blockSize.clone(), m, processSize.clone(), n);

     sc.close();
 }
}



