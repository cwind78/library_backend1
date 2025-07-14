package com.prj.home1.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test1 {
    public static void main(String[] args) {
        String s1 = "나는 커피를 좋아해";
        String s2 = "나는 커피를 사랑해";

        Map<String, Integer> vector1 = toWordVector(s1);
        Map<String, Integer> vector2 = toWordVector(s2);

        double similarity = cosineSimilarity(vector1, vector2);
        System.out.println("유사도 점수: " + similarity);
    }

    private static Map<String, Integer> toWordVector(String sentence) {
        Map<String, Integer> vector = new HashMap<>();
        for (String word : sentence.split("\\s+")) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }
        return vector;
    }

    private static double cosineSimilarity(Map<String, Integer> vec1, Map<String, Integer> vec2) {
        Set<String> allWords = new HashSet<>();
        allWords.addAll(vec1.keySet());
        allWords.addAll(vec2.keySet());

        int[] v1 = new int[allWords.size()];
        int[] v2 = new int[allWords.size()];

        int i = 0;
        for (String word : allWords) {
            v1[i] = vec1.getOrDefault(word, 0);
            v2[i] = vec2.getOrDefault(word, 0);
            i++;
        }

        return dot(v1, v2) / (magnitude(v1) * magnitude(v2) + 1e-10);
    }

    private static int dot(int[] v1, int[] v2) {
        int result = 0;
        for (int i = 0; i < v1.length; i++) result += v1[i] * v2[i];
        return result;
    }

    private static double magnitude(int[] v) {
        int sum = 0;
        for (int value : v) sum += value * value;
        return Math.sqrt(sum);
    }
}
