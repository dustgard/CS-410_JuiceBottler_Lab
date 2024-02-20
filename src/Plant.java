import java.util.LinkedList;
import java.util.Queue;

public class Plant{
    // How long do we want to run the juice processing

    public final int ORANGES_PER_BOTTLE = 3;

    private final Thread thread;

    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;

    private final Queue<Orange> fetched = new LinkedList<>();
    private final Queue<Orange> peeled = new LinkedList<>();
    private final Queue<Orange> squeezed = new LinkedList<>();
    private final Queue<Orange> bottled = new LinkedList<>();
    private final Queue<Orange> processed = new LinkedList<>();

    private PlantWorker[] plantWorkers;
    Plant(String plant) {
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, plant);

        }


    public void startPlant() {
        timeToWork = true;
//        thread.start();
        plantWorkers = new PlantWorker[5];
        //possible do enumeration for worker names
        plantWorkers[0]  = new PlantWorker("fetcher",fetched, fetched);
        plantWorkers[1] = new PlantWorker("peeler", fetched, peeled);
        plantWorkers[2] = new PlantWorker("squeezer", peeled, squeezed);
        plantWorkers[3] = new PlantWorker("bottler", squeezed, bottled);
        plantWorkers[4] = new PlantWorker("processor", bottled, processed);
        for (PlantWorker p : plantWorkers) {
            p.startWorker();
        }

    }

    public void stopPlant() {

        for (PlantWorker p : plantWorkers) {
            p.stopWorker();
        }

       for (PlantWorker p : plantWorkers) {


        timeToWork = false;


    }

    public void waitToStop() {

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

     Give the plants time to do work


    public void run() {
        System.out.println(thread.getName() + " Processing oranges");
        while (timeToWork) {

        }
        System.out.println("");
        System.out.println("Plant out of time");
        System.out.println(thread.getName() + " Done");
    }

    public void processEntireOrange(Orange o) {
        while (o.getState() != Orange.State.Bottled) {
            o.runProcess();
        }
        orangesProcessed++;
    }

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    public int getProvidedOranges() {
        return orangesProvided;
    }

    public int getProcessedOranges() {
        return orangesProcessed;
    }

    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}

