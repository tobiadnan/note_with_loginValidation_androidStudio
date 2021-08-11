package vsga.meet9.projekvsga1;

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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vsga.meet9.projekvsga1.activity.InsertAndViewActivity;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Map<String, Object> data;
    public static final int REQUEST_CODE_STORAGE = 100;
    String path = Environment.getExternalStorageDirectory().toString() + "/zona.code";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, InsertAndViewActivity.class);
                data = (Map<String, Object>) adapterView.getAdapter().getItem(i);
                intent.putExtra("filename", data.get("name").toString());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                data = (Map<String, Object>) adapterView.getAdapter().getItem(i);
                showDialogDeleteConfirmation(data.get("name").toString());
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkIjinPenyimpanan()) {
                getFileFolder();
            }
        } else {
            getFileFolder();
        }
    }

    public boolean checkIjinPenyimpanan() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    void getFileFolder() {
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            String[] filenames = new String[files.length];
            String[] dateCreated = new String[files.length];
            String[] contents = new String[files.length];

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            ArrayList<Map<String, Object>> itemDataList = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                filenames[i] = files[i].getName();
                //contents[i] = files[i].getName();
                Date lastMod = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lastMod);

                Map<String, Object> listItemMap = new HashMap<>();
                listItemMap.put("name", filenames[i]);
                //listItemMap.put("content", contents[i]);
                listItemMap.put("date", dateCreated[i]);

                itemDataList.add(listItemMap);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                    itemDataList,
                    android.R.layout.simple_list_item_2,
                    new String[]{
                            "name",
                            "date"
                    }, new int[]{
                    android.R.id.text1, android.R.id.text2
            });
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFileFolder();
                }
                break;
        }
    }

    void showDialogDeleteConfirmation(final String filename) {
        new AlertDialog.Builder(this).setTitle("Hapus Catatan")
                .setMessage("Apakah anda yakin ingin menghapus catatan " + filename + " ?")
                .setPositiveButton("Yoi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusFile(filename);
                    }
                }).setNegativeButton("Nggak", null).show();
    }

    void hapusFile(String filename) {
        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
        getFileFolder();
    }

    // inflate option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionAddNote:
                Intent intent = new Intent(MainActivity.this, InsertAndViewActivity.class);
                startActivity(intent);
                break;
            case R.id.optionExit:
                exit();
            case R.id.optionAbout:
                Toast.makeText(this, "About Me", Toast.LENGTH_SHORT).show();
                break;
            case R.id.optionSetting:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean doublePress = false;
    @Override
    public void onBackPressed() {
        if (doublePress){
            exit();
        }
        this.doublePress = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
    }

    void exit(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}