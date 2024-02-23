/**
 * The Orange class: This class is used to create an orange with the ability to
 * change the state of the orange. This is simulating the process from grabbing an orange
 * (the starting task), to the bottling stage (the ending task). The different stages
 * take a set amount of time to change from one to another. This simulates the functioning
 * of a real processing plants.
 */
public class Orange {

    private State state;

    /**
     * Constructor sets the state of the Orange to Fetched when the object
     * is created. The doWork method is called to simulate the time it takes
     * to grab an orange (create a new orange).
     */
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    /**
     * The runProcess method checks to see if the state of the orange
     * is already at the ending state (Processed). If it is not in
     * the process state, it calls the doWork() and then changes the state of
     * the orange to the next stage of completing (example: from Peeled to Squeezed).
     */
    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        doWork();
        state = state.getNext();
    }

    /**
     * The doWork() handles the amount of time set in place to simulate the processing
     * of the orange. Each state has a different amount of time to complete. This is
     * simulated by causing the thread to sleep for that amount.
     */
    private void doWork() {
        // Sleep for the amount of time necessary to do the work
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }

    /**
     * getState() is used for testing the different states during the process
     * to ensure that it is going into the correct queue and the runProcesses is
     * function properly.
     * @return the state that the oranges are currently in.
     */
    public State getState(){
        return state;}

    /**
     * The different states that the orange can be and the
     * amount of time to change to that state. This is used in
     * the doWork() to simulate the process time for each stage
     * of the orange through to the bottling stage.
     */
    public enum State {
        Fetched(15),
        Peeled(38),
        Squeezed(29),
        Bottled(17),
        Processed(1);

        private static final int finalIndex = State.values().length - 1;
        final int timeToComplete;

        /**
         * This is used to take the set time to complete parameter
         * to each stage of processing orange.
         * @param timeToComplete
         */
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }


        /**
         * Changes to the next state by increasing the index number
         * for the enum unless there is no other index spots left.
         * @return The nex state of the enum State value + 1
         */
        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
    }
}
