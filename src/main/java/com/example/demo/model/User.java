package com.example.demo.model;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
  private long userID;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String subscription;
  private String postalCode;
  private String province;
  private String country;
  private String phoneNumber;

  public User(long userID, String password) {
    this.userID = userID;
    this.password = password;
  }
}

/*
UserID int PK
FirstName varchar(30)
LastName varchar(30)
Email varchar(100)
Password varchar(30)
Subscription char(3)
PostalCode char(6)
Province varchar(50)
Country varchar(50)
PhoneNumber varchar(12)
 */