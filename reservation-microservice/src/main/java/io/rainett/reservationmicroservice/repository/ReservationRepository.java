package io.rainett.reservationmicroservice.repository;

import io.rainett.reservationmicroservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            select (count(r) > 0) from Reservation r
            where r.id = ?1
            and r.checkInDate between ?2 and ?3
            and r.checkOutDate between ?2 and ?3
            """)
    boolean existsWhereDateBetween(Long id, LocalDate checkInDate, LocalDate checkOutDate);
}
