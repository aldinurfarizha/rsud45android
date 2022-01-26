package com.ali.rsud45.Auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.ali.rsud45.Config.ExpiredSession;
import com.ali.rsud45.Config.ServerUrl;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.R;
import com.ali.rsud45.RegisterPoli.DaftarPoli;
import com.ali.rsud45.Util.ImageUtils;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaftarBaru extends AppCompatActivity {
EditText nama, nik, tempat_lahir, tanggal_lahir, dusun, rt, rw, no_telp, nama_ibu_kandung;
Spinner  provinsi, kabupaten, kecamatan, jenis_kelamin, pekerjaan, agama, pendidikan, status_martial;
ProgressDialog dialog;
ImageView kk_image;
Bitmap bitmap, bitmap2;
File file, destFile, kkfile, ktpfile, f;
Uri imageCaptureUri;
String provinsi_id, kabupaten_id, kecamatan_id, currentPhotoPath, message;
Button daftar;
List<String> ArrayProvinsi = new ArrayList<String>();
List<String> ArrayProvinsiId = new ArrayList<String>();
List<String> ArrayKabupaten = new ArrayList<String>();
List<String> ArrayKabupatenId = new ArrayList<String>();
List<String> ArrayKecamatan = new ArrayList<String>();
List<String> ArrayKecamatanId = new ArrayList<String>();
long timeMilli;
private DatePickerDialog datePickerDialog;
private SimpleDateFormat dateFormatter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int PICK_CAMERA_IMAGE = 0;
    private static final int PICK_CAMERA_IMAGE2 = 2;
    public static final String IMAGE_DIRECTORY = "RSUD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_baru);
        nama=(EditText)findViewById(R.id.nama);
        nik=(EditText)findViewById(R.id.nik);
        tempat_lahir=(EditText)findViewById(R.id.tempat_lahir);
        tanggal_lahir=(EditText)findViewById(R.id.tanggal_lahir);
        dusun=(EditText)findViewById(R.id.dusun);
        rt=(EditText)findViewById(R.id.rt);
        rw=(EditText)findViewById(R.id.rw);
        no_telp=(EditText)findViewById(R.id.no_telepon);
        nama_ibu_kandung=(EditText)findViewById(R.id.nama_ibu_kandung);
        provinsi=(Spinner)findViewById(R.id.provinsi);
        kabupaten=(Spinner)findViewById(R.id.kabupaten);
        kecamatan=(Spinner)findViewById(R.id.kecamatan);
        jenis_kelamin=(Spinner)findViewById(R.id.jenis_kelamin);
        pekerjaan=(Spinner)findViewById(R.id.pekerjaan);
        agama=(Spinner)findViewById(R.id.agama);
        pendidikan=(Spinner)findViewById(R.id.pendidikan);
        status_martial=(Spinner)findViewById(R.id.status_martial);
        kk_image=(ImageView)findViewById(R.id.kkimage);
        daftar=(Button)findViewById(R.id.daftar);
        Date dater = new Date();
        timeMilli = dater.getTime();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dialog=new ProgressDialog(DaftarBaru.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        AndroidNetworking.initialize(getApplicationContext());
        kabupaten.setEnabled(false);
        kabupaten.setClickable(false);
        kecamatan.setEnabled(false);
        kecamatan.setClickable(false);
        checkAndRequestPermissions();
        get_provinsi();
        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(DaftarBaru.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tanggal_lahir.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
        provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provinsi_id= ArrayProvinsiId.get(i);
                if(provinsi_id.equals("0")){
                    return;
                }
                get_kabupaten(provinsi_id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        kabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten_id= ArrayKabupatenId.get(i);
                if(kabupaten_id.equals("0")){
                    return;
                }
                get_kecamatan(kabupaten_id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kecamatan_id= ArrayKecamatanId.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        kk_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(DaftarBaru.this);
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }
    public void get_provinsi(){
        dialog.show();
        AndroidNetworking.post(ServerUrl.PROVINSI)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray array = jsonObject.getJSONArray("data");
                            ArrayProvinsi = new ArrayList<String>();
                            ArrayProvinsiId = new ArrayList<String>();
                            ArrayProvinsi.add("--Pilih Provinsi--");
                            ArrayProvinsiId.add("0");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                ArrayProvinsi.add(ob.getString("name"));
                                ArrayProvinsiId.add(ob.getString("id"));
                            }
                            load_spinner_provinsi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error.toString());
                    }
                });
    }
    public void load_spinner_provinsi(){
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ArrayProvinsi);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinsi.setAdapter(adapters);
    }
    public void get_kabupaten(String province_id){
        dialog.show();
        AndroidNetworking.post(ServerUrl.KABUPATEN)
                .addBodyParameter("province_id", province_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        kabupaten.setEnabled(true);
                        kabupaten.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray array = jsonObject.getJSONArray("data");
                            ArrayKabupaten = new ArrayList<String>();
                            ArrayKabupatenId = new ArrayList<String>();
                            ArrayKabupaten.add("--Pilih Kabupaten--");
                            ArrayKabupatenId.add("0");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                ArrayKabupaten.add(ob.getString("name"));
                                ArrayKabupatenId.add(ob.getString("id"));
                            }
                            load_spinner_kabupaten();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error.toString());
                    }
                });
    }
    public void load_spinner_kabupaten(){
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ArrayKabupaten);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kabupaten.setAdapter(adapters);
    }
    public void get_kecamatan(String regencies_id){
        dialog.show();
        AndroidNetworking.post(ServerUrl.KECAMATAN)
                .addBodyParameter("regency_id", regencies_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        kecamatan.setEnabled(true);
                        kecamatan.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray array = jsonObject.getJSONArray("data");
                            ArrayKecamatan = new ArrayList<String>();
                            ArrayKecamatanId = new ArrayList<String>();
                            ArrayKecamatan.add("--Pilih Kecamatan--");
                            ArrayKecamatanId.add("0");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                ArrayKecamatan.add(ob.getString("name"));
                                ArrayKecamatanId.add(ob.getString("id"));
                            }
                            load_spinner_kecamatan();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error.toString());
                    }
                });
    }
    public void load_spinner_kecamatan(){
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ArrayKecamatan);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kecamatan.setAdapter(adapters);
    }
    private void selectImage(Context context) {
        final CharSequence[] options = { "Ambil Foto","Pilih Dari Gallery", "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Upload Foto KK");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Ambil Foto")) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(DaftarBaru.this, Manifest.permission.CAMERA))
                    {
                        Toast.makeText(getApplicationContext(), "You did not give Camera permission or File Access, so go to Settings and give permission (Exituscommerce)", Toast.LENGTH_SHORT).show();

                    } else {
                        capture_image();
                        // Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        //startActivityForResult(takePicture, 0);
                    }


                } else if (options[item].equals("Pilih Dari Gallery")) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(DaftarBaru.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                    {
                        Toast.makeText(getApplicationContext(), "You did not give Camera permission or File Access, so go to Settings and give permission (ExitusCommerce)", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }


                } else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void capture_image() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            Log.d("camera", "a");
            File photoFile = null;
            try {
                photoFile = createImageFilekk();
            } catch (IOException ex) {
                Log.d("camera", "" + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("camera", "B");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.exitus.rsud45.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_CAMERA_IMAGE);
            }
        }
    }
    private File createImageFilekk() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        kkfile = new File(currentPhotoPath);
        Log.d("DESTFILE", "createImageFile: "+currentPhotoPath);
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK) {
                        // bitmap = (Bitmap) data.getExtras().get("data");
                        //imageView.setImageBitmap(bitmap);
                        //keterangan_foto.setVisibility(View.GONE);
                        Log.d("asd" + ".PICK_CAMERA_IMAGE", "Selected image uri path :" + imageCaptureUri);
                        File f = new File(currentPhotoPath);
                        bitmap = ImageUtils.getInstant().getCompressedBitmap(currentPhotoPath);
                        kk_image.setImageBitmap(bitmap);
                        Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        this.sendBroadcast(mediaScanIntent);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Log.d("picture path:", "picturepath"+picturePath);
                                f = new File(picturePath);
                                kkfile = new File(picturePath);
                                bitmap = decodeFile(f);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                        matrix, true);
                                kk_image.setImageBitmap(bitmap);


                                destFile = new File("img_firsttf"+timeMilli+ ".png");
                                imageCaptureUri = Uri.fromFile(destFile);

                                cursor.close();

                            }
                        }

                    }
                    break;

            }
        }
    }
    private Bitmap decodeFile(File f) {
        Bitmap b = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int IMAGE_MAX_SIZE = 800;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        destFile = new File("img_kk"+timeMilli+ ".png");
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    private  boolean checkAndRequestPermissions() {
        int Camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int File = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (File != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (File != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (Camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void sendData(){
        final String namas;
        final String niks;
        final String tmplahirs;
        final String tgllahirs;
        String kelamins;
        final String alamats;
        final String rts;
        final String rws;
        final String provinsis;
        final String kabupatens;
        final String kecamatans;
        final String nohps;
        final String pekerjaans;
        final String agamas;
        final String pendidikans;
        final String ibu_kandungs;
        final String status_martials;
        namas=nama.getText().toString();
        niks=nik.getText().toString();
        tmplahirs=tempat_lahir.getText().toString();
        tgllahirs=tanggal_lahir.getText().toString();
        kelamins=""+jenis_kelamin.getSelectedItemPosition();
        alamats=dusun.getText().toString();
        rts=rt.getText().toString();
        rws=rw.getText().toString();
        provinsis=provinsi_id;
        kabupatens=kabupaten_id;
        kecamatans=kecamatan_id;
        nohps=no_telp.getText().toString();
        pekerjaans=""+pekerjaan.getSelectedItemPosition();
        agamas=""+agama.getSelectedItemPosition();
        pendidikans=""+pendidikan.getSelectedItemPosition();
        ibu_kandungs=nama_ibu_kandung.getText().toString();
        status_martials=status_martial.getSelectedItem().toString();


        if(TextUtils.isEmpty(namas)){
            nama.requestFocus();
            nama.setError("Kolom Ini Wajib di isi");
            return;
        }
        if(TextUtils.isEmpty(niks)){
            nik.requestFocus();
            nik.setError("Kolom ini wajib di isi");
            return;
        }
        if(niks.length()<15){
            nik.requestFocus();
            nik.setError("Nomor KK Minimal 15 angka");
            return;
        }
        if(TextUtils.isEmpty(tmplahirs)){
            tempat_lahir.requestFocus();
            tempat_lahir.setError("Kolom ini wajib di isi");
            return;
        }
        if(TextUtils.isEmpty((tgllahirs))){
            tanggal_lahir.requestFocus();
            tanggal_lahir.setError("Kolom ini wajib di isi");
            return;
        }
        if(kelamins.equals("0")){
            openSnackbarMerah("Jenis Kelamin Harus di isi");
            return;
        }
        switch (kelamins){
            case "1":
                kelamins="L";
                break;
            case "2":
                kelamins="P";
        }
        if(TextUtils.isEmpty(alamats)){
            dusun.requestFocus();
            dusun.setError("Kolom ini wajib di isi");
            return;
        }
        if(TextUtils.isEmpty(rts)){
            rt.requestFocus();
            rt.setError("Kolom ini wajib di isi");
            return;
        }
        if(TextUtils.isEmpty(rws)){
            rw.requestFocus();
            rw.setError("Kolom ini wajib di isi");
            return;
        }
        if(provinsis.equals("0")){
            openSnackbarMerah("Provinsi Wajib di isi");
            return;
        }
        if(kabupatens.equals(("0"))){
            openSnackbarMerah("Kabupaten wajib di isi");
            return;
        }
        if(kecamatans.equals("0")){
            openSnackbarMerah("Kecamatan Wajib di isi");
            return;
        }
        if(TextUtils.isEmpty(nohps)){
            no_telp.requestFocus();
            no_telp.setError("Nomor Telepon wajib di isi");
            return;
        }
        if(nohps.length()<9){
            no_telp.requestFocus();
            no_telp.setError("Nomor Telepon minimal 9 Angka");
        }
        if(pekerjaans.equals("0")){
            openSnackbarMerah("Pekerjaan Wajib di isi");
            return;
        }
        if(agamas.equals("0")){
            openSnackbarMerah("Agama wajib di isi");
            return;
        }
        if(pendidikans.equals("0")){
            openSnackbarMerah("Pendidikan wajib di isi");
            return;
        }
        if(TextUtils.isEmpty(ibu_kandungs)){
            nama_ibu_kandung.requestFocus();
            nama_ibu_kandung.setError("Kolom ini wajib di isi");
            return;
        }
        if(status_martials.equals("--Pilih Status--")){
            openSnackbarMerah("Status Martial wajib di isi");
            return;
        }
        if(kkfile==null){
            openSnackbarMerah("Wajib melampirkan foto KK");
            return;
        }
        AndroidNetworking.upload(ServerUrl.REGISTRASI_PASIEN)
                .addMultipartFile("file",kkfile)
                .addMultipartParameter("nama",namas)
                .addMultipartParameter("nik",niks)
                .addMultipartParameter("tmplahir",tmplahirs)
                .addMultipartParameter("tgllahir",tgllahirs)
                .addMultipartParameter("kelamin",kelamins)
                .addMultipartParameter("alamat",alamats)
                .addMultipartParameter("rt",rts)
                .addMultipartParameter("rw",rws)
                .addMultipartParameter("provinces_id",provinsis)
                .addMultipartParameter("regencies_id",kabupatens)
                .addMultipartParameter("districts_id",kecamatans)
                .addMultipartParameter("nohp",nohps)
                .addMultipartParameter("pekerjaan_id",pekerjaans)
                .addMultipartParameter("agama_id",agamas)
                .addMultipartParameter("pendidikan_id",pendidikans)
                .addMultipartParameter("ibu_kandung",ibu_kandungs)
                .addMultipartParameter("status_martial",status_martials)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        dialog.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            if(response.getBoolean("success")){
                                Date date = new Date();
                                long timeMilli = date.getTime();
                                long exp_until=timeMilli + ExpiredSession.DURATION;
                                JSONObject arr =response.getJSONObject("data");
                                Credential credentials= new Credential(
                                        response.getInt("id"),
                                        1,
                                        response.getInt("id"),
                                        exp_until,
                                        arr.getString("nama"),
                                        arr.getString("tmplahir")+", "+arr.getString("tgllahir"),
                                        arr.getString("alamat")
                                );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(credentials);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), DaftarWaiting.class));
                            }else{
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Koneksi Buruk Cobal lagi", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void openSnackbarMerah(String messages) {
        Snackbar.make(findViewById(android.R.id.content), messages, Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();
    }

}