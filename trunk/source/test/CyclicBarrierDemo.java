package test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.Http;

public class CyclicBarrierDemo {

  public static void main(String[] args) throws Exception {
    int threads = 3;
    final CyclicBarrier barrier = new CyclicBarrier(threads);

    ExecutorService svc = Executors.newFixedThreadPool(threads);
    for (int i = 0; i < threads; i++) {
      svc.execute(new Runnable() {
        public void run() {
          try {

            String link = "http://gso.gbv.de/sru/DB=2.1/?version=1.1&operation=searchRetrieve&query=pica.zdb%3D%222006505-X%22&recordSchema=pica&sortKeys=YOP%2Cpica%2C0%2C%2C&maximumRecords=10&startRecord=1";

            // 1. Methode
            log("At run()");
            barrier.await();

            Http http = new Http();
            http.getWebcontent(link, 2000, 3);
            log("Content received!");

            log("Wait for end");
            barrier.await();

            log("Done");

            // Aufruf 2. Thread-Methode
//            MyCallable c = new MyCallable(link);
//            ExecutorService executor = Executors.newCachedThreadPool();
//            try {
//            Future<String> content = executor.submit( c );
//            content.get(2, TimeUnit.SECONDS);
//            System.out.println("hat geklappt!");
//            } catch (TimeoutException e) {
//              System.out.println("TimeoutException!");
//            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      Thread.sleep(100);
    }
  }

  private static void log(String msg) {
    System.out.println(System.currentTimeMillis() + ": "
        + Thread.currentThread().getId() + "  " + msg);
  }
}