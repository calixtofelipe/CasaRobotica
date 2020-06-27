package com.savecontroladoria.viewholders;

import android.view.View;
import android.widget.TextView;

import com.savecontroladoria.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by ADMIN on 29/04/2017.
 */

public class ComodosViewHolder extends GroupViewHolder {

    private TextView comodoTitle;

    public ComodosViewHolder(View itemView) {
        super(itemView);
        comodoTitle = (TextView) itemView.findViewById(R.id.tvcomodos);
    }

    public void setComodoTitle(String name) {
        comodoTitle.setText(name);
    }
}
