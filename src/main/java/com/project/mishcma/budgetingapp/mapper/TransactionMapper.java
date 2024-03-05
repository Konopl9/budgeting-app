package com.project.mishcma.budgetingapp.mapper;

import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Transaction;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

  private final ModelMapper modelMapper;

  public TransactionMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
    this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    // Create TypeMap for Transaction to TransactionDTO
    TypeMap<Transaction, TransactionDTO> transactionTypeMapToDTO = this.modelMapper.createTypeMap(Transaction.class, TransactionDTO.class);
    transactionTypeMapToDTO.addMapping(Transaction::getInitialQuantity, TransactionDTO::setQuantity);
    transactionTypeMapToDTO.addMappings(mapper -> mapper.skip(TransactionDTO::setTotalUnrealizedPL));
    transactionTypeMapToDTO.addMappings(mapper -> mapper.skip(TransactionDTO::setPortfolioDTO));

    // Create TypeMap for TransactionDTO to Transaction
    TypeMap<TransactionDTO, Transaction> transactionTypeMapToEntity = this.modelMapper.createTypeMap(TransactionDTO.class, Transaction.class);
    transactionTypeMapToEntity.addMapping(TransactionDTO::getQuantity, Transaction::setInitialQuantity);
  }

  public TransactionDTO toDTO(Transaction transaction) {
    return modelMapper.map(transaction, TransactionDTO.class);
  }

  public Transaction toEntity(TransactionDTO transactionDTO) {
    return modelMapper.map(transactionDTO, Transaction.class);
  }
}
