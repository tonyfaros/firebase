package com.example.anthony_pc.tarea6;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    String TAG = "";

    EditText user,password;
    private FirebaseAuth mAuth;
    public static ArrayList<Producto> listaProductos = new ArrayList<>();
    private FirebaseAuth.AuthStateListener listener;
    DatabaseReference databaseProductos;
    public static ArrayList<Bitmap> listaImages = new ArrayList<>();

    adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = findViewById(R.id.user);
        password = findViewById(R.id.pass);

        databaseProductos = FirebaseDatabase.getInstance().getReference("productos");

        user.setText("tonyfaros@gmail.com");
        password.setText("123456");


        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    startActivity(new Intent(MainActivity.this, productos_activity.class));
                    finish();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mAuth.addAuthStateListener(listener);

            databaseProductos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listaProductos.clear();
                    for (DataSnapshot productoSnapshot : dataSnapshot.getChildren()){
                        String nombre = productoSnapshot.child("nombre").getValue().toString();
                        String precio = productoSnapshot.child("precio").getValue().toString();
                        String descripcion = productoSnapshot.child("descripcion").getValue().toString();

                        listaProductos.add(new Producto(dataSnapshot.getKey(),nombre,Integer.valueOf(precio),descripcion));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*adapter = new adapter(listaProductos,this);
            lv.setAdapter(adapter);*/


    }

    private void updateUI(FirebaseUser currentUser) {

    }

    public void registrarUser(View view){
        if(user.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_SHORT).show();
            user.setText("");
            password.setText("");
        }else {
            checkEmail(user.getText().toString());
        }
    }

    private void createUser(){

            mAuth.createUserWithEmailAndPassword(user.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

    }

    public void signIn(View view){
        if(user.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(this,"Ingrese los datos",Toast.LENGTH_SHORT).show();
            user.setText("");
            password.setText("");
        }
        else {
            mAuth.signInWithEmailAndPassword(user.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(),productos_activity.class);
                                startActivity(intent);
                                finish();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    private void checkEmail(String email){

        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                boolean check = !task.getResult().getProviders().isEmpty();

                if(!check){
                    createUser();
                }else{
                    Toast.makeText(getApplicationContext(),"Email ya registrado, puede ingresar al sistema",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}


