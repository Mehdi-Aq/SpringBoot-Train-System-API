package com.example.demo.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReceiptDAO {

  private String confirmationNb;
  private long userID;
  private double totalFare;
  private String paymentMethod;
  private Date receiptDate;
  private List<Ticket> tickets;
}