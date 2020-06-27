package com.savecontroladoria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.savecontroladoria.Interface.RVHackConsultaAgenda;
import com.savecontroladoria.R;
import com.savecontroladoria.adapter.AgendaListAdapter;
import com.savecontroladoria.bancoDados.dao.AgendaDAO;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.objetos.Agendamento;

import java.util.LinkedList;

import de.greenrobot.event.EventBus;



public class AgendaActivity extends AppCompatActivity implements RVHackConsultaAgenda {
    RecyclerView mRecyclerView;
    AgendaListAdapter mAdapter;
    private LinkedList<Agendamento> mListaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(AgendaActivity.this);
        setContentView(R.layout.activity_agenda);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListaux = new LinkedList<Agendamento>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_agenda);
        mRecyclerView.setHasFixedSize(true);
        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mListaux.clear();
        AgendaDAO agendaDAO = new AgendaDAO(getApplicationContext());

        mListaux = agendaDAO.getAgendamentos();
        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new AgendaListAdapter(this, mListaux);


        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddAgenda);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAgenda = new Intent(AgendaActivity.this, AddAgendaActivity.class);
                startActivity(addAgenda);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mAdapter = new AgendaListAdapter(this, mListaux);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRVHackConsultaAgenda(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(ListenerEB listenerEB) {
        if (!listenerEB.getClasseDestino().equalsIgnoreCase(AgendaActivity.class + "")) {
            return;
        } else {
            if (listenerEB.getMensagem().equals("ATUALIZALISTA")) {
                mAdapter.addListItem(listenerEB.getAgendamento(), mAdapter.getItemCount());
            }
        }
    }


    @Override
    public void onClickListener(View view, int position) {
        Log.i("AgendaActivity", "click - posicao" + position);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Log.i("AgendaActivity", "Long - posicao" + position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(AgendaActivity.this);
    }
}
