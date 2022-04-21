package com.example.demo.model;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Trip {

  private String tripNb;
  private String routeID;
  private int trainNb;
  private Date tripDate;
  private Time departureTime;
  private Time arrivalTime;
  private Time duration;
}