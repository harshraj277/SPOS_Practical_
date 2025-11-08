package SRTF;

import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnaroundTime;
}

public class SRTF {
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
            processes[i].remainingTime = processes[i].burstTime;
            processes[i].completionTime = 0;
            processes[i].waitingTime = 0;
            processes[i].turnaroundTime = 0;
        }

        int completed = 0;
        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;

        while (completed < n) {
            int idx = -1;
            int minRemainingTime = Integer.MAX_VALUE;

            // Find the process with the shortest remaining time at the current time
            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime &&
                    processes[i].remainingTime > 0 &&
                    processes[i].remainingTime < minRemainingTime) {
                    minRemainingTime = processes[i].remainingTime;
                    idx = i;
                }
            }

            if (idx != -1) {
                // Execute process for 1 unit time
                processes[idx].remainingTime--;
                currentTime++;

                // If process is finished
                if (processes[idx].remainingTime == 0) {
                    completed++;
                    processes[idx].completionTime = currentTime;
                    processes[idx].turnaroundTime = processes[idx].completionTime - processes[idx].arrivalTime;
                    processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;

                    totalWaitingTime += processes[idx].waitingTime;
                    totalTurnaroundTime += processes[idx].turnaroundTime;
                }
            } else {
                // No process available to run, advance time
                currentTime++;
            }
        }

        // Output results
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
