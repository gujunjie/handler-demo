package com.example.abc.myapplication49;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_showMessage)
    TextView tvShowMessage;
    private MyHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_sendMessage)
    public void onViewClicked() {

        handler = new MyHandler(MainActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//模拟子线程执行耗时操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message =new Message();
                message.what = 1;
                message.arg1=2;
                message.arg2=3;
                message.obj=new StringBuffer("i love google");
                handler.sendMessage(message);

            }
        }).start();//注意调用start方法启动线程


    }


    private  static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;//定义一个activity的弱引用对象

        public MyHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

 //处理消息更新UI
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    MainActivity activity=weakReference.get();
                    activity.tvShowMessage.setText(msg.obj.toString()+msg.arg2+msg.arg1);
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        //activity销毁了，handler没必要再处理消息，把所有回调以及消息队列清空
    }
}
