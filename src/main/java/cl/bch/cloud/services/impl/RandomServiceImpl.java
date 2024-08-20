package cl.bch.cloud.services.impl;

import cl.bch.cloud.repositories.RandomGeneratedRepository;
import cl.bch.cloud.dtos.MessageDTO;
import cl.bch.cloud.entities.RandomGenerated;
import cl.bch.cloud.services.RandomService;

import io.micrometer.observation.annotation.Observed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Observed
@Service("RandomService")
public class RandomServiceImpl implements RandomService {

    private static final Logger logger = LoggerFactory.getLogger(RandomServiceImpl.class);

    private final RandomGeneratedRepository randomGeneratedRepository;

    @Value("${properties.randomService.minRandomValue}")
    private int min;

    @Value("${properties.randomService.statusMessageOk}")
    private String statusMessageOk;

    @Value("${properties.randomService.okMessage}")
    private String okMessage;

    @Value("${properties.randomService.statusMessageNok}")
    private String statusMessageNok;

    @Value("${properties.randomService.errorMessage}")
    private String errorMessage;

    @Autowired
    public RandomServiceImpl(final RandomGeneratedRepository randomGeneratedRepository) {
        this.randomGeneratedRepository = randomGeneratedRepository;
    }

    /**
     * Generates a random number between zero and a given max number
     * @param max {@link int} An int with the max value for the random number
     * @return {@link MessageDTO} A message with a text with the random number
     *         generated or an error text if the given number is lower than zero
     */
    @Override
    public MessageDTO random(final int max) {
        if (max < min) {
            String message = String.format(errorMessage, min);

            RandomGenerated randomGenerated = new RandomGenerated(min, max, -1, message, statusMessageNok);
            this.saveRandomGenerated(randomGenerated);

            return new MessageDTO(statusMessageNok, message);
        } else {
            SecureRandom secureRandom = new SecureRandom();
            int random = secureRandom.nextInt(secureRandom.nextInt(max-min+1)+min);
            String message = String.format(okMessage, min, max, random);

            RandomGenerated randomGenerated = new RandomGenerated(min, max, random, message, statusMessageOk);
            this.saveRandomGenerated(randomGenerated);

            return new MessageDTO(statusMessageOk, message);
        }
    }

    /**
     * Saves a random number generated into the database corresponding table
     * @param randomGenerated {@link RandomGenerated} The random number generated
     *                        with its message
     */
    private void saveRandomGenerated(final RandomGenerated randomGenerated) {
        randomGeneratedRepository.save(randomGenerated);
        logger.info("randomGenerated inserted in the Database");
    }

}