package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.*;
import java.util.List;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    private Ewoks ewoks;
    public HanSoloMicroservice() {
        super("Han");
        ewoks = Ewoks.getInstance();
    }


    @Override
    protected void initialize() {
        // subscribe himself to TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class,callback->{terminate();});
        //subscribe himself to event of type attack
        subscribeEvent(AttackEvent.class,(AttackEvent attackEvent)->{
            //get the Serial num for the requested ewoks
            List<Integer> requiredEwoks = attackEvent.getEwoksSerialNumList();
            // acquire the ewoks
            ewoks.getEwoks(requiredEwoks);
            try {
                // perform the attack
                Thread.sleep(attackEvent.getDuration_());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // realse the ewoks
            ewoks.realseEwoks(requiredEwoks);
            // informed the event is complete
            complete(attackEvent,true);
        });
    }
}
