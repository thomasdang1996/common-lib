package com.dang.commonlib.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long threadId;
    private UUID messageId;
    private String requestMessage;
    private String requestClassName;
    private String replyMessage;
    private String replyClassName;
}
