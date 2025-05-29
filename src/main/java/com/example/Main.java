package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        List<Path> allPathsLogs = getAllPaths(StringConstant.PATH_OF_LOGS);
        HashMap<String, List<String>> hashMap = hashMapFromPaths(allPathsLogs);
        writeLogs(hashMap);

        addFinalString(getAllPaths(StringConstant.PATH_OF_USERS_TRANSACTIONS));
    }

    public static List<Path> getAllPaths(String pathWithLogs) {
        try (Stream<Path> files = Files.list(Paths.get(pathWithLogs))
        ) {
            return files.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static HashMap<String, List<String>> hashMapFromPaths(List<Path> listOfPathLogs) {
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        for (Path allPathsLog : listOfPathLogs) {
            try (Scanner scanner = new Scanner(allPathsLog)) {
                while (scanner.hasNextLine()) {
                    String tempLog = scanner.nextLine();
                    String[] strings = tempLog.substring(21).trim().split(" ", 2);
                    String user = strings[0];
                    Matcher matcher = StringConstant.TRANSFERRED_FULL.matcher(tempLog);
                    if (matcher.find()) {
                        String dateTime = matcher.group(1);
                        String fromUser = matcher.group(2);
                        String amount = matcher.group(3);
                        String toUser = matcher.group(4);

                        String newAction = String.format("[%s] %s received %s from %s", dateTime, toUser, amount, fromUser);

                        hashMap.computeIfAbsent(toUser, k -> new ArrayList<>()).add(newAction);
                    }
                    hashMap.computeIfAbsent(user, k -> new ArrayList<>()).add(tempLog);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return hashMap;
    }

    public static void addFinalString(List<Path> listOfPathLogs) {
        for (Path path : listOfPathLogs) {
            try (
                    Scanner scanner = new Scanner(path);
                    BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)
            ) {
                double amount = 0;
                while (scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    amount = countAmount(s, amount);
                }
                String finalStatString = String.format("[%s] %s final balance %.2f%n", timeStap(), getFileName(path), amount);
                bufferedWriter.append(finalStatString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeLogs(HashMap<String, List<String>> hashMap) {
        hashMap.forEach((user, log) -> {
            File file = new File(StringConstant.PATH_OF_USERS_TRANSACTIONS + File.separator + user + ".log");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                List<String> sortedLogs = log.stream()
                        .sorted((s1, s2) -> {
                            Matcher patternData = StringConstant.LOG_DATE.matcher(s1);
                            Matcher patternTime = StringConstant.LOG_DATE.matcher(s2);
                            if (patternData.find() && patternTime.find()) {
                                String data = patternData.group(1);
                                String time = patternTime.group(1);
                                return data.compareTo(time);
                            }
                            return 0;
                        })
                        .map(str -> str.replace(",", "\n"))
                        .collect(Collectors.toList());
                for (String str : sortedLogs) {
                    writer.write(str);
                    writer.newLine();
                }

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    public static String getFileName(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.indexOf('.'); //
        return fileName.substring(0, dotIndex);
    }

    public static String timeStap() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static double countAmount(String string, double currentAmount) {
        double res = currentAmount;
        Matcher received = StringConstant.RECEIVED.matcher(string);
        Matcher transferred = StringConstant.TRANSFERRED.matcher(string);
        Matcher withdrew = StringConstant.WITHDREW.matcher(string);
        Matcher balance = StringConstant.BALANCE.matcher(string);
        if (received.find()) {
            res += Double.parseDouble(received.group(1));
        }
        if (transferred.find()) {
            res -= Double.parseDouble(transferred.group(1));
        }
        if (withdrew.find()) {
            res -= Double.parseDouble(withdrew.group(1));
        }
        if (balance.find()) {
            res = Double.parseDouble(balance.group(1));
        }
        return res;
    }
}
