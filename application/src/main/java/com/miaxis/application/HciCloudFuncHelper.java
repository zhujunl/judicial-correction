package com.miaxis.application;

import android.content.Context;
import android.os.Environment;
import android.widget.TextView;

import com.sinovoice.hcicloudsdk.api.HciCloudSys;
import com.sinovoice.hcicloudsdk.api.HciCloudUser;
import com.sinovoice.hcicloudsdk.api.vpr.HciCloudVpr;
import com.sinovoice.hcicloudsdk.common.HciErrorCode;
import com.sinovoice.hcicloudsdk.common.Session;
import com.sinovoice.hcicloudsdk.common.vpr.VprConfig;
import com.sinovoice.hcicloudsdk.common.vpr.VprEnrollResult;
import com.sinovoice.hcicloudsdk.common.vpr.VprEnrollVoiceData;
import com.sinovoice.hcicloudsdk.common.vpr.VprEnrollVoiceDataItem;
import com.sinovoice.hcicloudsdk.common.vpr.VprIdentifyResult;
import com.sinovoice.hcicloudsdk.common.vpr.VprInitParam;
import com.sinovoice.hcicloudsdk.common.vpr.VprVerifyResult;

import java.io.File;
import java.util.ArrayList;

public class HciCloudFuncHelper extends HciCloudHelper {
	private static final String TAG = HciCloudFuncHelper.class.getSimpleName();

	
	/*
	 * VPR 注册 （Enroll）
	 */
	public static boolean Enroll(String capkey, VprConfig enrollConfig, VprEnrollResult enrollResult, String realtime) {

		ShowMessage("vprEnroll enter...");
		// 组装音频，可以一次传入多段音频,如果使用实时识别，请将nEnrollDataCount设为1
		int nEnrollDataCount = 4;
		int nIndex = 0;
		ArrayList<VprEnrollVoiceDataItem> enrollVoiceDataList = new ArrayList<VprEnrollVoiceDataItem>();

		byte[] voiceData = getAssetFileData("enroll_0.pcm");   
		
		for (; nIndex < nEnrollDataCount; nIndex++) {
			String voiceDataName = "enroll_" + nIndex + ".pcm";
			voiceData = getAssetFileData(voiceDataName);
			if (null == voiceData) {
				ShowMessage("Open input voice file" + voiceDataName + "error!");
				break;
			}
			VprEnrollVoiceDataItem voiceDataItem = new VprEnrollVoiceDataItem();
			voiceDataItem.setVoiceData(voiceData);
			enrollVoiceDataList.add(voiceDataItem);
		}
		
		if (nIndex <= 0) {
			ShowMessage("no enroll data found in assets folder!");
			return false;
		}
		
		// 启动 VPR Session
		VprConfig sessionConfig = new VprConfig();
		sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_CAP_KEY,capkey);
		if (realtime.equals("yes")) {
			sessionConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, "yes");
		}
		// 根据实际情况指定资源前缀
		if (capkey.contains("local")) {
			//sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_RES_PREFIX, "");
		}
		
		//用户模型路径可根据实际情况指定，否则将指定授权路径为用户模型路径
		//sessionConfig.addParam("usermodelpath", Environment.getExternalStorageDirectory()+"");

		Session session = new Session();
		ShowMessage("sessionConfig is " + sessionConfig.getStringConfig());
		int errCode = HciCloudVpr.hciVprSessionStart(sessionConfig.getStringConfig(), session);
		if (HciErrorCode.HCI_ERR_NONE != errCode) {
			ShowMessage("hciVprSessionStart return " + errCode);
			return false;
		}
		ShowMessage("hciVprSessionStart success");
		
		ShowMessage("enrollConfig is " + enrollConfig.getStringConfig());
		
		// VPR 注册
		VprEnrollVoiceData enrollVoiceData = new VprEnrollVoiceData();
		enrollVoiceData.setEnrollVoiceDataCount(nEnrollDataCount);
		enrollVoiceData.setEnrollVoiceDataList(enrollVoiceDataList);
				
		if (realtime.compareTo("yes") == 0) {
			//实时识别
			int nPerLen = 3200*20;   //每段传入音频长度
			int nLen = 0;            //已传入音频的长度
			enrollConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, "yes");
			while (nLen+nPerLen<voiceData.length) {
				int nThisLen = 0;
				if (voiceData.length-nLen >= nPerLen) {
					nThisLen = nPerLen;
				}
				else {
					nThisLen =voiceData.length - nLen;
				}
				byte[] subVoiceData = new byte[nThisLen];
				System.arraycopy(voiceData, nLen, subVoiceData, 0, nPerLen);
				enrollVoiceData.getEnrollVoiceDataList().get(0).setVoiceData(subVoiceData);
				errCode = HciCloudVpr.hciVprEnroll(session, enrollVoiceData,enrollConfig.getStringConfig(), enrollResult);
		        if (errCode == HciErrorCode.HCI_ERR_PARAM_INVALID) {
		        	ShowMessage("Param invalid return "+errCode);
		        	break;
				}
				if (errCode == HciErrorCode.HCI_ERR_NONE)
				{
					break;
				}
				nLen += nThisLen;
			}
			
			// 若未检测到端点，但数据已经传入完毕，则需要告诉引擎数据输入完毕
			// 或者检测到末端了，也需要告诉引擎，获取结果
			if (errCode == HciErrorCode.HCI_ERR_VPR_REALTIME_WAIT || errCode == HciErrorCode.HCI_ERR_NONE) {
				errCode = HciCloudVpr.hciVprEnroll(session, null,enrollConfig.getStringConfig(), enrollResult);
				if (errCode != HciErrorCode.HCI_ERR_NONE) {
		        	ShowMessage("enroll return "+errCode);
				}
				else {
					ShowMessage("vprEnroll success");
					ShowMessage("enroll result = " + enrollResult.getUserId());
				}
			}
		}
		else {
			//非实时识别
			enrollConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, "no");
			errCode = HciCloudVpr.hciVprEnroll(session, enrollVoiceData,enrollConfig.getStringConfig(), enrollResult);
			if (errCode == HciErrorCode.HCI_ERR_PARAM_INVALID) {
	        	ShowMessage("Param invalid return "+errCode);
	        	return false;
			}
			if (HciErrorCode.HCI_ERR_NONE != errCode) {
				// 启动失败
				HciCloudVpr.hciVprSessionStop(session);
				ShowMessage("hciVprEnroll return " + errCode);
				return false;
			}
			ShowMessage("vprEnroll success");
			ShowMessage("enroll result = " + enrollResult.getUserId());
			
		}
		// 关闭session
		HciCloudVpr.hciVprSessionStop(session);
		ShowMessage("hciVprSessionStop success");
		ShowMessage("vprEnroll leave...");
		return true;
	}

	/*
	 * VPR 确认（Verify）
	 */
	public static boolean Verify(String capkey, VprConfig verifyConfig, String realtime) {
		ShowMessage("vprVerify enter...");
		String voiceDataName = "verify.pcm";
		byte[] voiceDataVerify = getAssetFileData(voiceDataName);
		if (null == voiceDataVerify) {
			ShowMessage("Open input voice file " + voiceDataName + " error!");
			return false;
		}

		// 启动 VPR Session
		VprConfig sessionConfig = new VprConfig();
		sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_CAP_KEY,capkey);
		if (realtime.equals("yes")) {
			sessionConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, realtime);
		}
		// 根据实际情况指定资源前缀
		if (capkey.contains("local")) {
			//sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_RES_PREFIX, "");
		}
		
		//用户模型路径可根据实际情况指定，否则将指定授权路径为用户模型路径
		//sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_USER_MODEL_PATH, Environment.getExternalStorageDirectory()+"");
		
		Session session = new Session();
		ShowMessage("sessionConfig is " + sessionConfig.getStringConfig());
		int errCode = HciCloudVpr.hciVprSessionStart(sessionConfig.getStringConfig(), session);
		if (HciErrorCode.HCI_ERR_NONE != errCode) {
			ShowMessage("hciVprSessionStart return " + errCode);
			return false;
		}
		ShowMessage("hciVprSessionStart success");
		
		// 开始校验
		VprVerifyResult verifyResult = new VprVerifyResult();			
		if (realtime.compareTo("yes") == 0) {
			//实时识别	
			int nPerLen = 3200*20;
			int nLen = 0;
			verifyConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, "yes");
			ShowMessage("verifyConfig is " + verifyConfig.getStringConfig());
			while (nLen+nPerLen<voiceDataVerify.length) {
				int nThisLen = 0;
				if (voiceDataVerify.length-nLen >= nPerLen) {
					nThisLen = nPerLen;
				}
				else {
					nThisLen =voiceDataVerify.length - nLen;
				}
				byte[] subVoiceData = new byte[nPerLen];
				System.arraycopy(voiceDataVerify, nLen, subVoiceData, 0, nPerLen);
				errCode = HciCloudVpr.hciVprVerify(session, subVoiceData,verifyConfig.getStringConfig(), verifyResult);
				if (errCode == HciErrorCode.HCI_ERR_PARAM_INVALID) {
		        	ShowMessage("Param invalid return "+errCode);
		        	break;
				}
				if (errCode == HciErrorCode.HCI_ERR_NONE)
				{
					break;
				}
				nLen += nThisLen;
			}
			
			// 若未检测到端点，但数据已经传入完毕，则需要告诉引擎数据输入完毕
			// 或者检测到末端了，也需要告诉引擎，获取结果
			if (errCode == HciErrorCode.HCI_ERR_VPR_REALTIME_WAIT || errCode == HciErrorCode.HCI_ERR_NONE) {
				errCode = HciCloudVpr.hciVprVerify(session, null,
						verifyConfig.getStringConfig(), verifyResult);
				if (errCode != HciErrorCode.HCI_ERR_NONE) {
		        	ShowMessage("verify return "+errCode);
				}
				else {
					if (verifyResult.getStatus() == VprVerifyResult.VPR_VERIFY_STATUS_MATCH) {
						ShowMessage("VprVerify success");
						ShowMessage("voice data matches with user_id !");
						ShowMessage("score:" + verifyResult.getScore());
					} else {
						ShowMessage("voice data doesn't match with user_id !");
					}
				}
			}
	    }
		else {
			//非实时识别
			verifyConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, "no");
			errCode = HciCloudVpr.hciVprVerify(session, voiceDataVerify,verifyConfig.getStringConfig(), verifyResult);
			if (errCode == HciErrorCode.HCI_ERR_PARAM_INVALID) {
	        	ShowMessage("Param invalid return "+errCode);
	        	return false;
			}
			if (HciErrorCode.HCI_ERR_NONE != errCode) {
				ShowMessage("Hcivpr hciVprVerify return " + errCode);
				HciCloudVpr.hciVprSessionStop(session);
				return false;
			}

			if (verifyResult.getStatus() == VprVerifyResult.VPR_VERIFY_STATUS_MATCH) {
				ShowMessage("VprVerify success");
				ShowMessage("voice data matches with user_id !");
				ShowMessage("score:"+verifyResult.getScore());
			} else {
				ShowMessage("the score is:"+verifyResult.getScore());
				ShowMessage("voice data doesn't match with user_id !");
			}
		}
		HciCloudVpr.hciVprSessionStop(session);
		ShowMessage("hciVprSessionStop Success");
		ShowMessage("vprVerify leave...");
		return true;
	}

	static void PrintIdentifyResult(VprIdentifyResult identifyResult)
	{
		for (int index = 0; index < identifyResult.getIdentifyResultItemList().size(); index++) {
			ShowMessage("index" + index);
			ShowMessage("userid:" + identifyResult.getIdentifyResultItemList().get(index).getUserId());
			ShowMessage("score:" + identifyResult.getIdentifyResultItemList().get(index) .getScore());
	    }
	}
	
	/*
	 * VPR 辨识（Identify）
	 */
	public static boolean Identify(String capkey, VprConfig identifyConfig, String realtime) {
		ShowMessage("Identify enter...");
		String voiceDataName = "verify.pcm";
		byte[] voiceDataVerify = getAssetFileData(voiceDataName);
		if (null == voiceDataVerify) {
			ShowMessage("Open input voice file " + voiceDataName + " error!");
			return false;
		}

		// 启动 VPR Session
		VprConfig sessionConfig = new VprConfig();
		sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_CAP_KEY,capkey);
		if (realtime.equals("yes")) {
			sessionConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, realtime);
		}
		// 根据实际情况指定资源前缀
		if (capkey.contains("local")) {
			//sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_RES_PREFIX, "");
		}
		//用户模型路径可根据实际情况指定，否则将指定授权路径为用户模型路径
		//sessionConfig.addParam(VprConfig.SessionConfig.PARAM_KEY_USER_MODEL_PATH, Environment.getExternalStorageDirectory()+"");
		Session session = new Session();
		ShowMessage("sessionConfig is " + sessionConfig.getStringConfig());
		int errCode = HciCloudVpr.hciVprSessionStart(sessionConfig.getStringConfig(), session);
		if (HciErrorCode.HCI_ERR_NONE != errCode) {
			ShowMessage("Hcivpr hciVprSessionStart return " + errCode);
			return false;
		}
		ShowMessage("hciVprSessionStart success");

		// 辨识
		VprIdentifyResult identifyResult = new VprIdentifyResult();		
		if (realtime.compareTo("yes") == 0) {
			//实时识别		
			//identifyConfig.addParam(VprConfig.AudioConfig.PARAM_KEY_IDENTIFY, identifyConfig.getParam("identify"));
			int nPerLen = 3200*20;    //传入每段音频长度
			int nLen = 0;             //已传入音频的长度

			identifyConfig.addParam(VprConfig.UserConfig.PARM_KEY_REAL_TIME, "yes");
			ShowMessage("identifyConfig is " + identifyConfig.getStringConfig());
			while (nLen+nPerLen<voiceDataVerify.length) {
				int nThisLen = 0;
				if (voiceDataVerify.length-nLen >= nPerLen) {
					nThisLen = nPerLen;
				}
				else {
					nThisLen =voiceDataVerify.length - nLen;
				}
				byte[] subVoiceData = new byte[nPerLen];
				System.arraycopy(voiceDataVerify, nLen, subVoiceData, 0, nPerLen);
				errCode = HciCloudVpr.hciVprIdentify(session, voiceDataVerify,identifyConfig.getStringConfig(), identifyResult);
				if (errCode == HciErrorCode.HCI_ERR_PARAM_INVALID) {
		        	ShowMessage("Param invalid return "+errCode);
		        	break;
				}
				if (errCode == HciErrorCode.HCI_ERR_NONE)
				{
					break;
				}
				nLen += nThisLen;
			}
			
			// 若未检测到端点，但数据已经传入完毕，则需要告诉引擎数据输入完毕
			// 或者检测到末端了，也需要告诉引擎，获取结果
			if (errCode == HciErrorCode.HCI_ERR_VPR_REALTIME_WAIT || errCode == HciErrorCode.HCI_ERR_NONE) {
				errCode = HciCloudVpr.hciVprIdentify(session, null,
						identifyConfig.getStringConfig(), identifyResult);
				ShowMessage(identifyConfig.getStringConfig());
				if (errCode != HciErrorCode.HCI_ERR_NONE) {
		        	ShowMessage("identify return "+errCode);
				}
				else {
					ShowMessage("vprIdentify success");
					ShowMessage("size: " + identifyResult.getIdentifyResultItemList().size());
					PrintIdentifyResult(identifyResult);
				}
		    }
		}
		else{
			errCode = HciCloudVpr.hciVprIdentify(session, voiceDataVerify,identifyConfig.getStringConfig(), identifyResult);
			if (errCode == HciErrorCode.HCI_ERR_PARAM_INVALID) {
	        	ShowMessage("Param invalid return "+errCode);
	        	return false;
			}
			if (HciErrorCode.HCI_ERR_NONE != errCode) {
				ShowMessage("Hcivpr hciVprIdentify return " + errCode);
				HciCloudVpr.hciVprSessionStop(session);
				return false;
			}
			ShowMessage("vprIdentify success");
			ShowMessage("size: " + identifyResult.getIdentifyResultItemList().size());
			PrintIdentifyResult(identifyResult);
		}
		HciCloudVpr.hciVprSessionStop(session);
		ShowMessage("hciVprSessionStop success");
		ShowMessage("Identify enter...");
		return true;
	}

	public static void Func(Context context, String capkey, TextView view) {

		setTextView(view);
		setContext(context);

		// 初始化VPR
		// 构造VPR初始化的帮助类的实例
		VprInitParam initParam = new VprInitParam();
		// 获取App应用中的lib的路径,放置能力所需资源文件。如果使用/data/data/packagename/lib目录,需要添加android_so的标记
		String sdcardState = Environment.getExternalStorageState();
        String sdPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();

        String dataPath = sdPath + File.separator + "sinovoice"
                    + File.separator + "com.sinovoice.example" + File.separator + "files"
                    + File.separator;
		//String dataPath = context.getFilesDir().getAbsolutePath().replace("files", "lib");
        ShowMessage(dataPath);
		initParam.addParam(VprInitParam.PARAM_KEY_DATA_PATH, dataPath);
		//initParam.addParam(VprInitParam.PARAM_KEY_FILE_FLAG, VprInitParam.VALUE_OF_PARAM_FILE_FLAG_ANDROID_SO);
		initParam.addParam(VprInitParam.PARAM_KEY_INIT_CAP_KEYS, capkey);
		copyAssetsFiles(context, dataPath);
		int errCode = HciCloudVpr.hciVprInit(initParam.getStringConfig());
		if (errCode != HciErrorCode.HCI_ERR_NONE) {
			ShowMessage("HciVprInit error:"	+ HciCloudSys.hciGetErrorInfo(errCode));
			return;
		} else {
			ShowMessage("HciVprInit Success");
		}

		String realtime = "no";//yes表示实时识别；no表示非实时识别
		VprEnrollResult enrollResult = new VprEnrollResult();
		
		// enroll
		VprConfig enrollConfig = new VprConfig();
		enrollConfig.addParam(VprConfig.UserConfig.PARAM_KEY_USER_ID, "123456");
		enrollConfig.addParam(VprConfig.AudioConfig.PARAM_KEY_AUDIO_FORMAT,VprConfig.AudioConfig.VALUE_OF_PARAM_AUDIO_FORMAT_PCM_16K16BIT);
		Enroll(capkey, enrollConfig, enrollResult, realtime);

		// Verify
		VprConfig verifyConfig = new VprConfig();
		verifyConfig.addParam(VprConfig.UserConfig.PARAM_KEY_USER_ID, enrollResult.getUserId());
		Verify(capkey, verifyConfig, realtime);
					
		String group_id;
        if (capkey.contains("cloud")) {			
			group_id = "test_example";
			HciCloudUser.hciCreateGroup(group_id, HciCloudUser.kHciGroupTypeShare);
		}
		else {
			group_id = "";
		} 
		
        HciCloudUser.hciAddUser(group_id, enrollResult.getUserId());
        
		// Identify
        VprConfig identifyConfig = new VprConfig();
		identifyConfig.addParam(VprConfig.UserConfig.PARAM_KEY_GROUP_ID, group_id);
		Identify(capkey, identifyConfig, realtime);    

		HciCloudUser.hciRemoveUser(group_id, enrollResult.getUserId());
		
		errCode = HciCloudUser.hciDeleteModel(group_id, HciCloudUser.kHciModelTypeVpr, HciCloudUser.kHciModelSubTypeUnknown);
		if (errCode != (int)HciErrorCode.HCI_ERR_NONE) {
			ShowMessage("delete model return:" + errCode);
		} else {
			ShowMessage("delete model success");
		}

		errCode = HciCloudUser.hciDeleteUser(enrollResult.getUserId());
		if (errCode != HciErrorCode.HCI_ERR_NONE) {
			ShowMessage("delete user return:" + errCode);
		} else {
			ShowMessage("delete user success");
		}
            
        errCode = HciCloudUser.hciRemoveUser(group_id, enrollResult.getUserId());
        if (errCode != HciErrorCode.HCI_ERR_NONE) {
			ShowMessage("remove user return:" + errCode);
		} else {
			ShowMessage("remove user success");
		}
			
		errCode = HciCloudUser.hciDeleteGroup(group_id);
        if (errCode != HciErrorCode.HCI_ERR_NONE) {
			ShowMessage("delete group return:" + errCode);
		} else {
			ShowMessage("delete group success");
		}  
		
		// 反初始化VPR
		HciCloudVpr.hciVprRelease();
		ShowMessage("hciVprRelease");
		return;
	}

}
