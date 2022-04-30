//package serverLogic;
//
//
//import java.io.*;
//import java.nio.ByteBuffer;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//import java.util.*;
//
///**
// * @author Amir Khanov
// * @version 1.0
// * Main class for <Strong>run</Strong> the application.
// * <img src="doc-files" alt-":^"/>
// */
//@Deprecated
//public class App {
//    public static final String USER_COMMAND = ;
//    public static boolean IS_SCRIPT = false;
//    private final Selector selector;
//    private final SocketChannel socketChannel;
//    private final Scanner consoleScanner;
//
//    public App(Selector selector, SocketChannel socketChannel, Scanner consoleScanner) {
//
//        this.selector = selector;
//        this.socketChannel = socketChannel;
//        this.consoleScanner = consoleScanner;
//
//
//    }
////
////        System.out.println("Добро пожаловать в обитель моей лабы . Здесь вы можете ознакомиться с коллекцией LabWorks.");
//////        logic.CollectionManager collectionManager = new logic.CollectionManager(System.getenv("VARRY"));
////        this.collectionManager = collectionManager;
//
//    public void interactiveMode() {
//        NewElementReader consoleNewElementReader = new NewElementReader(consoleScanner, false);
//        System.out.println("Подключился к серверу");
//
//        System.out.print("Введите команду: ");
//        String command = consoleScanner.nextLine();
//            while (!command.equals("exit")) {
//                try{
//                    CommandAnalyzer commandAnalyzer = new CommandAnalyzer(command);
//                    if(commandAnalyzer.isScript()){
//                        executeScript();
//                    }else {
//                        se
//                    }
//                }
//
//
//
//
//
//                if (!IS_SCRIPT) {
//                    System.out.print(CommandManager.PS2);
//                    USER_COMMAND = commandReader.nextLine().trim().split(" ", 2);
//                }
//                switch (USER_COMMAND[0]) {
//                    case "":
//                        break;
//                    case "help":
//                        CommandManager.help();
//                        break;
//                    case "info":
//                        collectionManager.info();
//                        break;
//                    case "show":
//                        collectionManager.show();
//                        break;
//                    case "add":
//                        collectionManager.add();
//                        break;
//                    case "update_id":
//                        collectionManager.updateId();
//                        break;
//                    case "remove_by_id":
//                        collectionManager.removeById();
//                        break;
//                    case "clear":
//                        collectionManager.clear();
//                        break;
//                    case "save":
//                        collectionManager.save();
//                        break;
//                    case "execute_script":
//                        executeScript();
//                        break;
//                    case "exit":
//                        collectionManager.exit();
//                        break;
//                    case "add_if_min":
//                        collectionManager.addIfMin();
//                        break;
//                    case "remove_greater":
//                        collectionManager.removeGreater();
//                        break;
//                    case "remove_lower":
//                        collectionManager.removeLower();
//                        break;
//                    case "max_by_author":
//                        collectionManager.maxByAuthor();
//                        break;
//                    case "count_less_than_author":
//                        collectionManager.countLessThanAuthor();
//                        break;
//                    case "filter_by_difficulty":
//                        collectionManager.filterByDifficulty();
//                        break;
//                    default:
//                        System.out.println(CommandManager.PS1 + "Такой команды не существует.");
//                        break;
//                }
//                if (IS_SCRIPT) {
//                    break;
//                }
//            }
//            if (!IS_SCRIPT) {
//                collectionManager.exit();
//            }
//    }
//
//    /**
//     * Executes the script.
//     */
//    public void executeScript() {
//        String scriptPath = USER_COMMAND[1];
//        if (CollectionManager.nameOfFilesThatWasBroughtToExecuteMethod.contains(scriptPath)) {
//            System.out.println("В файле присутствует конструкция, которая приводит к рекурсии!\n" +
//                    "Выполнение скрипта приостановлено");
//        } else {
//            CollectionManager.nameOfFilesThatWasBroughtToExecuteMethod.add(scriptPath);
//            IS_SCRIPT = true;
//            File file = new File(scriptPath);
//            if (file.canRead()) {
//                try {
//                    BufferedReader bufferedReader = new BufferedReader(new FileReader(scriptPath));
//                    String line = "";
//                    while ((line = bufferedReader.readLine()) != null) {
//                        USER_COMMAND = line.trim().split(" ", 2);
//                        if (USER_COMMAND[0].toLowerCase(Locale.ROOT).equals("add")) {
//                            StringBuilder elementLine = new StringBuilder();
//                            for (int i = 0; i < 11; i++) {
//                                elementLine.append(bufferedReader.readLine().trim()).append(";");
//                            }
//                            USER_COMMAND = new String[]{"add", elementLine.toString()};
//                        }
//                        interactiveMode();
//                    }
//                    System.out.println(CommandManager.PS1 + "Исполнение скрипта завершено.");
//                } catch (FileNotFoundException exception) {
//                    System.out.println(CommandManager.PS1 + "Файл-скрипт не найден.");
//                } catch (IOException exception) {
//                    System.out.println(CommandManager.PS1
//                            + "Возникла непредвиденная ошибка. Программа остановлена, обратитесь в поддержку.");
//                    exception.printStackTrace();
//                    System.exit(0);
//                }
//            } else {
//                System.out.println(CommandManager.PS1 + "У вас нет доступа к файл-скрипту.");
//            }
//            IS_SCRIPT = false;
//            interactiveMode();
//            CollectionManager.nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
//        }
//    }
//
//    private void sendReceiverLoop(String command, NewElementReader newElementReader) throws  IOException{
//        try{
//            Request request = RequestCreator.createRequest(command, newElementReader);
//            byte[] serializedRequest = Serializer.serializeRequest(request);
//            socketChannel.write(ByteBuffer.wrap(serializedRequest));
//            Response response = re
//        }
//    }
//
//    private Response receiveResponse() throws IOException{
//        final int bufferSize = 1024;
//        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
//
//    }
//
//
//
//
//
//
//    private Request receiveRequest() throws IOException {
//        final int size = 1024;
//        byte[] serializedRequest = new byte[size];
//        int bytesRead = inputStream.read(serializedRequest);
//        if (bytesRead == -1) {
//            throw new IOException();
//        }
////        ServerLogger.logDebugMessage("Прочитано {} байт", bytesRead);
//        try {
//            return Deserializer.deserializeRequest(serializedRequest);
//        } catch (DeserializeException e) {
//            int last = inputStream.available();
//            int len = bytesRead;
//            List<ByteBuffer> list = new ArrayList<>();
//            while (last > 0) {
////                ServerLogger.logDebugMessage("Осталось прочитать {} байт", last);
//                byte[] arr = new byte[last];
//                int bytes = inputStream.read(arr);
//                len += bytes;
//                if (bytes == -1) {
//                    throw new IOException();
//                }
//                list.add(ByteBuffer.wrap(arr));
////                ServerLogger.logDebugMessage("Прочитано {} байт", bytes);
//                last = inputStream.available();
//            }
//            ByteBuffer byteBuffer = ByteBuffer.allocate(len);
//            byteBuffer.put(serializedRequest);
//            list.forEach(byteBuffer::put);
//            return Deserializer.deserializeRequest(byteBuffer.array());
//        }
//    }
//}
//
