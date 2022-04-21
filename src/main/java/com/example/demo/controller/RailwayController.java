package com.example.demo.controller;


import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.service.RailwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RailwayController {

  @Autowired
  RailwayService railwayService;

  @PostMapping("/register")
    public ResponseEntity<Long> registerUser(@RequestBody User user) {
    try {
      return new ResponseEntity<>(railwayService.registerUser(user), HttpStatus.OK);
    }
    catch (EmailAlreadyRegisteredException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/login")
  public ResponseEntity<User> getAccount(@RequestBody LoginInfo loginInfo) {
    try {
      return new ResponseEntity<>(railwayService.getAccount(loginInfo), HttpStatus.OK);
    }
    catch (UserNotFoundException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    catch (WrongPasswordException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
  }

  @GetMapping("/user/{userID}/receipts")
  public ResponseEntity<List<Receipt>> getReceipts(@PathVariable long userID) {
    try {
      return new ResponseEntity<>(railwayService.getReceipts(userID), HttpStatus.OK);
    }
    catch (EmptyResultSetException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/tickets")
  public ResponseEntity<List<Ticket>> getTicketsByConfirmationNumber(@RequestParam(value = "ConfirmationNb") String confirmationNb) {
    try {
      return new ResponseEntity<>(railwayService.getTickets(confirmationNb), HttpStatus.OK);
    }
    catch (EmptyResultSetException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/user/{userID}/buy-tickets")
  // Example:  /user/1019/buy-tickets?TripNb=MT12162108&Class=Economy&PaymentMethod=Visa&SeatNb=46,47,48
  public ResponseEntity<Receipt> buyTickets(@RequestBody List<Passenger> passengers,
                                            @PathVariable long userID,
                                            @RequestParam(value = "TripNb") String tripNb,
                                            @RequestParam(value = "Class") String seatsClass,
                                            @RequestParam(value = "PaymentMethod") String paymentMethod,
                                            @RequestParam(value = "SeatNb") int[] seatNb) {
    try {
      return new ResponseEntity<>(railwayService.buyTickets(userID, tripNb, seatsClass, paymentMethod, seatNb, passengers), HttpStatus.OK);
    }
    catch (NoAvailableSeatsException | DatabaseErrorException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    catch (UnsuccessfulPaymentException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.PAYMENT_REQUIRED);
    }
  }

  @PutMapping("/user/{userID}")
  public ResponseEntity<User> modifyAccountInformation(@PathVariable int userID,
                                                       @RequestBody User user) {
    try {
      return new ResponseEntity<>(railwayService.modifyAccountInformation(userID, user), HttpStatus.OK);
    }
    catch (DatabaseErrorException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/cancel-tickets") // Cancel one or more tickets in a booking. Example: /cancel-tickets?TicketNb=643216889,879840656
  public ResponseEntity<List<Ticket>> cancelSomeTicketsInBooking(@RequestParam(value = "TicketNb") int[] ticketsNb) {
    try {
      return new ResponseEntity<>(railwayService.cancelSomeTicketsInBooking(ticketsNb), HttpStatus.OK);
    }
    catch (DatabaseErrorException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/cancel-booking") //  Cancel all the tickets in a booking. Example: /cancel-booking?ConfirmationNb=VYG518
  public ResponseEntity<Receipt> cancelAllTicketsInBooking(@RequestParam(value = "ConfirmationNb") String confirmationNb) {
    try {
      return new ResponseEntity<>(railwayService.cancelAllTicketsInBooking(confirmationNb), HttpStatus.OK);
    }
    catch (DatabaseErrorException exception) {
      return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}