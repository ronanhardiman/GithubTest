package com.igrs.tivic.phone.ViewGroup;

//import static android.util.Log.e;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.igrs.tivic.phone.R;
import com.igrs.tivic.phone.Activity.BaseActivity;
import com.igrs.tivic.phone.Global.FuncID;
import com.igrs.tivic.phone.Global.TivicGlobal;

public class BaseEmpty extends FrameLayout{

	private String TAG = "BaseEmpty";
	TivicGlobal tivicGlobal = TivicGlobal.getInstance();
	Context m_context;
	
    public BaseEmpty(Context context) {
        this(context, null);
    }
      
    public BaseEmpty(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_context = context;    
        initLayout();
                         
    }

   public void initLayout()
   {
	   switch(tivicGlobal.mCurrentFuncID)
	   {
	   case FuncID.ID_EPG:
		   LayoutInflater.from(m_context).inflate(R.layout.epg_main, this, true); 
		   break;
	   case FuncID.ID_MYTV:
		   LayoutInflater.from(m_context).inflate(R.layout.mytv_main, this, true); 
		   break;
	   case FuncID.ID_PUSHTV:
		   LayoutInflater.from(m_context).inflate(R.layout.pushtv_main, this, true); 
		   break;
	   case FuncID.ID_UGC:
		   LayoutInflater.from(m_context).inflate(R.layout.ugc_main, this, true); 
		   break;
	   case FuncID.ID_WATERFALL:
		   LayoutInflater.from(m_context).inflate(R.layout.waterfall_main, this, true); 
		   break;
	   case FuncID.ID_CONTENT:
		   LayoutInflater.from(m_context).inflate(R.layout.content_main, this, true); 
		   break;	
	   case FuncID.ID_PUBLISH:
		   LayoutInflater.from(m_context).inflate(R.layout.ugc_publish, this, true); 
		   break;
	   case FuncID.ID_SEARCH:
		   LayoutInflater.from(m_context).inflate(R.layout.search, this, true); 
		   break;
	   case FuncID.ID_UGC_JUMP:
		   LayoutInflater.from(m_context).inflate(R.layout.ugc_jump, this, true); 
		   break;		   
	   }
   }
 
   public void init()
   {
	   
   }
   //UsrMenuBar 弹出的时候,点击左边view closeMenuBar.
   @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	   boolean isMenuBarShow = false ;
	   if(((BaseActivity) m_context).mainGroup != null){
		   isMenuBarShow = ((BaseActivity) m_context).mainGroup.isMenuBarShow;
	   }
		if(isMenuBarShow){
			((BaseActivity) m_context).closeMenuBar();
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
