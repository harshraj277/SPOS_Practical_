package SJF;

import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int waitingTime = 0;
    int turnaroundTime = 0;
    boolean completed = false;
}

public class SJF_NonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];

        // Input process details
        for (int i = 0; i < n; i++) {
            processes[i] = new Process();
            processes[i].pid = i + 1;
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            processes[i].arrivalTime = sc.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            processes[i].burstTime = sc.nextInt();
        }

        int completed = 0, currentTime = 0;
        float totalWaitingTime = 0, totalTurnaroundTime = 0;

        while (completed < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            // Find process with shortest burst time available at current time
            for (int i = 0; i < n; i++) {
                if (!processes[i].completed &&
                    processes[i].arrivalTime <= currentTime &&
                    processes[i].burstTime < minBurst) {
                    minBurst = processes[i].burstTime;
                    idx = i;
                }
            }

            if (idx != -1) {
                // Process found
                processes[idx].waitingTime = currentTime - processes[idx].arrivalTime;
                currentTime += processes[idx].burstTime;
                processes[idx].turnaroundTime = processes[idx].waitingTime + processes[idx].burstTime;
                processes[idx].completed = true;
                completed++;

                totalWaitingTime += processes[idx].waitingTime;
                totalTurnaroundTime += processes[idx].turnaroundTime;
            } else {
                // No process has arrived yet, advance time
                currentTime++;
            }
        }

        // Display results
        System.out.println("\nPID\tArrival\tBurst\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            System.out.println(processes[i].pid + "\t"
                    + processes[i].arrivalTime + "\t"
                    + processes[i].burstTime + "\t"
                    + processes[i].waitingTime + "\t"
                    + processes[i].turnaroundTime);
        }

        System.out.println("\nAverage Waiting Time = " + (totalWaitingTime / n));
        System.out.println("Average Turnaround Time = " + (totalTurnaroundTime / n));

        sc.close();
    }
}
