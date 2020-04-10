package se.jg.mashup;

import java.util.Scanner;

public class TestDataUtils {

    public static String readTestDataFile(String filePath) {
        Scanner scanner = new Scanner(TestDataUtils.class.getResourceAsStream(filePath));
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.nextLine());
        }
        return sb.toString();

    }
}
