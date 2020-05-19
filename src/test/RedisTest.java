import org.hackDefender.util.RedisPoolSharedUtil;
import org.junit.Test;

/**
 * @author vvings
 * @version 2020/5/19 21:45
 */
public class RedisTest {
    @Test
    public void test() {
        String a = RedisPoolSharedUtil.get("a2b7b6d8" + "_limit");
        System.out.println(a);
    }
}
