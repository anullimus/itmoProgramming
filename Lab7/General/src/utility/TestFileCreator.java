package utility;

import java.io.*;

public class TestFileCreator {
    private FileWriter writer;

    public static void main(String[] args) {
        TestFileCreator testFileCreator = new TestFileCreator();
        try {
            testFileCreator.initFile();
            testFileCreator.fill();
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    private void initFile() throws IOException {
        writer = new FileWriter("C:\\Users\\amirh\\IdeaProjects\\itmoProgramming\\Lab7\\General\\src" +
                "\\data\\testFile.txt", false);
    }

    private void fill() throws IOException {
        for (int i = 0; i < 40000; i++) {
            write("add");
            write("Author" + i);
            write("2000-01-01");
            write("RUSSIA");
            write("1");
            write("1");
            write("1");
            write("TestObject" + i);
            write("1");
            write("1");
            write("10");
            write("TERRIBLE");
        }
    }

    private void write(String line) throws IOException {
        // запись всей строки
        writer.write(line + "\n");
        writer.flush();
    }
}
