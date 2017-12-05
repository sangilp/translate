package com.example.ducksang.translation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText bfText = (EditText) findViewById(R.id.before_text);
        TextView afText = (TextView) findViewById(R.id.before_text);
        Button transButton = (Button) findViewById(R.id.trans_button);

        // 번역실행 클릭했을때 발생할 이벤트 구현
        transButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 번역 할 단어가 입력되었는지 확인
                if(bfText.getText().toString().length()==0){
                    Toast.makeText(MainActivity.this, "번역할 단어를 입력하세요.", Toast.LENGTH_SHORT).show();
                    bfText.requestFocus();
                    return;

                }
            }
        });



    }
}
