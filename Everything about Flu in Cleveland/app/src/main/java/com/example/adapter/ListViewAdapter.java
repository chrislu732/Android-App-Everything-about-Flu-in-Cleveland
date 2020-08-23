package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.afinal.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;
//from https://github.com/wildcreek/MultiChoice
public class ListViewAdapter extends BaseAdapter {
    public List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public ListViewAdapter(Context context, List<Map<String, Object>> list){
        this.context = context;
        this.data = list;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public final class Zujian{
        public TextView device_name;
        public TextView device_address;
        public String phoneNumber;
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public double lat;
        public double lng;
        public String webUrl;
        public double getLat() {return lat;};
        public double getLng() {return lng;};
        public String getWebUrl() {return webUrl;}
    }
    @Override
    public int getCount(){
        return data.size();
    }
    public void clear(){data.clear();}
    @Override
    public Object getItem(int position){
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public String getNumber(int position){
        return (String)data.get(position).get("phoneNumber");
    }
    public String getName(int position){
        return (String)data.get(position).get("device_name");
    }
    public String getAddress(int position){
        return (String)data.get(position).get("device_address");
    }
    public String getWeb(int position) {return  (String)data.get(position).get("webUrl");}
    public Double getLat(int position) {return  (Double)data.get(position).get("lat");}
    public Double getLng(int position) {return  (Double)data.get(position).get("lng");}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            convertView=layoutInflater.inflate(R.layout.listview, null);
            zujian.device_name=(TextView)convertView.findViewById(R.id.device_name);
            zujian.device_address=(TextView)convertView.findViewById(R.id.device_address);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        //绑定数据
        zujian.device_name.setText((String)data.get(position).get("device_name"));
        zujian.device_address.setText((String)data.get(position).get("device_address"));
        return convertView;
    }

}
