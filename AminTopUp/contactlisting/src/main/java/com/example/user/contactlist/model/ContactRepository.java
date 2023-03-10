package com.example.user.contactlist.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ContactRepository {

    private Context context;
    private List<Contact> contacts;

    public ContactRepository(Context context) {
        this.context = context;
        contacts = new ArrayList<>();
    }

    @SuppressLint("Range")
    public List<Contact> fetchContacts() {
        Contact contact;
        //hold a list of Contacts without duplicates
        Map<String, Contact> cleanList = new LinkedHashMap<String, Contact>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                contact = new Contact();
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                Log.e("contact", "getAllContacts: " + name + " " + phoneNo + " " + photoUri);
                contact.setName(name);
                contact.setPhoneNumber(formatPhoneNumber(phoneNo));
                contact.setPhotoUri(photoUri);
                //contacts.add(contact);
                cleanList.put(contact.getPhoneNumber(), contact);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return new ArrayList<Contact>(cleanList.values());
    }

    //Using LinkedHashMap to eliminate duplicate Contacts
    private List<Contact> clearListFromDuplicatePhoneNumber(List<Contact> list1) {
        Map<String, Contact> cleanMap = new LinkedHashMap<String, Contact>();
        for (int i = 0; i < list1.size(); i++) {
            cleanMap.put(list1.get(i).getPhoneNumber(), list1.get(i));
        }
        return new ArrayList<Contact>(cleanMap.values());
    }

    //Format Phone Number
    private static String formatPhoneNumber(String phone) {
        String formatedPhone = phone.replaceAll(" ", "");
        int phoneNumberLength = formatedPhone.length();
        if (phoneNumberLength == 13) {
            formatedPhone = "0" + formatedPhone.substring(4);
        } else if (phoneNumberLength == 12) {
            formatedPhone = "0" + formatedPhone.substring(3);
        } else if (phoneNumberLength == 10) {
            return formatedPhone;
        }
        return formatedPhone;
    }
}
