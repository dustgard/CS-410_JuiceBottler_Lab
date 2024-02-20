import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public final int ORANGES_PER_BOTTLE = 3;
    private final String plantName;
    private final OrangeBasket<Orange> fetched = new OrangeBasket<>();
    private final OrangeBasket<Orange> peeled = new OrangeBasket<>();
    private final OrangeBasket<Orange> squeezed = new OrangeBasket<>();
    private final OrangeBasket<Orange> bottled = new OrangeBasket<>();
    private final OrangeBasket<Orange> processed = new OrangeBasket<>();
    private int orangesProvided;
    private int orangesProcessed;
    private Thread[] plantWorkers;
    private boolean timeToWork;

    private boolean finishOranges = true;

    Plant(String plant) {
        orangesProvided = 0;
        orangesProcessed = 0;
        plantName = plant;
    }
    public void startPlant() {
        plantWorkers = new Thread[5];
        plantWorkers[0] = new Thread(this,"fetcher");
        plantWorkers[1] = new Thread(this,"peeler");
        plantWorkers[2] = new Thread(this, "squeezer");
        plantWorkers[3] = new Thread(this , "bottler");
        plantWorkers[4] = new Thread(this,"processor");
        timeToWork = true;
        for (Thread p : plantWorkers) {
            p.start();
            System.out.println(getPlantName() + "[" + p.getName() + "]" + " Started Working");
        }
    }
    public void stopPlant() {
        timeToWork = false;
        for (Thread worker : plantWorkers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                System.err.println(worker.getName() + " stop malfunction");
            }
        }
    }
    public void run() {

        while(timeToWork){

            switch (Thread.currentThread().getName()){
                case "fetcher":
                    Orange orange = new Orange();
                    fetched.put(orange);
                    orangesProvided++;
                    break;
                case "peeler":
                    Orange peelingOrange = fetched.grab();
                    if (peelingOrange != null) {
                        peelingOrange.runProcess();
                        peeled.put(peelingOrange);
                    }
                    break;
                case "squeezer":
                    Orange squeezingOrange = peeled.grab();
                    if (squeezingOrange != null) {
                        squeezingOrange.runProcess();
                        squeezed.put(squeezingOrange);
                    }
                    break;
                case "bottler":
                    Orange bottlingOrange = squeezed.grab();
                    if (bottlingOrange != null) {
                        bottlingOrange.runProcess();
                        bottled.put(bottlingOrange);
                    }
                    break;
                case "processor":
                    Orange processingOrange = bottled.grab();
                    if (processingOrange != null) {
                        processingOrange.runProcess();
                        processed.put(processingOrange);
                        orangesProcessed++;
                    }
                    break;
            }

        }

        switch (Thread.currentThread().getName()){
            case "fetcher":
                System.out.println("Fetcher done");
                break;
            case "peeler":
                System.out.println("Peeler done");
                break;
            case "squeezer":
                System.out.println("Squeezer done");
                break;
            case "bottler":
                System.out.println("Bottler done");
                break;
            case "processor":
                System.out.println("Processor done");
                break;
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

    public String getPlantName() {
        return plantName;
    }

    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

}



