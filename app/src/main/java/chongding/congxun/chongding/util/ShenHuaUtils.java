package chongding.congxun.chongding.util;

import android.os.Handler;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import chongding.congxun.chongding.network.ClientService;
import chongding.congxun.chongding.network.LinkCTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShenHuaUtils {

    private static HashMap<String, Data> map=new HashMap<>();
    private static int Count=0;

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

                String phone = response.body().toString();

                RxBus.get().post(new UserEvent(1, "获取号码返回：" + phone));
                map.put(phone,new Data());
                Count=0;
                startTimer(phone);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                RxBus.get().post(new UserEvent(-1, "神话接口异常9"));
            }
        });
    }
    static Handler  handler = new Handler();
    private static void startTimer(final String phone) {


        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                // handler自带方法实现定时器
                try {
                    if(map.get(phone)!=null){
                        Data date=map.get(phone);
                        if(date.getFlag()){//已经正确返回

                        }else{
                            GMessage(Config.TOKEN,Config.CDDH,phone);
                            handler.postDelayed(this, 3000);
                        }
                    }else {
                        GMessage(Config.TOKEN,Config.CDDH,phone);
                        handler.postDelayed(this, 3000);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println("exception...");
                }
            }
        };
        handler.postDelayed(runnable, 100); //每隔1s执行
    }

    /**
     * 检查是否返回验证码
     * @param token
     * @param ItemId
     * @param phone
     */
    public static void GMessage(String token,String ItemId,String phone) {
        ClientService mservice = LinkCTService.createShenHuaService(ClientService.class);
        Call<String> call = mservice.GMessage(ItemId,token,phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String s = response.body().toString();
                String Schar[] = s.split("&");
                Count++;
                RxBus.get().post(new UserEvent(1, "验证码返回：" + s));
                if(Count>=3){
                    Data data=new Data();
                    data.setFlag(true);
                    map.put(Schar[2],data);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                RxBus.get().post(new UserEvent(-1, "神话接口异常9"));
            }
        });
    }

}