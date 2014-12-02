package com.xbc.utils.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;

/**
 * @author xiaobo.cui 2014年10月14日 下午5:08:22
 *
 */
public class CLogUtils {

	public static void write(Context context, String log) {
		try {
			File file = new File("/mnt/sdcard/" + context.getPackageName() + ".test.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			fw.write(log);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String path, String log) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(path);
			fw.write(log);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String log) {
		write("/mnt/sdcard/test.txt", log);
	}
}
