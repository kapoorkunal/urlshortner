package com.app.urlshortner.util;

import javax.validation.constraints.NotNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder {

  private static final Logger logger = LoggerFactory.getLogger(Encoder.class);

  public static String getStringEncoding(@NotNull String longUrl) {
    logger.info("Converting URL {}", longUrl);
    //using sha1 for encoding
    String encodedBytes = DigestUtils.sha1Hex(longUrl);
    return encodedBytes;
  }
}
