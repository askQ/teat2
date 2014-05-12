package com.example.teat2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class Teat2 extends Activity {
 
    private ArrayList<HashMap<String, String>> list;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
 
        list = new ArrayList<HashMap<String, String>>();
        BindData();
    }
 
    /** 綁定列表資料 */
    private void BindData() {
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(new BaseAdapter_addpic(this, list));
    }
 
    /** 新增圖片 */
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Intent destIntent = Intent.createChooser(intent, "選擇檔案");
        startActivityForResult(destIntent, 0);
    }
 
    /** 選擇圖片後回呼函式 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
        super.onActivityResult(requestCode, resultCode, data);
 
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
 
            if (uri != null) {
                //取得原始圖檔名稱
                String fileName = uri.getPath().substring(uri.getPath().lastIndexOf("/"));
 
                //SD Card 目的資料夾
                String extStorage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/thumbnail";
 
                File dir = new File(extStorage);
                if (!dir.exists())
                    dir.mkdirs();
 
                File file = new File(extStorage, fileName);
 
                try {
                    Bitmap bitmap = getBitmap(uri);
                    OutputStream outStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
 
                }
 
                // 將檔名資料加入列表
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("uri", uri.toString());
                item.put("filename", file.toString());
                list.add(item);
 
                BindData();
            }
        }
    }
 
    /** 取得縮圖 */
    public Bitmap getBitmap(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
 
            // 第一次 decode,只取得圖片長寬,還未載入記憶體
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, opts);
            in.close();
 
            // 取得動態計算縮圖長寬的 SampleSize (2的平方最佳)
            int sampleSize = computeSampleSize(opts, -1, 128 * 128);
 
            // 第二次 decode,指定取樣數後,產生縮圖
            in = getContentResolver().openInputStream(uri);
            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
 
            Bitmap bmp = BitmapFactory.decodeStream(in, null, opts);
            in.close();
 
            return bmp;
        } catch (Exception err) {
            return null;
        }
    }
 
    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
 
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
 
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
 
        return roundedSize;
    }
 
    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
 
        double w = options.outWidth;
        double h = options.outHeight;
 
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
 
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
 
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
 
    /** 移除項目 */
    public void removeItem(int position) {
        list.remove(position);
        BindData();
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test2, menu);
        return true;
    }
}