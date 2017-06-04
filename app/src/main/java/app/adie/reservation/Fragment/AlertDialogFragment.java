package app.adie.reservation.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import app.adie.reservation.entity.AlertDialogAction;

public class AlertDialogFragment extends DialogFragment implements OnClickListener {
    public static final int DIALOG_ALERT = 1;
    public static final int DIALOG_ALERT_NO_POSITIVE_BTN = 5;
    public static final int DIALOG_BASIC = 2;
    public static final int DIALOG_CUSTOM = 3;
    public static final int DIALOG_CUSTOM_VIEW = 4;
    private String mDialogKey;
    private int mDialogShow = 0;
    private String mDialogType;
    private String mMessage;
    private String mNegativeButton;
    private String mPositiveButton;
    private int mResourceView;
    private String mTitle;
    private View mView;



    public class DialogInitializer {
        private Context mContext;

        public DialogInitializer(Context context) {
            this.mContext = context;
        }

        public DialogInitializer setTitle(String title) {
            AlertDialogFragment.this.mTitle = title;
            return this;
        }

        public DialogInitializer setTitle(int title) {
            return setTitle(this.mContext.getString(title));
        }

        public DialogInitializer setMessage(String message) {
            AlertDialogFragment.this.mMessage = message;
            return this;
        }

        public DialogInitializer setMessage(int message) {
            return setMessage(this.mContext.getString(message));
        }

        public DialogInitializer setPositiveButton(String text) {
            AlertDialogFragment.this.mPositiveButton = text;
            return this;
        }

        public DialogInitializer setNegativeButton(String text) {
            AlertDialogFragment.this.mNegativeButton = text;
            return this;
        }

        public DialogInitializer setPositiveButton(int text) {
            return setPositiveButton(this.mContext.getString(text));
        }

        public DialogInitializer setNegativeButton(int text) {
            return setNegativeButton(this.mContext.getString(text));
        }

        public DialogInitializer setView(int resource) {
            AlertDialogFragment.this.mResourceView = resource;
            return this;
        }

        public DialogInitializer setView(View view) {
            AlertDialogFragment.this.mView = view;
            return this;
        }

        public DialogInitializer dialogKey(String key) {
            AlertDialogFragment.this.mDialogKey = key;
            return this;
        }

        public DialogInitializer dialogKey(int key) {
            return dialogKey(String.valueOf(key));
        }

        public DialogInitializer dialogType(String type) {
            AlertDialogFragment.this.mDialogType = type;
            return this;
        }
    }

    public static AlertDialogFragment newInstance() {
        return new AlertDialogFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        switch (this.mDialogShow) {
            case 1:
                return dialogAlert();
            case 2:
                return dialogBasic();
            case 3:
                return dialogCustom();
            case 4:
                return dialogCustomView();
            case 5:
                return dialogAlertNoPositiveButton();
            default:
                return null;
        }
    }

    private Dialog dialogAlert() {
        return new Builder(getActivity()).setMessage(this.mMessage).setPositiveButton(this.mPositiveButton, (OnClickListener) this).setNegativeButton(this.mNegativeButton, (OnClickListener) this).create();
    }

    private Dialog dialogAlertNoPositiveButton() {
        return new Builder(getActivity()).setMessage(this.mMessage).setNegativeButton(this.mNegativeButton, (OnClickListener) this).create();
    }

    private Dialog dialogBasic() {
        return new Builder(getActivity()).setTitle(this.mTitle).setMessage(this.mMessage).setPositiveButton(this.mPositiveButton, (OnClickListener) this).setNegativeButton(this.mNegativeButton, (OnClickListener) this).create();
    }

    private Dialog dialogCustom() {
        return new Builder(getActivity()).setTitle(this.mTitle).setView(this.mResourceView).setPositiveButton(this.mPositiveButton, (OnClickListener) this).setNegativeButton(this.mNegativeButton, (OnClickListener) this).create();
    }

    private Dialog dialogCustomView() {
        return new Builder(getActivity()).setView(this.mView).setPositiveButton(this.mPositiveButton, (OnClickListener) this).setNegativeButton(this.mNegativeButton, (OnClickListener) this).create();

    }

    public void onClick(DialogInterface dialogInterface, int whichButton) {
        switch (whichButton) {
            case -2:
                EventBus.getDefault().post(new AlertDialogAction("NEGATIVE", this.mDialogKey, this.mDialogType));
                dialogInterface.dismiss();
                return;
            case -1:
                EventBus.getDefault().post(new AlertDialogAction("POSITIVE", this.mDialogKey, this.mDialogType));
                return;
            default:
                return;
        }
    }

    public DialogInitializer init(Context context, int onShow) {
        this.mDialogShow = onShow;
        return new DialogInitializer(context);
    }
}
