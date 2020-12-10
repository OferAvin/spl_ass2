package bgu.spl.mics.application;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		// reading the json file to object class Input
		Gson gson = new Gson();
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader("/home/spl211/stud/SPL/Ass2/spl_ass2/src/main/input.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Input input = gson.fromJson(reader,Input.class);
		// initialize diary
		Diary diary = Diary.getInstance();
		// initialize Ewoks
		Ewoks ewoks = Ewoks.getInstance();
		ewoks.init(input.getNumEwoks());
		// initialize threads
		Vector<Thread> threadVector = new Vector<Thread>();
		// init C3PO
		Thread C3PO = new Thread(new C3POMicroservice());
		threadVector.add(C3PO);
		// init HanSolo
		Thread HanSolo = new Thread(new HanSoloMicroservice());
		threadVector.add(HanSolo);
		// init R2D2
		Thread R2S2 = new Thread(new R2D2Microservice(input.getR2D2_duration()));
		threadVector.add(R2S2);
		// init Lando
		Thread Lando = new Thread(new LandoMicroservice(input.getLando_duration()));
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
			FileWriter fileWriter = new FileWriter("/home/spl211/stud/SPL/Ass2/spl_ass2/src/main/output.json");
			gsonOutput.toJson(diary,fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}
