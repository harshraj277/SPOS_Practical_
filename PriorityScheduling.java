package Priority;

import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int completionTime;
    int waitingTime;
    int turnaroundTime;
    boolean isCompleted;

    Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.isCompleted = false;
    }
}

public class PriorityScheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            int arrival = sc.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burst = sc.nextInt();
            System.out.print("Enter priority (lower number = higher priority) for process " + (i + 1) + ": ");
            int priority = sc.nextInt();

            processes.add(new Process(i + 1, arrival, burst, priority));
        }

        int completed = 0;
        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        while (completed < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            // Find the process with highest priority that has arrived and not completed
            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.arrivalTime <= currentTime && !p.isCompleted) {
                    if (p.priority < highestPriority) {
                        highestPriority = p.priority;
                        idx = i;
                    } else if (p.priority == highestPriority) {
                        // If priorities are same, choose process with earlier arrival time
                        if (p.arrivalTime < processes.get(idx).arrivalTime) {
                            idx = i;
                        }
                    }
                }
            }

            if (idx != -1) {
                Process p = processes.get(idx);
                currentTime += p.burstTime;
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

                totalWaitingTime += p.waitingTime;
                totalTurnaroundTime += p.turnaroundTime;

                p.isCompleted = true;
                completed++;
            } else {
                // No process arrived yet, move time forward
                currentTime++;
            }
        }

        System.out.println("\nPID\tArrival\tBurst\tPriority\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.priority +
                    "\t\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        System.out.println("\nAverage Waiting Time = " + (totalWaitingTime / n));
        System.out.println("Average Turnaround Time = " + (totalTurnaroundTime / n));

        sc.close();
    }
}
