package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Attack;

public class Input {
    private Attack[]attacks;
    private int numEwoks;
    private long Lando_duration;
    private long R2D2_duration;

    public Attack[] getAttacks() {
        return attacks;
    }

    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
    }

    public int getNumEwoks() {
        return numEwoks;
    }

    public void setNumEwoks(int numEwoks) {
        this.numEwoks = numEwoks;
    }

    public long getLando_duration() {
        return Lando_duration;
    }

    public void setLando_duration(long lando_duration) {
        Lando_duration = lando_duration;
    }

    public long getR2D2_duration() {
        return R2D2_duration;
    }

    public void setR2D2_duration(long r2D2_duration) {
        R2D2_duration = r2D2_duration;
    }
}
