package com.urlshortener.service;

import com.urlshortener.dto.UrlLongRequest;
import com.urlshortener.entity.UrlEntity;
import com.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    public static final String LONG_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    public static final String SHORT_URL = "https://www.youtube.com/k";
    @InjectMocks
    UrlShortenerService urlService;

    @Mock
    UrlRepository mockUrlRepository;

    @Mock
    BaseConversion mockBaseConversion;

    @Test
    public void reduceUrlTest_200_newUrl() throws MalformedURLException {
        UrlLongRequest urlLongRequest = new UrlLongRequest();
        urlLongRequest.setLongUrl(LONG_URL);
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setId(10);

        when(mockUrlRepository.findByLongUrl(anyString())).thenReturn(Optional.empty());
        when(mockUrlRepository.save(any(UrlEntity.class))).thenReturn(urlEntity);
        when(mockBaseConversion.encode(anyLong())).thenReturn("k");

        String result = urlService.reduceUrl(urlLongRequest);
        assertEquals(SHORT_URL, result);
    }

    @Test
    public void reduceUrlTest_200_urlExist() throws MalformedURLException {
        UrlLongRequest urlLongRequest = new UrlLongRequest();
        urlLongRequest.setLongUrl(LONG_URL);
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setId(10);

        when(mockUrlRepository.findByLongUrl(anyString())).thenReturn(Optional.of(urlEntity));
        when(mockBaseConversion.encode(anyLong())).thenReturn("k");

        String result = urlService.reduceUrl(urlLongRequest);
        assertEquals(SHORT_URL, result);
    }

    @Test
    public void reduceUrlTest_500_MalformedURLException() throws MalformedURLException {
        UrlLongRequest urlLongRequest = new UrlLongRequest();
        urlLongRequest.setLongUrl("toto");
        Assertions.assertThrows(MalformedURLException.class, () -> urlService.reduceUrl(urlLongRequest));
    }

    @Test
    public void getOriginalUrlTest() throws MalformedURLException {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setId(10);
        urlEntity.setLongUrl(LONG_URL);
        when(mockBaseConversion.decode(anyString())).thenReturn(2L);
        when(mockUrlRepository.findById(anyLong())).thenReturn(Optional.of(urlEntity));

        String result = urlService.getLongUrl(SHORT_URL);
        assertEquals(LONG_URL, result);
    }
}
