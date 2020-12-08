package bgu.spl.mics;
import bgu.*;

public class ExampleEventHandlerService extends MicroService {
    public ExampleEventHandlerService(String name){
        super(name);
    }

    @Override
    protected void initialize() {
        subscribeEvent(ExampleEvent.class,calback->{});
    }
}
