package com.igrs.tivic.phone.Adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.LoginActivity;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramBean;
import com.igrs.tivic.phone.Bean.EPG.EPGProgramsBean;
import com.igrs.tivic.phone.Global.TivicGlobal;
import com.igrs.tivic.phone.Utils.Utils;

public class EPGProgramAdapter extends BaseAdapter {
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	private Context context;
//	private List<EPGProgramBean> programBeans = new ArrayList<EPGProgramBean>();
	private List<EPGProgramsBean> programsBeans = new ArrayList<EPGProgramsBean>();
	private LayoutInflater inflater;
	
	public EPGProgramAdapter(Context context,List<EPGProgramsBean> programsBeans) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.programsBeans = programsBeans;
	}

	/*public void setCurPostion(int curPostion) {
		this.curPostion = curPostion;
	}*/

	public void setProgramBeans(List<EPGProgramsBean> programsBeans) {
		this.programsBeans = programsBeans;
	}


	@Override
	public int getCount() {
		if(programsBeans != null)
			return programsBeans.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return programsBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*int type = getItemViewType(position);
		final int cur = position;*/
		ProgramHolder programHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.program_item, null);
			programHolder = new ProgramHolder();
			programHolder.name = (TextView) convertView.findViewById(R.id.program_name);
			programHolder.time = (TextView) convertView.findViewById(R.id.program_time);
			convertView.setTag(programHolder);
		}else{
			programHolder = (ProgramHolder) convertView.getTag();
		}
		//设置资源
		EPGProgramsBean programsBean = programsBeans.get(position);
		programHolder.name.setText(programsBean.getProgram_name());
		String start_time = programsBean.getStart_display();
		programHolder.time.setText(Utils.getProgramDateMDHM(start_time));
	/*	EPGProgramBean programBean = programBeans.get(position);
		programHolder.name.setText(programBean.getPrograme_title());
		programHolder.time.setText(programBean.getEnd_time());*/
		return convertView;
	}
	
	/*@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(curPostion == position){
			return SHOW_LIGHT;
		}
		return SHOW_NORMAL;
		
	}*/

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		if (observer != null) {       
			super.unregisterDataSetObserver(observer);   
		}  

	}

	@Override
	public int getViewTypeCount() {
		return 1; 
	}

	//add program
	public void addProgram(EPGProgramsBean programsBean){
		programsBeans.add(programsBean);
	}
	//add program list
	public void addListProgram(List<EPGProgramsBean> addprogramsBeans){
		programsBeans.addAll(addprogramsBeans);
	}
	
	class ProgramHolder{
		private TextView name;
		private TextView time;
	}
}
