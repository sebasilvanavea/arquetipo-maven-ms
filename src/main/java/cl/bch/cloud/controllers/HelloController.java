package cl.bch.cloud.controllers;

import cl.bch.cloud.dtos.MessageDTO;
import cl.bch.cloud.services.HelloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HelloController", description = "Controller de prueba")
@Observed
@RestController
@RequestMapping(path = "/domain/subdomain", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    private static final Logger stdLogger = LoggerFactory.getLogger("std.logger");

    private final HelloService helloService;

    /**
     * An interface that could be consumed in the root path for getting a greeting
     * message
     * @return {@link MessageDTO} A simple greeting message
     */
    @Operation(summary = "Returns a greetings message",
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
                    @ApiResponse(responseCode = "200", description = "Greetings message"),
            })
    @GetMapping("/greetings")
    public MessageDTO greetings() {
        logger.info("greetings - init");
        stdLogger.info("greetings - An example of writing to standard log");
        MessageDTO controllerResponse = helloService.greetings();
        logger.info("greetings - end");
        return controllerResponse;
    }

    /**
     * An interface that will always raise a BchException
     * @return {@link MessageDTO} A no greeting message
     */
    @Operation(summary = "Returns a greetings message",
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
                    @ApiResponse(responseCode = "200", description = "Greetings message"),
            })
    @GetMapping("/noGreetings")
    public MessageDTO noGreetings()  {
        logger.info("noGreetings - init");
        MessageDTO controllerResponse = helloService.noGreetings();
        logger.info("noGreetings - end");
        return controllerResponse;
    }

}