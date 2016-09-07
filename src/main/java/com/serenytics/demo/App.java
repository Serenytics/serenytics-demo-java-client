package com.serenytics.demo;

import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



public class App
{
    public static void main(String[] args) throws JSONException, IOException {

        if (args.length < 2) {
            System.out.println("This program requires 2 arguments: apiKey and webAppUuid");
            System.exit(1);
        }

        String apiKey = args[0];
        String webAppUuid = args[1];

        String urlTemplate = "https://api.serenytics.com/api/web_app/%s/embed";

        HttpPost request = new HttpPost(String.format(urlTemplate, webAppUuid));
        request.setHeader("X-Api-Key", apiKey);
        request.setHeader("Content-type", "application/json");

        JSONObject content = new JSONObject();
        content.put("payload", (new JSONObject()).put("quarter", "Q2"));
        content.put("expire_in", 3600);

        StringEntity entity = new StringEntity(content.toString(), "UTF-8");
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        request.setEntity(entity);


        CloseableHttpClient httpclient = HttpClients.createDefault();

        CloseableHttpResponse response = httpclient.execute(request);
        System.out.println(response.getStatusLine());
        System.out.println(response.toString());

        JSONObject result = new JSONObject(EntityUtils.toString(response.getEntity()));
        String embeddedUrl = result.getString("embeddedUrl");
        System.out.println(String.format("embeddedUrl: %s", embeddedUrl));
    }
}
