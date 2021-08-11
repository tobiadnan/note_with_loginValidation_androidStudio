package vsga.meet9.projekvsga1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

import vsga.meet9.projekvsga1.MainActivity;
import vsga.meet9.projekvsga1.R;
import vsga.meet9.projekvsga1.R.id;

public class InsertAndViewActivity extends AppCompatActivity implements View.OnClickListener {
    EditText notes, title;
    ImageButton btSave;
    String fileName = "", tempCatatan = "";
    int eventId = 0;
    public static final int REQUEST_CODE_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_view);
        setId();
        setBtSave();
    }

    void setBtSave(){
        btSave.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "Add Note", Toast.LENGTH_SHORT).show();
        } else {
            fileName = extras.getString("filename");
            title.setText(fileName);
            Toast.makeText(this, "Update Note", Toast.LENGTH_SHORT).show();

        }
        eventId = 1;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                readFile();
            }
        } else {
            readFile();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case id.btnSave:
                eventId = 2;
                if (!tempCatatan.equals(notes.getText().toString())) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            showSaveConfirmation();
                        }
                    } else {
                        showSaveConfirmation();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (eventId == 1) {
                    readFile();
                } else {
                    showSaveConfirmation();
                }
            }
        }
    }

    void readFile() {
        String path = Environment.getExternalStorageDirectory().toString() + "/zona.code";
        File file = new File(path, title.getText().toString());
        if (file.exists()) {
            StringBuilder builder = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempCatatan = builder.toString();
            notes.setText(builder.toString());
        }
    }

    void createAndUpdate() {
        String path = Environment.getExternalStorageDirectory().toString() + "/zona.code";
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            return;
        }
        File parent = new File(path);
        if (parent.exists()) {
            File file = new File(path, title.getText().toString());
            FileOutputStream fileOutputStream;

            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream);
                streamWriter.append(notes.getText());
                streamWriter.flush();
                streamWriter.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            parent.mkdir();
            File file = new File(path, title.getText().toString());
            FileOutputStream fileOutputStream;
            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file, false);
                fileOutputStream.write(notes.getText().toString().getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!tempCatatan.equals(notes.getText().toString()) || !fileName.equals(title.getText().toString())) {
            showBackConfirmation();
        }else {
            Intent intent = new Intent(InsertAndViewActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void showSaveConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Simpan Catatan")
                .setMessage("Apakah anda yakin ?")
                .setPositiveButton("Yoi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createAndUpdate();
                        Toast.makeText(InsertAndViewActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InsertAndViewActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Kagak", null).show();
    }

    void showBackConfirmation() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda ingin menyimpannya ?")
                .setPositiveButton("Yoi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createAndUpdate();
                        Toast.makeText(InsertAndViewActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InsertAndViewActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Kagak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(InsertAndViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    public void setId() {
        notes = findViewById(R.id.editNote);
        title = findViewById(R.id.editJudul);
        btSave = findViewById(R.id.btnSave);
    }
}