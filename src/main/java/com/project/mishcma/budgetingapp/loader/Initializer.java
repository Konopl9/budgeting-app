package com.project.mishcma.budgetingapp.loader;

import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class Initializer {

  Logger logger = LoggerFactory.getLogger(Initializer.class);

  public Initializer() {}

  @EventListener({ApplicationReadyEvent.class, TransactionResetEvent.class})
  public void reset() {
    logger.info("Pre-populated data:");
  }
}
