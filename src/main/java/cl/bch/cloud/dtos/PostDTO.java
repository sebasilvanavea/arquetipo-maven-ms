package cl.bch.cloud.dtos;

public record PostDTO(Integer userId,
        Integer id,
        String title,
        String body) {
}