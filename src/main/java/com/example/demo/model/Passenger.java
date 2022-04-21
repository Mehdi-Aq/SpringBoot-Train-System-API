package com.example.demo.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Passenger {
  private long passengerID;
  private String firstName;
  private String lastName;
  private String ageCategory;
}

/*
PassengerID int PK
FirstName varchar(30)
LastName varchar(30)
AgeCategory char(5)
 */