public class JuiceBottler {
    // How long do we want to run the juice processing
    public static final long PROCESSING_TIME = 5 * 1000;
    private static final int NUM_PLANTS = 3;

    public static void main(String[] args) {
        System.out.println(" ");
        System.out.println("############## Plant is Open ###############");
        System.out.println(" ");

        // Startup the plants
        Plant[] plants = new Plant[NUM_PLANTS];
        for (int i = 0; i < NUM_PLANTS; i++) {
            plants[i] = new Plant("Plant " + i);
            plants[i].startPlant();
        }

        System.out.println(" ");
        System.out.println("---- Please Wait ----");
        System.out.println(" ");

        // Give the plants time to do work
        delay(PROCESSING_TIME, "Plant malfunction");

        System.out.println(" ");
        System.out.println(" ");
        System.out.println("############## Plant hours are over ###############");
        System.out.println(" ");
        System.out.println(" ");

        // Stop the plant, and wait for it to shutdown
        for (Plant p : plants) {
            p.stopPlant();
        }

        System.out.println(" ");
        System.out.println(" ");
        System.out.println("############## Plant Totals ###############");
        System.out.println(" ");
        System.out.println(" ");

        // Summarize the results
        for (Plant p : plants) {
            int totalProvided = p.getOrangesFetched();
            int totalProcessed = p.getProcessedOranges();
            int totalPeeled = p.getOrangesPeeled();
            int totalSqueezed = p.getOrangesSqueezed();
            int totalBottles = p.getBottles();
            int totalWasted = p.getWaste();
            System.out.println(p.getPlantName() + "\n"
                    + "     Total Fetched Oranges = " + totalProvided + "\n"
                    + "     Total Peeled Oranges = " + totalPeeled + "\n"
                    + "     Total Squeezed Oranges = " + totalSqueezed + "\n"
                    + "     Total Processed Oranges = " + totalProcessed + "\n"
                    + "     Total Bottles Created = " + totalBottles + "\n"
                    + "     Total Wasted = " + totalWasted);
        }

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
