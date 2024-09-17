package com.demo.userprojectmanagement.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;

public class Util {
  public static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static Map<String, Object> convertToResponse(
      final Page<?> objects, String collectionName) {
    Map<String, Object> response = new HashMap<>();
    response.put(collectionName, objects.getContent());
    response.put("current-page", objects.getNumber());
    response.put("total-items", objects.getTotalElements());
    response.put("total-pages", objects.getTotalPages());
    return response;
  }
}
