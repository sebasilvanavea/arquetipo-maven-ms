package cl.bch.cloud.controllers;

import cl.bch.cloud.dtos.MessageDTO;
import cl.bch.cloud.services.RandomService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Observed
@RequiredArgsConstructor
@RequestMapping(path = "/domain/subdomain", produces = MediaType.APPLICATION_JSON_VALUE)
public class RandomController {

    private static final Logger logger = LoggerFactory.getLogger(RandomController.class);

    private final RandomService randomService;

    /**
     * An interface that generates a random number between zero and a given max
     * value
     * @param max {@link int} A max value given as a path variable in the interface
     *            invocation
     * @return {@link MessageDTO} A message with a text with the random number
     *         generated or an error text if the given number is lower than zero
     */
    @Operation(summary = "Generates a random number between zero and a given max value",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "bad input",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ProblemDetail.class)
                                    )
                            }),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ProblemDetail.class)
                                    )
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error de sistema",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ProblemDetail.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "200", description = "random number"),
            })
    @GetMapping(path = { "/random/{max}" })
    public MessageDTO random(@PathVariable int max) {
        logger.info("random - init");
        MessageDTO controllerResponse = randomService.random(max);
        logger.info("random - end");
        return controllerResponse;
    }
}