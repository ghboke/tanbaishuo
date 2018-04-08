package com.ghboke.tbs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/7.
 */

public class HonestsayFind {
    static Map<String, Integer> map = new HashMap<>();

    static {
        map.put("oe", 0);
        map.put("n", 0);
        map.put("z", 0);
        map.put("7c",6);
        map.put("oK", 1);
        map.put("6", 1);
        map.put("5", 1);
        map.put("ow", 2);
        map.put("on",0);
        map.put("-", 2);
        map.put("A", 2);
        map.put("oi", 3);
        map.put("o", 3);
        map.put("i", 3);
        map.put("7e", 4);
        map.put("v", 4);
        map.put("P", 4);
        map.put("7K", 5);
        map.put("4", 5);
        map.put("k", 5);
        map.put("7w", 6);
        map.put("C", 6);
        map.put("s", 6);
        map.put("7i", 7);
        map.put("S", 7);
        map.put("l", 7);
        map.put("Ne", 8);
        map.put("c", 8);
        map.put("F", 8);
        map.put("NK", 9);
        map.put("E", 9);
        map.put("q", 9);
    }
    public static String parse(String code) {
        StringBuilder sb = new StringBuilder();
        char[] chars = code.substring(4, code.length()).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append(map.get(a(chars[i], i+1==chars.length?'*':chars[i + 1])) != null?map.get(a(chars[i], chars[++i])):map.get(a(chars[i])));
        }
        return sb.toString();
    }

    private static String a(char... chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
