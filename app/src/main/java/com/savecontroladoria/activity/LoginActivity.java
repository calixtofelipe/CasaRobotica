package com.savecontroladoria.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savecontroladoria.R;
import com.savecontroladoria.bancoDados.dao.UsuarioDAO;
import com.savecontroladoria.objetos.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference("tabelas");
    private FirebaseAuth firebaseAuth;

    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private final static String TAG = "LoginActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.pbLogin);
        progressBar.setVisibility(View.GONE);
        //hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.savecontroladoria",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        // Set up the login form.
        firebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(getApplicationContext(),"Erro ao logar Fabebook - Sem conexao internet",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
            final Usuario usuario = new Usuario(user.getDisplayName().toString(), user.getEmail().toString(), "N", "S", "S", user.getUid());
            Integer countusu = usuarioDAO.usuExiste(usuario.getUid());
            Log.d(TAG, "LOGIN IRA INSERIR USUARIO" + countusu);
            if (countusu > 0) {
                usuarioDAO.updateUsuario(usuario);
            } else {
                usuarioDAO.insert(usuario);
            }
            Intent inicial = new Intent(LoginActivity.this, InicialActivity.class);
            startActivity(inicial);
            finish();
        }

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Falha na autenticação",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                Log.d(TAG, "já logado pelo facebook:" + user.getEmail());
                                DatabaseReference usersRef = ref.child("usuario").child(user.getUid());

                                final Usuario usuario = new Usuario(user.getDisplayName().toString(), user.getEmail().toString(), "N", "S", "S", user.getUid());
                                usersRef.setValue(usuario, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(getApplicationContext(), "Erro ao inserir" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG).show();
                                            UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
                                            Integer countusu = usuarioDAO.usuExiste(usuario.getUid());
                                            Log.d(TAG, "LOGIN IRA INSERIR USUARIO" + countusu);
                                            if (countusu > 0) {
                                                usuarioDAO.updateUsuario(usuario);
                                            } else {
                                                usuarioDAO.insert(usuario);
                                            }
                                        }
                                    }
                                });
                            }

                            Intent inicial = new Intent(LoginActivity.this, InicialActivity.class);
                            startActivity(inicial);
                            finish();
                        }


                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

