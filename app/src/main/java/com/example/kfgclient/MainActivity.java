package com.example.kfgclient;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.example.kfgclient.ui.main.MonitoringViewModel;
import com.example.kfgclient.ui.main.SectionsPagerAdapter;
import de.exlap.DataObject;
import de.vwn.kfg.client.KFGClient;

/**
 * https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
 */

public class MainActivity extends AppCompatActivity {

    private int i = 100;
    private MonitoringViewModel monitoringViewModel;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectionManager.init(this);

        monitoringViewModel =
                ViewModelProviders.of(this).get(MonitoringViewModel.class);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton connectBtn = findViewById(R.id.fab);



        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if(connectionManagerRef!=null) {
//                    new Thread(connectionManagerRef).start();
//                }
//                else{
//                    Log.e(Const.MYTAG,"ConnectionManager is null");
//                }

                monitoringViewModel.createOrUpdateObject(sb.toString(),String.valueOf(i*2));
                i++;
                sb.append('A');

            }
        });
    }


    /**
     * Updates an object view, e.g. from an object subscription with actual data coming from DataObjects.
     *
     * @param dataObject  contains the actual data for updating objects view box, must not be {@code null}
     * param isGetCmdRsp indicator if a {@link de.exlap.DataObject} should be displayed as a result of a
     *                    {@link de.exlap.command.GetCommand}
     */

    public void updateObjectsView(DataObject dataObject){

        Log.d(Const.MYTAG, "MainActivity.updateObjectsView(...) called");
        monitoringViewModel.createOrUpdateObject(dataObject.getUrl(), dataObject.getElement(0).getValueAsString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectionManager.getInstance().disconnect();
    }
}