package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;

@Entity
public class ChatMessage{

    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}