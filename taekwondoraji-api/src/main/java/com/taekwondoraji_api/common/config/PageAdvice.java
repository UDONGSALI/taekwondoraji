package com.taekwondoraji_api.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.LinkedHashMap;
import java.util.Map;

// 페이지에 이용된 파람을 화면에서 사용할수 있게 함 / 예시 : <input type="text" name="keyword" th:value="${currentParams.keyword}">
@ControllerAdvice
public class PageAdvice {

    @ModelAttribute("currentParams")
    public Map<String, String> currentParams(HttpServletRequest request) {
        Map<String, String> params = new LinkedHashMap<>();

        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            }
        });

        return params;
    }
}
