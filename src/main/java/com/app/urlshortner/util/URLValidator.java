package com.app.urlshortner.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class URLValidator {

  private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
  private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

  public static boolean validateLongURL(String url) {
    //validation long url
    Matcher m = URL_PATTERN.matcher(url);
    return m.matches();
  }

  public static boolean validateShortURL(String url) {
    //validation short url
    if (url.length() == 8 && url.matches("^[a-zA-Z0-9]*$")) {
      return true;
    }
    return false;
  }
}
