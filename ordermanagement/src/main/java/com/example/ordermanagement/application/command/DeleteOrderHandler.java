package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.repository.OrderWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteOrderHandler {
    private final OrderWriteRepository writeRepository;

    public DeleteOrderHandler(OrderWriteRepository writeRepository)
    {
        this.writeRepository = writeRepository;
    }

    @Transactional
    public void handle(DeleteOrderCommand command) {
        // FIXED: Convert the String ID from the command to a Long
        Long id = Long.valueOf(command.orderId());

        writeRepository.deleteById(id);
    }
}