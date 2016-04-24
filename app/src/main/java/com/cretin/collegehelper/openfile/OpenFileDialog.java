// filename: OpenFileDialog.java
package com.cretin.collegehelper.openfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cretin.collegehelper.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenFileDialog {
	public static String tag = "OpenFileDialog";
	static final public String sRoot = "/"; 
	static final public String sParent = "..";
	static final public String sFolder = ".";
	static final public String sEmpty = "";
	static final private String sOnErrorMsg = "No rights to access!";
	
	// ����˵��
	// context:������
	// dialogid:�Ի���ID
	// title:�Ի������
	// callback:һ������Bundle����Ļص��ӿ�
	// suffix:��Ҫѡ����ļ���׺��������Ҫѡ��wav��mp3�ļ���ʱ������Ϊ".wav;.mp3;"��ע�������Ҫһ���ֺ�(;)
	// images:������ݺ�׺��ʾ��ͼ����ԴID��
		//	��Ŀ¼ͼ�������ΪsRoot;
		//	��Ŀ¼������ΪsParent;
		//	�ļ��е�����ΪsFolder;
		//	Ĭ��ͼ�������ΪsEmpty;
		//	�����ֱ�Ӹ�ݺ�׺�����������.wav�ļ�ͼ�������Ϊ"wav"
	public static Dialog createDialog(int id, Context context, String title, CallbackBundle callback, String suffix, Map<String, Integer> images){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(new FileSelectView(context, id, callback, suffix, images));
		Dialog dialog = builder.create();
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setTitle(title);
		return dialog;
	}
	
	static class FileSelectView extends ListView implements OnItemClickListener{
		
		
		private CallbackBundle callback = null;
		private String path = sRoot;
		private List<Map<String, Object>> list = null;
		private int dialogid = 0;
		
		private String suffix = null;
		
		private Map<String, Integer> imagemap = null;
		
		public FileSelectView(Context context, int dialogid, CallbackBundle callback, String suffix, Map<String, Integer> images) {
			super(context);
			this.imagemap = images;
			this.suffix = suffix==null?"":suffix.toLowerCase();
			this.callback = callback;
			this.dialogid = dialogid;
			this.setOnItemClickListener(this);
			refreshFileList();
		}
		
		private String getSuffix(String filename){
			int dix = filename.lastIndexOf('.');
			if(dix<0){
				return "";
			}
			else{
				return filename.substring(dix+1);
			}
		}
		
		private int getImageId(String s){
			if(imagemap == null){
				return 0;
			}
			else if(imagemap.containsKey(s)){
				return imagemap.get(s);
			}
			else if(imagemap.containsKey(sEmpty)){
				return imagemap.get(sEmpty);
			}
			else {
				return 0;
			}
		}
		
		private int refreshFileList()
		{
			// ˢ���ļ��б�
			File[] files = null;
			try{
				files = new File(path).listFiles();
			}
			catch(Exception e){
				files = null;
			}
			if(files==null){
				// ���ʳ���
				Toast.makeText(getContext(), sOnErrorMsg,Toast.LENGTH_SHORT).show();
				return -1;
			}
			if(list != null){
				list.clear();
			}
			else{
				list = new ArrayList<Map<String, Object>>(files.length);
			}
			
			// �����ȱ����ļ��к��ļ��е������б�
			ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
			ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();
			
			if(!this.path.equals(sRoot)){
				// ��Ӹ�Ŀ¼ �� ��һ��Ŀ¼
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", sRoot);
				map.put("path", sRoot);
				map.put("img", getImageId(sRoot));
				list.add(map);
				
				map = new HashMap<String, Object>();
				map.put("name", sParent);
				map.put("path", path);
				map.put("img", getImageId(sParent));
				list.add(map);
			}
			
			for(File file: files)
			{
				if(file.isDirectory() && file.listFiles()!=null){
					// ����ļ���
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", file.getName());
					map.put("path", file.getPath());
					map.put("img", getImageId(sFolder));
					lfolders.add(map);
				}
				else if(file.isFile()){
					// ����ļ�
					String sf = getSuffix(file.getName()).toLowerCase();
					if(suffix == null || suffix.length()==0 || (sf.length()>0 && suffix.indexOf("."+sf+";")>=0)){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", file.getName());
						map.put("path", file.getPath());
						map.put("img", getImageId(sf));
						lfiles.add(map);
					}
				}  
			}
			
			list.addAll(lfolders); // ������ļ��У�ȷ���ļ�����ʾ������
			list.addAll(lfiles);	//������ļ�
			
			
			SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.filedialogitem, new String[]{"img", "name", "path"}, new int[]{R.id.filedialogitem_img, R.id.filedialogitem_name, R.id.filedialogitem_path});
			this.setAdapter(adapter);
			return files.length;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// ��Ŀѡ��
			String pt = (String) list.get(position).get("path");
			String fn = (String) list.get(position).get("name");
			if(fn.equals(sRoot) || fn.equals(sParent)){
				// ����Ǹ�Ŀ¼������һ��
				File fl = new File(pt);
				String ppt = fl.getParent();
				if(ppt != null){
					// ������һ��
					path = ppt;
				}
				else{
					// ���ظ�Ŀ¼
					path = sRoot;
				}
			}
			else{
				File fl = new File(pt);
				if(fl.isFile()){
					// ������ļ�
					((Activity)getContext()).dismissDialog(this.dialogid); // ���ļ��жԻ�����ʧ
					
					// ���ûص��ķ���ֵ
					Bundle bundle = new Bundle();
					bundle.putString("path", pt);
					bundle.putString("name", fn);
					// �����������õĻص�����
					this.callback.callback(bundle);
					return;
				}
				else if(fl.isDirectory()){
					// ������ļ���
					// ��ô����ѡ�е��ļ���
					path = pt;
				}
			}
			this.refreshFileList();
		}
	}
}
