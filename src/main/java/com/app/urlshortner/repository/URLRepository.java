package com.app.urlshortner.repository;

public interface URLRepository {

  void saveURL(String key, String longURL);

  String getById(String key);

  void evict(String key);

  int getCountById(String hashedKey, long start, long end);

}
