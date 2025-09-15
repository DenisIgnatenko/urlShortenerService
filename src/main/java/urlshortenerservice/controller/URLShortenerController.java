package urlshortenerservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import urlshortenerservice.dto.ShortenUrlRequest;
import urlshortenerservice.dto.ShortenUrlResponse;
import urlshortenerservice.service.URLShortenerService;

@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
@Slf4j
public class URLShortenerController {
    private final URLShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ShortenUrlResponse shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        log.info("Received request to shorten URL: {}", request.getOriginalUrl());
        return urlShortenerService.shortenUrl(request.getOriginalUrl());
    }

    @GetMapping("/{hash}")
    public RedirectView redirectToOriginalUrl(@PathVariable String hash) {
        log.info("Received request to redirect for hash: {}", hash);
        String originalUrl = urlShortenerService.getOriginalUrl(hash);
        return new RedirectView(originalUrl);
    }

}
