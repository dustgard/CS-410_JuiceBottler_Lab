
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Plant class: This class is used to control the state of the plant activities.
 * It manages 11 worker threads that split the task of processing an orange into orange juice
 * bottles for sale.
 *         1. Creates a queue for each orange state:
 *                  a.Accessed in synchronized fashion (thread safe).
 *         2. Sets the number of oranges per bottle.
 *         3. Creates 11 threads (workers that are created for grabbing an orange, changing the state,
 *             and placing it in the next state queue).
 *         4. Informs the JuiceBottler program that the workers are on the clock (Threads have started)
 *             or off the clock (Threads are finished with tasks).
 *         5. Stopping the workers after JuiceBottler program allotted work time has ended.
 *         6. Keeping track of all production numbers per task.
 */

public class Plant implements Runnable {
    private final String plantName;
    private boolean timeToWork;
    public final int ORANGES_PER_BOTTLE = 3;
    private volatile int orangesFetched = 0;
    private volatile int orangesPeeled = 0;
    private volatile int orangesSqueezed = 0;
    private volatile int orangesProcessed = 0;
    private volatile int totalWasted = 0;
    private Thread[] plantWorkers;
    // Using a thread safe queue. This uses a compare and swap technique.
    // This is a share resource and need to be protected from being acessed at the same
    // time
    private final ConcurrentLinkedQueue<Orange> fetched = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> peeled = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> squeezed = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> bottled = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Orange> processed = new ConcurrentLinkedQueue<>();

    /**
     * Plant contractor: When created the Plant object has
     * a name associated with it.
     * @param plant The string is passed to name the object
     *              and helps keep track of what Plants are
     *              processing what oranges.
     */
    Plant(String plant) {
        plantName = plant;
    }

    /**
     * StartPlant() is used to create and start the worker threads that are needed
     * for each task. There is a total of 11 but need to be adjusted based on computer
     * specs. This is to help balance the totals and reduce waste.
     */
    public void startPlant() {
        plantWorkers = new Thread[11];
        // Names associated with each thread to track progress and what
        // each tasks the threads are doing.  The name is used in the
        // run method using a switch statement to assign what action
        // each thread is going to take.
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
     * This method is used to stop the threads and set the
     * timeToWork variable to false. The threads use this variable
     * to content the while loop. When set to false the loop ends and
     * exits the run(). The threads are given time to finish the current
     * work, end and join the main thread.
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
     * The run() is required when utilizing threads and are where
     * the thread do their work. The while loop is dependent of the timeToWork
     * variable that is changed when a call to the stopPlant method is called.
     * to differentiate between the threads and the different task that need to be
     * completed. This is completed by using a switch statement based off the
     * threads assigned name. Each stage of the orange state is changed and placed
     * into a queue resenting that state it is in.
     */
    public void run() {

        while (timeToWork) {
            switch (Thread.currentThread().getName()) {
                case "fetcher":
                    Orange orange = new Orange();
                    // Critical section
                    fetched.add(orange);
                    orangesFetched++;
                    break;
                case "peeler":
                    // Critical section
                    Orange peelingOrange = fetched.poll();
                    if (peelingOrange != null) {
                        peelingOrange.runProcess();
                        // Critical section
                        peeled.add(peelingOrange);
                        orangesPeeled++;
                    }
                    break;
                case "squeezer":
                case "squeezer2":
                case "squeezer3":
                    // Critical section
                    Orange squeezingOrange = peeled.poll();
                    if (squeezingOrange != null) {
                        squeezingOrange.runProcess();
                        // Critical section
                        squeezed.add(squeezingOrange);
                        orangesSqueezed++;
                    }
                    break;
                case "bottler":
                case "bottler2":
                case "bottler3":
                    // Critical section
                    Orange bottlingOrange = squeezed.poll();
                    if (bottlingOrange != null) {
                        bottlingOrange.runProcess();
                        // Critical section
                        bottled.add(bottlingOrange);
                    }
                    break;
                case "processor":
                case "processor2":
                case "processor3":
                    // Critical section
                    Orange processingOrange = bottled.poll();
                    if (processingOrange != null) {
                        processingOrange.runProcess();
                        // Critical section
                        processed.add(processingOrange);
                        orangesProcessed++;
                    }
                    break;
            }
        }
    }

    /**
     * To keep track of how many oranges are fetch (new Oranges()) created
     * by increasing the number each time a new orange is created.
     * @return an int orangesFetched count.
     */
    public int getOrangesFetched() {
        return orangesFetched;
    }

    /**
     * To keep track of how many oranges are in the peeled state (State.Peeled)
     * by increasing the number each time an orange is peeled.
     * @return an int of orangesPeeled count.
     */
    public int getOrangesPeeled() {
        return orangesPeeled;
    }

    /**
     * To keep track of how many oranges are in the peeled state (State.Squeezed)
     * by increasing the number each time an orange is squeezed.
     * @return an in of orangesSqueezed count.
     */
    public int getOrangesSqueezed() {
        return orangesSqueezed;
    }

    /**
     * To keep track of how many oranges are in the Processed state (State.Processes)
     * by increasing the number each time an orange is peeled.
     * @return an int of ProcessedOranges count.
     */
    public int getProcessedOranges() {
        return orangesProcessed;
    }

    /**
     * To keep track of how many oranges are in the bottled state (State.Bottled)
     * by increasing the number each time an orange is bottled.
     * @return an int of bottled oranges. 3 oranges to make 1 bottle.
     */
    public int getBottlesOranges() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    /**
     * Used to print out a plant and placed with the correct
     * totals for each plant
     * @return String of set plantName.
     */
    public String getPlantName() {
        return plantName;
    }

    /**
     * Oranges that were not used in the bottles made and therefore considered waste.
     * This also includes oranges that did reach the processed stage.
     * @return int of total waste of left over oranges not bottles and the
     * difference for oranges fetched minus oranges processed.
     */
    public int getWaste() {
        int bottledWaste = orangesProcessed % ORANGES_PER_BOTTLE;
        totalWasted = bottledWaste + (orangesFetched-orangesProcessed);
        return totalWasted;
    }

    /**
     * This is the total output of oranges completed through the assembly line
     * divided by the total fetched oranges. This gives how efficient the plants are
     * utilizing the oranges used.
     * @return an int of the percent of efficiency cast to drop the decimal.
     */
    public int getEfficiency (){
        int wastedCount = orangesFetched-totalWasted;
        double efficiency = ((double) wastedCount / orangesFetched)*100;
        return (int)efficiency;
    }
}




