import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main1 {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>(List.of(5, 6, 7, 9, 10));
        System.out.println(list);

        int index;
        index = Collections.binarySearch(list, 3);
        System.out.println(index); // -2
        list.add(0, 3);
        System.out.println(list);

        index = Collections.binarySearch(list, 5);
        System.out.println(index); // -2
        list.add(index, 5);
        System.out.println(list);

        index = Collections.binarySearch(list, 8);
        System.out.println(index); // -2
        list.add(index*(-1)-1, 8);
        System.out.println(list);

        index = Collections.binarySearch(list, 11);
        System.out.println(index); // -2
        list.add(index*(-1)-1, 11);
        System.out.println(list);

    }
}
