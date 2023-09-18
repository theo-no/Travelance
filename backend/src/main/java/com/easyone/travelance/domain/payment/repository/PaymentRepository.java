package com.easyone.travelance.domain.payment.repository;

import com.easyone.travelance.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTravelRoomId(Long roomId);

    List<Payment> findByTravelRoomIdAndMemberId(Long roomId, Long memberId);
}