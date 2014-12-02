package com.xbc.utils.activity;

/**
 *@author xiaobo.cui 2014年11月7日 上午11:24:25
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;

public class GetContactsInfo {
	private List<Object> list;
	private Context context;
	private JSONObject contactData;
	private JSONObject jsonObject;

	public GetContactsInfo(Context context) {
		this.context = context;
	}

	/**
	 * 1.名字信息-StructuredName  
	 * 2.组织(公司,职位)-
	 * 3.号码
	 * 4.邮件
	 * 5.地址
	 * 6.称呼
	 * 7.网站
	 * 8.即时消息
	 *-- 9.互联网通话
	 * 10.生日
	 * 11.农历生日
	 * 12.备注
	 * 13.群组
	 * 
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String getContactInfo() throws JSONException {
		// 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
		list = new ArrayList<Object>();
		contactData = new JSONObject();
		String mimetype = "";
		int oldrid = -1;
		int contactId = -1;
		HashMap<String, String> groupsMap = new HashMap<String, String>();

		Cursor cursorGroup = context.getContentResolver().query(Groups.CONTENT_URI, null, null, null, null);
		while (cursorGroup.moveToNext()) {
			groupsMap.put(cursorGroup.getString(cursorGroup.getColumnIndex(Groups._ID)), cursorGroup.getString(cursorGroup.getColumnIndex("title")));
		}
		if (cursorGroup != null) {
			cursorGroup.close();
		}
		Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI, null, null, null, Data.RAW_CONTACT_ID);
		//System.out.println(cursor.getCount());
		int numm = 0;
		while (cursor.moveToNext()) {
			contactId = cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
			if (oldrid != contactId) {
				jsonObject = new JSONObject();
				contactData.putOpt("contact" + numm, jsonObject);
				numm++;
				oldrid = contactId;
			}
			if (contactId == 1892) {
				int i = 0;
				i++;
			}

			/*
			 * 取得mimetype类型
			 * MIME types are:
			 * CommonDataKinds.StructuredName StructuredName.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Phone Phone.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Email Email.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Photo Photo.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Organization Organization.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Im Im.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Nickname Nickname.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Note Note.CONTENT_ITEM_TYPE
			 * CommonDataKinds.StructuredPostal StructuredPostal.CONTENT_ITEM_TYPE
			 * CommonDataKinds.GroupMembership GroupMembership.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Website Website.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Event Event.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Relation Relation.CONTENT_ITEM_TYPE
			 * CommonDataKinds.SipAddress SipAddress.CONTENT_ITEM_TYPE
			 */
			mimetype = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));

			if (mimetype.equals(StructuredName.CONTENT_ITEM_TYPE)) {//姓名
				JSONObject name = jsonObject.optJSONObject("name");
				if (name == null) {
					name = new JSONObject();
					jsonObject.putOpt("name", name);
				}

				//名称前缀
				String prefix = cursor.getString(cursor.getColumnIndex(StructuredName.PREFIX));
				name.putOpt("prefix", prefix);

				//姓氏
				String family_name = cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME));
				name.putOpt("family_name", family_name);

				//中间名
				String middleName = cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME));
				name.putOpt("middle_name", middleName);

				//名字
				String given_name = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
				name.putOpt("given_name", given_name);

				//名称后缀
				String suffix = cursor.getString(cursor.getColumnIndex(StructuredName.SUFFIX));
				name.putOpt("suffix", suffix);

				String phonetic_given_name = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME));
				name.putOpt("phonetic_given_name", phonetic_given_name);

				String phonetic_middle_name = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_MIDDLE_NAME));
				name.putOpt("phonetic_middle_name", phonetic_middle_name);

				String phonetic_family_name = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME));
				name.putOpt("phonetic_family_name", phonetic_family_name);

			} else if (mimetype.equals(Phone.CONTENT_ITEM_TYPE)) {//电话
				//JSONObject phone=new JSONObject();
				JSONObject phone = jsonObject.optJSONObject("phone");
				if (phone == null) {
					phone = new JSONObject();
					jsonObject.putOpt("phone", phone);
				}

				// 取出电话类型
				int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
				String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));

				switch (phoneType) {
				case Phone.TYPE_MOBILE:
					phone.accumulate("mobile", number);
					break;
				case Phone.TYPE_WORK:
					phone.accumulate("work", number);
					break;
				case Phone.TYPE_HOME:
					phone.accumulate("home", number);
					break;
				case Phone.TYPE_MAIN:
					phone.accumulate("main", number);
					break;
				case Phone.TYPE_FAX_WORK:
					phone.accumulate("fax_work", number);
					break;
				case Phone.TYPE_FAX_HOME:
					phone.accumulate("fax_home", number);
					break;
				case Phone.TYPE_PAGER:
					phone.accumulate("pager", number);
					break;
				case Phone.TYPE_CALLBACK:
					phone.accumulate("callback", number);
					break;
				case Phone.TYPE_CAR:
					phone.accumulate("car", number);
					break;
				case Phone.TYPE_COMPANY_MAIN:
					phone.accumulate("company_main", number);
					break;
				case Phone.TYPE_ISDN:
					phone.accumulate("isdn", number);
					break;
				case Phone.TYPE_OTHER_FAX:
					phone.accumulate("other_fax", number);
					break;
				case Phone.TYPE_RADIO:
					phone.accumulate("radio", number);
					break;
				case Phone.TYPE_TELEX:
					phone.accumulate("telex", number);
					break;
				case Phone.TYPE_TTY_TDD:
					phone.accumulate("tty_tdd", number);
					break;
				case Phone.TYPE_WORK_MOBILE:
					phone.accumulate("work_mobile", number);
					break;
				case Phone.TYPE_WORK_PAGER:
					phone.accumulate("work_pager", number);
					break;
				case Phone.TYPE_ASSISTANT:
					phone.accumulate("assistant", number);
					break;
				case Phone.TYPE_MMS:
					phone.accumulate("mms", number);
					break;
				case Phone.TYPE_OTHER:
					phone.accumulate("other", number);
					break;
				case Phone.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Phone.LABEL));
					if (label == null) {
						label = "custom";
					}
					phone.accumulate(label, number);
					break;
				default:
					break;
				}
			} else if (mimetype.equals(Email.CONTENT_ITEM_TYPE)) {//邮件
				JSONObject email = jsonObject.optJSONObject("email");
				if (email == null) {
					email = new JSONObject();
					jsonObject.putOpt("email", email);
				}

				// 取出邮件类型
				int emailType = cursor.getInt(cursor.getColumnIndex(Email.TYPE));

				String address = cursor.getString(cursor.getColumnIndex(Email.DATA));
				switch (emailType) {
				case Email.TYPE_WORK:
					email.accumulate("work", address);
					break;
				case Email.TYPE_HOME:
					email.accumulate("home", address);
					break;
				case Email.TYPE_MOBILE:
					email.accumulate("mobile", address);
					break;
				case Email.TYPE_OTHER:
					email.accumulate("other", address);
					break;
				case Email.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Email.LABEL));
					if (label == null) {
						label = "custom";
					}
					email.accumulate(label, address);
					break;
				}

			} else if (mimetype.equals(Photo.CONTENT_ITEM_TYPE)) {//照片

			} else if (mimetype.equals(Organization.CONTENT_ITEM_TYPE)) {//组织
				JSONObject company = jsonObject.optJSONObject("company");
				if (company == null) {
					company = new JSONObject();
					jsonObject.putOpt("company", company);
				}
				company.accumulate("company", cursor.getString(cursor.getColumnIndex(Organization.COMPANY)));
				company.accumulate("department", cursor.getString(cursor.getColumnIndex(Organization.DEPARTMENT)));
				company.accumulate("position", cursor.getString(cursor.getColumnIndex(Organization.TITLE)));
				company.accumulate("job_description", cursor.getString(cursor.getColumnIndex(Organization.JOB_DESCRIPTION)));
				company.accumulate("office_location", cursor.getString(cursor.getColumnIndex(Organization.OFFICE_LOCATION)));

			} else if (mimetype.equals(Im.CONTENT_ITEM_TYPE)) {//即时通讯
				JSONObject im = jsonObject.optJSONObject("im");
				if (im == null) {
					im = new JSONObject();
					jsonObject.putOpt("im", im);
				}
				int protocalType = cursor.getInt(cursor.getColumnIndex(Im.PROTOCOL));

				String data = cursor.getString(cursor.getColumnIndex(Im.DATA));

				switch (protocalType) {
				case Im.PROTOCOL_QQ:
					im.accumulate("qq", data);
					break;
				case Im.PROTOCOL_AIM:
					im.accumulate("aim", data);
					break;
				case Im.PROTOCOL_MSN:
					im.accumulate("msn", data);
					break;
				case Im.PROTOCOL_YAHOO:
					im.accumulate("yahoo", data);
					break;
				case Im.PROTOCOL_SKYPE:
					im.accumulate("skype", data);
					break;
				case Im.PROTOCOL_GOOGLE_TALK:
					im.accumulate("google_talk", data);
					break;
				case Im.PROTOCOL_ICQ:
					im.accumulate("icq", data);
					break;
				case Im.PROTOCOL_JABBER:
					im.accumulate("jabber", data);
					break;
				case Im.PROTOCOL_NETMEETING:
					im.accumulate("netmeeting", data);
					break;
				case Im.PROTOCOL_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Im.LABEL));
					if (label == null) {
						label = cursor.getString(cursor.getColumnIndex("DATA6"));
					}
					if (label == null) {
						label = "custom";
					}
					im.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(Nickname.CONTENT_ITEM_TYPE)) {//称呼
				JSONObject nickname = jsonObject.optJSONObject("nickname");
				if (nickname == null) {
					nickname = new JSONObject();
					jsonObject.putOpt("nickname", nickname);
				}
				String data = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
				nickname.accumulate("nickname", data);

			} else if (mimetype.equals(Note.CONTENT_ITEM_TYPE)) {//备注

				JSONObject note = jsonObject.optJSONObject("note");
				if (note == null) {
					note = new JSONObject();
					jsonObject.putOpt("note", note);
				}
				String data = cursor.getString(cursor.getColumnIndex(Note.NOTE));
				note.accumulate("note", data);

			} else if (mimetype.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {//地址
				JSONObject address = jsonObject.optJSONObject("address");
				if (address == null) {
					address = new JSONObject();
					jsonObject.putOpt("address", address);
				}
				String data = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
				int postalType = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE));

				switch (postalType) {
				case StructuredPostal.TYPE_WORK:
					address.accumulate("work", data);
					break;
				case StructuredPostal.TYPE_HOME:
					address.accumulate("home", data);
					break;
				case StructuredPostal.TYPE_OTHER:
					address.accumulate("other", data);
					break;
				case StructuredPostal.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Im.LABEL));
					if (label == null) {
						label = "custom";
					}
					address.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(GroupMembership.CONTENT_ITEM_TYPE)) {//群组关系
				JSONObject group = jsonObject.optJSONObject("group");
				if (group == null) {
					group = new JSONObject();
					jsonObject.putOpt("group", group);
				}
				String data = cursor.getString(cursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
				if (groupsMap != null) {
					data = groupsMap.get(data);
					group.accumulate("group", data);
				}

			} else if (mimetype.equals(Website.CONTENT_ITEM_TYPE)) {//网站
				JSONObject url = jsonObject.optJSONObject("url");
				if (url == null) {
					url = new JSONObject();
					jsonObject.putOpt("url", url);
				}
				int webType = cursor.getInt(cursor.getColumnIndex(Website.TYPE));
				String data = cursor.getString(cursor.getColumnIndex(Website.URL));

				switch (webType) {
				case Website.TYPE_HOME:
					url.accumulate("home", data);
					break;
				case Website.TYPE_HOMEPAGE:
					url.accumulate("homepage", data);
					break;
				case Website.TYPE_WORK:
					url.accumulate("work", data);
					break;
				case Website.TYPE_BLOG:
					url.accumulate("blog", data);
					break;
				case Website.TYPE_PROFILE:
					url.accumulate("profile", data);
					break;
				case Website.TYPE_FTP:
					url.accumulate("ftp", data);
					break;
				case Website.TYPE_OTHER:
					//website.accumulate("other", data);
					url.accumulate("other", data);
					break;
				case Website.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Website.LABEL));
					if (label == null) {
						label = "custom";
					}
					url.accumulate(label, data);
					break;
				}
			} else if (mimetype.equals(Event.CONTENT_ITEM_TYPE)) {//事件
				JSONObject dates = jsonObject.optJSONObject("dates");
				if (dates == null) {
					dates = new JSONObject();
					jsonObject.putOpt("dates", dates);
				}

				int eventType = cursor.getInt(cursor.getColumnIndex(Event.TYPE));
				String data = cursor.getString(cursor.getColumnIndex(Event.START_DATE));

				switch (eventType) {
				case Event.TYPE_BIRTHDAY:
					dates.accumulate("birthday", data);
					break;
				case Event.TYPE_ANNIVERSARY:
					dates.accumulate("anniversary", data);
					break;
				case Event.TYPE_OTHER:
					dates.accumulate("other", data);
					break;
				case Event.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Event.LABEL));
					if (label == null) {
						label = "custom";
					}
					dates.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(Relation.CONTENT_ITEM_TYPE)) {//关系
				JSONObject relation = jsonObject.optJSONObject("relation");
				if (relation == null) {
					relation = new JSONObject();
					jsonObject.putOpt("relation", relation);
				}

				int eventType = cursor.getInt(cursor.getColumnIndex(Relation.TYPE));
				String data = cursor.getString(cursor.getColumnIndex(Relation.NAME));

				switch (eventType) {
				case Relation.TYPE_ASSISTANT:
					relation.accumulate("assistant", data);
					break;
				case Relation.TYPE_BROTHER:
					relation.accumulate("brother", data);
					break;
				case Relation.TYPE_CHILD:
					relation.accumulate("child", data);
					break;
				case Relation.TYPE_DOMESTIC_PARTNER:
					relation.accumulate("domestic_partner", data);
					break;
				case Relation.TYPE_FATHER:
					relation.accumulate("father", data);
					break;
				case Relation.TYPE_FRIEND:
					relation.accumulate("friend", data);
					break;
				case Relation.TYPE_MANAGER:
					relation.accumulate("manager", data);
					break;
				case Relation.TYPE_MOTHER:
					relation.accumulate("mother", data);
					break;
				case Relation.TYPE_PARENT:
					relation.accumulate("parent", data);
					break;
				case Relation.TYPE_PARTNER:
					relation.accumulate("partner", data);
					break;
				case Relation.TYPE_REFERRED_BY:
					relation.accumulate("referred_by", data);
					break;
				case Relation.TYPE_RELATIVE:
					relation.accumulate("relative", data);
					break;
				case Relation.TYPE_SISTER:
					relation.accumulate("sister", data);
					break;
				case Relation.TYPE_SPOUSE:
					relation.accumulate("spouse", data);
					break;
				case Relation.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Event.LABEL));
					if (label == null) {
						label = "custom";
					}
					relation.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(SipAddress.CONTENT_ITEM_TYPE)) {//互联网通话
				JSONObject sip = jsonObject.optJSONObject("sip");
				if (sip == null) {
					sip = new JSONObject();
					jsonObject.putOpt("sip", sip);
				}
				String data = cursor.getString(cursor.getColumnIndex(SipAddress.SIP_ADDRESS));
				sip.accumulate("sip", data);

			}

			if (1 == 1) {
				continue;
			}

			/**
			 * 获得通讯录中联系人的名字
			 * (前缀,姓,中间名,名,后缀,拼音[姓,中间名,名],公司,职位)
			 */
			if (StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)) {

				//全名
				String display_name = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));

				//名称前缀
				String prefix = cursor.getString(cursor.getColumnIndex(StructuredName.PREFIX));
				jsonObject.putOpt("name_prefix", prefix);

				//姓氏
				String firstName = cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME));
				jsonObject.putOpt("name_firstName", firstName);

				//中间名
				String middleName = cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME));
				jsonObject.putOpt("name_middleName", middleName);

				//名字
				String lastname = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
				jsonObject.putOpt("name_lastname", lastname);

				//名称后缀
				String suffix = cursor.getString(cursor.getColumnIndex(StructuredName.SUFFIX));
				jsonObject.putOpt("name_suffix", suffix);

				/*
				 * String phoneticFirstName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME));
				 * jsonObject.putOpt("phoneticFirstName", phoneticFirstName);
				 * String phoneticMiddleName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_MIDDLE_NAME));
				 * jsonObject.putOpt("phoneticMiddleName", phoneticMiddleName);
				 * String phoneticLastName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME));
				 * jsonObject.putOpt("phoneticLastName", phoneticLastName);
				 */
			}

			/**
			 * 获取组织信息
			 * (公司,部门,职位)
			 */
			if (Organization.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// 取出组织类型
				int orgType = cursor.getInt(cursor.getColumnIndex(Organization.TYPE));

				if (orgType == Organization.TYPE_CUSTOM) {
					//     if (orgType == Organization.TYPE_WORK) {

					//公司
					String company = cursor.getString(cursor.getColumnIndex(Organization.COMPANY));
					jsonObject.putOpt("company_company", company);

					//部门
					String department = cursor.getString(cursor.getColumnIndex(Organization.DEPARTMENT));
					jsonObject.putOpt("company_department", department);

					//职位
					String jobTitle = cursor.getString(cursor.getColumnIndex(Organization.TITLE));
					jsonObject.putOpt("company_jobTitle", jobTitle);
				}
			}

			/**
			 * 获取电话信息
			 * (手机,单位,住宅,总机,单位传真,住宅传真,寻呼机,其他,自定义)
			 */
			if (Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// 取出电话类型
				int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));

				// 手机
				if (phoneType == Phone.TYPE_MOBILE) {
					String mobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_mobile", mobile);
				}

				// 单位电话
				if (phoneType == Phone.TYPE_WORK) {
					String jobNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_jobNum", jobNum);
				}

				// 住宅电话
				if (phoneType == Phone.TYPE_HOME) {
					String homeNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_homeNum", homeNum);
				}

				// 总机
				if (phoneType == Phone.TYPE_MAIN) {
					String jobTel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_mainTel", jobTel);
				}

				/*
				 * // 公司总机
				 * if (phoneType == Phone.TYPE_COMPANY_MAIN) {
				 * String jobTel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("number_jobTel", jobTel);
				 * }
				 */

				// 单位传真
				if (phoneType == Phone.TYPE_FAX_WORK) {
					String workFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_workFax", workFax);
				}
				// 住宅传真
				if (phoneType == Phone.TYPE_FAX_HOME) {
					String homeFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_homeFax", homeFax);
				}
				// 寻呼机
				if (phoneType == Phone.TYPE_PAGER) {
					String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_pager", pager);
				}

				// 其他
				if (phoneType == Phone.TYPE_OTHER) {
					String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_other", pager);
				}

				// 自定义
				if (phoneType == Phone.TYPE_CUSTOM) {
					String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_custom", pager);
				}

				/*
				 * // 回拨号码
				 * if (phoneType == Phone.TYPE_CALLBACK) {
				 * String quickNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("quickNum", quickNum);
				 * }
				 * 
				 * // 车载电话
				 * if (phoneType == Phone.TYPE_CAR) {
				 * String carNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("carNum", carNum);
				 * }
				 * // ISDN
				 * if (phoneType == Phone.TYPE_ISDN) {
				 * String isdn = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("isdn", isdn);
				 * }
				 * 
				 * // 无线装置
				 * if (phoneType == Phone.TYPE_RADIO) {
				 * String wirelessDev = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("wirelessDev", wirelessDev);
				 * }
				 * // 电报
				 * if (phoneType == Phone.TYPE_TELEX) {
				 * String telegram = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("telegram", telegram);
				 * }
				 * // TTY_TDD
				 * if (phoneType == Phone.TYPE_TTY_TDD) {
				 * String tty_tdd = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("tty_tdd", tty_tdd);
				 * }
				 * // 单位手机
				 * if (phoneType == Phone.TYPE_WORK_MOBILE) {
				 * String jobMobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("jobMobile", jobMobile);
				 * }
				 * // 单位寻呼机
				 * if (phoneType == Phone.TYPE_WORK_PAGER) {
				 * String jobPager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("jobPager", jobPager);
				 * }
				 * // 助理
				 * if (phoneType == Phone.TYPE_ASSISTANT) {
				 * String assistantNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("assistantNum", assistantNum);
				 * }
				 * // 彩信
				 * if (phoneType == Phone.TYPE_MMS) {
				 * String mms = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("mms", mms);
				 * }
				 */
			}

			/**
			 * 查找email地址
			 * (工作,个人,其他,手机,自定义)
			 */
			if (Email.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// 取出邮件类型
				int emailType = cursor.getInt(cursor.getColumnIndex(Email.TYPE));

				// 单位邮件地址
				if (emailType == Email.TYPE_WORK) {
					String jobEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_jobEmail", jobEmail);
				}

				// 个人邮件地址
				if (emailType == Email.TYPE_HOME) {
					String homeEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_homeEmail", homeEmail);
				}

				// 手机邮件地址
				if (emailType == Email.TYPE_MOBILE) {
					String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_mobileEmail", mobileEmail);
				}

				// 其他邮件地址
				if (emailType == Email.TYPE_OTHER) {
					String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_otherEmail", mobileEmail);
				}

				// 自定义邮件地址
				if (emailType == Email.TYPE_CUSTOM) {
					String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_customEmail", mobileEmail);
				}

			}

			/**
			 *  即时消息
			 *  (QQ,AIM,Windows Live,雅虎,Skype,环聊,ICQ,Jabber)
			 */
			if (Im.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// 取出即时消息类型
				int protocal = cursor.getInt(cursor.getColumnIndex(Im.PROTOCOL));

				//QQ
				if (protocal == Im.PROTOCOL_QQ) {
					String instantsMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_QQ", instantsMsg);
				}

				//AIM
				if (protocal == Im.PROTOCOL_AIM) {
					String workMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_AIM", workMsg);
				}

				//MSN
				if (protocal == Im.PROTOCOL_MSN) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_MSN", msg);
				}

				//YAHOO
				if (protocal == Im.PROTOCOL_YAHOO) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_YAHOO", msg);
				}

				//SKYPE
				if (protocal == Im.PROTOCOL_SKYPE) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_SKYPE", msg);
				}

				//环聊
				if (protocal == Im.PROTOCOL_GOOGLE_TALK) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_GOOGLE_TALK", msg);
				}

				//ICQ
				if (protocal == Im.PROTOCOL_ICQ) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_ICQ", msg);
				}

				//JABBER
				if (protocal == Im.PROTOCOL_JABBER) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_JABBER", msg);
				}

				//自定义
				if (protocal == Im.TYPE_CUSTOM) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_CUSTOM", msg);
				}
			}

			/**
			 * 查找地址
			 * (单位,住宅,其他,自定义)
			 */
			if (StructuredPostal.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// 取出地址类型
				int postalType = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE));

				// 单位通讯地址
				if (postalType == StructuredPostal.TYPE_WORK) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);

					String street = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("street", street);
					String ciry = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("ciry", ciry);
					String box = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("box", box);
					String area = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("area", area);
					String state = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("state", state);
					String zip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("zip", zip);
					String country = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("country", country);
				}
				// 住宅通讯地址
				if (postalType == StructuredPostal.TYPE_HOME) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);
					String homeStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("homeStreet", homeStreet);
					String homeCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("homeCity", homeCity);
					String homeBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("homeBox", homeBox);
					String homeArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("homeArea", homeArea);
					String homeState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("homeState", homeState);
					String homeZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("homeZip", homeZip);
					String homeCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("homeCountry", homeCountry);
				}
				// 其他通讯地址
				if (postalType == StructuredPostal.TYPE_OTHER) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);
					String otherStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("otherStreet", otherStreet);
					String otherCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("otherCity", otherCity);
					String otherBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("otherBox", otherBox);
					String otherArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("otherArea", otherArea);
					String otherState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("otherState", otherState);
					String otherZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("otherZip", otherZip);
					String otherCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("otherCountry", otherCountry);
				}
				// 自定义通讯地址
				if (postalType == StructuredPostal.TYPE_CUSTOM) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);
					String otherStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("customStreet", otherStreet);
					String otherCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("otherCity", otherCity);
					String otherBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("otherBox", otherBox);
					String otherArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("otherArea", otherArea);
					String otherState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("otherState", otherState);
					String otherZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("otherZip", otherZip);
					String otherCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("otherCountry", otherCountry);
				}

				/**
				 * 获取昵称信息
				 * (称呼)
				 */
				if (Nickname.CONTENT_ITEM_TYPE.equals(mimetype)) {
					String nickName = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
					jsonObject.putOpt("nickName", nickName);
				}

				/**
				 *获取网站信息
				 *(网站)
				 */
				if (Website.CONTENT_ITEM_TYPE.equals(mimetype)) {
					// 取出组织类型
					int webType = cursor.getInt(cursor.getColumnIndex(Website.TYPE));

					// 主页
					if (webType == Website.TYPE_CUSTOM) {
						String home = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_custom", home);
					}
					// 主页
					if (webType == Website.TYPE_HOME) {
						String home = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_home", home);
					}

					// 个人主页
					if (webType == Website.TYPE_HOMEPAGE) {
						String homePage = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_homePage", homePage);
					}
					// 工作主页
					if (webType == Website.TYPE_WORK) {
						String workPage = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_workPage", workPage);
					}
				}

				/**
				 * 互联网通话
				 */

				/**
				 * 查找event(生日,周年纪念日)
				 * (生日,周年纪念日,其他)
				 */
				if (Event.CONTENT_ITEM_TYPE.equals(mimetype)) {
					// 取出时间类型
					int eventType = cursor.getInt(cursor.getColumnIndex(Event.TYPE));
					// 生日
					if (eventType == Event.TYPE_BIRTHDAY) {
						String birthday = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("birthday", birthday);
					}
					// 周年纪念日
					if (eventType == Event.TYPE_ANNIVERSARY) {
						String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("anniversary", anniversary);
					}
					// 其他
					if (eventType == Event.TYPE_OTHER) {
						String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("event_other", anniversary);
					}

					// 自定义
					if (eventType == Event.TYPE_CUSTOM) {
						String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("event_custom", anniversary);
					}

				}

				/**
				 * 获取备注信息
				 * (备注)
				 */
				if (Note.CONTENT_ITEM_TYPE.equals(mimetype)) {
					String remark = cursor.getString(cursor.getColumnIndex(Note.NOTE));
					jsonObject.putOpt("remark", remark);
				}

				/**
				 * 群组
				 */
				if (GroupMembership.CONTENT_ITEM_TYPE.equals(mimetype)) {
					String remark = cursor.getString(cursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
					jsonObject.putOpt("GroupMembership", remark);
				}
			}
		}
		cursor.close();
		//Log.i("contactData", contactData.toString());
		CLogUtils.write(contactData.toString());
		return contactData.toString();
	}
}
