package com.lxx.finder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalDb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Created by luoyingxing on 2019/4/7.
 */
public class FindActivity extends AppCompatActivity {
    private Spinner spinner;

    private SpAdapter adapter;
    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("录入");

        spinner = findViewById(R.id.spinner);
        adapter = new SpAdapter(this, new ArrayList<Info>());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                info = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FinalDb mFinalDb = FinalDb.create(getApplicationContext());
        List<Info> list = mFinalDb.findAll(Info.class);

        adapter.clear();
        adapter.addAll(list);
    }

    private class SpAdapter extends BaseAdapter {
        private List<Info> list;
        private LayoutInflater mInflater;

        public SpAdapter(Context context, List<Info> l) {
            list = l;
            mInflater = LayoutInflater.from(context);
        }

        public void clear() {
            list.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Info> l) {
            list.addAll(l);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Info getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View view = mInflater.inflate(R.layout.item, null);

            TextView textView = view.findViewById(R.id.tv_item);

            Info info = list.get(position);

            textView.setText(info.getName());

            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void find() {
        if (null == info) {
            Toast.makeText(getApplicationContext(), "请选择一项物品！", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Socket socket = new Socket();
                    SocketAddress address = new InetSocketAddress(info.getIp(), Integer.parseInt(info.getPort()));
                    socket.connect(address, 5 * 1000);

                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();

                    os.write("F".getBytes());
                    os.flush();

                    byte[] bytes = new byte[10];
                    while (!finish) {
                        int len = is.read(bytes);

                        if (len > 0) {
                            final String data = new String(bytes, "UTF-8");
                            Log.d("FindActivity", "收到： " + data);

                            if (!TextUtils.isEmpty(data) && data.contains("OK")) {
                                Log.w("FindActivity", "收到： " + data);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "查找成功！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    is.close();
                    os.close();
                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }

    private boolean finish;

    @Override
    protected void onStop() {
        super.onStop();
        finish = true;
        back();
    }

    private void back() {
        finish();
    }

}
