package com.example.anthony_pc.tarea6;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;

public class agregar_producto extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    EditText nombre, descripcion, precio;
    ImageView imageView;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filepath;
    StorageReference productReference;
    private DatabaseReference databaseReference;

    //atabaseReference databaseReference;
    int cont = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        nombre = findViewById(R.id.nombre);
        descripcion = findViewById(R.id.descripcion);
        precio = findViewById(R.id.precio);
        imageView = findViewById(R.id.imageView);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");
        //productReference = storageReference.child("Product");

        int permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void addImagen(View view) {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeriaIntent,RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK){

                filepath = data.getData();

                /*String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imagenSeleccionada,filePath,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();*/
                Bitmap bit = null;
                try {
                    bit = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    imageView.setImageBitmap(bit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*BitmapFactory.decodeFile(picturePath);
                imageView.setImageBitmap(bit);
                String fileNameSegments[] = picturePath.split("/");

                String fileName = fileNameSegments[fileNameSegments.length -1];
*/

        }
    }

    public void addProducto(View view){
        String nom = nombre.getText().toString();
        String pre = precio.getText().toString();
        String des = descripcion.getText().toString();
        //Bitmap bit = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Producto p = new Producto(nom,Integer.valueOf(pre),des);
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(p);


        uplImage(nom);

        Toast.makeText(this,"Agregado",Toast.LENGTH_SHORT).show();

        startActivity(new Intent(agregar_producto.this,productos_activity.class));
        finish();

    }

    public void uplImage(String nom){
        if(filepath != null) {

            StorageReference childRef = storageReference.child(nom);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filepath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(agregar_producto.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(agregar_producto.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
