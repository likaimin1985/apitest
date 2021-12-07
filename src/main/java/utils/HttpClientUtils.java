package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

//https://blog.csdn.net/loco_1/article/details/80649495
//手写http
public class HttpClientUtils {
	
	
	public static boolean openProxy=false;
	
	private static CloseableHttpClient httpClient;
	
	private static CloseableHttpClient init() {
		if(httpClient!=null) {
			return httpClient;
		}
		
		httpClient=SslUtil.SslHttpClientBuild(openProxy);
		return httpClient;
//		if(openProxy) {
//			 HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
//			 
//			 RequestConfig defaultRequestConfig = RequestConfig.custom()
//		                .setProxy(proxy)
//		                .build();
//			   //实例化CloseableHttpClient对象
//		         httpClient = HttpClients.custom().
//		        		setDefaultRequestConfig(defaultRequestConfig).build();
//		         return httpClient;
//		}else {
//			httpClient =HttpClients.createDefault();
//		    return httpClient;
//		}
		
	}

	//抓包
	public static void main(String[] args) {
		HttpClientUtils.openProxy=false;
		doGet("http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=test1&loginpass=test1", "token=61b3590090982a0185dda9d3bd793b46;userId=123");
		String result = doPost("http://118.24.13.38:8080/goods/UserServlet",
				"method=loginMobile&loginname=abc&loginpass=abc","zhangsan=test");
		System.out.println(result);

//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("method", "loginMobile");
//		params.put("loginname", "abc");
//		params.put("loginpass", "abc");
//		String result2 = doPost("http://118.24.13.38:8080/goods/UserServlet", params);
//		System.out.println(result2);
		
		//{"count":3}
//		String result3= doPostJson("http://118.24.13.38:8080/goods/json2", "{\"count\":3}","token=61b3590090982a0185dda9d3bd793b46;userId=123;userId=456");
//		System.out.println(result3);
		
		String result4= doPostJson("http://118.24.13.38:8080/goods/json2", "{\"count\":3}","token=61b3590090982a0185dda9d3bd793b46;userId=123;userId=456");
		System.out.println(result4);
		// System.out.println(doGet("http://www.baidu.com/"));
		// System.out.println(doGet("https://mvnrepository.com/artifact/org.apache.commons/commons-collections4/4.4"));
	}


	//删除
	public static String doDeleted(String url) {
		// 创建httpclient对象
		     CloseableHttpClient httpClient = init();
				// 创建post方式请求对象
				try {

					HttpDelete httpDelete = new HttpDelete(url);
					CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpDelete);
					HttpEntity httpEntity = closeableHttpResponse.getEntity();
					return EntityUtils.toString(httpEntity, "utf-8");
				} catch (Exception e) {
					// TODO: handle exception
				}
				return "error";
	}
	
	//put
	public static String doPut(String url) {
		// 创建httpclient对象
		 CloseableHttpClient httpClient = init();
				// 创建post方式请求对象
				try {
					HttpPut put = new HttpPut(url);
					CloseableHttpResponse closeableHttpResponse = httpClient.execute(put);
					HttpEntity httpEntity = closeableHttpResponse.getEntity();
					return EntityUtils.toString(httpEntity, "utf-8");
				} catch (Exception e) {
					// TODO: handle exception
				}
				return "error";
	}
	
	
	public static String doGet(String url) {
		return doGetMap(url,null);
	}
	
	public static String doGet(String url, String header) {
		return doGetMap(url, StringToMap.covert2(header));
	}
	
	
	//mac
	public static String doGetMap(String url, Map<String, Object> headerMap) {
        
	      CloseableHttpClient httpClient = init();
	        
		HttpGet get = new HttpGet(url);
		try {
			if(!MapUtils.isEmpty(headerMap)) {
				Set<String> keySet= headerMap.keySet();
				for (String key : keySet) {
					get.addHeader(key, String.valueOf(headerMap.get(key)));
				}
			}
			CloseableHttpResponse closeableHttpResponse = httpClient.execute(get);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error";
	}

	// c+v
	public static String doPostMap(String url, Map<String, Object> params, Map<String, Object> headerMap) {
		   //实例化CloseableHttpClient对象
		 CloseableHttpClient httpClient = init();
		 
		// 创建post方式请求对象
		HttpEntity repEntity = null;
		try {

			HttpPost post = new HttpPost(url);
			if(!MapUtils.isEmpty(headerMap)) {
				params.forEach((key,value)->post.addHeader(key, String.valueOf(value)));
				
//				Set<String> keySet= headerMap.keySet();
//				for (String key : keySet) {
//					post.addHeader(key, String.valueOf(headerMap.get(key)));
//				}
			}
			if (!MapUtils.isEmpty(params)) {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				params.forEach((key,value)->parameters.add(new BasicNameValuePair(key, String.valueOf(value))));
//				Set<String> keySet = params.keySet();
//				for (String key : keySet) {
//					parameters.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
//				}
				HttpEntity postEntity = new UrlEncodedFormEntity(parameters, "utf-8");
				post.setEntity(postEntity);
			}
			CloseableHttpResponse closeableHttpResponse = httpClient.execute(post);
			if(closeableHttpResponse.getStatusLine().getStatusCode()==200) {
				HttpEntity httpEntity = closeableHttpResponse.getEntity();
				return EntityUtils.toString(httpEntity, "utf-8");
			}
		}  catch (Exception e) {
			e.printStackTrace();
			   //log
			}finally {
				try {
					EntityUtils.consume(repEntity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return "error";
	}

	// method=loginMobile&loginname=test1&loginpass=test1
	// method=loginMobile&loginname=abc&loginpass=abc
	public static String doPost(String url, String params) {
		return doPostMap(url,StringToMap.covert1(params),null);
	}
	
	
	public static String doPost(String url, String params,String header) {
		return doPostMap(url,StringToMap.covert1(params),StringToMap.covert1(header));
	}
	
	
	public static String doPostJson(String url, String json, String headers) {
		//第一步：创建HttpClient对象
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		
		if(StringUtils.isNotBlank(json)&&!JSON.isValid(json)) {
			return "不是Json";
		}
		 CloseableHttpClient httpClient = init();
	        
		try {
			//第二步：创建httpPost对象
			HttpPost post = new HttpPost(url);
			if(StringUtils.isNotBlank(json)) {
				 //第三步：给httpPost设置JSON格式的参数
				   StringEntity requestEntity = new StringEntity(json,"utf-8");
			        requestEntity.setContentEncoding("UTF-8");    	        
			        post.setHeader("Content-type", "application/json");
			        post.setEntity(requestEntity);

			}
			//token=61b3590090982a0185dda9d3bd793b46;userId=123
			if(StringUtils.isNotBlank(headers)) {
				String[] header_array = headers.split(";");
				for (int i = 0; i < header_array.length; i++) {
					String header = header_array[i];
					String[] paStrings = header.split("=");
					//parameters.add(new BasicNameValuePair(paStrings[0], paStrings[1]));
					post.setHeader(new BasicHeader(paStrings[0],paStrings[1]));
					//post.addHeader(paStrings[0], paStrings[1]);
				}
			}
			//第四步 处理返回结果
			CloseableHttpResponse closeableHttpResponse = httpClient.execute(post);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "utf-8");
		} catch (Exception e) {
			//log
		}
		return "error";
	}
	
	
}
