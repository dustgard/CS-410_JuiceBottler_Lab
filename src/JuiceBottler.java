/**
 * Juice Bottler Lab
 *
 * The lab was created as an exercise to increase the knowledge of multiprocessing,
 * multithreading, thread safe techniques, data parallelization, and task parallelization.
 * The Juice Bottler lab is based off a plant assemble line that takes an orange from fetching,
 * peeling, squeezing, bottling, and processing (labeling the product).
 */

public class JuiceBottler {

    // How long do we want to run the juice processing.
    public static final long PROCESSING_TIME = 5 * 1000;

    // How many plants objects are going to be created.
    private static final int NUM_PLANTS = 3;

    public static void main(String[] args) {
        System.out.println(" ");
        System.out.println("############## Plant is Open ###############");
        System.out.println(" ");

        // Creates the plants based off the number hard coded
        // and assigns a name and number.
        Plant[] plants = new Plant[NUM_PLANTS];
        for (int i = 0; i < NUM_PLANTS; i++) {
            plants[i] = new Plant("Plant " + i);
            //Creates and starts the worker threads with the method call.
            plants[i].startPlant();
        }

        // User info showing that the plants are working on the oranges.
        System.out.println(" ");
        System.out.println("---- Please Wait ----");
        System.out.println(" ");

        // Give the plants time to do work
        delay(PROCESSING_TIME, "Plant malfunction");

        // Notifying the user that the plants time to work is over
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("############## Plant hours are over ###############");
        System.out.println(" ");
        System.out.println(" ");

        // Stop the plant workers
        for (Plant p : plants) {
            p.stopPlant();
        }

        // Notifying the user that the plants are done shutting down.
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("############## Plant Totals ###############");
        System.out.println(" ");
        System.out.println(" ");

        // Summarize the results for each plants.
        for (Plant p : plants) {
            int totalProvided = p.getOrangesFetched();
            int totalProcessed = p.getProcessedOranges();
            int totalPeeled = p.getOrangesPeeled();
            int totalSqueezed = p.getOrangesSqueezed();
            int totalBottles = p.getBottlesOranges();
            int totalWasted = p.getWaste();
            System.out.println(p.getPlantName() + "\n"
                    + "     Total Fetched Oranges = " + totalProvided + "\n"
                    + "     Total Peeled Oranges = " + totalPeeled + "\n"
                    + "     Total Squeezed Oranges = " + totalSqueezed + "\n"
                    + "     Total Processed Oranges = " + totalProcessed + "\n"
                    + "     Total Bottles Created = " + totalBottles + "\n"
                    + "     Total Wasted = " + totalWasted + "\n");
        }
    }

    /**
     * The method is used to create a delay in the main program to allow the workers in the
     * Plant class to have time to complete the tasked assigned.
     * @param time is passed to set amount of time in millisecond to create a delay
     *             in completing the method call.
     * @param errMsg is passed to set a user displayed message indicating an
     *               InterruptedException occurred.
     */

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }
}
