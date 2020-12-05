package bgu.spl.mics;
import java.lang.reflect.Type;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;




/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>> microservice2queue;
	private ConcurrentHashMap<Class<? extends Event>,LinkedBlockingQueue<MicroService>> event2microservice;
	private ConcurrentHashMap<Broadcast, Vector<MicroService>> broadcast2microservice;
	private ConcurrentHashMap<Class<? extends Event>,Future> event2Future;
	private static MessageBusImpl messageBusInstance = null;

	public static synchronized MessageBusImpl getInstance(){
		if (messageBusInstance==null){
			synchronized (MessageBusImpl.class){
				if (messageBusInstance==null){
					messageBusInstance = new MessageBusImpl();
				}
			}
		}
		return messageBusInstance;
	}

	private MessageBusImpl(){
		microservice2queue = new ConcurrentHashMap<>();
		event2microservice = new ConcurrentHashMap<>();
		broadcast2microservice = new ConcurrentHashMap<>();
		event2Future = new ConcurrentHashMap<>();
	}
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
		// find the Future map to this event
		// Future.resolve(result)
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// check broadcast type
		// if someone subscribe to this type: -*critical*-
		 		//get the broadcast map list
				// add to q of all ms in list
		//else return null;
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// create Future eventFuture= null
		// check event type
		// if someone subscribe to this type:
			// eventFuture = new Future
			// get the next microservice from the map event q
			// add the event to the microservice message q -*critical in order to ensure robin*-
		// return eventFuture;
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
