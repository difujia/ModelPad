package modelpad.activity.editor;

import modelpad.activity.R;
import modelpad.metamodel.Validator;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ValidateFragment extends DialogFragment {

	private Validator validator;

	public static ValidateFragment create(Validator validator) {
		ValidateFragment f = new ValidateFragment();
		f.validator = validator;
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int numOfMatched = validator.getAllMatched().size();
		int numOfUnexpected = validator.getAllUnexpected().size();
		int numOfExpected = validator.getAllExpected().size();
		int numOfMisplaced = validator.getAllMisplaced().size();
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(createCellView("Matched", String.format("%d / %d", numOfMatched, numOfExpected),
				"Matched labels"));
		layout.addView(createCellView("Misplaced", String.valueOf(numOfMisplaced), "Misplaced labels"));
		layout.addView(createCellView("Unexpected", String.valueOf(numOfUnexpected), "Unexpected labels"));

		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setView(layout).setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});
		return builder.create();
	}

	private View createCellView(String title, String figure, String commemt) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_list_cell, null);

		TextView titleView = (TextView) view.findViewById(R.id.dialog_cell_title);
		titleView.setText(title);

		TextView figureView = (TextView) view.findViewById(R.id.dialog_cell_figure);
		figureView.setText(figure);

		TextView commentView = (TextView) view.findViewById(R.id.dialog_cell_comment);
		commentView.setText(commemt);

		return view;
	}
}
