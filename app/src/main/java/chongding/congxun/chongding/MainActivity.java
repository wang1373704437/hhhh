package chongding.congxun.chongding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import chongding.congxun.chongding.network.ClientService;
import chongding.congxun.chongding.network.LinkCTService;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_test1;
    private TextView content;
    int count = 1;
    private ScrollView scrollView;
    private EditText phone;
    private Button bt_test2;
    public String phoneString;
    private EditText code;
    private String codeString;
    private String sessionToken;
    private String yaoqingmaString;
    private EditText yaoqingma;
    private boolean register;
    private Button bt_test3
            ;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        yaoqingma = (EditText) findViewById(R.id.yaoqingma);
        yaoqingma.setText("2922320");
        bt_test1 = (Button) findViewById(R.id.bt_test1);
        bt_test2 = (Button) findViewById(R.id.bt_test2);
        bt_test3 = (Button) findViewById(R.id.bt_test3);
        button = (Button) findViewById(R.id.button);
        content = (TextView) findViewById(R.id.content);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        bt_test2.setOnClickListener(this);
        bt_test3.setOnClickListener(this);
        button.setOnClickListener(this);
        bt_test1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(phone.getText().toString().trim())) {
                    phoneString = phone.getText().toString().trim();
                    try {
                        huoqucode(phone.getText().toString().trim());
                    } catch (JSONException e) {

                    }
                }
            }


        });

    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void huoqucode(String phone) throws JSONException {

        ClientService mService = LinkCTService
                .createRetrofitService(ClientService.class);

        JSONObject obj = new JSONObject();
        obj.put("phone", phone);
        String mJson = obj.toString();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mJson);
        Call<String> call = mService.getCode(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                log(response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log("获取验证码失败");
            }
        });

    }

    private void log(String s) {

        content.append(
                "\n第" + count + "行:"+s);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        Log.e("MainActivity", "=====" + s);

        count++;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_test2://绑定帐号
                codeString = code.getText().toString().trim();

                if (TextUtils.isEmpty(codeString)) {
                    log("绑定验证要先输入验证码");
                } else {
                    try {
                        bindAccont2(phoneString, codeString);
                    } catch (JSONException e) {

                    }
                }
                break;
            case R.id.bt_test3://加心
                yaoqingmaString =yaoqingma.getText().toString().trim();
                if(TextUtils.isEmpty(yaoqingmaString)){
                    log("请先添加邀请码");
                    break;
                }
                if(register){
                    log("此手机（"+phoneString+"）已注册===-》拉黑：sessionToken"+sessionToken);
                    bindInviteCode(sessionToken,yaoqingmaString);
                }else{
                    log("新用户：sessionToken："+sessionToken);

                    //填写推荐人
                    bindInviteCode(sessionToken,yaoqingmaString);
                }
                break;
            case R.id.button://进入自动加心程序

                startActivity(new Intent(MainActivity.this,AutoActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机验证码是否正确
     *
     * @param phoneString
     * @param codeString
     */
    private void bindAccont2(final String phoneString, String codeString) throws JSONException {

        ClientService mService = LinkCTService
                .createRetrofitService(ClientService.class);

        final JSONObject obj = new JSONObject();
        obj.put("phone", phoneString);
        obj.put("code", codeString);

        String mJson = obj.toString();
        log("准备验证绑定"+mJson);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mJson);
        Call<String> call = mService.login(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                log(response.body().toString());
                try {
                    JSONObject objAll=new JSONObject(response.body().toString());

                    JSONObject dataobj=objAll.getJSONObject("data");

                    JSONObject userObj=dataobj.getJSONObject("user");

                    sessionToken=userObj.getString("sessionToken");

                    register = dataobj.getBoolean("register");



                } catch (JSONException e) {
                    log("2解析json异常"+e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log("绑定帐号失败");
            }
        });
    }

    //提交推荐人信息
    private void bindInviteCode(String sessionToken, String guanjianma) {

        ClientService mService = LinkCTService
                .createRetrofitService(ClientService.class);

        final JSONObject obj = new JSONObject();
        try {
            obj.put("inviteCode", guanjianma);
        } catch (JSONException e) {

        }
        String mJson = obj.toString();
        log("准备开始加心："+mJson);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mJson);
        Call<String> call = mService.bindInviteCode("1.0.5","android",sessionToken,body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                log(response.body().toString());
                try {
                    JSONObject objAll=new JSONObject(response.body().toString());

                    int code=objAll.getInt("code");
                    if (code==0)
                        log("加心成功");
                    else if(code==28)
                        log("加心失败==->手机已经注册过");
                } catch (JSONException e) {
                    log("3解析json异常");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log("3接口请求失败");
            }
        });
    }
}
