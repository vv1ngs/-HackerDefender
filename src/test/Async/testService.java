package Async;

/**
 * @author vvings
 * @version 2020/5/17 10:58
 */
public class testService {
    public void longtime() {
        System.out.println("我在执行一项耗时任务");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("完成");
    }
}
