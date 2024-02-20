import java.sql.Timestamp;
import java.util.Date;

public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public final int ORANGES_PER_BOTTLE = 3;
    private final String plantName;
    private final OrangeBasket<Orange> fetched = new OrangeBasket<>();
    private final OrangeBasket<Orange> peeled = new OrangeBasket<>();
    private final OrangeBasket<Orange> squeezed = new OrangeBasket<>();
    private final OrangeBasket<Orange> bottled = new OrangeBasket<>();
    private final OrangeBasket<Orange> processed = new OrangeBasket<>();
    private int orangesFetched = 0;
    private int orangesPeeled = 0;
    private int orangesSqueezed = 0;
    private int orangesBottled = 0;
    private int orangesProcessed = 0;
    private Thread[] plantWorkers;
    private boolean timeToWork;

    Plant(String plant) {
        plantName = plant;
    }

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    public void startPlant() {
        plantWorkers = new Thread[5];
        plantWorkers[0] = new Thread(this, "fetcher");
        plantWorkers[1] = new Thread(this, "peeler");
        plantWorkers[2] = new Thread(this, "squeezer");
        plantWorkers[3] = new Thread(this, "bottler");
        plantWorkers[4] = new Thread(this, "processor");
        timeToWork = true;
        for (Thread worker : plantWorkers) {
            worker.start();
        }
        System.out.println(".........................................................................");
        System.out.println(getPlantName() + "\n"
                + "         Fetcher, Peeler, Squeezer, Bottler, and Processor are on the Clock: " + "\n"
                + "             " + new Timestamp(new Date().getTime()));
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
        System.out.println(".........................................................................");
        System.out.println(getPlantName() + "\n"
                + "         Fetcher, Peeler, Squeezer, Bottler, and Processor are off the Clock: " + "\n"
                + "             " + new Timestamp(new Date().getTime()));
    }

    public void run() {

        while (timeToWork) {
            switch (Thread.currentThread().getName()) {
                case "fetcher":
                    Orange orange = new Orange();
                    fetched.put(orange);
                    orangesFetched++;
                    break;
                case "peeler":
                    Orange peelingOrange = fetched.grab();
                    if (peelingOrange != null) {
                        peelingOrange.runProcess();
                        peeled.put(peelingOrange);
                        orangesPeeled++;
                    }
                    break;
                case "squeezer":
                    Orange squeezingOrange = peeled.grab();
                    if (squeezingOrange != null) {
                        squeezingOrange.runProcess();
                        squeezed.put(squeezingOrange);
                        orangesSqueezed++;
                    }
                    break;
                case "bottler":
                    Orange bottlingOrange = squeezed.grab();
                    if (bottlingOrange != null) {
                        bottlingOrange.runProcess();
                        bottled.put(bottlingOrange);
                        orangesBottled++;
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
    }

    public int getOrangesFetched() {
        return orangesFetched;
    }

    public int getOrangesPeeled() {
        return orangesPeeled;
    }

    public int getOrangesSqueezed() {
        return orangesSqueezed;
    }

    public int getOrangesBottled() {
        return orangesBottled;
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



