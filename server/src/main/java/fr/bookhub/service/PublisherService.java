package fr.bookhub.service;

import fr.bookhub.entity.Publisher;
import fr.bookhub.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public ServiceResponse<Publisher> createPublisher(String name) {
        if (name.isEmpty()) {
            return new ServiceResponse<>("2001", "Publisher name can't be empty");
        }

        Publisher publisher = new Publisher();
        publisher.setName(name.trim());

        publisherRepository.save(publisher);

        return new ServiceResponse<>("2000", "Publisher successfully created", publisher);
    }
}
