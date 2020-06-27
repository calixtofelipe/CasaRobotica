package com.savecontroladoria.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.savecontroladoria.R;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.fragment.ComandFragment;

import de.greenrobot.event.EventBus;

public class ComandosActivity extends AppCompatActivity {
    private static ComandosActivity instance;
    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comandos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new ComandFragment()).commit();

        mProgressView = findViewById(R.id.pbcomand);

        EventBus.getDefault().register(ComandosActivity.this);
        //mProgressView.setVisibility(View.VISIBLE);

    }


    public void onEvent(ListenerEB listenerEB) {
        if (!listenerEB.getClasseDestino().equalsIgnoreCase(ComandosActivity.class + "")) {
            return;
        } else {
            if (listenerEB.getMensagem().equals("PROGRESSBARENABLE")) {
                mProgressView.setVisibility(View.VISIBLE);
            }
            if (listenerEB.getMensagem().equals("PROGRESSBARDISABLE")) {
                mProgressView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(ComandosActivity.this);
    }
}
