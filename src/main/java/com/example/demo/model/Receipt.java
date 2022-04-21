package com.example.demo.model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Receipt {

  private String confirmationNb;
  private long userID;
  private double totalFare;
  private String paymentMethod;
  private Date receiptDate;
}
/*
ConfirmationNb
UserID
TotalFare
PaymentMethod
ReceiptDate
 */