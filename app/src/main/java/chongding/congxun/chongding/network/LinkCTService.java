package chongding.congxun.chongding.network;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import chongding.congxun.chongding.util.Config;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class LinkCTService {
	private LinkCTService() {
	}

	public static <T> T createRetrofitService(final Class<T> service) {

		Retrofit retrofit = new Retrofit.Builder()
				// .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.baseUrl(Config.Http + "/").build();
		return retrofit.create(service);

	}


	public static <T> T createShenHuaService(final Class<T> service) {

		Retrofit retrofit = new Retrofit.Builder()
				// .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.baseUrl(Config.ShenHuaHTTP + "/").build();
		return retrofit.create(service);

	}
}
