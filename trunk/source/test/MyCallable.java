package test;

import java.util.concurrent.Callable;

import util.Http;

class MyCallable implements Callable<String> {

    private final transient String link;

    MyCallable( final String link ) {
        this.link = link;
    }
    public String call() {
        final Http http = new Http();

        return http.getWebcontent(link, 2000, 3);
    }

}