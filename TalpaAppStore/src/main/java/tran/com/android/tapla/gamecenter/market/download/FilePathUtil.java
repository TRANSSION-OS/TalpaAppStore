package tran.com.android.tapla.gamecenter.market.download;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;

import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.StorageInfo;
import tran.com.android.tapla.gamecenter.market.util.StoragerUtil;
import tran.com.android.tapla.gamecenter.market.util.StringUtils;

public class FilePathUtil {

	/**
	 * 获取存放软件的位置
	 * 
	 * @return
	 */
	public static String getAPKFilePath(Context ctx) {
//		if (FileUtil.isExistSDcard()) {
//			pathString = getSDcardSoftWarePath();
//		} else {
//			pathString = getDataSoftWarePath(ctx);
//		}
		String pathString = null;
		try {
			List<StorageInfo> storageList  = StoragerUtil.getStorageVolumesPath(ctx);

			if(storageList.size() > 1){
				pathString = PreferenceManager.getDefaultSharedPreferences(ctx).getString(Globals.DSIK_NAME, null);

			}else{
				pathString = storageList.get(0).getPath();
			}

			if(pathString != null){
				pathString = pathString + Globals.GAMECENTER_DOWNLOAD;
				File temp = new File(pathString);
				if (!temp.exists()) {
					temp.mkdirs();
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return pathString;
	}

	/**
	 * 设置下载路径
	 */
	public static String setDownDir(Context mContext) {
		try {
			List<StorageInfo> storageList = StoragerUtil.getStorageVolumesPath(mContext);
			int sdcardSize = storageList.size();
			SharedPreferences  preferences;
			if (sdcardSize < 1 || storageList == null) {
				return null;
			}

			if (sdcardSize > 1) {
				// 默认使用外置SD卡
				return storageList.get(1).getPath();
			} else {
				return storageList.get(0).getPath();
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取存放自动更新软件的位置
	 * 
	 * @return
	 */
	public static String getAutoUpdateFilePath(Context ctx) {
		String pathString = "";
		if (FileUtil.isExistSDcard()) {
			pathString = getSDcardAutoUpdatePath();
		} else {
			pathString = getDataAutoUpdatePath(ctx);
		}
		File temp = new File(pathString);
		if (!temp.exists()) {
			temp.mkdirs();
		}
		return pathString;
	}

	/**
	 * 获取SD卡存放软件的位置
	 * 
	 * @return
	 */
	private static String getSDcardSoftWarePath() {
		return Environment.getExternalStorageDirectory()
				+ "/GameCenter Download/apk/";
	}

	/**
	 * 获取内部存储软件的位置
	 * 
	 * @param ctx
	 * @return
	 */
	private static String getDataSoftWarePath(Context ctx) {
		return "/data/data/" + ctx.getPackageName() + "/cache/apk/";

	}
	
	/**
	 * 获取SD卡存放自动更新软件的位置
	 * 
	 * @return
	 */
	private static String getSDcardAutoUpdatePath() {
		return Environment.getExternalStorageDirectory()
				+ "/market/autoupdate/";
	}

	/**
	 * 获取内部存储自动更新软件的位置
	 * 
	 * @param ctx
	 * @return
	 */
	private static String getDataAutoUpdatePath(Context ctx) {
		return "/data/data/" + ctx.getPackageName() + "/cache/autoupdate/";

	}

}
