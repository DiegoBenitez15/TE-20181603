package com.pf.proyectofinal.Servicios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FirebaseServicios {
    private static FirebaseStorage sref;

    public FirebaseServicios() {
        if(sref == null) {
            sref = FirebaseStorage.getInstance();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public Bitmap getBitmap(Context inContext,Uri inImageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(inContext.getContentResolver(), inImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String uploadFile(Uri imagUri,String url) {
        if (imagUri != null) {
            String ref;

            if(url == null) {
               ref = "android/media/" + imagUri.getLastPathSegment();
            }
            else{
                ref = url;
            }

            StorageReference imageRef = sref.getReference().child(ref);

            imageRef.putFile(imagUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            Task<Uri> downloadUri = imageRef.getDownloadUrl();
                            while (!downloadUri.isSuccessful());
                            Uri downloadUrl = downloadUri.getResult();
                            downloadUrl.toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // show message on failure may be network/disk ?
                        }
                    });
            return ref;
        }
        return null;
    }

    public void downloadImage(String url,ImageView img_view){
        StorageReference pathReference = sref.getReference().child(url);

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] b) {
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                img_view.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
