package docker;

import org.apache.commons.lang.time.DateUtils;
import org.hackDefender.util.DateTimeUtil;
import org.hackDefender.util.PropertiesUtil;

import java.util.Date;

/**
 * @author vvings
 * @version 2020/4/19 20:08
 */
public class timetest {
    public static void main(String[] args) {
        Date closeDate = DateUtils.addSeconds(new Date(), -Integer.parseInt(PropertiesUtil.getProperty("container_lasttime", "3600")));
        System.out.println(DateTimeUtil.DateToString(closeDate));
    }
}
