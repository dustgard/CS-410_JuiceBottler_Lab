import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Plant class:
 *
 */
public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public final int ORANGES_PER_BOTTLE = 3;
    private final String plantName;
    private final ConcurrentLinkedQueue<Orange> fetched = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> peeled = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> squeezed = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> bottled = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> processed = new ConcurrentLinkedQueue<>();
    private int orangesFetched = 0;
    private int orangesPeeled = 0;
    private int orangesSqueezed = 0;
    private int orangesProcessed = 0;

    private int totalWasted = 0;
    private Thread[] plantWorkers;
    private boolean timeToWork;

    /**
     *
     * @param plant
     */
    Plant(String plant) {
        plantName = plant;
    }

    /**
     *
     */
    public void startPlant() {
        plantWorkers = new Thread[11];
        plantWorkers[0] = new Thread(this, "fetcher");
        plantWorkers[1] = new Thread(this, "peeler");
        plantWorkers[2] = new Thread(this, "squeezer");
        plantWorkers[5] = new Thread(this, "squeezer2");
        plantWorkers[7] = new Thread(this, "squeezer3");
        plantWorkers[3] = new Thread(this, "bottler");
        plantWorkers[6] = new Thread(this, "bottler2");
        plantWorkers[8] = new Thread(this, "bottler3");
        plantWorkers[4] = new Thread(this, "processor");
        plantWorkers[9] = new Thread(this, "processor2");
        plantWorkers[10] = new Thread(this, "processor3");
        timeToWork = true;
        for (Thread worker : plantWorkers) {
            worker.start();
        }
        System.out.println(".........................................................................");
        System.out.println(getPlantName() + "\n"
                + "         Fetcher, Peeler, Squeezer1-3, Bottler, and Processor are on the Clock: " + "\n"
                + "             " + new Timestamp(new Date().getTime()));
    }

    /**
     *
     */
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

    /**
     *
     */
    public void run() {

        while (timeToWork) {
            switch (Thread.currentThread().getName()) {
                case "fetcher":
                    Orange orange = new Orange();
                    fetched.add(orange);
                    orangesFetched++;
                    break;
                case "peeler":
                    Orange peelingOrange = fetched.poll();
                    if (peelingOrange != null) {
                        peelingOrange.runProcess();
                        peeled.add(peelingOrange);
                        orangesPeeled++;
                    }
                    break;
                case "squeezer":
                case "squeezer2":
                case "squeezer3":
                    Orange squeezingOrange = peeled.poll();
                    if (squeezingOrange != null) {
                        squeezingOrange.runProcess();
                        squeezed.add(squeezingOrange);
                        orangesSqueezed++;
                    }
                    break;
                case "bottler":
                case "bottler2":
                case "bottler3":
                    Orange bottlingOrange = squeezed.poll();
                    if (bottlingOrange != null) {
                        bottlingOrange.runProcess();
                        bottled.add(bottlingOrange);
                    }
                    break;
                case "processor":
                case "processor2":
                case "processor3":
                    Orange processingOrange = bottled.poll();
                    if (processingOrange != null) {
                        processingOrange.runProcess();
                        processed.add(processingOrange);
                        orangesProcessed++;
                    }
                    break;
            }
        }
    }

    /**
     *
     * @return
     */
    public int getOrangesFetched() {
        return orangesFetched;
    }

    /**
     *
     * @return
     */
    public int getOrangesPeeled() {
        return orangesPeeled;
    }

    /**
     *
     * @return
     */
    public int getOrangesSqueezed() {
        return orangesSqueezed;
    }

    /**
     *
     * @return
     */
    public int getProcessedOranges() {
        return orangesProcessed;
    }

    /**
     *
     * @return
     */
    public int getBottlesOranges() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    /**
     *
     * @return
     */
    public String getPlantName() {
        return plantName;
    }

    /**
     *
     * @return
     */
    public int getWaste() {
        int bottledWaste = orangesProcessed % ORANGES_PER_BOTTLE;
        totalWasted = bottledWaste + (orangesFetched-orangesProcessed);
        return totalWasted;
    }

    public int getEfficiency (){
        int wastedCount = orangesFetched-totalWasted;
        double efficiency = ((double) wastedCount / orangesFetched)*100;

        return (int)efficiency;
    }

}




