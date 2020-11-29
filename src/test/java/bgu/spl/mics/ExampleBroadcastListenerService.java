package bgu.spl.mics;
//import bgu.*;

public class ExampleBroadcastListenerService extends MicroService {


    public ExampleBroadcastListenerService(String name) {
        super(name);
    }

    protected void initialize() {

        subscribeBroadcast(ExampleBroadcast.class, whenReceive -> {
        });
    }

}
