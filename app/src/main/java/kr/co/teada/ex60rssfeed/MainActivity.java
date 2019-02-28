package kr.co.teada.ex60rssfeed;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //1. 대량의 데이터들--> Item.java
    ArrayList<Item> items=new ArrayList<>();

    //거의 마지막! 연결
    RecyclerView recyclerView;
    MyAdapter adapter;

    //누르면 로딩되는 동안 뱅글뱅글 도는거
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2. 네트워크를 통해서 데이터들 읽어오기
        readRss();

        recyclerView= findViewById(R.id.recycler);
        adapter=new MyAdapter(items, this);
        recyclerView.setAdapter(adapter);

        //refresh layout 갱신하기  / 리스너 달 때 모르겠으면 그냥 listener 적어봐
        refreshLayout=findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            //스크롤을 맨 위로 당겨서 .. 뱅글뱅글 도는 아이콘 나올 때 자동 호출
            @Override
            public void onRefresh() {
                items.clear(); // 기차 싹 다 지워
                readRss();
            }
        });
    }

    //2-1. 데이터 읽어오는 작업 메소드
    void readRss(){
        //2-2. 네트워크 작업은 퍼미션 필요 --> manifest// use internet
        try {
            //URL url=new URL("http://rss.hankyung.com/new/news_main.xml");  //여기 인터넷 주소
            URL url=new URL("https://rss.blog.naver.com/landscapener.xml");

            //2-3. 네트워크 작업은 오래 걸리는 작업
            // --> 네트워크 작업은 별도의 Thread 해야해!!!!
            RssFeedTask task=new RssFeedTask();

            //2-4. doInBackground() 를 실행하는 명령
            task.execute(url);   //Thread 의 start() 같은 역할



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }// end of readRss()

    //2-3-1. RSS XML 문서를 네트워크로 읽어오는 작업 스레드의 이너클래스 설계 <> 주의
    class RssFeedTask extends AsyncTask<URL, Void, String>{

        //2-3-2. 빨간램프)implements 이 메소드가 Thread 의 run() 메소드 같은 역할
        //이 메소드 안에서만 네트워크 작업 해야해
        //이 메소드 안에서는 UI 변경작업 불가
        @Override
        protected String doInBackground(URL... urls) {  //...은 개수 제한이 없는 배열

            //2-4-1. 전달받은 URL 객체 참조
            URL url=urls[0];

            //2-4-2. 무지개로드(Stream)
            try {
                InputStream is=url.openStream();

                //2-4-3. XML 을 파싱해주는 객체 생성
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xpp=factory.newPullParser();

                //2-4-4. 무지개 로드를 통해서 XML 문서 읽어오기
                // 한글도 읽기 위한 "utf-8" 인코딩 방식 적용
                xpp.setInput(is, "utf-8");

                String tagName=null;
                Item item=null;

                int eventType=xpp.next();

                while(eventType != XmlPullParser.END_DOCUMENT){

                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            tagName=xpp.getName();

                            if(tagName.equals("item")){
                                item=new Item();

                            }else if(tagName.equals("title")){
                                xpp.next();
                                if(item != null) item.setTitle(xpp.getText());

                            }else if(tagName.equals("link")){
                                xpp.next();
                                if(item != null) item.setLink(xpp.getText());

                            }else if(tagName.equals("description")){
                                xpp.next();
                                if(item != null) item.setDesc(xpp.getText());

                            }else if (tagName.equals("image")){
                                xpp.next();
                                if(item != null) item.setImage(xpp.getText());

                            }else if(tagName.equals("pubDate")){
                                xpp.next();
                                if(item != null) item.setDate(xpp.getText());

                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tagName=xpp.getName();

                            if(tagName.equals("item")){
                                //기사 하나의 데이터 완성

                                //ArrayList 에 추가
                                items.add(item);
                                item=null; //붙여 놨으니까 더이상 필요 없어. 그래야 다음 놈이 깔끔하게 쓸 수 있어

                                //스레드의 작업 중간중간
                                //UI 갱신작업이 필요하다면 !!
                                publishProgress();

                                //억지로 느리게 하기 위해 스레드 잠시 재우기
//                                try {
//                                    Thread.sleep(1500);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }

                            }
                            break;
                    }
                    eventType=xpp.next();

                }// end of while

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            //-> 파싱 종료
            //완료된 작업 내용 화면에 띄우는 작업

            return "파싱종료";
        }//end of doInBackground()


        //publishProgress() 메소드를 실행하면
        //자동으로 호출되는 메소드.. 이 메소드 안에서 UI 작업 가능
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            //리사이클러의 갱신작업
            adapter.notifyItemInserted(items.size());
        }


        //doInBackground() 메소드가 종료된 후
        //UI 변경 작업을 수행하기 위해 마련된 콜백 메소드(자동호출)
        // 이 메소드 안에서는 UI 변경작업 가능

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //파라미터 : doInBackground() 의 리턴값--> "파싱종료"
            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();

            //리프레시 레이아웃의 뱅글뱅글 아이콘 제거
            refreshLayout.setRefreshing(false);

            //마지막 중요! 꼭!!! 리사이클러뷰의 화면 갱신!!!
            //adapter.notifyDataSetChanged();

            //파싱성공 여부 확인
//            StringBuffer buffer=new StringBuffer();
//            for(Item t : items){
//                String str=t.getTitle();
//                buffer.append(str+"\n\n");
//            }

            //new AlertDialog.Builder(MainActivity.this).setMessage(buffer.toString()).create().show();


        }
    }//end of RssFeedTask class


}//end of MainAct class
