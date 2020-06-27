package com.savecontroladoria.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.savecontroladoria.R;
import com.savecontroladoria.network.NetworkComandos;
import com.savecontroladoria.network.NetworkComandosCena2;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by ADMIN on 29/04/2017.
 */

public class AcaoViewHolder extends ChildViewHolder {

    private TextView acaoTitle;
    private Switch swAcao;
    public static final String TAG = "AcaoViewHolder";
    private Context mContext;

    public AcaoViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        acaoTitle = (TextView) itemView.findViewById(R.id.tvacoes);
        swAcao = (Switch) itemView.findViewById(R.id.swAcao);

        swAcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "POSICAO" +v.getId()+"cena: "+acaoTitle.getText());
                if (acaoTitle.getText().equals("TODAS AUTOMAÇÕES SALA")) {
                    if (swAcao.isChecked()) {
                        swAcao.setChecked(false);
                        NetworkComandos.getInstance(v.getContext()).execute("LIGARGERAL");
                    } else {
                        swAcao.setChecked(true);
                        NetworkComandos.getInstance(v.getContext()).execute("DESLIGARGERAL");
                    }
                }
                if (acaoTitle.getText().equals("TV/PLAYSTATION/DUOSAT")) {
                    Log.d(TAG,"STATUS "+swAcao.isChecked());
                    if (swAcao.isChecked()) {
                        swAcao.setChecked(false);
                        NetworkComandos.getInstance(v.getContext()).execute("LIGARRELE01");
                    } else {
                        swAcao.setChecked(true);
                        NetworkComandos.getInstance(v.getContext()).execute("DESLIGARRELE01");
                    }
                }
                if (acaoTitle.getText().equals("LUZ SALA TV")) {
                    if (swAcao.isChecked()) {
                        swAcao.setChecked(false);
                        NetworkComandos.getInstance(v.getContext()).execute("LIGARRELE02");
                    } else {
                        swAcao.setChecked(true);
                        NetworkComandos.getInstance(v.getContext()).execute("DESLIGARRELE02");
                    }
                }
                if (acaoTitle.getText().equals("LUZ SALA")) {
                    Log.d(TAG,"STATUS "+swAcao.isChecked());
                    if (swAcao.isChecked()) {
                        swAcao.setChecked(false);
                        NetworkComandos.getInstance(v.getContext()).execute("LIGARRELE03");
                    } else {
                        swAcao.setChecked(true);
                        NetworkComandos.getInstance(v.getContext()).execute("DESLIGARRELE03");
                    }
                }
                if (acaoTitle.getText().equals("LUZ HALL")) {
                    if (swAcao.isChecked()) {
                        swAcao.setChecked(false);
                        NetworkComandos.getInstance(v.getContext()).execute("LIGARRELE04");
                    } else {
                        swAcao.setChecked(true);
                        NetworkComandos.getInstance(v.getContext()).execute("DESLIGARRELE04");
                    }
                }
                if (acaoTitle.getText().equals("FILTRO")) {
                    if (swAcao.isChecked()) {
                        swAcao.setChecked(false);
                        NetworkComandosCena2.getInstance(v.getContext()).execute("LIGARRELE01");
                    } else {
                        swAcao.setChecked(true);
                        NetworkComandosCena2.getInstance(v.getContext()).execute("DESLIGARRELE01");
                    }
                }

            }
        });
    }

    public void setAcaoTitle(String nome) {
        acaoTitle.setText(nome);
    }

    public void setStatusSwitch(boolean status) {
        swAcao.setChecked(status);

    }

}
