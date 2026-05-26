package com.project.muttley.util;

public final class CpfUtils {

  private CpfUtils() {
  }

  public static String normalize(String cpf) {
    if (cpf == null) {
      return null;
    }
    return cpf.replaceAll("\\D", "");
  }

  public static boolean isValid(String cpf) {
    String digits = normalize(cpf);
    if (digits == null || digits.length() != 11) {
      return false;
    }
    if (digits.chars().distinct().count() == 1) {
      return false;
    }

    int firstDigit = calculateDigit(digits, 10, 10);
    int secondDigit = calculateDigit(digits, 11, 11);

    return digits.charAt(9) == Character.forDigit(firstDigit, 10)
        && digits.charAt(10) == Character.forDigit(secondDigit, 10);
  }

  public static String format(String cpf) {
    String digits = normalize(cpf);
    if (digits == null || digits.length() != 11) {
      return cpf;
    }
    return digits.substring(0, 3) + "."
        + digits.substring(3, 6) + "."
        + digits.substring(6, 9) + "-"
        + digits.substring(9, 11);
  }

  private static int calculateDigit(String digits, int length, int weightStart) {
    int sum = 0;
    int weight = weightStart;
    for (int i = 0; i < length - 1; i++) {
      sum += Character.getNumericValue(digits.charAt(i)) * weight--;
    }
    int remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }
}
