package chongding.congxun.chongding.util;

import android.text.TextUtils;

import chongding.congxun.chongding.network.ClientService;
import chongding.congxun.chongding.network.LinkCTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShenHuaUtils {


    public static void login(String name, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {//给出提示

            RxBus.get().post(new UserEvent(-1, "帐号或者密码不能为空"));
        } else {//进行登录
            ClientService mservice = LinkCTService.createShenHuaService(ClientService.class);
            Call<String> call = mservice.uLogin(name, password, Config.ShenHuatTag);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String s = response.body().toString();
                    String Schar[] = s.split("&");
                    if (Schar.length > 0) {
                        RxBus.get().post(new UserEvent(1, "获取到token：" + Schar[0]));
                        Config.TOKEN=Schar[0];
                        uGetItems(Config.TOKEN,"");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    RxBus.get().post(new UserEvent(-1, "神话接口异常1"));
                }
            });
        }
    }

    //获取项目列表
    public static void uGetItems(String token, String type) {
        ClientService mservice = LinkCTService.createShenHuaService(ClientService.class);
        Call<String> call = mservice.uGetItems(token, "ut");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String s = response.body().toString();
//                String Schar[]=s.split("&");

                RxBus.get().post(new UserEvent(1, "获取到项目：" + s));

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                RxBus.get().post(new UserEvent(-1, "神话接口异常2"));
            }
        });
    }


    public static void uExit(String token) {
        ClientService mservice = LinkCTService.createShenHuaService(ClientService.class);
        Call<String> call = mservice.uExit(token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String s = response.body().toString();

                RxBus.get().post(new UserEvent(1, "注销返回：" + s));

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                RxBus.get().post(new UserEvent(-1, "神话接口异常9"));
            }
        });
    }

    public static void GetPhone(String ItemId,String token) {
        ClientService mservice = LinkCTService.createShenHuaService(ClientService.class);
        Call<String> call = mservice.GetPhone(ItemId,token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String s = response.body().toString();

                RxBus.get().post(new UserEvent(1, "获取号码返回：" + s));

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                RxBus.get().post(new UserEvent(-1, "神话接口异常9"));
            }
        });
    }

}