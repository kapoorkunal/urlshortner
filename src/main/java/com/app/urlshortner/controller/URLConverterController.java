package com.app.urlshortner.controller;

import com.app.urlshortner.domain.URLRequest;
import com.app.urlshortner.service.URLConverterService;
import com.app.urlshortner.util.URLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class URLConverterController {

  private static final Logger logger = LoggerFactory.getLogger(URLConverterController.class);

  @Value("${url.domain}")
  private String domain;

  @Autowired
  private URLConverterService urlConverterService;

  @RequestMapping(value = "shorturl/", method = RequestMethod.POST, produces = {"application/json"})
  @ResponseBody
  public ResponseEntity<?> getShortURL(
      @Validated @RequestBody URLRequest urlRequest) {
    StopWatch stopwatch = new StopWatch();
    stopwatch.start();
    logger.info("urlRequest received {}", urlRequest);
    //validating url
    if (urlRequest.getUrl() != null) {
      if (URLValidator.validateLongURL(urlRequest.getUrl())) {
        String originalURL = urlRequest.getUrl();
        //calling shorturl service
        String shortURL = urlConverterService.getShortURL(originalURL, originalURL);
        String response = domain.concat(shortURL);
        stopwatch.stop();
        logger.info("Request for API took Time(ms): {} to shorten the longURL", stopwatch
            .toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
      }
    }
    return new ResponseEntity<>("Please enter a valid URL", HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @RequestMapping(value = "longurl/", method = RequestMethod.POST, produces = {"application/json"})
  @ResponseBody
  public ResponseEntity<?> getLongURL(
      @Validated @RequestBody URLRequest urlRequest) {
    StopWatch stopwatch = new StopWatch();
    stopwatch.start();
    logger.info("urlRequest received {}", urlRequest);
    //validating url
    if (urlRequest.getUrl() != null) {
      String shortURL = urlRequest.getUrl();
      //splitting the shorturl by "/"
      String[] keys = shortURL.split("/");
      String key = keys[1];
      if (URLValidator.validateShortURL(key)) {
        String longURL = urlConverterService.getLongURL(key);
        if (longURL != null && longURL != "") {
          stopwatch.stop();
          logger.info("Request for API took Time(ms): {} to longify", stopwatch
              .toString());
          return new ResponseEntity<>(longURL, HttpStatus.OK);
        }
      }
    }
    return new ResponseEntity<>("Please enter a valid URL", HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @RequestMapping(value = "shorturlcount/", method = RequestMethod.POST, produces = {
      "application/json"})
  @ResponseBody
  public ResponseEntity<?> getshortURLCount(
      @Validated @RequestBody URLRequest urlRequest) {
    StopWatch stopwatch = new StopWatch();
    stopwatch.start();
    logger.info("urlRequest received {}", urlRequest);
    if (urlRequest.getUrl() != null) {
      String shortURL = urlRequest.getUrl();
      String[] keys = shortURL.split("/");
      String key = keys[1];
      if (URLValidator.validateShortURL(key)) {
        // getting count between the range of timestamps
        int count = urlConverterService
            .getShortURLRequestCount(key, urlRequest.getT1(), urlRequest.getT2());
        stopwatch.stop();
        logger.info("Request for API took Time(ms): {} to count", stopwatch
            .toString());
        return new ResponseEntity<>(count, HttpStatus.OK);
      }
    }
    return new ResponseEntity<>("Please enter a valid URL", HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
