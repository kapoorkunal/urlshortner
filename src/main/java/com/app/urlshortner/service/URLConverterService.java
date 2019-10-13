package com.app.urlshortner.service;

import com.app.urlshortner.repository.URLRepository;
import com.app.urlshortner.util.Encoder;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("com.app.urlshortner")
public class URLConverterService {

  private static final Logger logger = LoggerFactory.getLogger(URLConverterService.class);

  @Autowired
  private URLRepository urlRepository;

  public String getShortURL(@NotNull String longURL, @NotNull String originalURL) {

    logger.info("getShortURL for - {}", longURL);
    String shortURL;
    String encodedString = Encoder.getStringEncoding(longURL);
    int encodedStringlen = encodedString.length();
    if (encodedStringlen > 8) {
      String shortencodedString = encodedString.substring(0, 8);
      shortURL = shortencodedString;
    } else {
      shortURL = encodedString;
    }
    String existingURL = urlRepository.getById(shortURL);

    if (null != existingURL) {
      shortURL = getShortURL(encodedString, existingURL);
    } else {
      urlRepository.saveURL(shortURL, originalURL);
    }
    return shortURL;
  }

  public String getLongURL(@NotNull String shortURL) {
    logger.info("getLongURL for {} ", shortURL);
    String longURL = urlRepository.getById(shortURL);
    return longURL;
  }

  public int getShortURLRequestCount(@NotNull String shortURL, long t1, long t2) {
    logger.info("getShortURLRequestCount for {} ", shortURL);
    int count = urlRepository.getCountById(shortURL, t1, t2);
    return count;
  }

}
