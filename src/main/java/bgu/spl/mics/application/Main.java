package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch downLatch;
	public static void main(String[] args) {
		Input input = null;
		try {
			input = JsonInputReader.getInputFromJson(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// initialize diary
		Diary diary = Diary.getInstance();
		// initialize Ewoks
		Ewoks ewoks = Ewoks.getInstance();
		ewoks.init(input.getEwoks());
		// initialize threads
		Vector<Thread> threadVector = new Vector<Thread>();
		// init C3PO
		Thread C3PO = new Thread(new C3POMicroservice());
		threadVector.add(C3PO);
		// init HanSolo
		Thread HanSolo = new Thread(new HanSoloMicroservice());
		threadVector.add(HanSolo);
		// init R2D2
		Thread R2S2 = new Thread(new R2D2Microservice(input.getR2D2()));
		threadVector.add(R2S2);
		// init Lando
		Thread Lando = new Thread(new LandoMicroservice(input.getLando()));
		threadVector.add(Lando);
		// init downLatch
		downLatch = new CountDownLatch(threadVector.size());
		// init Leia
		Thread Leia = new Thread(new LeiaMicroservice(input.getAttacks()));
		threadVector.add(Leia);
		// running the flow
		for (Thread thread:threadVector) {
			thread.start();
		}
		for (Thread thread:threadVector) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// write the output json
		Gson gsonOutput = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter fileWriter = new FileWriter(args[1]);
			gsonOutput.toJson(diary,fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
