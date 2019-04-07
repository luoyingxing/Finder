package com.lxx.finder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * <p/>
 * Created by luoyingxing on 2019/4/7.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText phoneET;
    private EditText passET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("登录");

        phoneET = findViewById(R.id.et_login_phone);
        passET = findViewById(R.id.et_login_password);

        findViewById(R.id.btn_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneET.getText().toString().trim();
                String pass = passET.getText().toString().trim();

                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "账号密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.equals("18847120943") && pass.equals("0943")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "账号或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_login_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}