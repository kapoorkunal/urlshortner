package com.app.urlshortner.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ComponentScan("com.app.urlshortner")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {URLConverterService.class})
public class URLConverterServiceTest {

  @Autowired
  private URLConverterService urlConverterService;

  @Test
  public void testGetShortURL() {
    String longURL = "https://mail.google.com/mail/u/0/#inbox/FMfcgxwDrlRLFSssgkdHwHcJhLFSHzTk?projector=1&messagePartId=0.1";
    String shorURL = urlConverterService.getShortURL(longURL, longURL);
    Assert.assertNotNull(shorURL);
  }

  @Test
  public void testGetLongURL() {
    String shortURL = "e38fb238";
    String longURL = urlConverterService.getLongURL(shortURL);
    Assert.assertNotNull(longURL);
  }

  @Test
  public void testGetCount() {
    String shortURL = "e38fb238";
    long t1 = 0;
    long t2 = System.currentTimeMillis();
    int count = urlConverterService.getShortURLRequestCount(shortURL, t1, t2);
    Assert.assertNotNull(count);
  }
}
