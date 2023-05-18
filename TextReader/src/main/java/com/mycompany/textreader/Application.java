/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textreader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Olga
 */
public class Application {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите путь к файлу с текстом, например(D:\\testtext.txt): ");
        String path = sc.nextLine();
        path = path.replace("/", "\\");
        
        try (InputStreamReader file = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(file)) {
            List<String> list = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                list.add(line.toLowerCase());
                line = reader.readLine();
            }
            //частота символов
            System.out.println("\t2 Пункт 1");
            countChar(list);
            //поиск популярных слов
            System.out.println("\t2 Пункт 2");
            countWord(list);
            //последние 10 строк
            System.out.println("\t3 Пункт 3");
            printlnWord(list);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void countChar(List<String> inList) {
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        char[] alphabetArray = alphabet.toCharArray();

        Map<String, Integer> result = new HashMap<>();

        //цикл по строкам
        for (String line : inList) {
            //удаляем спецсимволы
            line = line.replaceAll("[^A-Za-zА-Яа-яЁё]+", "").toLowerCase();
            char[] lineArray = line.toCharArray();
            for (int i = 0; i < lineArray.length; i++) {
                for (int j = 0; j < alphabetArray.length; j++) {
                    if (alphabetArray[j] == lineArray[i]) {
                        if (result.get(String.valueOf(alphabetArray[j])) == null) {
                            result.put(String.valueOf(lineArray[i]), 1);
                        } else {
                            Integer value = result.get(String.valueOf(alphabetArray[j]));
                            result.put(String.valueOf(lineArray[i]), ++value);
                        }
                    }
                }
            }
        }

        //сортируем от большего к меньшему
        result.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(k -> System.out.println(k));
    }

    private static void countWord(List<String> inList) {
        //массив слов с частотой
        Map<String, Integer> wordList = inList.stream()
                .flatMap(x -> Arrays.stream(x.split("\\s+")))
                .map(word -> word.replaceAll("[^A-Za-zА-Яа-яЁё]+", "").toLowerCase())
                .filter(word -> word.length() > 3)
                .collect(Collectors.toMap(key -> key, val -> 1, Integer::sum));
        //сортировка по значениям
        List<Map.Entry<String, Integer>> entries = wordList.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());
        //сортировка по ключу
        Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
            public int compare(
                    Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o1.getKey().substring(1).compareTo(o2.getKey().substring(1));
            }
        });
        entries.forEach(k -> System.out.println(k));
    }

    private static void printlnWord(List<String> inList) {
        if (inList.size() > 10) {
            inList.stream().skip(inList.size() - 10).forEach(x -> System.out.println(x));
        } else {
            inList.stream().forEach(x -> System.out.println(x));
        }
    }
}
