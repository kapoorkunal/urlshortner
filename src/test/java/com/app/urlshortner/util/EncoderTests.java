package com.app.urlshortner.util;

import org.junit.Assert;
import org.junit.Test;

public class EncoderTests {

  @Test
  public void testEncoder() {
    String longURL = "https://mail.google.com/mail/u/0/#inbox/FMfcgxwDrlRLFSssgkdHwHcJhLFSHzTk?projector=1&messagePartId=0.1";
    String shortURL = Encoder.getStringEncoding(longURL);
    Assert.assertNotNull(shortURL);
  }
}
