package bgu.spl.mics;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static class SingletonHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>> microservice2queue;
	private ConcurrentHashMap<Class<? extends Event>,LinkedBlockingQueue<MicroService>> event2microservice;
	private ConcurrentHashMap<Class<? extends Broadcast>, CopyOnWriteArrayList<MicroService>> broadcast2microservice;
	private ConcurrentHashMap<Event,Future> event2Future;
	private static MessageBusImpl messageBusInstance = null;

	public static MessageBusImpl getInstance(){
		return SingletonHolder.instance;
	}

	private MessageBusImpl(){
		microservice2queue = new ConcurrentHashMap<>();
		event2microservice = new ConcurrentHashMap<>();
		broadcast2microservice = new ConcurrentHashMap<>();
		event2Future = new ConcurrentHashMap<>();

	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// for every new event type map a microservice q
		event2microservice.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
		//add m to his map q;
		synchronized (type){
			LinkedBlockingQueue<MicroService> subscribersQ = event2microservice.get(type);
			subscribersQ.add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// for every new Broadcast type map a microservice list
		broadcast2microservice.putIfAbsent(type, new CopyOnWriteArrayList<>());
		// add m to his map list;
		broadcast2microservice.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		// find the Future map to this event
		Future<T> f = event2Future.get(e);
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
			CopyOnWriteArrayList<MicroService> subscribers = broadcast2microservice.get(type);
				// add to q of all ms in list and notify
			for (MicroService m : subscribers) {
				LinkedBlockingQueue<Message> messageQ = microservice2queue.get(m);
				synchronized (m) {
					if(messageQ != null){
						messageQ.add(b);
						m.notifyAll();
					}
				}
			}

		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// create Future eventFuture= nullevent2Future.get(e);
		Future<T> eventFuture = null;
		// check event type
		Class eventType = e.getClass();
		// if someone subscribe to this type:
		if(event2microservice.containsKey(eventType)) {
			// assign future to event
			eventFuture = new Future<T>();
			event2Future.put(e, eventFuture);
			LinkedBlockingQueue<MicroService> subscribersQ = event2microservice.get(eventType);
			MicroService m = safeRoundRobin(eventType, subscribersQ);
			// add the event to the microservice message and notify q need to check if -*critical*-
			LinkedBlockingQueue<Message> messageQ = microservice2queue.get(m);
			synchronized (m){
				if(messageQ != null){
					messageQ.add(e);
					m.notifyAll();
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
		//clean all reference related to him: if q/list is empty after act remove key
		Set<Class<? extends Event>> eventTypes = event2microservice.keySet();
		for (Class<? extends Event> eventType : eventTypes) {
			LinkedBlockingQueue<MicroService> q = event2microservice.get(eventType);
			synchronized (eventType){
				if(q != null){
					q.remove(m);
					if(q.isEmpty()){
						event2microservice.remove(eventType);
					}
				}
			}
		}
		Set<Class<? extends Broadcast>> broadTypes = broadcast2microservice.keySet();
		for(Class<? extends Broadcast> broadType : broadTypes){
			CopyOnWriteArrayList<MicroService> list = broadcast2microservice.get(broadType);
			list.remove(m);
			if(list.isEmpty()){
				broadcast2microservice.remove(broadType);
			}
		}
		synchronized (m){
			microservice2queue.remove(m);
		}


	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// if notregister
		if(!microservice2queue.containsKey(m))
			//throw exp
			throw new IllegalStateException("this microservice is not registered");
		LinkedBlockingQueue<Message> messageQ = microservice2queue.get(m);
		synchronized (m){
			while (messageQ.isEmpty()) {
				m.wait();
			}
			return messageQ.poll();
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

