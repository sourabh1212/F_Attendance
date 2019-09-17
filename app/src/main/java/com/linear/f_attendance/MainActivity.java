package com.linear.f_attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
//yuhi
public class MainActivity extends AppCompatActivity {
Button sub_butn;
    DatabaseHelper databaseHelper;
    Cursor res;
    CustomAdapter customAdapter = new CustomAdapter();
    ArrayList<String> sub=new ArrayList<>();
    ArrayList<String> perc =new ArrayList<>();
    ArrayList<String> miss=new ArrayList<>();
    ArrayList<String> att=new ArrayList<>();
    ArrayList<String> id=new ArrayList<>();
    ArrayList<String> req = new ArrayList<>();



    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView myListView ;


        databaseHelper = new DatabaseHelper(this);
        sub_butn = (Button)findViewById(R.id.addNew);
        sub_butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });
        res = databaseHelper.allData();

        myListView =(ListView) findViewById(R.id.SubListView);

        myListView.setAdapter(customAdapter);
        if(res.getCount() != 0) {



            while (res.moveToNext()) {

                id.add(res.getString(0));
                sub.add(res.getString(1));
                att.add(res.getString(2));
                miss.add(res.getString(3));
                perc.add(res.getString(4));
                req.add(res.getString(5));


            }

            customAdapter.notifyDataSetChanged();
        }


    }

    public void popup()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_box);
        dialog.show();
        final EditText sub_edit = (EditText)dialog.findViewById(R.id.dilog_SubName);
        final EditText attend_edit = (EditText)dialog.findViewById(R.id.dilog_AttClasses);
        final EditText missed_edit = (EditText)dialog.findViewById(R.id.dilog_missClassses);




        Button butn_dia_submit = (Button)dialog.findViewById(R.id.submit_btn);

        butn_dia_submit.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                databaseHelper.insertData(sub_edit.getText().toString(), attend_edit.getText().toString(), missed_edit.getText().toString());
                dialog.dismiss();
                gen_list();

          }
       });
    }

    public void gen_list()
    {
        res = databaseHelper.allData();
        id.clear();
        sub.clear();
        att.clear();
        miss.clear();
        perc.clear();
        req.clear();

        while (res.moveToNext()) {

            id.add(res.getString(0));
            sub.add(res.getString(1));
            att.add(res.getString(2));
            miss.add(res.getString(3));
            perc.add(res.getString(4));
            req.add(res.getString(5));

        }
        customAdapter.notifyDataSetChanged();
    }
    //ArrayAdapter
    public  void yes_no(final int point)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();
        final Button butnYes = (Button)dialog.findViewById(R.id.btn_yes);
        final Button butnNo = (Button)dialog.findViewById(R.id.btn_no);
        butnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteData(id.get(point));
                gen_list();
                dialog.dismiss();


            }
        });

        butnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return sub.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.my_list, null);

            TextView textView1 = (TextView)view.findViewById(R.id.sub_name);
            TextView textView2 = (TextView)view.findViewById(R.id.percentage);
            TextView textView3 = (TextView)view.findViewById(R.id.att);
            ImageView Img_cross = (ImageView)view.findViewById(R.id.cross);
            ImageView img_right = (ImageView)view.findViewById(R.id.right);
            TextView textView4 = (TextView)view.findViewById(R.id.miss);
            TextView textView5 = (TextView)view.findViewById(R.id.required);
            Button butn_delete = (Button)view.findViewById(R.id.btn_delete);

            textView1.setText(sub.get(position));
            textView2.setText(perc.get(position));
            textView3.setText("attended : "+att.get(position));
            textView4.setText("Missed : " + miss.get(position));
            if(req.get(position) == "0")
            {

                textView5.setText("On track !");
            }
            else {
                textView5.setText("Attend next "+req.get(position)+" classes to get on track");
                Log.i("tag","'"+req.get(position)+"'");
            }

            butn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    yes_no(position);
                }
            });

            Img_cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = databaseHelper.updateData_miss(id.get(position), miss.get(position), att.get(position));
                    if(b == true)
                    {
                        Log.i("update","successful");
                    }
                    gen_list();

                }
            });

            img_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = databaseHelper.updateData_att(id.get(position), miss.get(position), att.get(position));
                    if(b == true)
                    {
                        Log.i("update","successful");
                    }
                    gen_list();

                }
            });

            return view;
        }
    }
}
