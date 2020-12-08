package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.*;


import bgu.spl.mics.MicroService;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

	private Ewoks ewoks;
    public C3POMicroservice() {
        super("C3PO");
        ewoks = new Ewoks();
    }

    @Override
    protected void initialize() {
        //subscribe him self to event of type attack

        subscribeBroadcast(TerminateBroadcast.class,callback->{terminate();});
        subscribeEvent(AttackEvent.class,callback->{

        });
    }

}
