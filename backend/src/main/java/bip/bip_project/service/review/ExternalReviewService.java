package bip.bip_project.service.review;

import bip.bip_project.model.review.ExternalReview;
import bip.bip_project.model.review.Review;
import bip.bip_project.repository.review.IExternalReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExternalReviewService implements IExternalReviewService {

    private final IExternalReviewRepository externalReviewRepository;
    private final RestTemplate restTemplate;

    public ExternalReviewService(IExternalReviewRepository externalReviewRepository) {
        this.externalReviewRepository = externalReviewRepository;
        this.restTemplate = new RestTemplate(); // Можно также внедрять как @Bean
    }

    @Override
    public List<ExternalReview> fetchAndSaveExternalReviews(Review review) {
        // Параметры запроса берём из устройства
        String brand = review.getDevice().getManufacturer();
        String model = review.getDevice().getModel();

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8000/api/reviews")
                .queryParam("brand", brand)
                .queryParam("model", model)
                .queryParam("language", "ru")
                .toUriString();

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        List<Map<String, Object>> rawData = (List<Map<String, Object>>) response.getBody().get("data");

        List<ExternalReview> externalReviews = new ArrayList<>();

        for (Map<String, Object> data : rawData) {
            ExternalReview er = new ExternalReview();
            er.setTitle((String) data.get("title"));
            er.setContent((String) data.get("content"));
            er.setPros((String) data.get("pros"));
            er.setCons((String) data.get("cons"));
            er.setAuthor((String) data.get("author"));
            er.setDate((String) data.get("date"));
            er.setSource((String) data.get("source"));
            er.setUrl((String) data.get("url"));
            String retrievedAtRaw = (String) data.get("retrieved_at");
            if (retrievedAtRaw != null) {
                er.setRetrievedAt(OffsetDateTime.parse(retrievedAtRaw));
            }
            er.setReview(review);
            externalReviews.add(er);
        }

        return externalReviewRepository.saveAll(externalReviews);
    }
}
