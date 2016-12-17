package com.iotclientbeta;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public TextView[][] textViews_T=new TextView[7][5];
    public TextView[][] textViews_H=new TextView[7][5];
    public LinearLayout[][] BG_linearLayouts=new LinearLayout[7][5];
    public double WarnHighTemp,WarnHighHumi,WarnLowTemp,WarnLowHumi;
    private String [][] UIdata=new String[10][7];
    private mSimpleCache viewCache=new mSimpleCache(128);
    private ViewData viewData;

    String serverUrl="http://192.168.1.105:8080/api/getdata";
    OkHttpClient mHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
            .url(serverUrl)
            .build();
    ReceiveThread receiveThread=new ReceiveThread();
    ReloadView reloadView=new ReloadView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WarnLowHumi = 5.0;
        WarnLowTemp = 5.0;
        WarnHighHumi = 20.0;
        WarnHighTemp = 20.0;

        textViews_H[0][0]=(TextView)findViewById(R.id.A_1_humi);
        textViews_H[0][1] = (TextView)findViewById(R.id.B_1_humi);
        textViews_H[0][2] = (TextView)findViewById(R.id.C_1_humi);
        textViews_H[0][3]= (TextView)findViewById(R.id.D_1_humi);
        textViews_H[0][4]= (TextView)findViewById(R.id.E_1_humi);
        textViews_H[1][0]= (TextView)findViewById(R.id.A_2_humi);
        textViews_H[1][1]= (TextView)findViewById(R.id.B_2_humi);
        textViews_H[1][2]= (TextView)findViewById(R.id.C_2_humi);
        textViews_H[1][3] = (TextView)findViewById(R.id.D_2_humi);
        textViews_H[1][4] = (TextView)findViewById(R.id.E_2_humi);
        textViews_H[2][0] = (TextView)findViewById(R.id.A_3_humi);
        textViews_H[2][1] = (TextView)findViewById(R.id.B_3_humi);
        textViews_H[2][2] = (TextView)findViewById(R.id.C_3_humi);
        textViews_H[2][3] = (TextView)findViewById(R.id.D_3_humi);
        textViews_H[2][4] = (TextView)findViewById(R.id.E_3_humi);
        textViews_H[3][0] = (TextView)findViewById(R.id.A_4_humi);
        textViews_H[3][1] = (TextView)findViewById(R.id.B_4_humi);
        textViews_H[3][2] = (TextView)findViewById(R.id.C_4_humi);
        textViews_H[3][3] = (TextView)findViewById(R.id.D_4_humi);
        textViews_H[3][4] = (TextView)findViewById(R.id.E_4_humi);
        textViews_H[4][0] = (TextView)findViewById(R.id.A_5_humi);
        textViews_H[4][1] = (TextView)findViewById(R.id.B_5_humi);
        textViews_H[4][2] = (TextView)findViewById(R.id.C_5_humi);
        textViews_H[4][3]  = (TextView)findViewById(R.id.D_5_humi);
        textViews_H[4][4] = (TextView)findViewById(R.id.E_5_humi);
        textViews_H[5][0] = (TextView)findViewById(R.id.A_6_humi);
        textViews_H[5][1] = (TextView)findViewById(R.id.B_6_humi);
        textViews_H[5][2] = (TextView)findViewById(R.id.C_6_humi);
        textViews_H[5][3] = (TextView)findViewById(R.id.D_6_humi);
        textViews_H[5][4] = (TextView)findViewById(R.id.E_6_humi);
        textViews_H[6][0]  = (TextView)findViewById(R.id.A_7_humi);
        textViews_H[6][1] = (TextView)findViewById(R.id.B_7_humi);
        textViews_H[6][2] = (TextView)findViewById(R.id.C_7_humi);
        textViews_H[6][3] = (TextView)findViewById(R.id.D_7_humi);
        textViews_H[6][4] = (TextView)findViewById(R.id.E_7_humi);

        textViews_T[0][0] = (TextView)findViewById(R.id.A_1_temp);
        textViews_T[0][1] = (TextView)findViewById(R.id.B_1_temp);
        textViews_T[0][2] = (TextView)findViewById(R.id.C_1_temp);
        textViews_T[0][3] = (TextView)findViewById(R.id.D_1_temp);
        textViews_T[0][4] = (TextView)findViewById(R.id.E_1_temp);
        textViews_T[1][0] = (TextView)findViewById(R.id.A_2_temp);
        textViews_T[1][1] = (TextView)findViewById(R.id.B_2_temp);
        textViews_T[1][2] = (TextView)findViewById(R.id.C_2_temp);
        textViews_T[1][3] = (TextView)findViewById(R.id.D_2_temp);
        textViews_T[1][4] = (TextView)findViewById(R.id.E_2_temp);
        textViews_T[2][0] = (TextView)findViewById(R.id.A_3_temp);
        textViews_T[2][1] = (TextView)findViewById(R.id.B_3_temp);
        textViews_T[2][2] = (TextView)findViewById(R.id.C_3_temp);
        textViews_T[2][3] = (TextView)findViewById(R.id.D_3_temp);
        textViews_T[2][4] = (TextView)findViewById(R.id.E_3_temp);
        textViews_T[3][0] = (TextView)findViewById(R.id.A_4_temp);
        textViews_T[3][1] = (TextView)findViewById(R.id.B_4_temp);
        textViews_T[3][2] = (TextView)findViewById(R.id.C_4_temp);
        textViews_T[3][3] = (TextView)findViewById(R.id.D_4_temp);
        textViews_T[3][4]  = (TextView)findViewById(R.id.E_4_temp);
        textViews_T[4][0] = (TextView)findViewById(R.id.A_5_temp);
        textViews_T[4][1] = (TextView)findViewById(R.id.B_5_temp);
        textViews_T[4][2] = (TextView)findViewById(R.id.C_5_temp);
        textViews_T[4][3] = (TextView)findViewById(R.id.D_5_temp);
        textViews_T[4][4] = (TextView)findViewById(R.id.E_5_temp);
        textViews_T[5][0] = (TextView)findViewById(R.id.A_6_temp);
        textViews_T[5][1] = (TextView)findViewById(R.id.B_6_temp);
        textViews_T[5][2] = (TextView)findViewById(R.id.C_6_temp);
        textViews_T[5][3] = (TextView)findViewById(R.id.D_6_temp);
        textViews_T[5][4] = (TextView)findViewById(R.id.E_6_temp);
        textViews_T[6][0] = (TextView)findViewById(R.id.A_7_temp);
        textViews_T[6][1] = (TextView)findViewById(R.id.B_7_temp);
        textViews_T[6][2] = (TextView)findViewById(R.id.C_7_temp);
        textViews_T[6][3] = (TextView)findViewById(R.id.D_7_temp);
        textViews_T[6][4] = (TextView)findViewById(R.id.E_7_temp);

        BG_linearLayouts[0][0]= (LinearLayout) findViewById(R.id.backgroundA_1);
        BG_linearLayouts[0][1]= (LinearLayout) findViewById(R.id.backgroundB_1);
        BG_linearLayouts[0][2]= (LinearLayout) findViewById(R.id.backgroundC_1);
        BG_linearLayouts[0][3]= (LinearLayout) findViewById(R.id.backgroundD_1);
        BG_linearLayouts[0][4]= (LinearLayout) findViewById(R.id.backgroundE_1);
        BG_linearLayouts[1][0]= (LinearLayout) findViewById(R.id.backgroundA_2);
        BG_linearLayouts[1][1]= (LinearLayout) findViewById(R.id.backgroundB_2);
        BG_linearLayouts[1][2]= (LinearLayout) findViewById(R.id.backgroundC_2);
        BG_linearLayouts[1][3]= (LinearLayout) findViewById(R.id.backgroundD_2);
        BG_linearLayouts[1][4]= (LinearLayout) findViewById(R.id.backgroundE_2);
        BG_linearLayouts[2][0]= (LinearLayout) findViewById(R.id.backgroundA_3);
        BG_linearLayouts[2][1]= (LinearLayout) findViewById(R.id.backgroundB_3);
        BG_linearLayouts[2][2]= (LinearLayout) findViewById(R.id.backgroundC_3);
        BG_linearLayouts[2][3]= (LinearLayout) findViewById(R.id.backgroundD_3);
        BG_linearLayouts[2][4]= (LinearLayout) findViewById(R.id.backgroundE_3);
        BG_linearLayouts[3][0]= (LinearLayout) findViewById(R.id.backgroundA_4);
        BG_linearLayouts[3][1]= (LinearLayout) findViewById(R.id.backgroundB_4);
        BG_linearLayouts[3][2]= (LinearLayout) findViewById(R.id.backgroundC_4);
        BG_linearLayouts[3][3]= (LinearLayout) findViewById(R.id.backgroundD_4);
        BG_linearLayouts[3][4]= (LinearLayout) findViewById(R.id.backgroundE_4);
        BG_linearLayouts[4][0]= (LinearLayout) findViewById(R.id.backgroundA_5);
        BG_linearLayouts[4][1]= (LinearLayout) findViewById(R.id.backgroundB_5);
        BG_linearLayouts[4][2]= (LinearLayout) findViewById(R.id.backgroundC_5);
        BG_linearLayouts[4][3]= (LinearLayout) findViewById(R.id.backgroundD_5);
        BG_linearLayouts[4][4]= (LinearLayout) findViewById(R.id.backgroundE_5);
        BG_linearLayouts[5][0]= (LinearLayout) findViewById(R.id.backgroundA_6);
        BG_linearLayouts[5][1]= (LinearLayout) findViewById(R.id.backgroundB_6);
        BG_linearLayouts[5][2]= (LinearLayout) findViewById(R.id.backgroundC_6);
        BG_linearLayouts[5][3]= (LinearLayout) findViewById(R.id.backgroundD_6);
        BG_linearLayouts[5][4]= (LinearLayout) findViewById(R.id.backgroundE_6);
        BG_linearLayouts[6][0]= (LinearLayout) findViewById(R.id.backgroundA_7);
        BG_linearLayouts[6][1]= (LinearLayout) findViewById(R.id.backgroundB_7);
        BG_linearLayouts[6][2]= (LinearLayout) findViewById(R.id.backgroundC_7);
        BG_linearLayouts[6][3]= (LinearLayout) findViewById(R.id.backgroundD_7);
        BG_linearLayouts[6][4]= (LinearLayout) findViewById(R.id.backgroundE_7);


        new Thread(receiveThread).start();
        new Thread(reloadView).start();
        initUiData();
    }
    public class ReceiveThread implements Runnable{//接收数据线程
        @Override
        public void run() {
            while (true){
                try {
                    Logger.d("开始接收数据");
                    mHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String body=response.body().string();
                            Logger.d(body);
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray Jarray = parser.parse(body).getAsJsonArray();

                            for(JsonElement obj : Jarray ){
                                ViewData mviewData=gson.fromJson( obj ,ViewData.class);
                                Logger.d(mviewData.ToString());
                                viewCache.put(Math.abs(new Random().nextInt()%1048576),mviewData);
                            }
                        }
                    });
                    Thread.sleep(60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class ReloadView implements Runnable {//刷新UI线程
        Gson gson = new Gson();
        @Override
        public void run() {
            while (true) {
                try {
                    for (Object key : viewCache.getKeySet()) {
//                        viewData = gson.fromJson((String) viewCache.get(key), ViewData.class);
                        viewData = (ViewData) viewCache.get(key);
                        Logger.d("开始更新界面了"+viewData.ToString());
                        Logger.d("temp "+viewData.temperature);
                        Logger.d("humi "+viewData.humidity);
                        //二维数组第一个数是sensorNum，且奇数是temperature
                        char[] sensorCharArray=viewData.sensorNum.toCharArray();
                        int x=sensorCharArray[0]-'A'+1;
                        int y=Integer.valueOf(viewData.arduinoNum);
                        Logger.d(viewData.temperature+"test");
                        if(!(viewData.temperature==null||viewData.temperature=="null"))
                        {
                            UIdata[2*x-1][y-1]=viewData.temperature;
                            Logger.d("UItemp变化了："+UIdata[2*x-1][y-1]);
                        }
                        if(!(viewData.humidity==null||viewData.humidity=="null"))
                        {
                            UIdata[2*x-2][y-1]=viewData.humidity;
                            Logger.d("UIhum变化了："+UIdata[2*x-2][y-1]);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("UI更新启动");
                            for(int i=0;i<7;i++)
                                for(int j=0;j<5;j++)
                                {
                                    if(Double.valueOf(UIdata[2*j][i]) > WarnHighHumi)
                                        BG_linearLayouts[i][j].setBackgroundColor(Color.CYAN);
                                    else if(Double.valueOf(UIdata[2*j][i]) < WarnLowHumi)
                                        BG_linearLayouts[i][j].setBackgroundColor(Color.GRAY);
                                    if(Double.valueOf(UIdata[2*j+1][i]) > WarnHighTemp)
                                        BG_linearLayouts[i][j].setBackgroundColor(Color.RED);
                                    else if(Double.valueOf(UIdata[2*j+1][i]) < WarnLowTemp)
                                        BG_linearLayouts[i][j].setBackgroundColor(Color.BLUE);
                                    textViews_T[i][j].setText(" "+UIdata[2*j+1][i]+"℃");
                                    textViews_H[i][j].setText(" "+UIdata[2*j][i]+"%");
                                }
                        }
                    });
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                viewData=null;
            }
        }

    }

    public void initUiData(){
        for (int i=0;i<10;i++)
            for (int j=0;j<7;j++){
                if(i%2==0)
                    UIdata[i][j]="20";
                else
                    UIdata[i][j]="10";
            }

    }
}
