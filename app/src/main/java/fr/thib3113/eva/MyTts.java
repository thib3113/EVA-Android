package fr.thib3113.eva;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by thibaut on 20/03/2015.
 */
public class MyTts implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener{
    private  static TextToSpeech mTts;
    private  static boolean isInit = false;
    private static Activity parent_activity;
    private static List<String> waiting_list = new ArrayList<String>();

    public static void speak(String out_str){

        if(isInit()){
            if(Build.VERSION.SDK_INT >= 21 ){
                mTts.speak((CharSequence) out_str, TextToSpeech.QUEUE_ADD, new Bundle(), TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            }
            else{
                mTts.speak(out_str, TextToSpeech.QUEUE_ADD, null);
            }
        }
        else{
            Toast.makeText(parent_activity.getApplicationContext(), out_str, Toast.LENGTH_LONG).show();
            waiting_list.add(out_str);
        }
    }

    public static boolean isInit() {
        return isInit;
    }

    public void stop(){
        mTts.stop();
    }

    public void shutdown(){
        mTts.shutdown();
    }

    public static TextToSpeech getmTts() {
        return mTts;
    }

    public MyTts(Activity a){
        parent_activity = a;
        mTts = new TextToSpeech(parent_activity, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        String str = "TTs en français n'est pas supporté";
                        MyTts.log(str);
                        Toast.makeText(parent_activity, str, Toast.LENGTH_LONG);
                    }
                    else{
                        isInit = true;
                        for (String out_str : waiting_list) {
                            MyTts.speak(out_str); // error speak is not static
                        }
                    }
                } else{
                    String str = "Initialization fail";
                    MyTts.log(str);
                    Toast.makeText(parent_activity, str, Toast.LENGTH_LONG);
                }
            }
        });
    }

    private static void log(String str){
        Log.e("[TextToSpeech]", str);
        System.out.println(str);
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

    }
}
