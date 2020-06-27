package com.savecontroladoria.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.savecontroladoria.R;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.network.NetworkComandos;

import de.greenrobot.event.EventBus;

/**
 * Created by ADMIN on 20/04/2017.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ComandFragment extends PreferenceFragment {
    private SwitchPreference geral_switch;
    private SwitchPreference ctrl_rele01;
    private SwitchPreference ctrl_rele02;
    private SwitchPreference ctrl_rele03;
    private SwitchPreference ctrl_rele04;
    private SwitchPreference ctrl_rele05;
    private SwitchPreference ctrl_rele06;
    private SwitchPreference ctrl_rele07;
    private SwitchPreference ctrl_rele08;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(ComandFragment.this);
        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        geral_switch = (SwitchPreference) findPreference("geral_switch");
        ctrl_rele01 = (SwitchPreference) findPreference("ctrl_rele01");
        ctrl_rele02 = (SwitchPreference) findPreference("ctrl_rele02");
        ctrl_rele03 = (SwitchPreference) findPreference("ctrl_rele03");
        ctrl_rele04 = (SwitchPreference) findPreference("ctrl_rele04");
        ctrl_rele05 = (SwitchPreference) findPreference("ctrl_rele05");
        ctrl_rele06 = (SwitchPreference) findPreference("ctrl_rele06");
        ctrl_rele07 = (SwitchPreference) findPreference("ctrl_rele07");
        ctrl_rele08 = (SwitchPreference) findPreference("ctrl_rele08");
        //geral_switch.setWidgetLayoutResource(R.layout.pre); // THIS IS THE KEY OF ALL THIS. HERE YOU SET A CUSTOM LAYOUT FOR THE WIDGET
        geral_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean retorno = (boolean) newValue;
                if (retorno) {
                    Log.d("ComandosActivity", "retorno - LIGARGERAL: " + retorno);
                    NetworkComandos.getInstance(getContext()).execute("LIGARGERAL");
                    ctrl_rele01.setChecked(true);
                    ctrl_rele02.setChecked(true);
                    ctrl_rele03.setChecked(true);
                    ctrl_rele04.setChecked(true);
                    ctrl_rele05.setChecked(true);
                    ctrl_rele06.setChecked(true);
                    ctrl_rele07.setChecked(true);
                    ctrl_rele08.setChecked(true);
                } else {
                    NetworkComandos.getInstance(getContext()).execute("DESLIGARGERAL");
                    ctrl_rele01.setChecked(false);
                    ctrl_rele02.setChecked(false);
                    ctrl_rele03.setChecked(false);
                    ctrl_rele04.setChecked(false);
                    ctrl_rele05.setChecked(false);
                    ctrl_rele06.setChecked(false);
                    ctrl_rele07.setChecked(false);
                    ctrl_rele08.setChecked(false);
                }
                return true;
            }
        });

        ctrl_rele01.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean retorno = (boolean) newValue;
                Log.d("ComandosActivity", "retorno - LIGARRELE01: " + retorno);
                if (retorno) {
                    NetworkComandos.getInstance(getContext()).execute("LIGARRELE01");
                } else {
                    NetworkComandos.getInstance(getContext()).execute("DESLIGARRELE01");
                }
                return true;
            }
        });
        ctrl_rele02.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean retorno = (boolean) newValue;
                Log.d("ComandosActivity", "retorno - LIGARRELE02: " + retorno);
                if (retorno) {
                    NetworkComandos.getInstance(getContext()).execute("LIGARRELE02");
                } else {
                    NetworkComandos.getInstance(getContext()).execute("DESLIGARRELE02");
                }
                return true;
            }
        });
        ctrl_rele03.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean retorno = (boolean) newValue;
                Log.d("ComandosActivity", "retorno - LIGARRELE03: " + retorno);
                if (retorno) {
                    NetworkComandos.getInstance(getContext()).execute("LIGARRELE03");
                } else {
                    NetworkComandos.getInstance(getContext()).execute("DESLIGARRELE03");
                }
                return true;
            }
        });
        ctrl_rele04.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean retorno = (boolean) newValue;
                Log.d("ComandosActivity", "retorno - LIGARRELE04: " + retorno);
                if (retorno) {
                    NetworkComandos.getInstance(getContext()).execute("LIGARRELE04");
                } else {
                    NetworkComandos.getInstance(getContext()).execute("DESLIGARRELE04");
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //startActivity(new Intent(getActivity(), SettingsActivity.class));
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(ListenerEB listenerEB) {
        if (!listenerEB.getClasseDestino().equalsIgnoreCase(ComandFragment.class + "")) {
            return;
        } else {
            if (listenerEB.getMensagem().equals("LIGARGERAL")) {
                geral_switch.setChecked(true);
                ctrl_rele01.setChecked(true);
                ctrl_rele02.setChecked(true);
                ctrl_rele03.setChecked(true);
                ctrl_rele04.setChecked(true);
                ctrl_rele05.setChecked(true);
                ctrl_rele06.setChecked(true);
                ctrl_rele07.setChecked(true);
                ctrl_rele08.setChecked(true);
            }
            if (listenerEB.getMensagem().equals("DESLIGARGERAL")) {
                geral_switch.setChecked(false);
                ctrl_rele01.setChecked(false);
                ctrl_rele02.setChecked(false);
                ctrl_rele03.setChecked(false);
                ctrl_rele04.setChecked(false);
                ctrl_rele05.setChecked(false);
                ctrl_rele06.setChecked(false);
                ctrl_rele07.setChecked(false);
                ctrl_rele08.setChecked(false);
            }
            if (listenerEB.getMensagem().equals("RELE01LIGA")) {
                ctrl_rele01.setChecked(true);
            }
            if (listenerEB.getMensagem().equals("RELE01DESLIGA")) {
                ctrl_rele01.setChecked(false);
            }
            if (listenerEB.getMensagem().equals("RELE02LIGA")) {
                ctrl_rele02.setChecked(true);
            }
            if (listenerEB.getMensagem().equals("RELE02DESLIGA")) {
                ctrl_rele02.setChecked(false);
            }
            if (listenerEB.getMensagem().equals("RELE03LIGA")) {
                ctrl_rele03.setChecked(true);
            }
            if (listenerEB.getMensagem().equals("RELE03DESLIGA")) {
                ctrl_rele03.setChecked(false);
            }
            if (listenerEB.getMensagem().equals("RELE04LIGA")) {
                ctrl_rele04.setChecked(true);
            }
            if (listenerEB.getMensagem().equals("RELE05DESLIGA")) {
                ctrl_rele04.setChecked(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(ComandFragment.this);
    }
}
