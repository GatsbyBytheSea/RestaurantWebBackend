package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(Reservation reservation) {
        // 设置初始状态
        reservation.setStatus("CREATED");
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setUpdateTime(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        Optional<Reservation> opt = reservationRepository.findById(id);
        return opt.orElseThrow(() -> new RuntimeException("Reservation not found with id = " + id));
    }

    public List<Reservation> getReservationsByCustomerPhone(String phone) {
        return reservationRepository.findByCustomerPhone(phone);
    }

    public List<Reservation> getReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerName(customerName);
    }

    public List<Reservation> getReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }


    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation updateReservation(Long id, Reservation updated) {
        Reservation existing = getReservationById(id);
        // 根据实际需求，校验是否允许更新
        existing.setReservationTime(updated.getReservationTime());
        existing.setNumberOfGuests(updated.getNumberOfGuests());
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerPhone(updated.getCustomerPhone());
        existing.setUpdateTime(LocalDateTime.now());
        return reservationRepository.save(existing);
    }

    public void confirmReservation(Long id) {
        Reservation existing = getReservationById(id);

        existing.setStatus("CONFIRMED");
        existing.setUpdateTime(LocalDateTime.now());
        reservationRepository.save(existing);
    }

    public void cancelReservation(Long id) {
        Reservation existing = getReservationById(id);
        // todo: 增加业务逻辑: 检查是否超时、是否已确认等
        existing.setStatus("CANCELLED");
        existing.setUpdateTime(LocalDateTime.now());
        reservationRepository.save(existing);
    }
}
