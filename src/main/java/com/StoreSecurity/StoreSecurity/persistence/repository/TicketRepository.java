package com.StoreSecurity.StoreSecurity.persistence.repository;

import com.StoreSecurity.StoreSecurity.persistence.entity.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
