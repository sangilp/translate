package com.example.ducksang.translation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

public class FirstActivity extends Activity {
    private long lastBackPressed;   // 마지막으로 뒤로가기 버튼이 터치된 시간
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_main);

//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setImageResource(R.drawable.start);

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
    @Override

    public void onBackPressed(){
        // 1.5초 이내에 뒤로가기 버튼을 두번 눌리면 앱 종료
        if (System.currentTimeMillis() - lastBackPressed < 1500){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        Toast.makeText(this, "'Back' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        lastBackPressed = System.currentTimeMillis();   // 뒤로가기 클릭 시 현재시간 기록

    }
}
