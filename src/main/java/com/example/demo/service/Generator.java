package com.example.demo.service;

public class Generator {

  // TicketNb is a 9 digit int
  public static int generateTicketNb() {
    return 100000000 + (int)(Math.random() * 900000000);
  }

  // ConfirmationNb has 3 letters and 3 digits
  public static String generateConfirmationNb() {
    StringBuilder confirmationNb = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      confirmationNb.append((char)('A' + (int)(Math.random() * (('Z' - 'A') + 1))));
    }
    confirmationNb.append(100 + (int)(Math.random() * 900));
    return confirmationNb.toString();
  }
}