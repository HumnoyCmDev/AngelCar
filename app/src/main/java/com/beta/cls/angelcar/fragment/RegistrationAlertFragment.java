package com.beta.cls.angelcar.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Patterns;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.util.RegistrationResult;

import java.util.regex.Pattern;

/**
 * Created by humnoyDeveloper on 3/18/2016 AD. 11:50
 */
public class RegistrationAlertFragment extends DialogFragment{
    public static final int REGISTRATION_OK = 0;
    public static final int REGISTRATION_CANCEL = 1;

    private String EMAIL_ADDRESS = "ไม่พบ Email";


    public static RegistrationAlertFragment newInstance() {
//        Bundle args = new Bundle();
        RegistrationAlertFragment fragment = new RegistrationAlertFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // GET Email // registration
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();

        if (accounts.length > 0 && emailPattern.matcher(accounts[0].name).matches())
            EMAIL_ADDRESS = accounts[0].name;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("ระบบลงทะเบียนด้วย Email")
                .setMessage("ลงทะเบียนด้วยบัญชี Email: "+EMAIL_ADDRESS)
                // Positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BusProvider.getInstance().post(new RegistrationResult(0));
                    }
                })

                // Negative Button
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,	int which) {
//                        BusProvider.getInstance().post(new RegistrationResult(1));
//                    }
//                })
                .create();
    }


}
