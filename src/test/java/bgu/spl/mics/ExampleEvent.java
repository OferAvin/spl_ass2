package bgu.spl.mics;
import bgu.spl.mics.Event;

public class ExampleEvent implements Event<String> {
    private String m;
    public ExampleEvent(String message){
        this.m = message;
    }
    public String getMessage(){
        return m;
    }
}
