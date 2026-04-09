package com.mahi.pds.controllers;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Problem {

    public static void main(String[] args) {

        int[] arr = new int[]{6,7,3,4,5,6};

       //convert above array into list
        List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());

        List<String> result = list.stream().map( i -> {
            if(list.indexOf(i) != list.lastIndexOf(i)) return "#";

            return String.valueOf(i);
        }).collect(Collectors.toList());

        System.out.println(result);

        int[] arr2 = new int[]{6,7,3,4,5,6};

        /*
        * You are given an array of integers.
If any element appears more than once, replace the duplicate occurrence(s) with # and print the new modified array.
Example:
Input: [6, 7, 3, 4, 5, 6]
Output: [#, 7, 3, 4, 5, #]
        * */








    }
}
