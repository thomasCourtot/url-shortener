package com.urlshortener.controller;

import com.urlshortener.dto.UrlLongRequest;
import com.urlshortener.service.UrlShortenerService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @ApiOperation(value = "Redirect", notes = "Finds original url from short url and redirects")
    @GetMapping("/")
    public ResponseEntity<String> getAndRedirect(@RequestParam String shortUrl) throws MalformedURLException {
        return new ResponseEntity<>(
                urlShortenerService.getLongUrl(shortUrl),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Convert new url", notes = "Converts long url to short url")
    @PostMapping("create-short")
    public String convertToShortUrl(@RequestBody UrlLongRequest request) throws MalformedURLException {
        return urlShortenerService.reduceUrl(request);
    }
}
