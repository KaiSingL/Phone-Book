package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static HashMap<String, String> phoneBook = new HashMap<>();
    static String[][] phoneBookArray;
    static LinkedList<String> nameList = new LinkedList<>();
    static String loadDataPath = "/Users/operation_xiii/Downloads/directory.txt";
    static String loadNamesPath = "/Users/operation_xiii/Downloads/find.txt";
//    static String loadDataPath = "/Users/operation_xiii/Downloads/small_directory.txt";
//    static String loadNamesPath = "/Users/operation_xiii/Downloads/small_find.txt";


    public static void main(String[] args) {
        long linearT;
        long hashT;
        hashT = loadData();
        loadNames();
        linearT = timeLinearSearch();
//        linearT = Long.MAX_VALUE;
        timeBubbleSortJumpSearch(linearT);
        loadData();
        timeQuickSortBinarySearch();
        timeHashTableSearch(hashT);


    }

    static void timeQuickSortBinarySearch() {
        String[][] phoneBookArrayClone = phoneBookArray.clone();
        long timeSpend;
        System.out.println("Start searching (quick sort + binary search)...");
        long t1 = System.currentTimeMillis();
        quickSort(phoneBookArrayClone);
//        for (String[] array: phoneBookArrayClone) {
//            System.out.println(array[0]);
//        }
        long t2 = System.currentTimeMillis();
        doBinarySearch(phoneBookArrayClone);
        long t3 = System.currentTimeMillis();
        timeSpend = t3 - t1;
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);
        timeSpend = t2 - t1;
        System.out.printf("Sorting time: %d min. %d sec. %d ms.\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);
        timeSpend = t3 - t2;
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);

    }

    static void quickSort(String[][] array) {
        String[] pivot = array[array.length-1];
        ArrayList<String[]> leftArray = new ArrayList<>();
        ArrayList<String[]> rightArray = new ArrayList<>();
        for (int i = 0; i < array.length - 1; i++) {
            if(isSmallerString(pivot[0],array[i][0])){
                leftArray.add(array[i]);
            } else {
                rightArray.add(array[i]);
            }
        }
        ArrayList<String[]> sortedArrayList = new ArrayList<>();
        sortedArrayList.addAll(quickSort(leftArray));
        sortedArrayList.add(pivot);
        sortedArrayList.addAll(quickSort(rightArray));
        leftArray.clear();
        rightArray.clear();
        for (int i = 0; i < sortedArrayList.size(); i++) {
            array[array.length - i - 1] = sortedArrayList.get(i);
        }
        sortedArrayList.clear();
    }

    static ArrayList<String[]> quickSort(ArrayList<String[]> array) {
        if(array.size() <= 1) return array;
        String[] pivot = array.get(array.size() - 1);
        ArrayList<String[]> leftArray = new ArrayList<>();
        ArrayList<String[]> rightArray = new ArrayList<>();
        for (int i = 0; i < array.size() - 1; i++) {
            String[] current = array.get(i);
            if(isSmallerString(pivot[0],current[0])){
                leftArray.add(current);
            } else {
                rightArray.add(current);
            }
        }
        array.clear();
        array.addAll(quickSort(leftArray));
        array.add(pivot);
        array.addAll(quickSort(rightArray));
        leftArray.clear();
        rightArray.clear();
        return array;

    }

    static void doBinarySearch(String[][] array) {
        LinkedList<String> nameListClone = new LinkedList<>(nameList);
        int total = 0;
        int count = 0;
        String fullName;
        while (!nameListClone.isEmpty()) {
            total++;
            fullName = nameListClone.poll();
            if (binarySearch(fullName, array, 0 , array.length - 1)) {
                count++;
            }
        }

        System.out.printf("Found %d / %d entries. ", count, total);
    }


    static boolean binarySearch(String name,String[][] array, int head, int tail) {
        if (name.equals(array[head][0])) return true;
        if (name.equals(array[tail][0])) return true;
        if (tail == head) return false;
        int mid = (tail + head) / 2;
        if (name.equals(array[mid][0])) return true;
        if (isSmallerString(name, array[mid][0])) {
            return binarySearch(name, array, head, mid - 1);
        } else {
            return binarySearch(name,array,mid + 1, tail);
        }
    }




        static long loadData() {
        File file = new File(loadDataPath);
        String phoneNumber;
        String fullName;
        Object[] nameList;
        long timeSpend = 0;
        try (Scanner scanner = new Scanner(file)){
            long t1 = System.currentTimeMillis();
            while (scanner.hasNext()) {
                phoneNumber = scanner.next();
                fullName = scanner.nextLine().replaceFirst(" ","");
                phoneBook.put(fullName,phoneNumber);
            }
            long t2 = System.currentTimeMillis();
            timeSpend = t2 - t1;
            nameList =  phoneBook.keySet().toArray();
            phoneBookArray = new String[phoneBook.size()][2];
            for (int i = 0; i < phoneBook.size(); i++) {
                phoneBookArray[i][0] = (String) nameList[i];
                phoneBookArray[i][1] = phoneBook.get(nameList[i]);
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            return timeSpend;
        }
        


    }

    static void loadNames() {
        File file = new File(loadNamesPath);
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNext()) {
                nameList.add(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    static void timeHashTableSearch(long hashTime) {
        System.out.println("Start searching (hash table)...");
        long t1 = System.currentTimeMillis();
        hashTableSearch();
        long t2 = System.currentTimeMillis();
        long hashTableSearchTime = t2 - t1;
        long timeSpend = hashTime + hashTableSearchTime;
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n", (timeSpend / 60000) % 60, (timeSpend / 1000) % 60, timeSpend % 1000);
        System.out.printf("Creating time: %d min. %d sec. %d ms.\n", (hashTime / 60000) % 60, (hashTime / 1000) % 60, hashTime % 1000);
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n", (hashTableSearchTime / 60000) % 60, (hashTableSearchTime / 1000) % 60, hashTableSearchTime % 1000);

    }

    static void hashTableSearch() {
        int total = 0;
        int count = 0;
        while (!nameList.isEmpty()) {
            total ++;
            if (phoneBook.containsKey(nameList.poll())) {
                count ++;
            }
        }
        System.out.printf("Found %d / %d entries. ", count, total);
    }

    static void linearSearch() {
        int total = 0;
        int count = 0;
        String fullName;
        LinkedList<String> nameListClone = new LinkedList<>(nameList);

        System.out.println("Start searching...");
        while (!nameListClone.isEmpty()) {
            total ++;
            fullName = nameListClone.poll();
            for (String name : phoneBook.keySet()) {
                if (fullName.equals(name)) {
                    count++;
                    //System.out.println(count + ". " + fullName + " found!");
                }
            }

        }
        System.out.printf("Found %d / %d entries. ", count, total);
    }

    static long timeLinearSearch() {
        System.out.println("Start searching (linear search)...");
        long t1 = System.currentTimeMillis();
        linearSearch();
        long t2 = System.currentTimeMillis();
        long timeSpend = t2 - t1;
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);
        return timeSpend;
    }




    static boolean bubbleSort(long time, String[][] phoneBookArrayClone) {
        long t1 = System.currentTimeMillis();
        long t2;
        long timeSpend;
        String[] a;
        String[] b;
        String[] c;
        boolean swapInRound = true;
        while (swapInRound) {
            //System.out.println("Starting index: " + startIndex);
            swapInRound = false;
            for (int i = 0; i < phoneBookArrayClone.length - 1; i++) {
                a = phoneBookArrayClone[i];
                b = phoneBookArrayClone[i + 1];
                if (!isSmallerString(a[0], b[0])) {
                    c = a.clone();
                    phoneBookArrayClone[i][0] = b[0];
                    phoneBookArrayClone[i][1] = b[1];
                    phoneBookArrayClone[i + 1][0] = c[0];
                    phoneBookArrayClone[i + 1][1] = c[1];
                    swapInRound = true;

                    //System.out.println("Swap " + b[0] + " with " + a[0]);
                }
            }
            t2 = System.currentTimeMillis();
            timeSpend = t2 - t1;
            if (timeSpend > time * 10) {
                return false;
            }
        }

//            System.out.println("Phone book is sorted.");
//            for (int i = 0; i < phoneBookArray.length; i++) {
//                System.out.println(phoneBookArray[i][0]);
//            }
        return true;

    }

    static boolean isSmallerString(String a, String b) {
        a = a.toLowerCase(Locale.ROOT);
        b = b.toLowerCase(Locale.ROOT);
        int aLength = a.length();
        int bLength = b.length();
        int len = Math.min(aLength, bLength);
        for (int i = 0; i < len; i++) {
            if ((int) a.charAt(i) < (int) b.charAt(i)) return true;
            if ((int) b.charAt(i) < (int) a.charAt(i)) return false;
        }
        return aLength < bLength;
    }

    static boolean jumpSearch(int head, int tail, String name) {
        int current = head;
        int offSet = (int) Math.sqrt(tail - head + 1);
        while (current <= tail - offSet) {
            if (name.equals(phoneBookArray[current][0])) {
                return true;
            }
            if (isSmallerString(name, phoneBookArray[current][0])) {
                return jumpSearch(current - offSet, current, name);
            }
            current += offSet;
        }
        if (isSmallerString(name,phoneBookArray[tail][0])) {
            return jumpSearch(current, tail, name);
        }
        return false;
       
    }

    static void timeBubbleSortJumpSearch(long linearT) {

        String[][] phoneBookArrayClone = phoneBookArray.clone();
        System.out.println("Start searching (bubble sort + jump search)...");
        long t1 = System.currentTimeMillis();
        boolean sorted = bubbleSort(linearT, phoneBookArrayClone);
        long t2 = System.currentTimeMillis();
        if(sorted) {
            doJumpSearch(phoneBookArrayClone);
        } else {
            linearSearch();
        }
        long t3 = System.currentTimeMillis();

        long timeSpend = t3 - t1;
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);
        timeSpend = t2 - t1;
        if (sorted) {
            System.out.printf("Sorting time: %d min. %d sec. %d ms.\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);

        } else {
            System.out.printf("Sorting time: %d min. %d sec. %d ms. - STOPPED, moved to linear search\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);
        }
        timeSpend = t3 - t2;
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n", (timeSpend/60000)%60, (timeSpend/1000) % 60, timeSpend % 1000);
    }


    static void doJumpSearch(String[][] phoneBookArrayClone) {
        LinkedList<String> nameListClone = new LinkedList<>(nameList);
        int total = 0;
        int count = 0;
        String fullName;
        while (!nameListClone.isEmpty()) {
            total++;
            fullName = nameListClone.poll();
            if (jumpSearch(0, phoneBookArrayClone.length - 1, fullName)) {
                count++;
            }
        }

        System.out.printf("Found %d / %d entries. ", count, total);
    }
}
