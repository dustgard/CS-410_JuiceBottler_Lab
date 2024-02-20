import java.util.Queue;

public class PlantWorker implements Runnable {

    private static final Object lock = new Object();
    private String job;
    private Queue<Orange> queued1;
    private Queue<Orange> queued2;
    private Thread worker;
    private boolean timeToWork;

    PlantWorker(String workerTitle, Queue<Orange> queue1, Queue<Orange> queue2) {
        job = workerTitle;
        queued1 = queue1;
        queued2 = queue2;
        worker = new Thread(this, workerTitle);
    }

    public void startWorker() {
        timeToWork = true;
        worker.start();


    }


    public void stopWorker() {
        timeToWork = false;
    }

    @Override
    public void run() {
//        if (job.equals("fetcher")) {
            System.out.println("Worker[" + Thread.currentThread().getName() + "] Started");
            while (timeToWork) {
//                if (!queued1.isEmpty()) {
//                    addOrange();
//                    lock.notifyAll();
//                }
            }
            System.out.println("Fetcher out of time");
//        } else {
//            while (timeToWork) {
//                if (!queued1.isEmpty()) {
//                    System.out.println("testing other workers");//not working
//                    Orange orange = removeFromQuedued();
//                    orange.runProcess();
//                    addToQuedued(orange);
//                }
//            }
        }

//    }

    private void addOrange() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ignore) {
            }
        }
        Orange orange = new Orange();
        queued1.add(orange);
    }

    private synchronized void addToQuedued(Orange orange) {
        queued2.add(orange);
    }

    private synchronized Orange removeFromQuedued() {
        Orange orange = queued1.remove();
        return orange;
    }

    public void waitForWorker() {
        try {
            worker.join();
        } catch (InterruptedException e) {
            System.err.println(worker.getName() + " stop malfunction");
        }
    }
}

