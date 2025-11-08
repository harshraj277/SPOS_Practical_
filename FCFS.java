package FCFS;

import java.util.*;

class Process {
    int pid;             // Process ID
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int turnaroundTime;
}

public class FCFS {
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

        // Sort processes by arrival time (FCFS)
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                    Process temp = processes[j];
                    processes[j] = processes[j + 1];
                    processes[j + 1] = temp;
                }
            }
        }

        int currentTime = 0;
        float totalWaitingTime = 0, totalTurnaroundTime = 0;

        // Calculate waiting and turnaround times
        for (int i = 0; i < n; i++) {
            if (currentTime < processes[i].arrivalTime)
                currentTime = processes[i].arrivalTime;

            processes[i].waitingTime = currentTime - processes[i].arrivalTime;
            currentTime += processes[i].burstTime;
            processes[i].turnaroundTime = processes[i].waitingTime + processes[i].burstTime;

            totalWaitingTime += processes[i].waitingTime;
            totalTurnaroundTime += processes[i].turnaroundTime;
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

