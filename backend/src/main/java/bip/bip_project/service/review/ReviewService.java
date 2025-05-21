package bip.bip_project.service.review;

import bip.bip_project.exception.review.ReviewException;
import bip.bip_project.exception.review.ReviewNotFoundException;
import bip.bip_project.model.device.Device;
import bip.bip_project.model.device.DeviceMapper;
import bip.bip_project.model.device.DeviceRequestDto;
import bip.bip_project.model.device.FilterType;
import bip.bip_project.model.review.Review;
import bip.bip_project.model.review.ReviewRequestDto;
import bip.bip_project.model.review.ReviewResponseDto;
import bip.bip_project.model.review.ReviewMapper;
import bip.bip_project.model.user.User;
import bip.bip_project.repository.review.IReviewRepository;
import bip.bip_project.service.user.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {

    IUserService userService;
    IReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    DeviceMapper deviceMapper;
    IExternalReviewService externalReviewService;

    public ReviewService(IUserService userService, IReviewRepository reviewRepository, ReviewMapper reviewMapper, DeviceMapper deviceMapper, IExternalReviewService externalReviewService) {
        this.userService = userService;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.deviceMapper = deviceMapper;
        this.externalReviewService = externalReviewService;
    }

    @Override
    public List<ReviewResponseDto> getReviews(Map<String, LocalDate> dates) {
        LocalDate startDate = dates.get("startDate");
        LocalDate endDate = dates.get("endDate");
        if (startDate == null || endDate == null) {
            throw new ReviewException("Field 'startDate' or 'endDate' is null in Map<String, LocalDate> dates");
        }
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for (Review review : reviewRepository.findAllBetweenDates(startDate, endDate)){
            reviewResponseDtos.add(reviewMapper.toDto(review));
        }
        return reviewResponseDtos;
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public boolean existsById(Integer reviewId) {
        return reviewRepository.existsById(reviewId);
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isEmpty()) {
            throw new ReviewNotFoundException("Not found Review with such ID");
        }
        return review.get();
    }

    @Override
    public ReviewResponseDto getReviewResponseDtoById(Integer reviewId) {
        return reviewMapper.toDto(getReviewById(reviewId));
    }

    @Override
    public List<ReviewResponseDto> getReviewsByFilter(FilterType filterType, String value) {
        List<ReviewResponseDto> reviews = new ArrayList<>();

        switch (filterType) {
            case DEVICE_TYPE:
                for (Review review : reviewRepository.findByDeviceType(value)) {
                    reviews.add(reviewMapper.toDto(review));
                }
                return reviews;
            case DEVICE_MODEL:
                for (Review review : reviewRepository.findByDeviceModel(value)) {
                    reviews.add(reviewMapper.toDto(review));
                }
                return reviews;
            case MANUFACTURER:
                for (Review review : reviewRepository.findByManufacturer(value)) {
                    reviews.add(reviewMapper.toDto(review));
                }
                return reviews;
            default:
                throw new IllegalArgumentException("Unknown filter type: " + filterType);
        }
    }

    @Override
    public ReviewResponseDto createReview(String email, ReviewRequestDto reviewRequestDto) {
        User author = userService.getUserByEmail(email);

        Review review = reviewMapper.toEntity(reviewRequestDto);

        review.setDateOfCreation(LocalDate.now());
        review.setAuthor(author);

        Device device = review.getDevice();
        if (device != null) {
            device.setReview(review); // установить обратную связь
            device.setDateOfCreation(LocalDate.now());
        }
        reviewRepository.save(review);

        // Получаем и сохраняем внешние обзоры
        externalReviewService.fetchAndSaveExternalReviews(review);

        Review savedReview = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new RuntimeException("Review not found after save"));

        return reviewMapper.toDto(savedReview);
    }

    @Override
    public ReviewResponseDto updateReview(String email, ReviewRequestDto reviewRequestDto) {
        User author = userService.getUserByEmail(email);

        if (reviewRequestDto.getId() == null) {
            throw new IllegalArgumentException("Review ID must be provided for update");
        }

        Review existingReview = getReviewById(reviewRequestDto.getId());

        if (!existingReview.getAuthor().getEmail().equals(email)) {
            throw new SecurityException(String.format("User with email: %s do not have permission to update this review", email));
        }

        reviewMapper.updateReviewFromDto(reviewRequestDto, existingReview);
        existingReview.setDateOfChanged(LocalDate.now());

        // Обновление устройства
        DeviceRequestDto deviceDto = reviewRequestDto.getDevice();
        if (deviceDto != null) {
            Device existingDevice = existingReview.getDevice();

            if (existingDevice == null) {
                Device newDevice = deviceMapper.toEntity(deviceDto);
                newDevice.setReview(existingReview);
                newDevice.setDateOfCreation(LocalDate.now());
                existingReview.setDevice(newDevice);
            } else {
                deviceMapper.updateDeviceFromDto(deviceDto, existingDevice);
                existingDevice.setReview(existingReview); // на всякий случай
            }
        }
        reviewRepository.save(existingReview);

        return reviewMapper.toDto(existingReview);
    }


    @Override
    public void deleteReviewById(String email, Integer reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ReviewNotFoundException(String.format("Not found Review with ID = %d", reviewId));
        }
        Review review = getReviewById(reviewId);

        if (!review.getAuthor().getEmail().equals(email)) {
            throw new SecurityException(String.format("User with email: %s do not have permission to delete this review", email));
        }
        reviewRepository.deleteById(reviewId);
    }
}
