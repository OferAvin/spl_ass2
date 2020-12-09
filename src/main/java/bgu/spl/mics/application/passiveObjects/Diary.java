package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks;
    private ConcurrentHashMap<String, Long> timestamps;
//    long HanSoloFinish;
//    long C3POFinish;
//    long R2D2Deactivate;
//    long LeiaTerminate;
//    long C3PTerminate;
//    long R2D2Terminate;
//    long LandoTerminate;

    public void increaseAttacksNum() {
        totalAttacks.incrementAndGet();
    }
    public void logTimestamp(String eventToLog, Long timestamp){
        timestamps.put(eventToLog, timestamp);
    }

}

