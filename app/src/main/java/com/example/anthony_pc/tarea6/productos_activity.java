package com.example.anthony_pc.tarea6;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class productos_activity extends AppCompatActivity {

    ListView lv;
    public final ArrayList<Producto> listaProductos = new ArrayList<>();
    int cont = 0;

    DatabaseReference databaseProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_activity);

        lv = findViewById(R.id.listView);

        databaseProductos = FirebaseDatabase.getInstance().getReference("productos");

        //populateLv(this);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int idProducto = position;
                new android.support.v7.app.AlertDialog.Builder(productos_activity.this)
                        .setTitle("Alerta")
                        .setMessage("Desea eliminar el producto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = listaProductos.get(idProducto).getId();FirebaseDatabase database = FirebaseDatabase.getInstance();
                                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database1.getReference("productos");
                                myRef.child(id).removeValue();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return false;
            }
        });
        new downloadImages().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();

        /*databaseProductos.addValueEventListener(new ValueEventListener() {
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
        Log.e("adf",String.valueOf(MainActivity.listaProductos.size()));
        adapter = new adapter(MainActivity.listaProductos,this);
        lv.setAdapter(adapter);
*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(productos_activity.this, MainActivity.class));
        finish();
    }

    public void agregar_productoActivity(View view){
        startActivity(new Intent(productos_activity.this, agregar_producto.class));
        finish();
    }

    public void populateLv(Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("productos");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    Log.e("asdf",ds.child("precio").getValue().toString());

                    String nombre = ds.child("nombre").getValue().toString();
                    String precio = ds.child("precio").getValue().toString();
                    String descripcion = ds.child("descripcion").getValue().toString();

                    listaProductos.add(new Producto(ds.getKey(),nombre,Integer.valueOf(precio),descripcion));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public class downloadImages extends AsyncTask<Void, Integer, String> {
        public String url = "https://firebasestorage.googleapis.com/v0/b/tarea6-d34b6.appspot.com/o/";
        String url2 = "?alt=media&token=eb71b096-c31e-4b8c-95f9-447b6d3fb8ac";
        //ArrayList<Producto> lista = ;
        adapter adapter;
        @Override
        protected String doInBackground(Void... voids) {

            try {
                for (Producto i : MainActivity.listaProductos) {
                    Bitmap resultado = null;
                    URL link = new URL(url + i.getNombre() + url2);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    resultado = BitmapFactory.decodeStream(inputStream);

                    MainActivity.listaImages.add(resultado);

                    i.setFoto(resultado);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("largo123",String.valueOf(MainActivity.listaProductos.size()));
            adapter = new adapter(MainActivity.listaProductos,productos_activity.this);
            Log.e("largo123",String.valueOf(MainActivity.listaProductos.size()));
            lv.setAdapter(adapter);


        }
    }



}
