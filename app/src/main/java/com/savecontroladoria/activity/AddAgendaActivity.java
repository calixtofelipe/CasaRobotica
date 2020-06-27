package com.savecontroladoria.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.savecontroladoria.R;
import com.savecontroladoria.bancoDados.dao.AgendaDAO;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.objetos.Agendamento;
import com.savecontroladoria.util.SpinnerUtils;

public class AddAgendaActivity extends AppCompatActivity {
    CheckBox checkBoxDom;
    CheckBox checkBoxSeg;
    CheckBox checkBoxTer;
    CheckBox checkBoxQua;
    CheckBox checkBoxQui;
    CheckBox checkBoxSex;
    CheckBox checkBoxSab;
    CheckBox checkBoxOnOff;
    Spinner spinnerAcao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddAgenda);
        setSupportActionBar(toolbar);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePickerAgenda);
        Button confirmar = (Button) findViewById(R.id.btnConfirmarAgenda);
        Button cancelar = (Button) findViewById(R.id.btnCancelarAgenda);
        checkBoxDom = (CheckBox) findViewById(R.id.checkBoxDom);
        checkBoxSeg = (CheckBox) findViewById(R.id.checkBoxSeg);
        checkBoxTer = (CheckBox) findViewById(R.id.checkBoxTer);
        checkBoxQua = (CheckBox) findViewById(R.id.checkBoxQua);
        checkBoxQui = (CheckBox) findViewById(R.id.checkBoxQui);
        checkBoxSex = (CheckBox) findViewById(R.id.checkBoxSex);
        checkBoxSab = (CheckBox) findViewById(R.id.checkBoxSab);
        checkBoxOnOff = (CheckBox) findViewById(R.id.checkBoxONOFF);
        spinnerAcao = (Spinner) findViewById(R.id.spinnerAcao);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, SpinnerUtils.getSpinnerAcao());

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinnerAcao.setAdapter(dataAdapter);
        spinnerAcao.setSelection(0);



        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddAgenda","Log: Hora: "+timePicker.getHour()+
                        " minutos: "+timePicker.getMinute()+" acao: "+spinnerAcao.getSelectedItemPosition());
                AgendaDAO agendaDAO = new AgendaDAO(getApplicationContext());
                JSONObject json = new JSONObject();
                Calendar c = Calendar.getInstance();
                String horas = String.valueOf(timePicker.getHour());
                String minutos = String.valueOf(timePicker.getMinute());
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = "semuid";
                if(user != null) {
                    uid = user.getUid();
                }
                try {
                    json.put("codhora",agendaDAO.count());
                    json.put("descrhora",(horas.length()==1? "0"+horas : horas)+":"+ (minutos.length()==1? "0"+minutos : minutos));
                    json.put("hora",timePicker.getHour());
                    json.put("minuto",timePicker.getMinute());
                    json.put("segunda",checkBoxSeg.isChecked()? "S" : "N");
                    json.put("terca",checkBoxTer.isChecked()? "S" : "N");
                    json.put("quarta",checkBoxQua.isChecked()? "S" : "N");
                    json.put("quinta",checkBoxQui.isChecked()? "S" : "N");
                    json.put("sexta",checkBoxSex.isChecked()? "S" : "N");
                    json.put("sabado",checkBoxSab.isChecked()? "S" : "N");
                    json.put("domingo",checkBoxDom.isChecked()? "S" : "N");
                    json.put("onoff",checkBoxOnOff.isChecked()? "S" : "N");
                    json.put("acao",spinnerAcao.getSelectedItemPosition());
                    json.put("uid",uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                boolean retorno = agendaDAO.insert(json);
                if(retorno) {
                    EventBus eventBus = EventBus.getDefault();
                    ListenerEB msg = new ListenerEB();
                    msg.setClasseDestino(AgendaActivity.class + "");
                    msg.setMensagem("ATUALIZALISTA");
                    Agendamento ag = new Agendamento();
                    ag.setDescrHora((horas.length()==1? "0"+horas : horas)+":"+ (minutos.length()==1? "0"+minutos : minutos));
                    ag.setOnoff(checkBoxOnOff.isChecked()? "S" : "N");
                    ag.setDomingo(checkBoxDom.isChecked()? "S" : "N");
                    ag.setSegunda(checkBoxSeg.isChecked()? "S" : "N");
                    ag.setTerca(checkBoxTer.isChecked()? "S" : "N");
                    ag.setQuarta(checkBoxQua.isChecked()? "S" : "N");
                    ag.setQuinta(checkBoxQui.isChecked()? "S" : "N");
                    ag.setSexta(checkBoxSex.isChecked()? "S" : "N");
                    ag.setSabado(checkBoxSab.isChecked()? "S" : "N");
                    ag.setAcao(String.valueOf(spinnerAcao.getSelectedItemPosition()));
                    msg.setAgendamento(ag);
                    eventBus.post(msg);
                        onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(),"Erro ao Inserir",Toast.LENGTH_LONG);
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //startActivity(new Intent(getActivity(), SettingsActivity.class));
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
