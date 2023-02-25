package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int SELECT_FILE = 1;
    Button btnSeleccionarFoto, btnEnviarFoto;
    Uri selectedImage;
ImageView imgFoto;
String rutaImagenes;
private static final int REQUEST_CODIGO_CAMARA=200;
private static final int REQUEST_CODIGO_CAPTURAR_IMAGEN=300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgFoto = findViewById(R.id.imgFoto);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto);

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentW = new Intent(Intent.ACTION_SEND);
                intentW.setType("image/*");
                intentW.setPackage("com.whatsapp");

                if (selectedImage != null) {
                    intentW.putExtra(Intent.EXTRA_STREAM, selectedImage);

                    try {
                        startActivity(intentW);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error al enviar\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No se selecciono una imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void abrirGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                SELECT_FILE);
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    String selectedPath=selectedImage.getPath();
                    if (requestCode == SELECT_FILE) {

                        if (selectedPath != null) {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                            // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                            ImageView mImg = (ImageView) findViewById(R.id.imgFoto);
                            mImg.setImageBitmap(bmp);

                        }
                    }
                }
                break;
        }
    }
}