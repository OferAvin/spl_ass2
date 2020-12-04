package bgu.spl.mics;
import java.util.concurrent.ConcurrentHashMap;
/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap hashMap1;
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// check if type exist -*critical*-
		// 		if dont exist add the type
		//add m to his map q;
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// check if type exist -*critical*-
		// 		if dont exist add the type
		// add m to his map list;
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// check broadcast type
		// if someone subscribe to this type:
		 		//get the broadcast map list
				// add to q of all ms in list
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// check event type
		// if someone subscribe to this type:
			// get the next microservice from the map event q
			// add the event to the microservice message q -*critical in order to ensure robin*-
        return null;
	}

	@Override
	public void register(MicroService m) {
		// add m to has map and create his q;
	}

	@Override
	public void unregister(MicroService m) {
		//clean all reference related to him
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// if notregister
			//throw exp
		//while(q is empty)
			//wait
		// return q.pop
		return null;
	}
}
