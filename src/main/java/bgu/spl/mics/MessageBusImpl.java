package bgu.spl.mics;
import java.lang.reflect.Type;
import java.util.Queue;
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
	private ConcurrentHashMap<Class<? extends Broadcast>, Vector<MicroService>> broadcast2microservice;
	private ConcurrentHashMap<Event,Future> event2Future;
	private static MessageBusImpl messageBusInstance = null;
	private Object lockEvent2Ms = new Object();

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
			//if dont exist add the type
		event2microservice.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
		//add m to his map q;
		event2microservice.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// check if type exist -*critical*-
			//if dont exist add the type
		broadcast2microservice.putIfAbsent(type, new Vector<>());
		// add m to his map list;
		broadcast2microservice.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		// find the Future map to this event
		Future f = event2Future.get(e);
		// Future.resolve(result)
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// check broadcast type
		Class type = b.getClass();
		// if someone subscribe to this type:
		if(broadcast2microservice.containsKey(type)){
			//get the broadcast map list
			Vector<MicroService> subscribers = broadcast2microservice.get(type);
			// add to q of all ms in list need to check if -*critical*-
			for (MicroService m: subscribers) {
				microservice2queue.get(m).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// create Future eventFuture= null
		Future<T> eventFuture = null;
		// check event type
		Class eventType = e.getClass();
		// if someone subscribe to this type:
		if(event2microservice.containsKey(eventType)) {
			// assign future to event
			eventFuture = new Future<>();
			event2Future.put(e, eventFuture);

			LinkedBlockingQueue<MicroService> msQ = event2microservice.get(eventType);
			MicroService m = safeRoundRobin(msQ);
			// add the event to the microservice message q need to check if -*critical*-
			microservice2queue.get(m).add(e);
		}
			return eventFuture;
	}

	@Override
	public void register(MicroService m) {
		// add m to has map and create his q;
	}

	@Override
	public void unregister(MicroService m) {
		//clean all reference related to him: if q/vector is empty after act remove key
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

	private MicroService safeRoundRobin(LinkedBlockingQueue<MicroService> q){
		synchronized (q) {
			try {
				// get the next microservice from the map event q
				MicroService m = q.take();
				// -*critical in order to ensure round robin*-
				q.add(m);
				return m;
			} catch (InterruptedException interruptedException) {
				return null;
			}
		}
	}
}

