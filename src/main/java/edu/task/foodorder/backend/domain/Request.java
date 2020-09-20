package edu.task.foodorder.backend.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User client_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User operator_id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Column(name = "ord")
    private String order = "";

    private String comment = "";

    @CreationTimestamp
    private Timestamp created_at;

    private Timestamp updated_at;

    public Request() {
    }

    public Request(@NotNull Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClient_id() {
        return client_id;
    }

    public void setClient_id(User client_id) {
        this.client_id = client_id;
    }

    public User getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(User operator_id) {
        this.operator_id = operator_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
