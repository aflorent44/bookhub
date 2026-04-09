package fr.bookhub.entity;

import jakarta.persistence.Entity;

import java.awt.print.Book;
import java.time.LocalDate;

@Entity
public class Loan {
    private Long id;
    private LocalDate debut_date;
    private LocalDate end_date;
    private LocalDate return_date;
    private boolean isLate;
    private Status status;

    private User user;
    private Book book;

    public Loan() {}
    public Loan(Long id, LocalDate debut_date, LocalDate end_date, LocalDate return_date, boolean isLate, Status status, User user, Book book) {
        this.id = id;
        this.debut_date = debut_date;
        this.end_date = end_date;
        this.return_date = return_date;
        this.isLate = isLate;
        this.status = status;
        this.user = user;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDebut_date() {
        return debut_date;
    }

    public void setDebut_date(LocalDate debut_date) {
        this.debut_date = debut_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public LocalDate getReturn_date() {
        return return_date;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
