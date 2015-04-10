package nachos.threads;

/**
 * Created by mario on 15/03/15.
 * --------------------------
 * TimerThread Description
 * --------------------------
 * This class is a helper class for the implementation of the Alarm waitUntil().
 * It's an object that has the KThread and it's timer in one single place.
 *
 */
public class TimerThread{
    private Long timerThread;
    private KThread waitingThread;

    public TimerThread(KThread waitingThread, Long timerThread){
        this.waitingThread = waitingThread;
        this.timerThread = timerThread;
    }

    public KThread getWaitingKThread(){
        return waitingThread;
    }
    public Long getWaitingTimer(){
        return timerThread;
    }

}