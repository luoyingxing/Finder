package com.lxx.finder;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.tsz.afinal.FinalDb;

/**
 * <p/>
 * Created by luoyingxing on 2019/4/7.
 */
public class SaveActivity extends AppCompatActivity {
    private EditText nameET;
    private EditText ipET;
    private EditText portET;

    private FinalDb mFinalDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("录入");

        mFinalDb = FinalDb.create(getApplicationContext());

        nameET = findViewById(R.id.et_name);
        ipET = findViewById(R.id.et_ip);
        portET = findViewById(R.id.et_port);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString().trim();
                String ip = ipET.getText().toString().trim();
                String port = portET.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
                    Toast.makeText(getApplicationContext(), "信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                save(name, ip, port);
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void save(String name, String ip, String port) {
        mFinalDb.save(new Info(name, ip, port));

        nameET.setText("");
        Toast.makeText(getApplicationContext(), "录入成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
