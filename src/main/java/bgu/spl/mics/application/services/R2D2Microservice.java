package bgu.spl.mics.application.services;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private long duration_;
    public R2D2Microservice(long duration) {
        super("R2D2");
        duration_ = duration;
    }

    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();
        //subscribe himself to event of type Deactivation
        subscribeEvent(DeactivationEvent.class,(DeactivationEvent deactEvent)->{
            try {
                // perform the DeactivationEvent
                Thread.sleep(duration_);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //log to diary
            diary.setR2D2Deactivate(System.currentTimeMillis());
            // informed the event is complete
            complete(deactEvent,true);
        });
        // subscribe himself to TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast terminateBroadcast) -> {
            terminate();
            diary.setR2D2Terminate(System.currentTimeMillis());
        });
    }
}
