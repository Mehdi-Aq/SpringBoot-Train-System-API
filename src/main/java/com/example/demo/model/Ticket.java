package com.example.demo.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {

  private int ticketNb;
  private String confirmationNb;
  private long passengerID;
  private String tripNb;
  private String seatClass;
  private int car;
  private int seatNb;
  private double fare;
}