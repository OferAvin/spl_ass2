package bgu.spl.mics.application.passiveObjects;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private static class SingletonHolder{
        private static Ewoks instance = new Ewoks();
    }
    private ConcurrentHashMap<Integer,Ewok> ewokConcurrentHashMap = new ConcurrentHashMap<>();

    public static Ewoks getInstance(){
        return Ewoks.SingletonHolder.instance;
    }

    private Ewoks(){}

    public void init(int numOfEwoks){
        for(int i = 1;i <= numOfEwoks;i++){
            ewokConcurrentHashMap.put(i,new Ewok(i));
        }
    }

    public void getEwoks(List<Integer> requireEwoks){
        List<Integer> sortedRequireEwoks = requireEwoks;
        // sort the requireEwoks list in order to prevent deadlock
        Collections.sort(sortedRequireEwoks);
        for (int i:sortedRequireEwoks) {
            // get the ewok map to the serialNumber
            Ewok e = ewokConcurrentHashMap.get(i);
            try {
                synchronized (e) {
                    while (!e.isAvailable()) {
                        e.wait();
                    }
                    e.acquire();
                }
            }
            catch(InterruptedException b){
                b.printStackTrace();
            }
        }
    }

    public void realseEwoks(List<Integer> requireEwoks){
        List<Integer> sortedRequireEwoks = requireEwoks;
        // sort the requireEwoks list in order to prevent deadlock
        Collections.sort(sortedRequireEwoks);
        for (int i:sortedRequireEwoks) {
            // get the ewok map to the serialNumber
            Ewok e = ewokConcurrentHashMap.get(i);
            e.release();
        }
    }

}
