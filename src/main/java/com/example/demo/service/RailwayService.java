package com.example.demo.service;

import com.example.demo.exception.AccountNotUpdatedException;
import com.example.demo.exception.DatabaseErrorException;
import com.example.demo.exception.EmptyResultSetException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.RailwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RailwayService {

  @Autowired
  RailwayRepository railwayRepository;

  public Long registerUser(User user) {
    return railwayRepository.registerUser(user);
  }

  public User getAccount(LoginInfo loginInfo) {
    try {
      return railwayRepository.getAccount(loginInfo);
    } catch (DataAccessException exception) {
      throw new UserNotFoundException("No user is registered with this email: " + loginInfo.getEmail());
    }
  }

  public List<Receipt> getReceipts(long userID) {
    List<Receipt> receipts = railwayRepository.getReceipts(userID);
    if (receipts.isEmpty()) {
      throw new EmptyResultSetException("No purchases have been made by this user");
    }
    return receipts;
  }

  public List<Ticket> getTickets(String confirmationNb) {
    List<Ticket> tickets = railwayRepository.getTickets(confirmationNb);
    if (tickets.isEmpty()) {
      throw new EmptyResultSetException("This confirmation number doesn't match any booking");
    }
    return tickets;
  }

  public Receipt buyTickets(long userID, String tripNb, String seatsClass, String paymentMethod, int[] seatNb, List<Passenger> passengers) {
    return railwayRepository.buyTickets(userID, tripNb, seatsClass, paymentMethod, seatNb, passengers);
  }

  public User modifyAccountInformation(int userID, User user) {
    try {
      return railwayRepository.modifyAccountInformation(userID, user);
    } catch (DataAccessException | AccountNotUpdatedException exception) {
      throw new DatabaseErrorException("Account information update failed");
    }
  }

  public List<Ticket> cancelSomeTicketsInBooking(int[] ticketsNb) {
    return railwayRepository.cancelSomeTicketsInBooking(ticketsNb);
  }

  public Receipt cancelAllTicketsInBooking(String confirmationNb) {
    return railwayRepository.cancelAllTicketsInBooking(confirmationNb);
  }
}