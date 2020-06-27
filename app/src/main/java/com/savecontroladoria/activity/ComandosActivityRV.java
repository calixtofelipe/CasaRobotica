package com.savecontroladoria.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.database.ValueEventListener;
import com.savecontroladoria.R;
import com.savecontroladoria.adapter.ComodosListAdapter;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.objetos.Acao;
import com.savecontroladoria.objetos.Comodo;
import com.savecontroladoria.util.VerificaConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ComandosActivityRV extends AppCompatActivity {
    private static ComandosActivityRV instance;
    private View mProgressView;
    private ComodosListAdapter comodosListAdapter;
    private RecyclerView recyclerView;
    private List<Comodo> comodoList;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference tab = database.getReference("tabelas");
    private static final String TAG = "ComandosActRV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comandosrv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(ComandosActivityRV.this);

        mProgressView = findViewById(R.id.pbcomandrv);
        recyclerView = (RecyclerView) findViewById(R.id.rvComandos);
        mProgressView.setVisibility(View.VISIBLE);
        if(VerificaConnection.isConnected(getApplicationContext())) {
            getListAcoes();
        } else {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final ArrayList<Comodo> acoes = new ArrayList<>();
            List<Acao> salaList = new ArrayList<>();
            salaList.add(new Acao("TODAS AUTOMAÇÕES", false, user.getUid(), 1, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
            salaList.add(new Acao("TV/PLAYSTATION/DUOSAT", false, user.getUid(), 2, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
            salaList.add(new Acao("LUZ SALA TV", false, user.getUid(), 3, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
            salaList.add(new Acao("LUZ SALA", false, user.getUid(), 4, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
            salaList.add(new Acao("LUZ HALL", false, user.getUid(), 5, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
            acoes.add(new Comodo("Sala", salaList));

            List<Acao> cozinhaList = new ArrayList<>();
            cozinhaList.add(new Acao("TODAS AUTOMAÇÕES", false, user.getUid(), 6, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
            cozinhaList.add(new Acao("FOGÃO", false, user.getUid(), 7, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
            cozinhaList.add(new Acao("FILTRO", false, user.getUid(), 8, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
            cozinhaList.add(new Acao("MAQUINA DE LAVAR", false, user.getUid(), 9, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
            cozinhaList.add(new Acao("MICROONDAS", false, user.getUid(), 10, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
            cozinhaList.add(new Acao("LUZ", false, user.getUid(), 11, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
            acoes.add(new Comodo("Cozinha", cozinhaList));
            mProgressView.setVisibility(View.GONE);
            comodoList = acoes;

            comodosListAdapter = new ComodosListAdapter(comodoList, getApplicationContext());

            recyclerView.setLayoutManager(new LinearLayoutManager(ComandosActivityRV.this));
            recyclerView.setAdapter(comodosListAdapter);
        }



    }


    public void getListAcoes() {

        final ArrayList<Comodo> acoes = new ArrayList<>();
        DatabaseReference liststatus = FirebaseDatabase.getInstance().getReference().child("tabelas").child("status");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<Acao> todos = new ArrayList<>();
            liststatus.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "ENTROU AQUI ONDATACHANGE");
                    if (dataSnapshot.exists()) {
                        List<Acao> salaList = new ArrayList<>();
                        List<Acao> cozinhaList = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Acao action = postSnapshot.getValue(Acao.class);
                            Log.d(TAG, "RETORNO-ACAO" + action.getComodo());
                            if (action.getComodo().equals("Sala")) {
                                Log.d(TAG, "RETORNO-ACAO-ENTROU SALA" + action.getComodo());
                                salaList.add(action);
                            }
                            if (action.getComodo().equals("Cozinha")) {
                                cozinhaList.add(action);
                            }
                        }
                        acoes.add(new Comodo("Sala", salaList));
                        acoes.add(new Comodo("Cozinha", cozinhaList));
                        mProgressView.setVisibility(View.GONE);
                        comodoList = acoes;

                        comodosListAdapter = new ComodosListAdapter(comodoList, getApplicationContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(ComandosActivityRV.this));
                        recyclerView.setAdapter(comodosListAdapter);

                    } else {
                        List<Acao> salaList = new ArrayList<>();
                        salaList.add(new Acao("TODAS AUTOMAÇÕES", false, user.getUid(), 1, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                        salaList.add(new Acao("TV/PLAYSTATION/DUOSAT", false, user.getUid(), 2, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                        salaList.add(new Acao("LUZ SALA TV", false, user.getUid(), 3, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                        salaList.add(new Acao("LUZ SALA", false, user.getUid(), 4, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                        salaList.add(new Acao("LUZ HALL", false, user.getUid(), 5, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                        acoes.add(new Comodo("Sala", salaList));

                        List<Acao> cozinhaList = new ArrayList<>();
                        cozinhaList.add(new Acao("TODAS AUTOMAÇÕES", false, user.getUid(), 6, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
                        cozinhaList.add(new Acao("FOGÃO", false, user.getUid(), 7, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
                        cozinhaList.add(new Acao("FILTRO", false, user.getUid(), 8, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
                        cozinhaList.add(new Acao("MAQUINA DE LAVAR", false, user.getUid(), 9, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
                        cozinhaList.add(new Acao("MICROONDAS", false, user.getUid(), 10, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
                        cozinhaList.add(new Acao("LUZ", false, user.getUid(), 11, dateFormat.format(Calendar.getInstance().getTime()), "Cozinha"));
                        acoes.add(new Comodo("Cozinha", cozinhaList));


                        todos.addAll(salaList);
                        todos.addAll(cozinhaList);
                        DatabaseReference tabela = tab.child("status");
                        tabela.setValue(todos, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    mProgressView.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Erro ao inserir" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    mProgressView.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        mProgressView.setVisibility(View.GONE);
                        comodoList = acoes;

                        comodosListAdapter = new ComodosListAdapter(comodoList, getApplicationContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(ComandosActivityRV.this));
                        recyclerView.setAdapter(comodosListAdapter);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mProgressView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Cancelada Ação de consulta",Toast.LENGTH_LONG).show();
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

    public void onEvent(ListenerEB listenerEB) {
        if (!listenerEB.getClasseDestino().equalsIgnoreCase(ComandosActivityRV.class + "")) {
            return;
        } else {
            if (listenerEB.getMensagem().equals("PROGRESSBARENABLE")) {
                mProgressView.setVisibility(View.VISIBLE);
            }
            if (listenerEB.getMensagem().equals("PROGRESSBARDISABLE")) {
                mProgressView.setVisibility(View.GONE);
            }
            if (!(listenerEB.getRetorno() == null)) {
                Log.d(TAG, listenerEB.getRetorno()+"retorno agendamento"+listenerEB.getComando());
                comodosListAdapter.trocaStatusBotoes(listenerEB.getComando(), listenerEB.getRetorno());
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(ComandosActivityRV.this);
    }

}
