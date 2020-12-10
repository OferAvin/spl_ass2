package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private long duration_;
    public LandoMicroservice(long duration) {
        super("Lando");
        duration_ = duration;
    }

    @Override
    protected void initialize() {
        Diary diary  = Diary.getInstance();
        //subscribe himself to event of type BombDestroyer
       subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent bombDestroyerEvent)->{
           try {
               // perform the bombDestroyerEvent
               Thread.sleep(duration_);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           // informed the event is complete
           complete(bombDestroyerEvent,true);
       });
        // subscribe himself to TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast terminateBroadcast)->{
            terminate();
            diary.setLandoTerminate(System.currentTimeMillis());
        });
        Main.downLatch.countDown();
    }
}
