package com.example.kfgclient.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kfgclient.AndroidViewHelper;
import com.example.kfgclient.Const;
import com.example.kfgclient.R;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 * https://stackoverflow.com/questions/17315842/how-to-call-a-method-in-mainactivity-from-another-class
 */

/**
 *
 * czyli z Main trzeba powiadamiać ViewModel, a nie bezpośrednio Fragment?
 */


public class MonitoringFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    //ViewModel
    private MonitoringViewModel monitoringViewModel;


    /* Start new subscription popup window */
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private AutoCompleteTextView new_object_url;
    private SeekBar timeStepSB;
    private ImageView dropListBtn;
    private TextView timeStepTV;
    private Button subscribeBtn;


    //Subscription box
    private View objectsContainerLayout;
    private ScrollView objectsScrollView;
    private TextView url_tv;
    private TextView current_value;
    private Button remove_box_btn;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Czy ViewModel jest inicjowany na pewno tylko raz, dzięki ViewModelProviders?
        monitoringViewModel = ViewModelProviders.of(getActivity()).get(MonitoringViewModel.class);

        //Register Observer
        observeIncomingObject(monitoringViewModel);

    }

    private void observeIncomingObject(MonitoringViewModel monitoringViewModel) {
        monitoringViewModel.getObjectsMap().observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, String> stringStringHashMap) {

                Map.Entry<String,String> entry = stringStringHashMap.entrySet().iterator().next();

                String key= entry.getKey();
                String value=entry.getValue();
                createSubscriptionBox(key,value);

                Log.d(Const.MYTAG,"onChanged called. Key: "+key+" value: " + value);
            }
        });
    }





    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_monitor, container, false);
        Button addButton = (Button) root.findViewById(R.id.monitor_frag_add_sub);
        objectsContainerLayout = (ViewGroup) root.findViewById(R.id.frameLayout);
        objectsScrollView = (ScrollView) root.findViewById(R.id.objectsScrollView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }
        });

        return root;
    }


    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        View popUpView = getLayoutInflater().inflate(R.layout.new_sub_window, null);
        new_object_url =  popUpView.findViewById(R.id.new_sub_window_url);
        timeStepSB = popUpView.findViewById(R.id.new_sub_window_interval);
        dropListBtn =  popUpView.findViewById(R.id.new_sub_window_arrow);
        timeStepTV = popUpView.findViewById(R.id.new_sub_window_time_step);
        timeStepTV.setText((R.string.def_sub_time));
        subscribeBtn =  popUpView.findViewById(R.id.new_sub_window_sub_start);
        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        new_object_url.setDropDownHeight(300);

        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Const.ALL_EXLAP_OBJECTS);
        new_object_url.setAdapter(autoCompleteAdapter);

        dropListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_object_url.showDropDown();
            }
        });


        timeStepSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress / 100;
                progress = progress * 100;
                timeStepTV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //not in use
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //not in use

            }
        });

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!new_object_url.getText().toString().isEmpty() && !timeStepTV.getText().toString().isEmpty()){
                   monitoringViewModel.startSubscription(new_object_url.getText().toString(),Integer.valueOf(timeStepTV.getText().toString()));
                    dialog.dismiss();
                }else{
                    Toast.makeText(getActivity(), "No URL or Time Step specified", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private  void createSubscriptionBox(final String tagNameForBox, String value){

        View new_box =  objectsContainerLayout.findViewWithTag(tagNameForBox);

        if(new_box==null){

            final View objectBox =LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.view_object, (ViewGroup) objectsContainerLayout,false);
            objectBox.setTag(tagNameForBox);

            //positioning
            int topMargin = AndroidViewHelper.dpToPx(10, getResources());
            Log.d(Const.MYTAG, "topMargin: " + topMargin + "px");

            for (int i=0; i<((ViewGroup) objectsContainerLayout).getChildCount();i++){

                View tmp_box =((ViewGroup) objectsContainerLayout).getChildAt(i);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) tmp_box.getLayoutParams();
                Log.d(Const.MYTAG,"margin top: " + marginLayoutParams.topMargin + " height: " + tmp_box.getHeight() + " width: " + tmp_box.getWidth());

                if(topMargin<= marginLayoutParams.topMargin){

                    topMargin =marginLayoutParams.topMargin +tmp_box.getHeight() + AndroidViewHelper.dpToPx((10),getResources());
                }
            }

            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);

            flp.setMargins(AndroidViewHelper.dpToPx(10, getResources()), topMargin, 0, 0);
            objectBox.setLayoutParams(flp);

            ((ViewGroup) objectsContainerLayout).addView(objectBox);


            url_tv = objectBox.findViewById(R.id.objectViewUrlText);
            current_value = objectBox.findViewById(R.id.objectViewUrlValue);
            remove_box_btn = objectBox.findViewById(R.id.buttonClose);


            url_tv.setText(tagNameForBox);
            //current_value.setText(value);


            // dragging
            objectBox.setOnTouchListener(new ViewTouchDragListener(objectsScrollView));

            //removing subscription
            remove_box_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Removed " + tagNameForBox, Toast.LENGTH_SHORT).show();
                    monitoringViewModel.stopSubscription(tagNameForBox);
                    ((ViewGroup) objectsContainerLayout).removeView(objectBox);
                }
            });


            Log.d(Const.MYTAG,"Added new View to the containter with url: " + tagNameForBox);
        }

        else{
            View current_box = new_box;
            current_value = current_box.findViewById(R.id.objectViewUrlValue);
            current_value.setText(value);
            Log.d(Const.MYTAG, "Updated View " + tagNameForBox + " with value: " + value);

        }

    }

}