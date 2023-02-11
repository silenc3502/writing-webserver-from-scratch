package com.eddicorp.util;

import java.util.ArrayList;
import java.util.List;

public class MustacheStringReplacer {

    static private List<Integer> startIdx = new ArrayList<>();
    static private List<Integer> endIdx = new ArrayList<>();

    static private List<String> mustacheString = new ArrayList<>();

    final public static Integer BRACE_START = 2;
    final public static Integer BRACE_END = 2;

    public static void analysisMustacheString(String analysisString) {
        int idx = 0;

        while ((idx = analysisString.indexOf("{{", idx)) != -1) {
            startIdx.add(idx);
            idx++;
        }

        while ((idx = analysisString.indexOf("}}", idx)) != -1) {
            endIdx.add(idx);
            idx++;
        }

        for (int i = 0; i < startIdx.size(); i++) {
            mustacheString.add(analysisString.substring(startIdx.get(i) + 2, endIdx.get(i)));
        }
    }

    public static List<Integer> getStartIdx() {
        return startIdx;
    }

    public static List<Integer> getEndIdx() {
        return endIdx;
    }

    public static List<String> getMustacheString() {
        return mustacheString;
    }
}
