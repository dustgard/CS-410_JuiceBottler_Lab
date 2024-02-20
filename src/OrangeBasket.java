import java.util.Iterator;
import java.util.LinkedList;

public class OrangeBasket<Orange> {
    private final LinkedList<Orange> oranges = new LinkedList<>();

    private boolean grabLock;
    private boolean putLock;
    public synchronized void put(Orange orange) {
        while (putLock) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }
        putLock = true;
        oranges.add(orange);
        putLock = false;
        this.notifyAll();
    }
    public synchronized Orange grab() {
        while (grabLock) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }
        if(!oranges.isEmpty()) {
            Iterator<Orange> nextInLine = oranges.iterator();
            Orange orange = nextInLine.next();
            grabLock = true;
            nextInLine.remove();
            grabLock = false;
            this.notifyAll();
            return orange;
        }
        return null;
    }

    public synchronized int size (){
        return oranges.size();
    }

}