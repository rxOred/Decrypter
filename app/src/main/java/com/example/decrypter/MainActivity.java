package com.example.decrypter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    private ArrayList filesToDecrypt;
    private List<String> extensionsToDecrypt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        this.filesToDecrypt = new ArrayList();
        String[] arrayOfString = new String[1];
        arrayOfString[0] = "enc";
        this.extensionsToDecrypt = Arrays.asList(arrayOfString);
        File file = new File(Environment.getExternalStorageDirectory().toString());
        getFileNames(file);
        try {
            decrypt();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private void getFileNames(File paramFile) {
        File[] arrayOfFile = paramFile.listFiles();
        for (int i = 0;; i++) {
            int k = arrayOfFile.length;
            if (i >= k)
                return;
            String absolutePath = paramFile.getAbsolutePath();
            String fileName = arrayOfFile[i].getName();
            File file = new File(absolutePath, fileName);
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                File[] arrayOfFile1 = file.listFiles();
                if (arrayOfFile1 != null) {
                    // if a directory, get names of files inside it recursively
                    getFileNames(file);
                    continue;
                }
            }
            // if not a directory
            String str3 = file.getAbsolutePath();
            String subStr = str3.substring(str3.lastIndexOf(".") + 1);
            List<String> list = this.extensionsToDecrypt;
            // if pathname contains .enc
            boolean bool1 = list.contains(subStr);
            if (bool1) {
                list = this.filesToDecrypt;
                fileName = file.getAbsolutePath();
                list.add(fileName);
            }
            continue;
        }
    }

    private boolean isExternalStorageWritable() {
        String ext = Environment.getExternalStorageState();
        String mounted = "mounted";
        boolean isMounted = mounted.equals(ext);
        if (isMounted)
            return true;
        isMounted = false;
        mounted = null;
        return isMounted;
    }

    public void decrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        boolean isWritable = isExternalStorageWritable();
        if (isWritable) {
            AesCrypt aesCrypt = new AesCrypt("jndlasf074hr");
            Iterator<String> iterator = this.filesToDecrypt.iterator();
            while (true) {
                boolean hasNext = iterator.hasNext();
                if (hasNext) {
                    String inputFileName = iterator.next();
                    hasNext = false;
                    int i = inputFileName.lastIndexOf(".");
                    String outputFileName = inputFileName.substring(0, i);
                    aesCrypt.decrypt(inputFileName, outputFileName);
                    File file = new File(inputFileName);
                    file.delete();
                    continue;
                }
                return;
            }
        }
    }
}