package com.company.data.secretDontOpen;

import com.company.logic.CommandInformer;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class SecretExit {
    private final Scanner commandReader;
    private char[] passwd;
    private final String passwdInvite;
    private final String multyStars;
    private final String multyTabs;

    public SecretExit() {
        System.out.println("Поздравляю, вы нашли Пасхальное яйцо!\n" +
                "Подсказка для разгадки пароля для доступа к нему: In python except '0': hash(float('?')) == 0;");
    }

    {
        commandReader = new Scanner(System.in);
        passwd = null;
        passwdInvite = "Введите пароль для доступа: ";
        multyTabs =  "                                      ";
        multyStars = "♪ ♫ ♬♪ ♫ ♬♪ ♫ ♬♪ ♫ ♬♪ ♫ ♬♪ ♫ ♬♪ ♫ ";
    }

    public void passwdAsker() {
        Console console = System.console();
        if (console != null) {
            passwd = console.readPassword(passwdInvite);
        } else {
            System.out.print(passwdInvite);
            passwd = commandReader.next().trim().toLowerCase(Locale.ROOT).toCharArray();
        }
    }

    public boolean passwdChecker() {
        return Arrays.equals(passwd, new char[]{'n', 'a', 'n'});
    }

    public void rejected() {
        System.out.println("Пароль неверный.");
    }

    public void someProblemMessagePrint() {
        System.out.println("Из-за непредвиденной ошибки доступ запрещен.");
    }

    public void play() throws InterruptedException, IOException,
            UnsupportedAudioFileException, LineUnavailableException {
        URL urlTextOfSong = getClass().getResource("lovelySong.txt");
        URL urlSoundOfSong = getClass().getResource("cat.wav");
        assert urlTextOfSong != null;
        BufferedReader reader = new BufferedReader(new FileReader(urlTextOfSong.getPath()));
        String line = "";
        assert urlSoundOfSong != null;
        File song = new File(urlSoundOfSong.getPath());
        if (song.canRead()) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(song);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setFramePosition(0);

            System.out.println("Password is correct ✔");
            Thread.sleep(1000);
            System.out.println("Приятного прослушивания.");
            printPictureOfIpod();
            Thread.sleep(1000);
            System.out.println("Автор музыки 'ты похож на кота' - zhanulka. Материал взят из портала YouTube.");

            final int constValueBeforeText = 50;
            for (int i = 0; i < constValueBeforeText; i++) {
                System.out.println();
            }
            printPictureOfCatMain();
            clip.start();
            Thread.sleep(1000);
            while (true) {
                System.out.print(multyTabs);
                line = reader.readLine();
                if (line != null) {
                    System.out.print(line);
                    System.out.println(multyTabs);
                    Thread.sleep(3800);
                } else {
                    break;
                }
            }
            Thread.sleep(1000);
            clip.close();
            System.out.println();
        } else {
            System.out.println(CommandInformer.PS1 + "У вас нет доступа к файлу-песне.");
        }

    }

    private void printPictureOfCatMain() {
        StringBuilder picture = new StringBuilder();
        picture.append(multyStars).append("░░▄▄▄░░░░░░░░░░░░░░░░░░░░░░░░░░░░▄▄▄░░").append(multyStars).append("\n")
                .append(multyStars).append("░▄████▄░░░░░░░░░░░░░░░░░░░░░░░▄▄████▄░").append(multyStars).append("\n")
                .append(multyStars).append("░██░▀▀███▄▄░▄▄▄████████▄▄▄░▄▄███▀░███░").append(multyStars).append("\n")
                .append(multyStars).append("░██░░░░░▀███████▀████▀▀██████▀░░░░███░").append(multyStars).append("\n")
                .append(multyStars).append("░██▄░░░░░░░░░▀█▀░███░░░██▀▀░░░░░░░██▀░").append(multyStars).append("\n")
                .append(multyStars).append("░▀██▄▄░░░░░░░░░░░░▀░░░░▀░░░░░░░▄▄▄██░░").append(multyStars).append("\n")
                .append(multyStars).append("░░▀██▀░░░░░░░░░░░░░░░░░░░░░░░░░▀███▀░░").append(multyStars).append("\n")
                .append(multyStars).append("░░▄██░░░░░░░░░░░░░░░░░░░░░░░░░░░░██▄░░").append(multyStars).append("\n")
                .append(multyStars).append("░░████▀░░███░░░░░░░░░░░░░░███░░█████░░").append(multyStars).append("\n")
                .append(multyStars).append("░░███▀░░░█████░░░░░░░░░░█████░░░▀███░░").append(multyStars).append("\n")
                .append(multyStars).append("░░██░░░░░░▀▀▀▀░░░░░░░░░░▀▀▀▀░░░░░▀██░░").append(multyStars).append("\n")
                .append(multyStars).append("▄▄███▄▄▄▄░░░░░░░░░░░░░░░░░░░░▄▄▄▄███▄▄").append(multyStars).append("\n")
                .append(multyStars).append("░▄▄██▄▄░░░▄█░░░░▄▀▀▀▀▄░░░░█▄░░░▄███▄▄░").append(multyStars).append("\n")
                .append(multyStars).append("▀░░▄████▀▀▀▀░░░░░▀▄▄▀░░░░░▀▀▀▀████▄░░▀").append(multyStars).append("\n")
                .append(multyStars).append("░▄▀░░▀███▄▄░░░█▄▄█▀▀█▄▄▀░░░▄▄██▀░░░▀▄░").append(multyStars).append("\n")
                .append(multyStars).append("░░░░░░░░▀███▄▄░░░░░░░░░░▄▄███▀░░░░░░░░").append(multyStars).append("\n")
                .append(multyStars).append("░░░░░░░░░░▀▀████▄▄▄▄▄▄████▀▀░░░░░░░░░░").append(multyStars).append("\n")
                .append(multyStars).append("░░░░░░░░░░░░░░▀▀▀▀▀▀▀▀▀▀░░░░░░░░░░░░░░").append(multyStars);
        System.out.println(picture);
    }

    private void printPictureOfIpod(){
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══╗ ♪\n")
                .append("║███║ ♫\n")
                .append("║ (●) ♫\n")
                .append("╚═══╝♪♪");
        System.out.println(sb);
    }
}
