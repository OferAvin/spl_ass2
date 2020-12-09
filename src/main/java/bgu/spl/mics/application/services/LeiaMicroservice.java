package bgu.spl.mics.application.services;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bgu.spl.mics.MicroService;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private Vector<Future<Boolean>> attacksFuture;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		attacksFuture = new Vector<Future<Boolean>>();
    }


    @Override
    protected void initialize() {
        for (Attack attack:attacks) {
            AttackEvent attackEvent = new AttackEvent(attack.getEwoksSerialNumList(),attack.getDuration());
            attacksFuture.add(sendEvent(attackEvent));
        }
        for (Future f : attacksFuture){
            f.get();
        }
        // subscribe to terminate broadcast
    }
}
