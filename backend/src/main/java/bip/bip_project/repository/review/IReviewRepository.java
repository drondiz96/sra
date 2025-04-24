package bip.bip_project.repository.review;

import bip.bip_project.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.dateOfCreation BETWEEN :startDate AND :endDate")
    List<Review> findAllBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Review r WHERE r.device.deviceType = :deviceType")
    List<Review> findByDeviceType(@Param("deviceType") String deviceType);

    @Query("SELECT r FROM Review r WHERE r.device.model = :deviceModel")
    List<Review> findByDeviceModel(@Param("deviceModel") String deviceModel);

    @Query("SELECT r FROM Review r WHERE r.device.manufacturer = :manufacturer")
    List<Review> findByManufacturer(@Param("manufacturer") String manufacturer);
}