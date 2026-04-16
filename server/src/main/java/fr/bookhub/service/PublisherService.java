package fr.bookhub.service;

import fr.bookhub.entity.Publisher;
import fr.bookhub.repository.PublisherRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public ServiceResponse<Publisher> createPublisher(String name) {
        if (name.isEmpty()) {
            throw new ApiException(ApiCode.PUBLISHER_NAME_EMPTY);
        }

        Publisher publisher = new Publisher();
        publisher.setName(name.trim());

        publisherRepository.save(publisher);

        return new ServiceResponse<>(ApiCode.PUBLISHER_CREATED, publisher);
    }
}
