package cn.touchmedia.sean.mongo;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;

public class AutoSign {
	@Scheduled(cron="0 0 6 * * ?")
	public void work() throws Exception {
		System.out.println("AutoSign do it's work");
		try {
			doWork();
		} catch ( Exception e ) {
			
		}
	}
	
	
	private void doWork() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        try {
        	HttpGet httpget = new HttpGet("http://www.zimuzu.tv/user/login");
        	CloseableHttpResponse response1 = httpclient.execute(httpget);
        	try {
        		HttpEntity entity = response1.getEntity();

                EntityUtils.consume(entity);

//                List<Cookie> cookies = cookieStore.getCookies();
//                if (cookies.isEmpty()) {
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                    }
//                }
        	} finally {
        		response1.close();
        	}
        	
        	HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("http://www.zimuzu.tv/User/Login/ajaxLogin"))
                    .addParameter("account", "shwhg")
                    .addParameter("password", "gjl55l")
                    .addParameter("remember", "1")
                    .addParameter("url_back", "http://www.zimuzu.tv/")
                    .addHeader("Origin", "http://www.zimuzu.tv")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("Referer", "http://www.zimuzu.tv/user/login")
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();

                EntityUtils.consume(entity);

//                List<Cookie> cookies = cookieStore.getCookies();
//                if (cookies.isEmpty()) {
//                    System.out.println("None");
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
//                }
            } finally {
                response2.close();
            }
            
            // access sign page
            HttpUriRequest access = RequestBuilder.get()
            		.setUri(new URI("http://www.zimuzu.tv/user/sign"))
            		.build();
            CloseableHttpResponse accessResponse = httpclient.execute(access);
            try {
            	HttpEntity entity = accessResponse.getEntity();
            	EntityUtils.consume(entity);
            } finally {
            	accessResponse.close();
            }
            
//            System.out.println("wait 15 seconds ...");
            try {
            	Thread.sleep(15000);
            } catch( InterruptedException e ) {
            	
            }
            
            // do sign
            HttpUriRequest sign = RequestBuilder.get()
            		.setUri(new URI("http://www.zimuzu.tv/user/sign/dosign"))
            		.addHeader("X-Requested-With", "XMLHttpRequest")
            		.addHeader("Referer", " http://www.zimuzu.tv/user/sign")
            		.build();
            CloseableHttpResponse response3 = httpclient.execute(sign);
            
            try {
            	HttpEntity entity = response3.getEntity();
            	
//            	EntityUtils.consume(entity);
            	String json = EntityUtils.toString(entity);
            	System.out.println("auto sign response: " + json);
//            	Gson gson = new Gson();
//            	SignResponse result = gson.fromJson(json, SignResponse.class);
//            	System.out.println("status: " + result.getStatus());
//            	System.out.println("info: " + result.getInfo());
//            	System.out.println("data: " + result.getData());
            } finally {
            	response3.close();
            }
        } finally {
        	httpclient.close();
        }

	}
}
