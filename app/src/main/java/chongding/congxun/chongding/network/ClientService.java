package chongding.congxun.chongding.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ClientService {

    //冲顶大会 请求验证码
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("user/requestSmsCode")
    Call<String> getCode(@Body RequestBody body);

    //冲顶大会绑定帐号信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("user/login ")
    Call<String> login(@Body RequestBody body);


    //冲顶大会填写推荐人
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("user/bindInviteCode")
    Call<String> bindInviteCode(@Header("X-Live-App-Version") String version,
                                @Header("X-Live-Device-Type") String type,
                                @Header("X-Live-Session-Token") String token,
                                @Body RequestBody body);

    //神话平台登录
    @GET("pubApi/uLogin")
    Call<String> uLogin(@Query("uName") String uName, @Query("pWord") String pWord,
                        @Query("Developer") String Developer); //神话平台登录

    //GET - GET方式调用实例：http://api.shjmpt.com:9002/uGetItems?token=token&tp=ut
    @GET("uGetItems")
    Call<String> uGetItems(@Query("token") String token, @Query("tp") String tp);

    @GET("pubApi/uExit")
    Call<String> uExit(@Query("token") String token);

    @GET("pubApi/GetPhone")
    Call<String> GetPhone(@Query("ItemId") String ItemId, @Query("token") String token);

    @GET("GMessage")
    Call<String> GMessage(@Query("token") String token, @Query("ItemId") String ItemId, @Query("Phone") String Phone);
}
