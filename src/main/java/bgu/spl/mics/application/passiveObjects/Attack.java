package bgu.spl.mics.application.passiveObjects;

import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final List<Integer> EwoksSerialNumList;
    final int duration;

    /**
     * Constructor.
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        this.EwoksSerialNumList = serialNumbers;
        this.duration = duration;
    }
    public List<Integer> getEwoksSerialNumList(){
        return EwoksSerialNumList;
    }
    public int getDuration(){
        return duration;
    }
}
