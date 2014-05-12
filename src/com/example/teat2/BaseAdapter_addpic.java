package com.example.teat2;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseAdapter_addpic extends BaseAdapter {
	 
    private Teat2 mainActivity;
    private LayoutInflater myInflater;
    private ArrayList<HashMap<String, String>> list = null;
    private ViewTag viewTag;
 
    public BaseAdapter_addpic(Teat2 context, ArrayList<HashMap<String, String>> list) {
        //���o MainActivity ����
        this.mainActivity = context;
        this.myInflater = LayoutInflater.from(context);
        this.list = list;
    }
 
    public int getCount() {
        return list.size();
    }
 
    public Object getItem(int position) {
        return list.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (convertView == null) {
            // ���olistItem�e�� view
            convertView = myInflater.inflate(R.layout.mylistview, null);
 
            // �غclistItem���eview
            viewTag = new ViewTag(
                    (ImageView) convertView.findViewById(R.id.imageView1),
                    (Button) convertView.findViewById(R.id.button1));
 
            // �]�m�e�����e
            convertView.setTag(viewTag);
 
        } else {
            viewTag = (ViewTag) convertView.getTag();
        }
 
        //���o�Y���ɦW�A�]�w ImageView
        String fileName = list.get(position).get("filename");
        Bitmap bm = BitmapFactory.decodeFile(fileName);
        viewTag.iv1.setImageBitmap(bm);
 
        //�]�w���s��ť�ƥ�ζǤJ MainActivity ����
        viewTag.btn1.setOnClickListener(new ItemButton_Click(this.mainActivity, position));
 
        return convertView;
    }
 
    public class ViewTag {
        ImageView iv1;
        TextView btn1;
 
        public ViewTag(ImageView imageView1, Button button1) {
            this.iv1 = imageView1;
            this.btn1 = button1;
        }
    }
 
    //�ۭq���s��ť�ƥ�
    class ItemButton_Click implements OnClickListener {
        private int position;
        private Teat2 mainActivity;
 
        ItemButton_Click(Teat2 context, int pos) {
            this.mainActivity = context;
            position = pos;
        }
 
        public void onClick(View v) {
            this.mainActivity.removeItem(position);
        }
    }
}