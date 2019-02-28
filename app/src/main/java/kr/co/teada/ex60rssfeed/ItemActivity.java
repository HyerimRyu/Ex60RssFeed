package kr.co.teada.ex60rssfeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ItemActivity extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent=getIntent();
        String link=intent.getStringExtra("Link");

        //받은 link 주소를 WebView 에 보여주기!!
        wv=findViewById(R.id.wv);

        //이 설정 안하면 폰에 있는 브라우저 앱 자동 실행
        wv.setWebViewClient(new WebViewClient());

        //이 설정 안하면 웹페이지의 기능 중에서 다이얼로그 같은 기능 동작 불가
        wv.setWebChromeClient(new WebChromeClient());

        //웹사이트의 자바스크립트 기능이 동작되게 하려면
        wv.getSettings().setJavaScriptEnabled(true);

        wv.loadUrl(link);

    }
}
