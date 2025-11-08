package Scheduling_Algorithm;

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

public class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        System.out.print("Enter time quantum: ");
        int timeQuantum = sc.nextInt();

        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            processes[i] = new Process();
            processes[i].pid = i + 1;
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            processes[i].arrivalTime = sc.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            processes[i].burstTime = sc.nextInt();
            processes[i].remainingTime = processes[i].burstTime;
        }

        int currentTime = 0;
        int completed = 0;
        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n];

        // Enqueue processes that arrive at time 0
        for (int i = 0; i < n; i++) {
            if (processes[i].arrivalTime == 0) {
                queue.add(i);
                inQueue[i] = true;
            }
        }

        while (completed < n) {
            if (queue.isEmpty()) {
                currentTime++;
                // Check for new arrivals
                for (int i = 0; i < n; i++) {
                    if (!inQueue[i] && processes[i].arrivalTime <= currentTime) {
                        queue.add(i);
                        inQueue[i] = true;
                    }
                }
                continue;
            }

            int idx = queue.poll();
            int execTime = Math.min(processes[idx].remainingTime, timeQuantum);

            // Execute process for execTime
            processes[idx].remainingTime -= execTime;
            currentTime += execTime;

            // Check for new arrivals during execution
            for (int i = 0; i < n; i++) {
                if (!inQueue[i] && processes[i].arrivalTime <= currentTime) {
                    queue.add(i);
                    inQueue[i] = true;
                }
            }

            if (processes[idx].remainingTime == 0) {
                completed++;
                processes[idx].completionTime = currentTime;
                processes[idx].turnaroundTime = processes[idx].completionTime - processes[idx].arrivalTime;
                processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;
            } else {
                queue.add(idx);
            }
        }

        // Display results
        System.out.println("\nPID\tArrival\tBurst\tWaiting\tTurnaround");
        float totalWaitingTime = 0, totalTurnaroundTime = 0;

        for (int i = 0; i < n; i++) {
            System.out.println(processes[i].pid + "\t" +
                    processes[i].arrivalTime + "\t" +
                    processes[i].burstTime + "\t" +
                    processes[i].waitingTime + "\t" +
                    processes[i].turnaroundTime);
            totalWaitingTime += processes[i].waitingTime;
            totalTurnaroundTime += processes[i].turnaroundTime;
        }

        System.out.println("\nAverage Waiting Time = " + (totalWaitingTime / n));
        System.out.println("Average Turnaround Time = " + (totalTurnaroundTime / n));

        sc.close();
    }
}
