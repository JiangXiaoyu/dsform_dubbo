package com.dotoyo.ims.dsform.allin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;

import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.dotoyo.dsform.log.LogProxy;


public class FrameHttp extends AbstractFrame implements IFrameHttp {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameHttp.class));

	protected static FrameHttp instance = null;
	private CloseableHttpClient httpclient = null;

	protected FrameHttp() {
		// Use custom message parser / writer to customize the way HTTP
		// messages are parsed from and written out to the data stream.
		HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

			@Override
			public HttpMessageParser<HttpResponse> create(
					SessionInputBuffer buffer, MessageConstraints constraints) {
				LineParser lineParser = new BasicLineParser() {

					@Override
					public Header parseHeader(final CharArrayBuffer buffer) {
						try {
							return super.parseHeader(buffer);
						} catch (ParseException ex) {
							return new BasicHeader(buffer.toString(), null);
						}
					}

				};
				return new DefaultHttpResponseParser(buffer, lineParser,
						DefaultHttpResponseFactory.INSTANCE, constraints) {

					@Override
					protected boolean reject(final CharArrayBuffer line,
							int count) {
						// try to ignore all garbage preceding a status line
						// infinitely
						return false;
					}

				};
			}

		};
		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

		// Use a custom connection factory to customize the process of
		// initialization of outgoing HTTP connections. Beside standard
		// connection
		// configuration parameters HTTP connection factory can define message
		// parser / writer routines to be employed by individual connections.
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
				requestWriterFactory, responseParserFactory);

		// Client HTTP connection objects when fully initialized can be bound to
		// an arbitrary network socket. The process of network socket
		// initialization,
		// its connection to a remote address and binding to a local one is
		// controlled
		// by a connection socket factory.

		// SSL context for secure connections can be created either based on
		// system or application specific properties.
		SSLContext sslcontext = SSLContexts.createSystemDefault();
		// Use custom hostname verifier to customize SSL hostname verification.
		X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();

		// Create a registry of custom connection socket factories for supported
		// protocol schemes.
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create().register("http",
						PlainConnectionSocketFactory.INSTANCE).register(
						"https",
						new SSLConnectionSocketFactory(sslcontext,
								hostnameVerifier)).build();

		// Use custom DNS resolver to override the system DNS resolution.
		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

			@Override
			public InetAddress[] resolve(final String host)
					throws UnknownHostException {
				if (host.equalsIgnoreCase("myhost")) {
					return new InetAddress[] { InetAddress
							.getByAddress(new byte[] { 127, 0, 0, 1 }) };
				} else {
					return super.resolve(host);
				}
			}

		};

		// Create a connection manager with custom configuration.
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry, connFactory, dnsResolver);

		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
				.build();
		// Configure the connection manager to use socket configuration either
		// by default or for a specific host.
		connManager.setDefaultSocketConfig(socketConfig);

		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom()
				.setMaxHeaderCount(200).setMaxLineLength(2000).build();
		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(
						Consts.UTF_8).setMessageConstraints(messageConstraints)
				.build();
		// Configure the connection manager to use connection configuration
		// either
		// by default or for a specific host.
		connManager.setDefaultConnectionConfig(connectionConfig);

		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(50);

		// Use custom cookie store if necessary.
		CookieStore cookieStore = new BasicCookieStore();
		// Use custom credentials provider if necessary.
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// Create global request configuration
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(false)
				.setStaleConnectionCheckEnabled(true)
				.setTargetPreferredAuthSchemes(
						Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.setSocketTimeout(2 * 60 * 1000).setConnectTimeout(
						1 * 60 * 1000).setConnectionRequestTimeout(30 * 1000)

				.build();

		// Create an HttpClient with the given custom dependencies and
		// configuration.
		httpclient = HttpClients.custom().setConnectionManager(connManager)
				.setDefaultCookieStore(cookieStore)
				.setDefaultCredentialsProvider(credentialsProvider)

				.setDefaultRequestConfig(defaultRequestConfig).build();
	}

	public static FrameHttp getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameHttp();
		}
	}

	public void startup() {

	}

	public void shutdown() {
		try {
			httpclient.close();
		} catch (Throwable e) {
			log.error("", e);
		}
		instance = null;
	}

	public String postJson(HttpBo bo, Map<String, String> head, String json)
			throws Exception {
		String url = bo.getUrl();
		HttpPost httpPost = new HttpPost(url);
		StringEntity se = new StringEntity(URLEncoder.encode(json, "utf-8"));

		se.setContentEncoding("UTF-8");
		se.setContentType("application/json;charset=UTF-8");
		httpPost.setEntity(se);

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			// 若状态值为200，处理成功报文
			if (code == HttpStatus.SC_OK) {

				HttpEntity entity2 = response.getEntity();

				String ret = EntityUtils.toString(entity2, "utf-8");
				EntityUtils.consume(entity2);
				log.debug(ret);
				return ret;
			} else {
				// 3000000000000010=HTTP请求失败:${code}
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", code + "");
				FrameException e = new FrameException("3000000000000010", map);
				throw e;
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}

	}
	public String postXml(HttpBo bo, Map<String, List<String>> headList, String xml)
	throws Exception{
		String url = bo.getUrl();
		HttpPost httpPost = new HttpPost(url);
		StringEntity se = new StringEntity(xml, "utf-8");
		se.setContentEncoding("UTF-8");
		se.setContentType("application/xml;charset=UTF-8");
		postJson4Head(httpPost, headList);
		httpPost.setEntity(se);

		CloseableHttpResponse response = null;
		try {
			
			response = httpclient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			// 若状态值为200，处理成功报文
			if (code == HttpStatus.SC_OK) {

				HttpEntity entity2 = response.getEntity();

				String ret = EntityUtils.toString(entity2, "utf-8");
				EntityUtils.consume(entity2);
				log.debug(ret);
				return ret;
			} else {
				// 3000000000000010=HTTP请求失败:${code}
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", code + "");
				FrameException e = new FrameException("3000000000000010", map);
				throw e;
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
	public String getData(HttpBo bo) throws Exception {
		String url = bo.getUrl();
		HttpGet httpGet = new HttpGet(url);

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			// 若状态值为200，处理成功报文
			if (code == HttpStatus.SC_OK) {

				HttpEntity entity2 = response.getEntity();

				String ret = EntityUtils.toString(entity2, "utf-8");
				EntityUtils.consume(entity2);
				log.debug(ret);
				return ret;
			} else {
				// 3000000000000010=HTTP请求失败:${code}
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", code + "");
				FrameException e = new FrameException("3000000000000010", map);
				throw e;
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	public String postInputStream(HttpBo bo, Cookie[] cs,
			Map<String, List<String>> map, InputStream is) throws Exception {
		try {
			String url = bo.getUrl();
			HttpPost httpPost = new HttpPost(url);

			postJson4Head(httpPost, map);
			InputStreamEntity se = new InputStreamEntity(is, -1,
					ContentType.APPLICATION_JSON);
			httpPost.setEntity(se);
			HttpClientContext context = HttpClientContext.create();
			BasicCookieStore cookieStore = new BasicCookieStore();
			cookieStore.addCookies(postJson4Cookie(cs));
			context.setCookieStore(cookieStore);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httpPost, context);

				int code = response.getStatusLine().getStatusCode();
				// 若状态值为200，处理成功报文
				if (code == HttpStatus.SC_OK) {

					HttpEntity entity2 = response.getEntity();

					String ret = EntityUtils.toString(entity2, "utf-8");
					EntityUtils.consume(entity2);
					log.debug(ret);
					return ret;
				} else {
					// 3000000000000010=HTTP请求失败:${code}
					Map<String, String> param = new HashMap<String, String>();
					param.put("status", code + "");
					FrameException e = new FrameException("3000000000000010",
							param);
					throw e;
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {

				}
			}
		}
	}

	private void postJson4Head(HttpPost httpPost, Map<String, List<String>> map)
			throws Exception {
		for (String name : map.keySet()) {
			List<String> list = map.get(name);
			if (list == null) {
				continue;
			}
			for (String value : list) {
				httpPost.addHeader(name, value);
			}
		}
	}

	private org.apache.http.cookie.Cookie[] postJson4Cookie(Cookie[] cs)
			throws Exception {
		if (cs == null) {
			return new org.apache.http.cookie.Cookie[0];
		}
		org.apache.http.cookie.Cookie list[] = new org.apache.http.cookie.Cookie[cs.length];
		for (int i = 0; i < cs.length; i++) {
			Cookie c = cs[i];
			BasicClientCookie e = new BasicClientCookie(c.getName(), c
					.getValue());
			e.setComment(c.getComment());
			e.setDomain(c.getDomain());
			if (c.getMaxAge() == -1) {
				e.setExpiryDate(null);
			} else {
				e.setExpiryDate(new java.sql.Date(System.currentTimeMillis()
						+ c.getMaxAge() * 1000));
			}

			e.setPath(c.getPath());
			e.setSecure(c.getSecure());
			e.setVersion(c.getVersion());

			list[i] = e;
		}
		return list;
	}

	public String postParam(HttpBo bo, Cookie[] cs,
			Map<String, List<String>> headMap, Map<String, String[]> paramMap)
			throws Exception {
		try {
			String url = bo.getUrl();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> parameters = postParam4getParam(paramMap);
			UrlEncodedFormEntity se = new UrlEncodedFormEntity(parameters,
					"UTF-8");
			httpPost.setEntity(se);
			postJson4Head(httpPost, headMap);
			HttpClientContext context = HttpClientContext.create();
			BasicCookieStore cookieStore = new BasicCookieStore();
			cookieStore.addCookies(postJson4Cookie(cs));
			context.setCookieStore(cookieStore);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httpPost, context);
				int code = response.getStatusLine().getStatusCode();
				// 若状态值为200，处理成功报文
				if (code == HttpStatus.SC_OK) {

					HttpEntity entity2 = response.getEntity();

					String ret = EntityUtils.toString(entity2, "utf-8");
					EntityUtils.consume(entity2);
					log.debug(ret);
					return ret;
				} else {
					// 3000000000000010=HTTP请求失败:${code}
					Map<String, String> param = new HashMap<String, String>();
					param.put("status", code + "");
					FrameException e = new FrameException("3000000000000010",
							param);
					throw e;
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} finally {

		}
	}

	private List<NameValuePair> postParam4getParam(Map<String, String[]> map)
			throws Exception {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (String name : map.keySet()) {
			String[] list = map.get(name);
			if (list == null) {
				continue;
			}
			for (String value : list) {
				BasicNameValuePair pair = new BasicNameValuePair(name, value);
				parameters.add(pair);
			}
		}
		return parameters;
	}

	public String postJson(HttpBo bo, Cookie cs[],
			Map<String, List<String>> headList, byte bytes[]) throws Exception {
		ByteArrayInputStream is = null;
		try {
			String url = bo.getUrl();
			HttpPost httpPost = new HttpPost(url);

			postJson4Head(httpPost, headList);
			is = new ByteArrayInputStream(bytes);
			InputStreamEntity se = new InputStreamEntity(is, -1,
					ContentType.APPLICATION_JSON);

			httpPost.setEntity(se);
			HttpClientContext context = HttpClientContext.create();
			BasicCookieStore cookieStore = new BasicCookieStore();
			cookieStore.addCookies(postJson4Cookie(cs));
			context.setCookieStore(cookieStore);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httpPost, context);

				int code = response.getStatusLine().getStatusCode();
				// 若状态值为200，处理成功报文
				if (code == HttpStatus.SC_OK) {

					HttpEntity entity2 = response.getEntity();

					String ret = EntityUtils.toString(entity2, "utf-8");
					EntityUtils.consume(entity2);
					log.debug(ret);
					return ret;
				} else {
					// 3000000000000010=HTTP请求失败:${code}
					Map<String, String> param = new HashMap<String, String>();
					param.put("status", code + "");
					FrameException e = new FrameException("3000000000000010",
							param);
					throw e;
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {

				}
			}
		}
	}

	public static void main1(String[] args) throws Exception {
		IFrameHttp http = FrameFactory.getHttpFactory(null);
		HttpBo bo = new HttpBo();
		String url = "http://192.168.0.138:7777/v10/user_sso_login";
		bo.setUrl(url);
		Map<String, String> head = new HashMap<String, String>();
		String json = "{sessionId:'7A53D1843911F5C0A8478D31B15AA76E'}";
		json = http.postJson(bo, head, json);

		log.debug(json);
	}

	public static void main(String[] args) throws Exception {
		String url = "";
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}
}
