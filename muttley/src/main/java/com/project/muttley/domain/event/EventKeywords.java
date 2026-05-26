package com.project.muttley.domain.event;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EventKeywords {

  private EventKeywords() {
  }

  public static String normalize(String raw) {
    if (raw == null || raw.isBlank()) {
      return null;
    }
    return parse(raw).stream().collect(Collectors.joining(","));
  }

  public static List<String> parse(String raw) {
    if (raw == null || raw.isBlank()) {
      return List.of();
    }
    return Arrays.stream(raw.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toList();
  }

  public static void validateMax(String raw, int max) {
    if (parse(raw).size() > max) {
      throw new IllegalArgumentException("No máximo " + max + " competências são permitidas");
    }
  }
}
