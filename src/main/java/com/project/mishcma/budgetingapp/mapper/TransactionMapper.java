package com.project.mishcma.budgetingapp.mapper;

import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Transaction;
import org.modelmapper.ModelMapper;

public class TransactionMapper {

  private static final ModelMapper modelMapper = new ModelMapper();

  public static TransactionDTO toDTO(Transaction transaction) {
    return modelMapper.map(transaction, TransactionDTO.class);
  }

  public static Transaction toEntity(TransactionDTO transactionDTO) {
    return modelMapper.map(transactionDTO, Transaction.class);
  }
}
