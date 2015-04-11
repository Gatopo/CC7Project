package nachos.threads;

import nachos.machine.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
        Long machineTime = Machine.timer().getTime();
        for (int i = 0; i < waitingThreads.size(); i++){
            TimerThread waitingThread = waitingThreads.get(i);
            System.out.println("DOING SOMWTHING WITH: " + machineTime);
            System.out.println("WAITING TO: " +  waitingThread.getWaitingTimer());
            if (machineTime >= waitingThread.getWaitingTimer()){
                System.out.println("DOING SOMETHING");
                waitingThread = waitingThreads.get(i);
                KThread wakeThread = waitingThread.getWaitingKThread();
                waitingThread = waitingThreads.remove(i);

                wakeThread.ready();
            }
        }
        KThread.currentThread().yield();

    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
        // Implementation of waitUntil using Java PriorityQue, for this
        // TimerThread was implemented to order the threads.
        Machine.interrupt().disable();
        Long machineTime = Machine.timer().getTime() + x;
        KThread calledThread = KThread.currentThread();
        TimerThread timerThread = new TimerThread(calledThread, machineTime);
        waitingThreads.add(timerThread);
        Machine.interrupt().enable();
        calledThread.sleep();
        /*while (wakeTime > Machine.timer().getTime())
            KThread.yield();
        }*/
    }

    private ArrayList<TimerThread> waitingThreads = new ArrayList<TimerThread>();
}
