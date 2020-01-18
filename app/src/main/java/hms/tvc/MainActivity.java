package hms.tvc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    HandlerThread hth = null;
    Looper hthLooper = null;
    NetworkHandler nwhndlr = null;


    public TextView exceptions_tv_ = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        exceptions_tv_ = (TextView)this.findViewById(R.id.exceptions_tb);
        hth = new HandlerThread("NetworkThread",android.os.Process.THREAD_PRIORITY_BACKGROUND);
        hth.start();
        hthLooper = hth.getLooper();
        nwhndlr = new NetworkHandler(hthLooper,this);
    }

    @Override
    protected void onDestroy() {
        hth.quit();
        super.onDestroy();
    }

    public boolean r2_on_onclick(View but){
        int req = 0x110279;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }

    public boolean r2_off_onclick(View but){
        int req = (int)0x000279L;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }
    public boolean r1_on_onclick(View but){
        int req = (int)0x110179L;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }

    public boolean r1_off_onclick(View but){
        int req = (int)0x000179L;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }
    public boolean noise_on_onclick(View but){
        int req = (int)0x007979L;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }

    public boolean noise_off_onclick(View but){
        int req = (int)0x117979L;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }
    public boolean save_onClick(View but){
        int req = (int)0x11022A;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }
    public boolean clear_onClick(View but){
        this.exceptions_tv_.setText("");
        return true;
    }

    public boolean klvon_onClick(View but){
        int req = (int)0xF5FF38;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }

    public boolean klvoff_onClick(View but){
        int req = (int)0xA90038;
        Message msg =  nwhndlr.obtainMessage();
        msg.arg1=req;
        nwhndlr.sendMessage(msg);
        return true;
    }


}
