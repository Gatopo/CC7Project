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
        busyListener = new Condition(communicatorLock);
        busySpeaker = new Condition(communicatorLock);
        waitForListener = new Condition(communicatorLock);
        waitForSpeaker = new Condition(communicatorLock);
        waitForRead = new Condition(communicatorLock);
        message = 0;
        isListening = false;
        isSpeaking = false;
        messageIsReady = false;
        messageReaded = false;
        messageComplete = false;
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
        while(isSpeaking){
            waitForSpeaker.wake();
            busySpeaker.sleep();
        }
        isSpeaking = true;
        waitForSpeaker.wake();
        while(!isListening){
            waitForSpeaker.wake();
            waitForListener.sleep();
        }
        message = word;
        messageIsReady = true;
        //System.out.println("+++++++ THE MESSAGE IS: " + word);
        waitForSpeaker.wake();
        while(!messageReaded){
            waitForSpeaker.wake();
            waitForListener.sleep();
        }
        isSpeaking = false;
        messageIsReady = false;
        messageComplete = true;
        waitForSpeaker.wakeAll();
        messageReaded = false;
        busySpeaker.wakeAll();
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
        while(isListening){
            waitForListener.wake();
            busyListener.sleep();
        }
        isListening = true;
        waitForListener.wake();
        while(!isSpeaking || !messageIsReady){
            waitForListener.wake();
            waitForSpeaker.sleep();
        }
        int wordReaded = message;
        //System.out.println("------- MESSAGE RECEIVED : " + wordReaded);
        messageReaded = true;        
        waitForListener.wake();
        while(!messageComplete){
            waitForListener.wake();
            waitForSpeaker.sleep();
        }
        //busySpeaker.wakeAll();
        messageComplete = false;
        isListening = false;
        busyListener.wakeAll();
        communicatorLock.release();
	return wordReaded;
    }

    private Lock communicatorLock;
    private Condition busyListener;
    private Condition busySpeaker;
    private Condition waitForListener;
    private Condition waitForSpeaker;
    private Condition waitForRead;
    private int message;
    private boolean isListening;
    private boolean isSpeaking;
    private boolean messageIsReady;
    private boolean messageReaded;
    private boolean messageComplete;


    //*** EVERYTHING BELOW HERE IS JUST FOR TESTING ***
    private static class CommunicatorSendTest implements Runnable {
        private String name;
        private Communicator communicator; 
        private int word;

        CommunicatorSendTest(String name, Communicator communicator, int word) {
            this.name=name;
            this.communicator=communicator;
            this.word=word;
        }
    
        public void run() {
            System.out.println("*** " + name + " ===> Before call to speak with " + word);
            communicator.speak(word);
            System.out.println("*** " + name + " ===> After call to speak with " + word);
        }
    }


    private static class CommunicatorListenTest implements Runnable {
        private String name;
        private Communicator communicator;

        CommunicatorListenTest(String name, Communicator communicator) {
            this.name=name;
            this.communicator=communicator;
        }
        
        public void run() {
            System.out.println("*** " + name + " ===> Before call to listen.");
            int word=communicator.listen();
            System.out.println("*** " + name + " ===> After call to listen. Received " + word);
        }
    }

    public static void selfTest() {
        // Communicator Tests
        Communicator communicator = new Communicator();
        new KThread(new CommunicatorSendTest("one",communicator,10)).fork();
        new KThread(new CommunicatorSendTest("two",communicator,20)).fork();
        new KThread(new CommunicatorListenTest("one",communicator)).fork();
        new KThread(new CommunicatorListenTest("two",communicator)).fork();
    }
}
