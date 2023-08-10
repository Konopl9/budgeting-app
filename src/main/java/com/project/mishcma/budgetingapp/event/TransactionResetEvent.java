package com.project.mishcma.budgetingapp.event;

import org.springframework.context.ApplicationEvent;

import java.time.Instant;

public class TransactionResetEvent extends ApplicationEvent {
    public TransactionResetEvent() {
        super(Instant.now());
    }
}
