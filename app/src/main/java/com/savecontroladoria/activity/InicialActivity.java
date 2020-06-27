package com.savecontroladoria.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savecontroladoria.R;
import com.savecontroladoria.bancoDados.dao.AgendaDAO;
import com.savecontroladoria.bancoDados.dao.UsuarioDAO;
import com.savecontroladoria.network.Links;
import com.savecontroladoria.objetos.Usuario;
import com.savecontroladoria.tarefa.MyTaskService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicialActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = "InicialActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MyTaskService.cancelAll(getApplicationContext());
        MyTaskService.scheduleRepeat(getApplicationContext());

        Intent menu = null;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Usuario usuario = new Usuario();
        if (user != null) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
            usuario = usuarioDAO.getUsuario(user.getUid());
            if (usuario.getAcessoAcao().equals("S")) {
                menu = new Intent(InicialActivity.this, ComandosActivityRV.class);
                startActivity(menu);
            } else {
                return;
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicial, menu);
        MenuItem ativaradm = menu.findItem(R.id.ativaradm);
        updateMenuTitle(ativaradm);
        return true;
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private boolean checkAndRequestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        String ssid = "nada";
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;

    }

    public void updateMenuTitle(MenuItem item) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UsuarioDAO usuDAO = new UsuarioDAO(getApplicationContext());
        if (user != null) {
            Usuario userLogado = usuDAO.getUsuario(user.getUid());
            Log.d(TAG, "ENTROU MENU" + userLogado.getAdm());
            if (userLogado.getAdm() == null) {
                item.setTitle("ADM desativado");
            } else {
                if (userLogado.getAdm().equals("S")) {
                    item.setTitle("ADM ativado");
                    Log.d(TAG, "ENTROU NO ATIVADO");
                } else {
                    item.setTitle("ADM desativado");
                    Log.d(TAG, "ENTROU NO DESATIVADO");
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            MyTaskService.cancelAll(getApplicationContext());
            finish();

            return true;
        }


        if (id == R.id.ativaradm) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference("tabelas");
            final MenuItem ativaradm = item;
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(InicialActivity.this);
            View mview = getLayoutInflater().inflate(R.layout.acesso_admin, null);
            final EditText mUsuario = (EditText) mview.findViewById(R.id.useradmin);
            final EditText mSenhaadm = (EditText) mview.findViewById(R.id.senhaadmin);
            Button mConfirmaradm = (Button) mview.findViewById(R.id.confirmarAdm);
            Button mCancelaradm = (Button) mview.findViewById(R.id.cancelarAdm);
            mBuilder.setView(mview);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            mConfirmaradm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        if ((Links.getUserName().equals(mUsuario.getText().toString())) && (Links.getPassword().equals(mSenhaadm.getText().toString()))) {
                            if (ativaradm.getTitle().toString().equals("ADM desativado")) {
                                DatabaseReference usersRef = ref.child("usuario");
                                final Usuario usuario = new Usuario(user.getDisplayName().toString(), user.getEmail().toString(), "S", "S", "S", user.getUid());
                                Map<String, Usuario> users = new HashMap<String, Usuario>();
                                users.put(user.getUid(), new Usuario(user.getDisplayName().toString(), user.getEmail().toString(), "S", "S", "S", user.getUid()));
                                usersRef.setValue(users, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(getApplicationContext(), "Erro ao inserir" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG).show();
                                            UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
                                            Integer countusu = usuarioDAO.usuExiste(usuario.getUid());
                                            Log.d(TAG, "DESATIVADO = PARA ATIVAR" + countusu);
                                            if (countusu > 0) {
                                                usuarioDAO.updateUsuario(usuario);
                                            } else {
                                                usuarioDAO.insert(usuario);
                                            }
                                            dialog.cancel();
                                            ativaradm.setTitle("ADM ativado");
                                        }
                                    }
                                });
                            } else {
                                DatabaseReference usersRef = ref.child("usuario");
                                Map<String, Usuario> users = new HashMap<String, Usuario>();
                                final Usuario usuario = new Usuario(user.getDisplayName().toString(), user.getEmail().toString(), "N", "S", "S", user.getUid());
                                users.put(user.getUid(), new Usuario(user.getDisplayName().toString(), user.getEmail().toString(), "N", "S", "S", user.getUid()));
                                usersRef.setValue(users, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(getApplicationContext(), "Erro ao inserir" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG).show();
                                            UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
                                            Integer countusu = usuarioDAO.usuExiste(usuario.getUid());
                                            Log.d(TAG, "ATIVADO = PARA DESATIVAR" + countusu);
                                            if (countusu > 0) {
                                                usuarioDAO.updateUsuario(usuario);
                                            } else {
                                                usuarioDAO.insert(usuario);
                                            }
                                            dialog.cancel();
                                            ativaradm.setTitle("ADM desativado");
                                        }
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario e Senha de ADM não conferem", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "usuario" + Links.getUserName().equals(mUsuario.getText().toString()) + " senha" + Links.getPassword().equals(mSenhaadm.getText().toString()));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario não está logado, verificar", Toast.LENGTH_LONG).show();
                    }
                }
            });

            mCancelaradm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "btncancelar");
                    dialog.cancel();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent menu = null;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Usuario usuario = new Usuario();
        if (user != null) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
            usuario = usuarioDAO.getUsuario(user.getUid());
        }
        if (id == R.id.nav_camera) {
            if (usuario.getAcessoAcao().equals("S")) {
                menu = new Intent(InicialActivity.this, ComandosActivityRV.class);
            } else {
                Toast.makeText(this, "Usuário sem Acesso", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_gallery) {
            if (usuario.getAcessoJob().equals("S")) {
                menu = new Intent(InicialActivity.this, AgendaActivity.class);
            } else {
                AgendaDAO agendaDAO = new AgendaDAO(getApplicationContext());
                agendaDAO.deleteAgendaUID(user.getUid());
                Toast.makeText(this, "Usuário sem Acesso", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_slideshow) {
            menu = new Intent(InicialActivity.this, ConfigActivity.class);
        } else if (id == R.id.nav_manage) {

        }
        if (menu == null) {
            Toast.makeText(getApplicationContext(), "NAO FOI IMPLEMENTADO", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(menu);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //FirebaseAuth.getInstance().signOut();
        //LoginManager.getInstance().logOut();
    }
}
