package vsga.meet9.projekvsga1.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import vsga.meet9.projekvsga1.MainActivity;
import vsga.meet9.projekvsga1.R;
/*
username : zonacode
password : 12345
 */
public class SignInActivity extends AppCompatActivity {
    public static final String FILENAME = "signIn";

    TextView tvDaftar;
    EditText etUserName, etPassword;
    Button btMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setId();
        setLogin();
        setTvDaftar();
    }

    void setLogin() {
        btMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUserName.getText().toString().equals("") && !etPassword.getText().toString().equals("")) setBtMasuk();
                else {
                    Toast.makeText(SignInActivity.this, "Username dan password tidak boleh kosong",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void setBtMasuk() {
        File sdCard = getFilesDir();
        File file = new File(sdCard, etUserName.getText().toString());
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    text.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }

            String data = text.toString();
            String[] dataUser = data.split(";");

            if (dataUser[1].equals(etPassword.getText().toString())) {
                signInFile();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Username \"" + etUserName.getText().toString() + "\" tidak ditemukan, silahkan cek kembali Username anda atau lakukan registrasi",
                    Toast.LENGTH_LONG).show();
        }
    }

    void setTvDaftar() {
        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    void signInFile() {
        String isiFile = etUserName.getText().toString() + ";" + etPassword.getText().toString();
        File file = new File(getFilesDir(), FILENAME);

        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(isiFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Login Sukses", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    void setId() {
        tvDaftar = findViewById(R.id.textdaftar);
        etUserName = findViewById(R.id.signInUserName);
        etPassword = findViewById(R.id.signInPassword);
        btMasuk = findViewById(R.id.btnMasuk);
    }
}