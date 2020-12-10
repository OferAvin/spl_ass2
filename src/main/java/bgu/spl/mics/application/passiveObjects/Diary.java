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
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long C3POTerminate;
    private long HanSoloTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }
    private Diary(){
        totalAttacks = new AtomicInteger(0);
        HanSoloFinish = 0;
        C3POFinish = 0;
        R2D2Deactivate = 0;
        LeiaTerminate = 0;
        C3POTerminate = 0;
        HanSoloTerminate = 0;
        R2D2Terminate = 0;
        LandoTerminate = 0;
    }

    //getters
    public static Diary getInstance(){
        return SingletonHolder.instance;
    }

    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }
    public long getHanSoloFinish() {
        return HanSoloFinish;
    }
    public long getC3POFinish() {
        return C3POFinish;
    }
    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }
    public long getLeiaTerminate() {
        return LeiaTerminate;
    }
    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }
    public long getC3POTerminate() {
        return C3POTerminate;
    }
    public long getR2D2Terminate() {
        return R2D2Terminate;
    }
    public long getLandoTerminate() {
        return LandoTerminate;
    }

    //setters
    public void increaseAttacksNum() {
        totalAttacks.incrementAndGet();
    }
    public void setHanSoloFinish(long timestamp){
        HanSoloFinish = timestamp;
    }
    public void setC3POFinish(long timestamp){
        C3POFinish = timestamp;
    }
    public void setR2D2Deactivate(long timestamp){
        R2D2Deactivate = timestamp;
    }
    public void setLeiaTerminate(long timestamp){
        LeiaTerminate = timestamp;
    }
    public void setC3POTerminate(long timestamp){
        C3POTerminate = timestamp;
    }
    public void setHanSoloTerminate(long timestamp){
        HanSoloTerminate = timestamp;
    }
    public void setR2D2Terminate(long timestamp){
        R2D2Terminate = timestamp;
    }
    public void setLandoTerminate(long timestamp){
        LandoTerminate = timestamp;
    }
}

