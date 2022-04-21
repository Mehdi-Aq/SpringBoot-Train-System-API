package com.example.demo.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class EmailAlreadyRegisteredException extends RuntimeException {

  private String message;

}