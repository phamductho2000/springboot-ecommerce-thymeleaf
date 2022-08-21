package com.webecommerce.springboot.util;

import java.util.Map;
import java.util.stream.Collectors;

public class Util {

    public static Map<String, String> getSearchParams(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(p -> !p.getKey().equalsIgnoreCase("page")
                        && !p.getKey().equalsIgnoreCase("limit")
                        && !p.getKey().equalsIgnoreCase("sort")
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
