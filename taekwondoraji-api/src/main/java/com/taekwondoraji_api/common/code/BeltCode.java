package com.taekwondoraji_api.common.code;

import java.util.List;

public final class BeltCode {

    private static final List<CodeLabel> OPTIONS = List.of(
            new CodeLabel("white", "\uD770\uB760"),
            new CodeLabel("yellow", "\uB178\uB780\uB760"),
            new CodeLabel("green", "\uCD08\uB85D\uB760"),
            new CodeLabel("blue", "\uD30C\uB780\uB760"),
            new CodeLabel("red", "\uBE68\uAC04\uB760"),
            new CodeLabel("black_1", "\uAC80\uC740\uB760 1\uB2E8"),
            new CodeLabel("black_2", "\uAC80\uC740\uB760 2\uB2E8"),
            new CodeLabel("black_3", "\uAC80\uC740\uB760 3\uB2E8"),
            new CodeLabel("black_4", "\uAC80\uC740\uB760 4\uB2E8"),
            new CodeLabel("black_5", "\uAC80\uC740\uB760 5\uB2E8"),
            new CodeLabel("black_6", "\uAC80\uC740\uB760 6\uB2E8"),
            new CodeLabel("black_7", "\uAC80\uC740\uB760 7\uB2E8"),
            new CodeLabel("black_8", "\uAC80\uC740\uB760 8\uB2E8"),
            new CodeLabel("black_9", "\uAC80\uC740\uB760 9\uB2E8")
    );

    private BeltCode() {
    }

    public static List<CodeLabel> options() {
        return OPTIONS;
    }

    public static String label(String value) {
        if ("black".equals(value)) {
            return "\uAC80\uC740\uB760";
        }

        return OPTIONS.stream()
                .filter(option -> option.value().equals(value))
                .findFirst()
                .map(CodeLabel::label)
                .orElse(value == null || value.isBlank() ? "-" : value);
    }

    public static boolean exists(String value) {
        return "black".equals(value) || OPTIONS.stream()
                .anyMatch(option -> option.value().equals(value));
    }
}
