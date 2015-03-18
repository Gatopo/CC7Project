package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
        communicatorLock = new Lock();
        conditionListener = new Condition2(communicatorLock);
        conditionSpeaker = new Condition2(communicatorLock);
        waitForListener = new Condition2(communicatorLock);
        waitForSpeaker = new Condition2(communicatorLock);
        message = 0;
        isListening = false;
        isSpeaking = false;
        messageIsReady = false;
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
        communicatorLock.acquire();
        while(isSpeaking)
            conditionSpeaker.sleep();
        isSpeaking = true;
        waitForSpeaker.wakeAll();
        while(!isListening)
            waitForListener.sleep();        
        message = word;
        messageIsReady = true;
        waitForSpeaker.wakeAll();
        isSpeaking = false;    
        communicatorLock.release();
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
        communicatorLock.acquire();
        while(isListening)
            conditionListener.sleep();
        isListening = true;
        waitForListener.wakeAll();
        while(!isSpeaking || !messageIsReady)
            waitForSpeaker.sleep();
        int wordReaded = message;
        messageIsReady = false;
        isListening = false;
        communicatorLock.release();
	return wordReaded;
    }

    private Lock communicatorLock;
    private Condition2 conditionListener;
    private Condition2 conditionSpeaker;
    private Condition2 waitForListener;
    private Condition2 waitForSpeaker;
    private int message;
    private boolean isListening;
    private boolean isSpeaking;
    private boolean messageIsReady;
}
