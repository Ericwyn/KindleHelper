package com.ericwyn.kindlesharehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ericwyn.filechooseutil.FileChooseDialogBuilder;
import com.ericwyn.kindlesharehelper.R;

/**
 * 待发送文件列表的Fragment
 * Created by ericwyn on 17-5-5.
 */

public class FileChooseFragment extends Fragment{
    private ImageButton button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_filechoose,container,false);
        button=(ImageButton)view.findViewById(R.id.fab_fileChoose);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooseDialogBuilder fileChooseDialogBuilder=new FileChooseDialogBuilder(getContext());
                fileChooseDialogBuilder.show();
            }
        });
        return view;
    }
}
