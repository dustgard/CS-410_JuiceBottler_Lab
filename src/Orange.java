/**
 * The Orange class
 */
public class Orange {

    private State state;

    /**
     * Constructor sets the state of the Orange to State.Fetched when the object
     * is created.
     */
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    /**
     *
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
     *
     */
    private void doWork() {
        // Sleep for the amount of time necessary to do the work
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }

    public State getState(){ return state;}

    /**
     *
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
         *
         * @param timeToComplete
         */
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }


        /**
         *
         * @return
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
