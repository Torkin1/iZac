package view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import torkin.homework.cocktail.R;

public class NotImplementedDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icons8_construction_26);
        builder.setTitle(R.string.unimplemented_feature_title);
        builder.setMessage(R.string.unimplemented_feature_msg);
        builder.setPositiveButton(R.string.OK, null);
        return builder.create();
    }
}
