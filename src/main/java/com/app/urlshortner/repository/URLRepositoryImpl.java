package com.app.urlshortner.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Configuration
public class URLRepositoryImpl implements URLRepository {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;
  private static final String HASHED_VALUE = "urlshortner";
  private static final String SHORT_URL_COUNT = "urlshortnercounter";

  private static final Logger logger = LoggerFactory.getLogger(URLRepositoryImpl.class);


  @Override
  public void saveURL(String hashedKey, String longURL) {
    logger.info("adding id {} to cache for longURL object {}", hashedKey, longURL);
    String key = HASHED_VALUE.concat("-").concat(hashedKey);
    try {
      //storing shorturl with longurl
      redisTemplate.opsForHash().put(key, hashedKey, longURL);
    } catch (DataAccessException e) {
      logger.error(HASHED_VALUE + e.getMessage());
    }
  }

  @Override
  public String getById(String hashedKey) {
    logger.info("reading cache for hashedKey {}", hashedKey);
    String longURL = null;
    try {
      // get long url by short url
      String key = HASHED_VALUE.concat("-").concat(hashedKey);
      longURL = (String) redisTemplate.opsForHash().get(key, hashedKey);
      if (longURL != null) {
        String zkey = SHORT_URL_COUNT.concat("-").concat(hashedKey);
        redisTemplate.opsForZSet().add(zkey, System.currentTimeMillis(), 1);
      }
    } catch (DataAccessException e) {
      logger.error(HASHED_VALUE + e.getMessage());
    }
    return (null == longURL) ? null : longURL;
  }

  @Override
  public int getCountById(String hashedKey, long start, long end) {
    logger.info("reading count for hashedKey {}", hashedKey);
    int count = 0;
    try {
      String key = SHORT_URL_COUNT.concat("-").concat(hashedKey);
      // getting count between the range of timestamps
      count = redisTemplate.opsForZSet().range(key, start, end).size();
    } catch (DataAccessException e) {
      logger.error(HASHED_VALUE + e.getMessage());
    }
    return count;
  }

  @Override
  public void evict(String hashedKey) {
    logger.info("evict(String hashedKey) {}", hashedKey);
    try {
      String key = HASHED_VALUE.concat("-").concat(hashedKey);
      redisTemplate.opsForHash().delete(key, hashedKey);
    } catch (DataAccessException e) {
      logger.error(HASHED_VALUE + e.getMessage());
    }
  }
}
