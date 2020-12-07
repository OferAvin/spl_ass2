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

	public static MessageBusImpl getInstance(){
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
		LinkedBlockingQueue<MicroService> subscribersQ = event2microservice.get(type);
		synchronized (type){
			subscribersQ.add(m);
		}
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
		if (broadcast2microservice.containsKey(type)) {
			//get the broadcast map list
			Vector<MicroService> subscribers = broadcast2microservice.get(type);
			// add to q of all ms in list and notify need to check if -*critical*-
			for (MicroService m : subscribers) {
				LinkedBlockingQueue<Message> messageQ = microservice2queue.get(m);
				synchronized (m) {
					if(messageQ != null){
						messageQ.add(b);
						notifyAll();
					}
				}
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

			LinkedBlockingQueue<MicroService> subscribersQ = event2microservice.get(eventType);
			MicroService m = safeRoundRobin(eventType, subscribersQ);
			// add the event to the microservice message and notify q need to check if -*critical*-
			LinkedBlockingQueue<Message> messageQ = microservice2queue.get(m);
			synchronized (m){
				if(messageQ != null){
					messageQ.add(e);
					notifyAll();
				}
			}
		}
			return eventFuture;
	}

	@Override
	public void register(MicroService m) {
		// add m to has map and create his q;
		microservice2queue.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		//change terminate to true
		m.terminate();
		//clean all reference related to him: if q/vector is empty after act remove key


	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// if notregister
		if(!microservice2queue.containsKey(m))
			//throw exp
			throw new IllegalStateException("this microservice is not registered");
		LinkedBlockingQueue<Message> messageQ = microservice2queue.get(m);
		//while(q is empty)
		synchronized (m){
			while (messageQ.isEmpty()) {
				//wait
				wait();
			}
			// return q.pop
			return messageQ.poll(); //check later if can be outside the sync
		}
	}

	private MicroService safeRoundRobin(Object lock, LinkedBlockingQueue<MicroService> q) {
		synchronized (lock){
			// -*critical in order to ensure round robin*-
			// get the next microservice from the map event q
			MicroService m = q.poll();
			q.add(m);
			return m;
		}
	}

}

