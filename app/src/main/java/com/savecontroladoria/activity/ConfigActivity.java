package com.savecontroladoria.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savecontroladoria.R;
import com.savecontroladoria.adapter.UsuarioListAdapter;
import com.savecontroladoria.bancoDados.dao.UsuarioDAO;
import com.savecontroladoria.objetos.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = "ConfigActivity";

    RecyclerView mRecyclerView;
    UsuarioListAdapter mAdapter;
    private LinkedList<Usuario> mListaux;
    Button btnConfok;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference("tabelas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListaux = new LinkedList<Usuario>();
        mRecyclerView = (RecyclerView) findViewById(R.id.rvUsuarios);
        mRecyclerView.setHasFixedSize(true);
        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mListaux.clear();
        final UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
        mListaux = usuarioDAO.getUsuarios();
        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        Log.d(TAG,mListaux.size()+"tamanho1"+mListaux);
        mAdapter = new UsuarioListAdapter(this, mListaux);


        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mRecyclerView.setAdapter(mAdapter);
        mAdapter = new UsuarioListAdapter(this, mListaux);
        mRecyclerView.setAdapter(mAdapter);

        btnConfok = (Button) findViewById(R.id.btnConfok);
        btnConfok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DADOS-" + mListaux.get(0).getAcessoAcao());
                DatabaseReference usersRef = ref.child("usuario");
                Map<String, Usuario> users = new HashMap<String, Usuario>();
                LinkedList<Usuario> usuarios = mAdapter.getmDataset();
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    UsuarioDAO usuDAO = new UsuarioDAO(getApplicationContext());
                    usuDAO.updateUsuario(usuarios.get(i));
                    users.put(usuarios.get(i).getUid(), usuarios.get(i));
                }
                usersRef.setValue(users, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(getApplicationContext(), "Erro ao inserir" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        DatabaseReference listusuario = FirebaseDatabase.getInstance().getReference().child("tabelas").child("usuario");
        listusuario.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Log.d(TAG,"DADOS"+dataSnapshot.getValue());
                        //collectUsers((Map<String,Object>) dataSnapshot.getValue());
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Usuario user = postSnapshot.getValue(Usuario.class);
                            Log.d(TAG,"filho"+ user.getNomeusu());
                            Integer countusu = usuarioDAO.usuExiste(user.getUid());
                            Log.d(TAG, user.getUid()+"LOGIN IRA INSERIR USUARIO" + countusu);
                            if (countusu == 0) {
                                usuarioDAO.insert(user);
                            } else {
                                usuarioDAO.updateUsuario(user);
                            }
                        }
                        mListaux = usuarioDAO.getUsuarios();
                        mAdapter.refreshItens(mListaux);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    private void collectUsers(Map<String,Object> users) {

        ArrayList<String> userdados = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            userdados.add((String) singleUser.get("email"));
        }

        Log.d(TAG,userdados.toString());
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



}
