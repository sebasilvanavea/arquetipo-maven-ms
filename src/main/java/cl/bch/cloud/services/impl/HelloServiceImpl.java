package cl.bch.cloud.services.impl;

import cl.bch.cloud.dtos.MessageDTO;
import cl.bch.cloud.exceptions.TooManyRequestException;

import cl.bch.cloud.repositories.JsonPlaceHolderRepository;
import cl.bch.cloud.services.HelloService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Observed
@Service("HelloService")
@RequiredArgsConstructor
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Value("${properties.helloService.statusMessageOk}")
    private String statusMessageOk;

    @Value("${properties.helloService.greetingMessage}")
    private String greetingMessage;

    private final JsonPlaceHolderRepository jsonPlaceHolderRepository;

    /**
     * Returns a simple greeting text wrapped in a MessageDTO
     * @return {@link MessageDTO} A simple greeting message
     */
    @Override
    public MessageDTO greetings() {
        return new MessageDTO(statusMessageOk, greetingMessage);
    }

    /**
     * This method will always throw a BchException after fetching post from 
     * an external REST API using feign in order to simulate an error
     * @return {@link MessageDTO} A greeting message that will never be returned
     */
    @Override
    public MessageDTO noGreetings() {
        try {
            jsonPlaceHolderRepository.getAllPosts();
            logger.info("noGreetings - Esta excepcion es solo ilustrativa para mostrar el @ControllerAdvice");
            throw new TooManyRequestException("Too many requests", new RuntimeException());
        } finally {
            /* Esta excepción es sólo ilustrativa para mostrar el @ControllerAdvice */
            logger.info("finnaly noGreeting");
        }
    }

}