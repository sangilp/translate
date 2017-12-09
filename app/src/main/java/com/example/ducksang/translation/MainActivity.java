package com.example.ducksang.translation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText bfText;
    TextView afText;
    Button transButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bfText = (EditText) findViewById(R.id.before_text);
        afText = (TextView) findViewById(R.id.after_text);
        transButton = (Button) findViewById(R.id.trans_button);

        // 번역실행 클릭했을때 발생할 이벤트 구현
        transButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 번역 할 내용이 입력되었는지 확인
                if(bfText.getText().toString().length()==0){
                    Toast.makeText(MainActivity.this, "번역할 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    bfText.requestFocus();
                    return;
                }

                // 번역 할 내용이 입력 되었다면 Naver Open API 연동
                NaverTranslateTask nTrans = new NaverTranslateTask();
                String sText = bfText.getText().toString();
                nTrans.execute(sText);
            }
        });

     }

    public class NaverTranslateTask extends AsyncTask<String, Void, String> {

        public String resultText;
        // Naver Open API client 연동
        String clientId = "xUmNqbKm8ykD9sC4P_b0";
        String clientSecret = "h1coWEbBMP";

        String srcLang = "en";
        String tarLang = "ko";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 네이버 Papago NMT API 예제
        @Override
        protected String doInBackground(String... strings) {

            String sourceText = strings[0];
            try {
                String text = URLEncoder.encode(sourceText, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-Naver-Client-Id", clientId);
                conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                String post = "source=" + srcLang + "&target=" + tarLang + "&text=" + text;
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(post);
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else { // 에러 발생
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                return response.toString();

            } catch (Exception e) {
                return null;
            }
        }
        // 번역된 결과를 받아서 처리
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Naver Open Api는 출력 포맷이 Json 이기때문에 자바객체로 변환하기 위해 Gson 사용
            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(s.toString())
                    // 원하는 데이터를 찾기
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("result");
            Item items = gson.fromJson(rootObj.toString(), Item.class);

            // 번역결과를 텍스트뷰에 넣기
            afText.setText(items.getTranslatedText());
        }

        // 자바 객체로 변환한 값을 담을 클래스
        private class Item {
            String translatedText;

            public String getTranslatedText() {
                return translatedText;
            }
        }
    }
}
