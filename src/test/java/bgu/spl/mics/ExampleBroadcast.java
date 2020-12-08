package bgu.spl.mics;
import bgu.spl.mics.Broadcast;

public class ExampleBroadcast implements Broadcast {
    private String m;
    public ExampleBroadcast(String message){
        this.m = message;
    }
    public String getMessage(){
        return m;
    }
}
