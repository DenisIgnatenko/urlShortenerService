package urlshortenerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ShortenUrlRequest {

    @NotBlank(message = "Original URL is required")
    private String originalUrl;
}
