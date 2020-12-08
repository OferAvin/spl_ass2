package bgu.spl.mics;

public class NonBroadcastListener extends MicroService {
    private int check = 0;
    public NonBroadcastListener(String name){
        super(name);
    }

    @Override
    protected void initialize() {


    }
    public int getCheckNum(){
        return check;
    }
}
