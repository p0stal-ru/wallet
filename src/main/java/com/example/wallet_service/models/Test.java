package com.example.wallet_service.models;

public class Test {


    public static void main(String args[]) {
        int[] sortArr = {12, 6, 4, 1, 15, 10};
        bubbleSort(sortArr);
        for (int i = 0; i < sortArr.length; i++) {
            System.out.print(sortArr[i] + "\n");
        }
    }

    private static void bubbleSort(int[] sortArr) {

        int min = 0;

        for (int i = 0; i < sortArr.length - 1; i++) {
            for (int j = 0; j < sortArr.length - 1 - i; j++) {
                if (sortArr[j] < sortArr[j + 1]) {
                    int temp = sortArr[j];
                    sortArr[j] = sortArr[j + 1];
                    sortArr[j + 1] = temp;
//                    sortArr[j + 1] = sortArr[j];
                }

            }
        }

    }
}
