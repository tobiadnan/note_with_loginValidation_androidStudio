package vsga.meet9.projekvsga1.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import vsga.meet9.projekvsga1.R;

public class SignUpActivity extends AppCompatActivity {

    TextView tMasuk;
    EditText etUser, etPass, etName, etEmail, etAdress, etUniv;
    Button btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setId();
        setBtnDaftar();
        settMasuk();
    }

    void setBtnDaftar() {
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidation()) {
                    simpanFileData();
                    Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SignUpActivity.this, "Mohon lengkapi seluruh data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean isValidation() {
        return !etUser.getText().toString().equals("") &&
                !etPass.getText().toString().equals("") &&
                !etEmail.getText().toString().equals("") &&
                !etName.getText().toString().equals("") &&
                !etUniv.getText().toString().equals("") &&
                !etAdress.getText().toString().equals("");

    }

    void simpanFileData() {
        String isiFile = etUser.getText().toString() + ";" +
                etPass.getText().toString() + ";" +
                etEmail.getText().toString() + ";" +
                etName.getText().toString() + ";" +
                etUniv.getText().toString() + ";" +
                etAdress.getText().toString();

        File file = new File(getFilesDir(), etUser.getText().toString());

        FileOutputStream fileOutputStream;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(isiFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Registrasi berhasil, silahkan lekukan Login", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    void settMasuk() {
        tMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    void setId() {
        tMasuk = findViewById(R.id.textMasuk);
        etUser = findViewById(R.id.signUpUserName);
        etPass = findViewById(R.id.signUpPassword);
        etAdress = findViewById(R.id.signUpAdress);
        etEmail = findViewById(R.id.signUpEmail);
        etName = findViewById(R.id.signUpNama);
        etUniv = findViewById(R.id.signUpUniv);
        btnDaftar = findViewById(R.id.btnDaftar);
    }
}