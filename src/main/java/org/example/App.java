package org.example;

import org.example.cars.Car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 1 - reading txt file
 * 2 - number of distinct words
 * 3 - compare by length, asc
 * 4 - dsc
 * 5 - iterator reverse
 * 6 - sout by input numbers of rows
 */
public class App 
{
    public static void main( String[] args )
    {
        /** ===============================================================================================================
         *  CARS *
         *  ==============================================================================================================*/
        List<Car> crossovers = new ArrayList<>();
        List<Car> hatchbacks = new ArrayList<>();
        List<Car> sedans = new ArrayList<>();
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Lada", "sedan"));
        cars.add(new Car("Lada", "hatchback"));
        cars.add(new Car("Mercedes", "sedan"));
        cars.add(new Car("BMW", "crossover"));
        cars.add(new Car("Ford", "hatchback"));
        cars.add(new Car("Peugeot", "crossover"));
        cars.add(new Car("Toyota", "sedan"));
        cars.forEach(car -> {
            if(car.getType().equals("sedan")) {
                sedans.add(car);
            }
            if(car.getType().equals("hatchback")){
                hatchbacks.add(car);
            }
            if(car.getType().equals("crossover")) {
                crossovers.add(car);
            }
        });
    /**  ==============================================================================================================*/

        //считали файл
        Path path = readFilePath();

        //считываем строки из файла
        List<String> rows = getRowsFromTxt(path);

        //Разбиваем строки на слова
        List<String> words = getWordsFromRows(rows);

        //подсчет и вывод количества различных слов в файле
        Map<String, Integer> wordsDist = wordsDist(words);
        System.out.println("Words in file:");
        System.out.println(wordsDist.toString());
        System.out.println("===========================================");

        //Вывод на экран список различных слов файла,
        // отсортированный по возрастанию их длины (компаратор сначала по длине слова, потом по тексту).
        System.out.println("SORTED:");
        System.out.println(sortedWordsDist(wordsDist.keySet()));
        System.out.println("===========================================");

        // вывод строк в обратном порядке с помощью своего итератора
        System.out.println("ROWS IN REVERSE ORDER:");
        outputFromEnd(rows);
        System.out.println("===========================================");

        //вывод строк по заданным с консоли номерам
        outputRowsByNumber(rows);

    }


    /**
     * @description read .txt file path from command line
     * @return file's path
     */
    public static Path readFilePath() {
        Path path = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            do {
                System.out.println("input txt file");
                try {
                    path = Paths.get(br.readLine());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                if (!checkTxtFile(path)) {
                    path = null;
                    System.out.println("Incorrect file");
                }
            } while (path == null);
        return path;
    }

    /**
     * @param path - file's path
     * @return true if file exists and .txt
     */
    public static boolean checkTxtFile(Path path) {
        if ((Files.exists(path)) && (!Files.isDirectory(path)) && (path.toString().endsWith(".txt"))) {
            return true;
        } else return false;
    }

    /**
     *
     * @param path - file's path
     * @return List<String> with string rows from file
     */
    public static List<String> getRowsFromTxt (Path path) {
        List<String> content = new ArrayList<>();
        try {
            content = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * @Description разбиваем строки на слова
     * @param rows - строки
     * @return List<String> слова toLowerCase
     */
    public static List<String> getWordsFromRows (List<String> rows) {
        List<String> words = new ArrayList<>();
        rows.forEach(row -> {
            words.addAll(Arrays.asList(row.split("[\\p{Punct}\\s]"))
                    .stream()

                    .filter(e -> !e.equals("")) //отсеили хрень....
                    .map(String::toLowerCase)
                    .collect(Collectors.toList()));
        });
        return words;
    }

    /**
     * @Description подсчитывает количество повторений слов
     * @param words - список слов
     * @return Map<K,V>  K - слово, V - количество повторений
     */
    public static Map<String, Integer> wordsDist (List<String> words){
        Map<String, Integer> wordsDist = new HashMap<>();
        words.forEach(word ->{
            if(wordsDist.containsKey(word)){
                wordsDist.replace(word, wordsDist.get(word) + 1);
            } else {
                wordsDist.put(word, 1);
            }
        });
        return wordsDist;
    }

    /**
     * @Description сортирует множество
     * @param words
     * @return сортированное множество
     */
    public static SortedSet<String> sortedWordsDist (Set<String> words) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int temp = o1.length() - o2.length();
                if (temp == 0) {
                    return o1.compareTo(o2);
                }
                return temp;
            }
        };
        SortedSet<String> wordsSet = new TreeSet<>(comparator);
        wordsSet.addAll(words);
        return wordsSet;
    }

    /**
     * @Description считывает с консоли номера и выводит соответствующие строки на экран
     * @param rows - строки, считанные из файла
     */
    public static void outputRowsByNumber(List<String> rows) {
        List<Integer> rowsNumbers = new ArrayList<>();
        String temp;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            Boolean correctNumbers = false;
            do {
                System.out.println("Input numbers of rows, separated by commas (,)");
                temp = br.readLine();
                try {
                    correctNumbers = true;
                    String[] tmp = temp.split(",");
                    for (String s:tmp
                         ) {
                        Integer rowNumber = Integer.parseInt(s);
                        if(rowNumber > rows.size()){
                            throw new Exception();
                        }
                        rowsNumbers.add(rowNumber);
                    }
                } catch (Exception e) {
                    correctNumbers = false;
                }
            } while (!correctNumbers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Integer rowNumber: rowsNumbers
             ) {
            System.out.println(rows.get(rowNumber));
        }
    }

    /**
     * @Description выводит список строк с конца
     * @param rows - список строк
     */

    public static void outputFromEnd(List<String> rows) {
        /** вывод при помощи собственного итератора */
        Iterator<String> iter = new MyListIterator(rows);
        while (iter.hasNext()){
            System.out.println(iter.next());
        }

/** вывод при помощи стандартного итератора */
/*        ListIterator iterator = rows.listIterator(rows.size());
        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous());
        }*/
    }

}
