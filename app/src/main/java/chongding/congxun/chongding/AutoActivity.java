package chongding.congxun.chongding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;

import org.w3c.dom.Text;

import chongding.congxun.chongding.util.Config;
import chongding.congxun.chongding.util.RxBus;
import chongding.congxun.chongding.util.ShenHuaUtils;
import chongding.congxun.chongding.util.UserEvent;
import rx.Subscription;
import rx.functions.Action1;

public class AutoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText yaoqingma;
    private EditText count;
    private EditText username;
    private EditText password;
    private TextView content;
    private Button bn_start;
    private Button bn_login;
    private Button bn_exit;
    private TextView tv_token;
    private Button bn_resut;
    private ScrollView scrollView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        initView();
    }

    private void initView() {
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        yaoqingma = (EditText) findViewById(R.id.yaoqingma);
        count = (EditText) findViewById(R.id.count);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username.setText("android_alex");
        password.setText("wang123456");
        bn_start = (Button) findViewById(R.id.bn_start);
        bn_resut = (Button) findViewById(R.id.bn_resut);
        bn_login = (Button) findViewById(R.id.bn_login);
        bn_exit = (Button) findViewById(R.id.bn_exit);
        bn_start.setOnClickListener(this);
        bn_resut.setOnClickListener(this);
        bn_login.setOnClickListener(this);
        bn_exit.setOnClickListener(this);

        content = (TextView) findViewById(R.id.content);
        tv_token = (TextView) findViewById(R.id.tv_token);

        RxBus.get().register(this);

    }

    @Subscribe
    public void print(UserEvent user) {
        log(user.getId(),user.getName());
    }
    private void log(int level, String s) {
        switch (level){
            case 1://神话平台登录成功

                tv_token.append(s);
                break;
            default:
                break;
        }
        content.append("\n"+s);
        scrollView1.post(new Runnable() {
            public void run() {
                scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        Log.e("MainActivity", "=====" + s);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bn_start:
                ShenHuaUtils.GetPhone(Config.CDDH,Config.TOKEN);
                break;
            case R.id.bn_login:
                ShenHuaUtils.login(username.getText().toString().trim(),
                        password.getText().toString().trim()
                );
                break;
            case R.id.bn_exit:
                ShenHuaUtils.uExit(Config.TOKEN);
                break;
            case R.id.bn_resut:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
