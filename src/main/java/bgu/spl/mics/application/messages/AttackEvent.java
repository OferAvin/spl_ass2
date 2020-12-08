package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private List<Integer> EwoksSerialNumList;
    private int duration_;

    public AttackEvent(List<Integer> serials,int duration){
        EwoksSerialNumList = serials;
        duration_ = duration;
    }
}
