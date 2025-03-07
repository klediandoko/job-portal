package com.internship.portal.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.portal.model.enums.ApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();

    @Column(name = "motivational_letter", nullable = false)
    private String motivationalLetter;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Job job;


    public Application() {
    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getMotivationalLetter() {
        return motivationalLetter;
    }

    public void setMotivationalLetter(String motivationalLetter) {
        this.motivationalLetter = motivationalLetter;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", status=" + status +
                ", appliedDate=" + appliedDate +
                ", motivationalLetter='" + motivationalLetter + '\'' +
                '}';
    }
}
