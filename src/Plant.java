import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public final int ORANGES_PER_BOTTLE = 3;
    private final String plantName;
    private final Queue<Orange> fetched = new ConcurrentLinkedQueue<>();
    private final Queue<Orange> peeled = new ConcurrentLinkedQueue<>();
    private final Queue<Orange> squeezed = new ConcurrentLinkedQueue<>();
    private final Queue<Orange> bottled = new ConcurrentLinkedQueue<>();
    private final Queue<Orange> processed = new ConcurrentLinkedQueue<>();
    private int orangesProvided;
    private int orangesProcessed;
    private Thread[] plantWorkers;
    private boolean timeToWork;

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
                    fetched.add(orange);
                    orangesProvided++;
                    break;
                case "peeler":
                    Orange peelingOrange = fetched.poll();
                    if (peelingOrange != null) {
                        peelingOrange.runProcess();
                        peeled.add(peelingOrange);
                    }
                    break;
                case "squeezer":
                    Orange squeezingOrange = peeled.poll();
                    if (squeezingOrange != null) {
                        squeezingOrange.runProcess();
                        squeezed.add(squeezingOrange);
                    }
                    break;
                case "bottler":
                    Orange bottlingOrange = squeezed.poll();
                    if (bottlingOrange != null) {
                        bottlingOrange.runProcess();
                        bottled.add(bottlingOrange);
                    }
                    break;
                case "processor":
                    Orange processingOrange = bottled.poll();
                    if (processingOrange != null) {
                        processingOrange.runProcess();
                        processed.add(processingOrange);
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


}



