package com.urlshortener.service;

import com.urlshortener.dto.UrlLongRequest;
import com.urlshortener.entity.UrlEntity;
import com.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final BaseConversion conversion;

    public String reduceUrl(UrlLongRequest urlLongRequest) throws MalformedURLException {
        URL url = new URL(urlLongRequest.getLongUrl());
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setLongUrl(urlLongRequest.getLongUrl());
        UrlEntity entitySave = urlRepository.findByLongUrl(urlLongRequest.getLongUrl()).orElse(urlRepository.save(urlEntity));

        return url.getProtocol()
                + "://"
                + url.getAuthority()
                + "/"
                + conversion.encode(entitySave.getId());
    }

    public String getLongUrl(String shortUrl) throws MalformedURLException {
        URL url = new URL(shortUrl);
        long id = conversion.decode(url.getPath().replace("/", ""));

        UrlEntity entity = urlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with " + shortUrl));

        return entity.getLongUrl();
    }

}
