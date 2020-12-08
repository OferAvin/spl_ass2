package bgu.spl.mics.application.passiveObjects;


import java.util.List;

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
    private List<Ewok> ewokList;
    private Ewoks
    public static Ewoks getInstance(){
        return Ewoks.SingletonHolder.instance;
    }
    public void addEwok(Ewok ewok){
        ewokList.add(ewok);
    }

}
