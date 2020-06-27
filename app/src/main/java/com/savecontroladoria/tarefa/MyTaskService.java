package com.savecontroladoria.tarefa;

/**
 * Created by ADMIN on 17/11/2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savecontroladoria.activity.ComandosActivityRV;
import com.savecontroladoria.bancoDados.dao.AgendaDAO;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.fragment.ComandFragment;
import com.savecontroladoria.network.NetworkComandos;
import com.savecontroladoria.network.NetworkComandosCena2;
import com.savecontroladoria.objetos.Acao;
import com.savecontroladoria.objetos.Agendamento;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import de.greenrobot.event.EventBus;


public class MyTaskService extends GcmTaskService {

    public static final String GCM_ONEOFF_TAG = "oneoff|[0,0]";
    public static final String GCM_REPEAT_TAG = "JOBAGENDA";
    private static final String GCM_30EM30_TAG = "JOBTAB";
    private static final String TAG = MyTaskService.class.getSimpleName();
    private static int taskID = 1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GcmNetworkManager mScheduler;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference tab = database.getReference("tabelas");
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //EXEMPLO DE AGENDAMENTO PERIODICO
    public static void scheduleperiodic(Context context) {

        long periodSecs = 30L; // the task should be executed every 30 seconds

        long flexSecs = -15L; // the task can run as early as -15 seconds from the scheduled time

        String tag = "periodic  | " + taskID++ + ": " + periodSecs + "s, f:" + flexSecs;  // a unique task identifier
        Bundle data = new Bundle();
        data.putString("teste", "teste");
        PeriodicTask periodic = new PeriodicTask.Builder()
                .setService(MyTaskService.class)
                .setPeriod(periodSecs)
                .setFlex(flexSecs)
                .setTag(tag)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setExtras(data)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED) //so roda se tiver internet,
                //para rodar sempre colocar NETWORK_STATE_ANY
                .setRequiresCharging(false)
                .build();
        GcmNetworkManager.getInstance(context).schedule(periodic);
    }

    //EXEMPLO DE AGENDAMENTO POR EXECUÇAO
    public static void scheduleOneOff(Context context) {
        //in this method, single OneOff task is scheduled (the target service that will be called is MyTaskService.class)
        Bundle data = new Bundle();
        data.putString("some key", "some budle data");
        try {
            OneoffTask oneoff = new OneoffTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(MyTaskService.class)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(GCM_ONEOFF_TAG)
                    //executed between 0 - 10s from now
                    .setExecutionWindow(10, 10)
                    .setExecutionWindow(
                            0 * DateUtils.MINUTE_IN_MILLIS, 15 * DateUtils.SECOND_IN_MILLIS)
                    //set required network state, this line is optional
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    //set some data we want to pass to our task
                    .setExtras(data)
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(oneoff);
            Log.v(TAG, "oneoff task scheduled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //AGENDAMENTO PARA ATUALIZACAO DE PRODUTOS E ESTOQUES E ENVIO DE PEDIDOS - 1 EM 1 MINUTO
    public static void scheduleRepeat(Context context) {
        long periodSecs = 60L; // the task should be executed every 1 MINUTO

        long flexSecs = 15L; // the task can run as early as -15 seconds from the scheduled time

        String repeat = "periodic  | " + taskID++ + ": " + periodSecs + "s, f:" + flexSecs;  // a unique task identifier
        //in this method, single Repeating task is scheduled (the target service that will be called is MyTaskService.class)
        Bundle data = new Bundle();
        data.putString("repeat", repeat);
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(MyTaskService.class)
                    //repeat every 60 seconds
                    .setPeriod(periodSecs)
                    //specify how much earlier the task can be executed (in seconds)
                    .setFlex(flexSecs)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(GCM_REPEAT_TAG)
                    //extras
                    .setExtras(data)
                    //whether the task persists after device reboot
                    .setPersisted(true)
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    //set required network state, this line is optional
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.v(TAG, "repeating task scheduled");
        } catch (Exception e) {
            Log.e(TAG, "scheduling failed");
            e.printStackTrace();
        }
    }

    //AGENDAMENTO RODA DE 30 EM 30 MIN PARA ATUALIZAR PARCEIROS E TABELA DE PREÇO
    public static void schedule30em30(Context context) {
        long periodSecs = 1800L; // the task should be executed every 1800 segundos

        long flexSecs = 15L; // the task can run as early as -15 seconds from the scheduled time

        String repeat = "periodic  | " + taskID++ + ": " + periodSecs + "s, f:" + flexSecs;  // a unique task identifier
        //in this method, single Repeating task is scheduled (the target service that will be called is MyTaskService.class)
        Bundle data = new Bundle();
        data.putString("30em30", repeat);
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(MyTaskService.class)
                    //repeat every 60 seconds
                    .setPeriod(periodSecs)
                    //specify how much earlier the task can be executed (in seconds)
                    .setFlex(flexSecs)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(GCM_30EM30_TAG)
                    //extras
                    .setExtras(data)
                    //whether the task persists after device reboot
                    .setPersisted(true)
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    //set required network state, this line is optional
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.v(TAG, "ativado job 30 em 30 min GCM_30EM30_TAG");
        } catch (Exception e) {
            Log.e(TAG, "job 30 em 30 min falhou");
            e.printStackTrace();
        }
    }

    //CANCELAMENTO DE TAREFA POR EXECUCAO
    public static void cancelOneOff(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(GCM_ONEOFF_TAG, MyTaskService.class);
    }

    //CANCELAMENTO DE TAREFA DE AGENDAMENTO
    public static void cancelRepeat(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(GCM_REPEAT_TAG, MyTaskService.class);
    }

    //CANCELAMENTO DE TAREFA DE AGENDAMENTO 30 em 30 min
    public static void cancel30em30(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(GCM_30EM30_TAG, MyTaskService.class);
    }

    //CANCELA TODAS AS TAREFAS
    public static void cancelAll(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelAllTasks(MyTaskService.class);
    }

    @Override
    public void onInitializeTasks() {
        //called when app is updated to a new version, reinstalled etc.
        //you have to schedule your repeating tasks again
        super.onInitializeTasks();
    }

    @Override
    public int onRunTask(final TaskParams taskParams) {
        //do some stuff (mostly network) - executed in background thread (async)
        //obtain your data
        //METODO CHAMADO PARA EXECUCAO TAREFAS
        Bundle extras = taskParams.getExtras();

        Handler h = new Handler(getMainLooper());
        Log.v(TAG, "onRunTask" + taskParams.getTag());

        if (taskParams.getTag().equals(GCM_REPEAT_TAG)) {
            final String execute = taskParams.getTag();
            final String extra = extras.get("repeat").toString();
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("MyTaskService", extra + "EXECUCAO TAREFA" + execute);
                    AgendaDAO agendaDAO = new AgendaDAO(getApplicationContext());
                    LinkedList<Agendamento> agendas = agendaDAO.getJobAgenda();

                    if (agendas.size() > 0) {
                        Log.d("TaskService", "Possui agendamentos" + agendas.size());
                        for (int i = 0; i < agendas.size(); i++) {
                            //SERA UMA ACAO DE LIGAR
                            Log.d("TaskService", "hora" + agendas.get(i).getHora() + "dhatual: " + agendas.get(i).getDhatual());
                            if (agendas.get(i).getOnoff().equals("S")) {

                                //IRA ligar O TODOS
                                if (agendas.get(i).getAcao().equals("0")) {
                                    NetworkComandos.getInstance(getApplicationContext()).execute("LIGARGERAL");
                                    agendaDAO.updateDhExeAgenda(agendas.get(i).getId().toString());
                                    EventBus eventBus = EventBus.getDefault();
                                    ListenerEB msg = new ListenerEB();
                                    msg.setClasseDestino(ComandFragment.class + "");
                                    msg.setMensagem("AGENDAMENTO");
                                    msg.setRetorno("ok");
                                    msg.setComando("LIGARGERAL");
                                    eventBus.post(msg);
                                }


                                //IRA ligar PLAY
                                if (agendas.get(i).getAcao().equals("1")) {
                                    NetworkComandos.getInstance(getApplicationContext()).execute("LIGARRELE01");
                                    agendaDAO.updateDhExeAgenda(agendas.get(i).getId().toString());
                                    EventBus eventBus = EventBus.getDefault();
                                    ListenerEB msg = new ListenerEB();
                                    msg.setClasseDestino(ComandosActivityRV.class + "");
                                    msg.setMensagem("AGENDAMENTO");
                                    msg.setRetorno("ok");
                                    msg.setComando("LIGARRELE01");
                                    eventBus.post(msg);
                                    tab.child("status").child("1").setValue(new Acao("TV/PLAYSTATION/DUOSAT", true, user.getUid(), 2, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));

                                }

                                //IRA ligar FILTRO
                                if (agendas.get(i).getAcao().equals("2")) {
                                    NetworkComandosCena2.getInstance(getApplicationContext()).execute("LIGARRELE01");
                                    agendaDAO.updateDhExeAgenda(agendas.get(i).getId().toString());
                                    EventBus eventBus = EventBus.getDefault();
                                    ListenerEB msg = new ListenerEB();
                                    msg.setClasseDestino(ComandosActivityRV.class + "");
                                    msg.setMensagem("AGENDAMENTO");
                                    msg.setRetorno("ok-cozinha");
                                    msg.setComando("LIGARRELE01");
                                    eventBus.post(msg);
                                    tab.child("status").child("7").setValue(new Acao("FILTRO",true,user.getUid(),8,dateFormat.format(Calendar.getInstance().getTime()),"Cozinha"));

                                }


                            } else {

                                //acao de desligar
                                //IRA DESLIGAR TODOS
                                if (agendas.get(i).getAcao().equals("0")) {
                                    NetworkComandos.getInstance(getApplicationContext()).execute("DESLIGARGERAL");
                                    agendaDAO.updateDhExeAgenda(agendas.get(i).getId().toString());
                                    EventBus eventBus = EventBus.getDefault();
                                    ListenerEB msg = new ListenerEB();
                                    msg.setClasseDestino(ComandosActivityRV.class + "");
                                    msg.setMensagem("AGENDAMENTO");
                                    msg.setRetorno("ok");
                                    msg.setComando("DESLIGARGERAL");
                                    eventBus.post(msg);
                                }

                                //IRA DESLIGAR O RELE 1 PLAY
                                if (agendas.get(i).getAcao().equals("1")) {
                                    NetworkComandos.getInstance(getApplicationContext()).execute("DESLIGARRELE01");
                                    agendaDAO.updateDhExeAgenda(agendas.get(i).getId().toString());
                                    EventBus eventBus = EventBus.getDefault();
                                    ListenerEB msg = new ListenerEB();
                                    msg.setClasseDestino(ComandosActivityRV.class + "");
                                    msg.setMensagem("AGENDAMENTO");
                                    msg.setRetorno("ok");
                                    msg.setComando("DESLIGARRELE01");
                                    eventBus.post(msg);
                                    tab.child("status").child("1").setValue(new Acao("TV/PLAYSTATION/DUOSAT", false, user.getUid(), 2, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));

                                }

                                //IRA DESLIGAR O RELE 1 FILTRO
                                if (agendas.get(i).getAcao().equals("2")) {
                                    NetworkComandosCena2.getInstance(getApplicationContext()).execute("DESLIGARRELE01");
                                    agendaDAO.updateDhExeAgenda(agendas.get(i).getId().toString());
                                    EventBus eventBus = EventBus.getDefault();
                                    ListenerEB msg = new ListenerEB();
                                    msg.setClasseDestino(ComandosActivityRV.class + "");
                                    msg.setMensagem("AGENDAMENTO");
                                    msg.setRetorno("ok-cozinha");
                                    msg.setComando("DESLIGARRELE01");
                                    eventBus.post(msg);
                                    tab.child("status").child("7").setValue(new Acao("FILTRO",false,user.getUid(),8,dateFormat.format(Calendar.getInstance().getTime()),"Cozinha"));

                                }

                            }

                        }
                    } else {
                        Log.d("TaskService", "Não Possui agendamentos" + agendas.size());
                    }


                }
            });
        }
        /*
        if (taskParams.getTag().equals(GCM_30EM30_TAG)) {
            final String execute = taskParams.getTag();
            final String extra = extras.get("30em30").toString();
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("MyTaskService", extra + "EXECUCAO TAREFA" + execute);

                    getExecTarefa30em30();

                }
            });
        } */
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}