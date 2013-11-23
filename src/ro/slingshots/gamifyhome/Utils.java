package ro.slingshots.gamifyhome;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class Utils{

	private static final String DEFAULT_DIRNAME = "GamifyHome";
	public static File getFileByName(String fileName){
		File dir = new File(Environment.getExternalStorageDirectory()+File.separator+DEFAULT_DIRNAME);
		if(!dir.exists())
			dir.mkdirs();
		
		return new File(dir,fileName);
	}
	public static String getNewFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date now = new Date();
		return "Snapshot_"+formatter.format(now)+".jpg";	}
}