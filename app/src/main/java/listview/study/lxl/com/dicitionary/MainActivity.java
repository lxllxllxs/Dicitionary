package listview.study.lxl.com.dicitionary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
	private final String PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/dictionary";
	private  final String dicPath=PATH+"/dictionary.db";
	private TextView textView;
	private EditText editText;
	private String whatQuery;
	private  String sql="select chinese from t_words where english=?";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		copyDicitionnary();
		UIinit();
	}
	public void UIinit(){
		editText=(EditText)findViewById(R.id.edittext);
		textView=(TextView)findViewById(R.id.textview);
		(findViewById(R.id.query)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				whatQuery=editText.getText().toString();
				Cursor cs=openDatabase().rawQuery(sql,new String[]{whatQuery});
				if (cs.getCount()>0){
					cs.moveToFirst();
					whatQuery=cs.getString(cs.getColumnIndex("chinese"));
					textView.setText(whatQuery);
				}
			}
		});



	}

	public  void copyDicitionnary() {
		//	File file=new File(path+"dictionary.db");
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdir();
		}
		//check the database file if exists
		if (!new File(dicPath).exists()) {
			InputStream fis = getResources().openRawResource(R.raw.dictionary);
			try {
				FileOutputStream fos = new FileOutputStream(new File(dicPath));//曾忘了加斜杠导致不能正常复制
				byte[] buff = new byte[1024];
				int count = 0;
				while ((count = fis.read(buff)) != -1) {
					Log.d("COPY", count + "");
					fos.write(buff, 0, count);
				}
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public  SQLiteDatabase openDatabase(){
		SQLiteDatabase sqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(dicPath,null);
			return  sqLiteDatabase;
	}

}
