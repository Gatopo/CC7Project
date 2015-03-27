package nachos.threads;

/**
 * Created by mario on 15/03/15.
 * --------------------------
 * TimerThread Description
 * --------------------------
 * This class is a helper class for the implementation of the Alarm waitUntil().
 * It stores the thread with its timer and implements the compareTo method from the Comparable int.
 *
 */
public class TimerThread implements Comparable<TimerThread> {
    private Long timerThread;
    private KThread waitingThread;

    public void TimerThread(Long timerThread, KThread waitingThread){
        this.timerThread = timerThread;
        this.waitingThread = waitingThread;
    }

    @Override
    public int compareTo(TimerThread nextTimerThread) {
        System.out.println("COMPARE TO: " + nextTimerThread.timerThread);
        return this.timerThread.compareTo(nextTimerThread.timerThread);
    }
}
