package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.dream.NiuFaNet.Bean.Contact;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/17 0017.
 */
public class FrendsUtils {
    // 查询所有联系人的姓名，电话，邮箱

    public static List<Contact> loadContacts(Activity activity) throws Exception {
        Log.i("wwj","来到这了");

        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

        ContentResolver resolver = activity.getContentResolver();

        Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
                null);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        while (cursor.moveToNext()) {

            Contact contact = new Contact();
            int contractID = cursor.getInt(0);

            StringBuilder sb = new StringBuilder("contractID=");

            contact.setId(String.valueOf(contractID));
            sb.append(contractID);

            uri = Uri.parse("content://com.android.contacts/raw_contacts/"
                    + contractID + "/data");

            Cursor cursor1 = resolver.query(uri, new String[] { "mimetype",
                    "data1", "data2" }, null, null, null);

            while (cursor1.moveToNext()) {

                String data1 = cursor1.getString(cursor1
                        .getColumnIndex("data1"));
                contact.setEmail("");
                String mimeType = cursor1.getString(cursor1
                        .getColumnIndex("mimetype"));

                if ("vnd.android.cursor.item/name".equals(mimeType)) { // 是姓名
                    contact.setName(data1);
                    sb.append(",name=" + data1);

                } else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { // 邮箱
                    if (!TextUtils.isEmpty(data1)) {
                        contact.setEmail(data1);
                    }else {
                        contact.setEmail("");
                    }
                    //contacts.add(contact);
                    sb.append(",email=" + data1);

                } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { // 手机
                    contact.setPhoneNumber(data1.replaceAll("-", "").replace(" ",""));
                    sb.append(",phone=" + data1.replaceAll("-", "").replace(" ",""));
                }

            }
            Log.i("wwj", new Gson().toJson(contact)+"联系人");
            contacts.add(contact);
            cursor1.close();

            Log.i("wwj", sb.toString());

        }

        cursor.close();
        return contacts;

    }
}
