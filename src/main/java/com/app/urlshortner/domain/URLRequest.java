package com.app.urlshortner.domain;

public class URLRequest {

  private String url;
  private long t1;
  private long t2;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getT1() {
    return t1;
  }

  public long getT2() {
    return t2;
  }

  public void setT1(long t1) {
    this.t1 = t1;
  }

  public void setT2(long t2) {
    this.t2 = t2;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
