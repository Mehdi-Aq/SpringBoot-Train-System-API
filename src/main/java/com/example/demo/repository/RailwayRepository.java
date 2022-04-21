package com.example.demo.repository;

import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.service.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RailwayRepository {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Long registerUser(User u) {
    Integer count = jdbcTemplate.queryForObject(
            "select count(*) from Users where Email=?",
            Integer.class,
            u.getEmail()
    );
    if (count == null || count != 0) {
      throw new EmailAlreadyRegisteredException("Email has already been used to create an account");
    } else {
      Integer newID = jdbcTemplate.queryForObject(
              "select max(UserID) from Users", Integer.class);
      u.setUserID(++newID);
      jdbcTemplate.update(
              "insert into Users values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
              u.getUserID(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(),
              u.getSubscription(), u.getPostalCode(), u.getProvince(), u.getCountry(), u.getPhoneNumber()
      );
    }
    return u.getUserID();
  }

  public User getAccount(LoginInfo loginInfo) {
    User user = jdbcTemplate.queryForObject(
            "select * from Users where email = ?",
            (rs, rowNum) ->
                    new User(
                            rs.getLong("UserID"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Subscription"),
                            rs.getString("PostalCode"),
                            rs.getString("Province"),
                            rs.getString("Country"),
                            rs.getString("PhoneNumber")
                    ),
            loginInfo.getEmail()
    );
    if (user.getPassword().equals(loginInfo.getPassword())) {
      return user;
    } else {
      throw new WrongPasswordException("Wrong password");
    }
  }

  public List<Receipt> getReceipts(long userID) {
    return jdbcTemplate.query(
            "select * from Receipts where UserID = ?",
            (rs, rowNum) ->
                    new Receipt(
                            rs.getString("ConfirmationNb"),
                            rs.getLong("UserID"),
                            rs.getDouble("TotalFare"),
                            rs.getString("PaymentMethod"),
                            rs.getDate("ReceiptDate")
                    ),
            userID
    );
  }

  public List<Ticket> getTickets(String confirmationNb) {
    return jdbcTemplate.query(
            "select * from Tickets where ConfirmationNb = ?",
            (rs, rowNum) ->
                    new Ticket(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getLong(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getInt(6),
                            rs.getInt(7),
                            rs.getDouble(8)
                    ),
            confirmationNb
    );
  }

  public Receipt buyTickets(long userID, String tripNb, String seatsClass, String paymentMethod, int[] seatNb, List<Passenger> passengers) {
    Integer nbOfAvailableSeats = jdbcTemplate.queryForObject(
            "select NbOfAvailableSeats from AvailableSeats where TripNb=? and class=?",
            Integer.class,
            tripNb, seatsClass
    );
    if (nbOfAvailableSeats == null || nbOfAvailableSeats <= passengers.size()) {
      throw new NoAvailableSeatsException("There is not enough available seats for the selected trip and class");
    }
    String confirmationNb = Generator.generateConfirmationNb();
    int nbOfAffectedRows = jdbcTemplate.update(
            "insert into Receipts (ConfirmationNb,UserID,PaymentMethod,ReceiptDate) VALUES (?,?,?,?)",
            confirmationNb, userID, paymentMethod, LocalDate.now()
    );
    if (nbOfAffectedRows != 1) {
      throw new DatabaseErrorException("Receipt hasn't being created");
    }
    Integer newID = jdbcTemplate.queryForObject(
            "select max(PassengerID) from Passengers", Integer.class);
    for (int i = 0; i < passengers.size(); i++) {
      Passenger p = passengers.get(i);
      p.setPassengerID(++newID);
      jdbcTemplate.update(
              "insert into Passengers (PassengerID, FirstName, LastName, Agecategory) values (?,?,?,?)",
              p.getPassengerID(), p.getFirstName(), p.getLastName(), p.getAgeCategory()
      );
      int ticketNb = Generator.generateTicketNb();
      /*
        The Before_insert Trigger in the Tickets table will:
         1. Update the number of available seats remaining for the selected trip.
         2. Add the fare value for each ticket and calculate the TotalFare in the receipt.
       */
      jdbcTemplate.update(
              "insert into Tickets (TicketNb,ConfirmationNb,PassengerID,TripNb,Class,Car,SeatNb) VALUES (?,?,?,?,?,?,?)",
              ticketNb, confirmationNb, p.getPassengerID(), tripNb, seatsClass, ((seatNb[i]/15)+1), seatNb[i]
      );
    }
    // return the receipt for all the purchased tickets
    return jdbcTemplate.queryForObject(
            "select * from Receipts where ConfirmationNb = ?",
            (rs, rowNum) ->
                    new Receipt(
                            rs.getString("ConfirmationNb"),
                            rs.getLong("UserID"),
                            rs.getDouble("TotalFare"),
                            rs.getString("PaymentMethod"),
                            rs.getDate("ReceiptDate")
                    ),
            confirmationNb
    );
  }

  public User modifyAccountInformation(int userID, User u) {
    int nbOfAffectedRows = jdbcTemplate.update(
            "update Users set FirstName=?, LastName=?, Email=?, Password=?, Subscription=?, PostalCode=?, Province=?, Country=?, PhoneNumber=? where UserID=?",
            u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getSubscription(),
            u.getPostalCode(), u.getProvince(), u.getCountry(), u.getPhoneNumber(), userID
    );
    if (nbOfAffectedRows != 1) {
      throw new AccountNotUpdatedException();
    }
    u.setUserID(userID);
    return u;
  }

  public List<Ticket> cancelSomeTicketsInBooking(int[] ticketsNb) {
    List<Ticket> ticketsToCancel = new ArrayList<>();
    for (int i = 0; i < ticketsNb.length; i++) {
      ticketsToCancel.add(jdbcTemplate.queryForObject(
              "select * from Tickets where TicketNb = ?",
              (rs, rowNum) ->
                      new Ticket(
                              rs.getInt(1),
                              rs.getString(2),
                              rs.getLong(3),
                              rs.getString(4),
                              rs.getString(5),
                              rs.getInt(6),
                              rs.getInt(7),
                              rs.getDouble(8)
                      ),
              ticketsNb[i]
      ));
      int nbOfAffectedRows = jdbcTemplate.update(
              "delete from Tickets where TicketNb=?",
              ticketsNb[i]
      );
      if (nbOfAffectedRows == 0) {
        throw new DatabaseErrorException("Tickets couldn't been canceled");
      }
    }
    return ticketsToCancel;
  }

  public Receipt cancelAllTicketsInBooking(String confirmationNb) {
    Receipt bookingToCancel = jdbcTemplate.queryForObject(
            "select * from Receipts where ConfirmationNb = ?",
            (rs, rowNum) ->
                    new Receipt(
                            rs.getString("ConfirmationNb"),
                            rs.getLong("UserID"),
                            rs.getDouble("TotalFare"),
                            rs.getString("PaymentMethod"),
                            rs.getDate("ReceiptDate")
                    ),
            confirmationNb
    );
    int nbOfAffectedRowsInTicketsTable = jdbcTemplate.update(
            "delete from Tickets where ConfirmationNb=?",
            confirmationNb
    );
    int nbOfAffectedRowsInReceiptsTable = jdbcTemplate.update(
            "delete from Receipts where ConfirmationNb=?",
            confirmationNb
    );
    if (nbOfAffectedRowsInReceiptsTable == 0 || nbOfAffectedRowsInTicketsTable == 0) {
      throw new DatabaseErrorException("Booking couldn't been canceled");
    }
    return bookingToCancel;
  }
}