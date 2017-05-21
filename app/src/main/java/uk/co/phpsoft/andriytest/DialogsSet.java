package uk.co.phpsoft.andriytest;
/**
 * Created by Andriy on 21/05/2017.
 */

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Collection;

/**
 * Dialog generator layer for material-dialogs:
 * https://github.com/afollestad/material-dialogs
 */
public class DialogsSet extends Application{

    /**
     * Simple input field dialog
     */
    public static void showInputDialog(Context mContext, final EditText mText, int inputType) {

        new MaterialDialog.Builder(mContext)
                .title(R.string.input)
                .content(mText.getContentDescription())
                .inputType(inputType)
                .input(mText.getText(), mText.getHint(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        setMViewText(mText, input);
                    }
                }).show();
    }


    /**
     * multiselect checkbox dialog
     * @param mContext
     * @param mItems
     * @param mView
     */
    public static void showCheckboxDialog(Context mContext, Collection mItems, final EditText mView) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.enterTitle)
                .content(mView.getContentDescription())
                .items(mItems)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        CharSequence resText = "";

                        for (CharSequence cs : text){
                            resText = resText.length() == 0 ? cs.toString() : resText + ", " + cs.toString();
                        }

                        setMViewText(mView, resText);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
    }


    /**
     * Single select radio button dialog
     * @param mContext
     * @param mItems
     * @param mView
     */
    public static void showRadioButtonDialog(Context mContext, Collection mItems, final EditText mView) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.title)
                .content(mView.getContentDescription())
                .items(mItems)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        setMViewText(mView, text);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
    }


    /**
     * EditText generic setter
     * Called from dialogs callback function
     * @param mView
     * @param text
     */
    private static void setMViewText(EditText mView, CharSequence text) {
        mView.setText(text);
    }

}
