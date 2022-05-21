package com.company;

public class Main {
    static int[] a = new int[12];
    static int numFrom = 2, numTo = 24, ind = 0;
    static float[] x = new float[17];
    static float[][] b = new float[12][17];

    public static float firstFun(float arg){
        float el = Math.abs(arg);
        el = (float) Math.log(el);
        el = (float) Math.cos(el) / 2;
        el = (float) Math.pow(el, 3);
        return el;
    }
    public static float secondFun(float arg){
        float el = (float) Math.pow(((arg - 1) / arg), 3);
        el = (float) Math.exp(el);
        el = (float) Math.sin(el);
        el = (float) Math.asin(el);
        return el;
    }
    public static float thirdFun(float arg){
        float el = Math.abs(arg);
        el = (float) Math.exp(-el);
        el = (float) Math.acos(el);
        el = (float) Math.pow(-el, (float) 1 / (float) 3);
        el = (float) Math.exp(el);
        return el;
    }

    public static void createA(){
        System.out.println("1. Создать одномерный массив a типа int. Заполнить его чётными числами от 2 до 24 включительно в порядке убывания.");
        for (int i = numTo; i > numFrom; i--) {
            if (i % 2 == 0) {
                a[ind] = i;
                ind++;
            }
        }
        for (int aInt : a) {
            System.out.print(aInt + "\t");
        }
        System.out.println("\n");
    }
    public static void createX(){
        System.out.println("2. Создать одномерный массив x типа float. Заполнить его 17-ю случайными числами в диапазоне от -2.0 до 10.0.");
        numTo = 17;
        for (int i = 0; i < numTo; i++) {
            double randomNum0 = 12 * Math.random() - 2;
            float randNum = (float) randomNum0;
            x[i] = randNum;
            ind++;
        }
        for (float aFloat : x) {
            System.out.printf("%.5f ", aFloat);
        }
        System.out.println("\n");
    }
    public static void createB(){
        System.out.println("3. Создать двумерный массив b размером 12x17. Вычислить его элементы по следующей формуле (где x = x[j]):");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 17; j++) {
                float el = 0;
                float arg = x[j];
                if (a[i] == 6) {
                    el = firstFun(arg);
                } else if ((a[i] == 2) || (a[i] == 4) || (a[i] == 8) || (a[i] == 10)
                        || (a[i] == 18) || (a[i] == 22)) {
                    el = secondFun(arg);
                } else {
                    el = thirdFun(arg);
                }
                b[i][j] = el;
            }

        }

    }
    public static void printB() {
        System.out.println("4. Напечатать полученный в результате массив в формате с пятью знаками после запятой.");
        for (float[] floats : b) {
            for (float aFloat : floats) {
                if (Float.isNaN(aFloat))
                    System.out.printf("%9s", " -_-  ");
                else
                    System.out.printf("%9.5f", aFloat);
//                    System.out.printf("%+10.5f", aFloat);
//                    System.out.printf("%-10.5f", aFloat);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        createA();
        createX();
        createB();
        printB();
    }
}