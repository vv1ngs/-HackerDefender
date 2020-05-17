package Async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * @author vvings
 * @version 2020/5/17 10:56
 */
public class test {

    public static void main(String[] args) {
        System.out.println("main函数开始执行");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture future = CompletableFuture.supplyAsync(new Supplier() {
            @Override
            public Object get() {
                System.out.println("===task start===");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("===task finish===");
                return null;
            }
        }, executor);
        System.out.println("main函数执行结束");
    }


}
