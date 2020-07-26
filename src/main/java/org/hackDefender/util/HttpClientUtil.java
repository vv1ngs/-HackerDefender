package org.hackDefender.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author vvings
 * @version 2020/4/17 11:04
 */
@Slf4j
public class HttpClientUtil {
    public static String getRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(get);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void putRequest(String url, String str) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut put = new HttpPut(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(100000).build();
            put.setEntity(new StringEntity(str, ContentType.DEFAULT_TEXT));
            put.setConfig(requestConfig);
            HttpResponse httpResponse = httpClient.execute(put);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code != 200) {
                log.error("修改frp失败" + url + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
