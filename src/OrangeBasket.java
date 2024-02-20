import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;

public class OrangeBasket<Orange>{

    private LinkedList<Orange> oranges;

    public Iterator<Orange> iterator() {
        return oranges.iterator();
    }

    public int size() {
        return oranges.size();
    }

    public boolean put(Orange orange) {
        if(orange == null) {
            return false;
        }
        oranges.add(orange);
        return true;
    }

    public Orange grab() {
        Iterator<Orange> nextInLine = oranges.iterator();
        Orange orange = nextInLine.next();
        if(orange != null){
            nextInLine.remove();
            return orange;
        }
        return null;
    }

    public Orange peek() {
        return oranges.getFirst();
    }
}
