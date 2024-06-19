package hack.maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("Quest/Dockerfile"));
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
//                if (line.contains("ENV") || line.contains("env")) {
//                    String[] split = line.split("\\s+");
//                    env.put(split[1], split[2]);
//                }
            line = reader.readLine();
        }
        reader.close();
    }
}
