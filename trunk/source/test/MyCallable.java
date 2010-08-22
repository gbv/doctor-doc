package test;

import java.util.concurrent.Callable;

import util.Http;

class MyCallable implements Callable<String> {

  private final String link;

  MyCallable( String link ) {
    this.link = link;
  }
  public String call() {
    Http http = new Http();
      String content = http.getWebcontent(link, 2000, 3);
    return content;
  }

}