package com.example.demo.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginInfo {

  private String email;
  private String password;
}