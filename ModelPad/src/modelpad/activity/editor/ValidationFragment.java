package modelpad.activity.editor;

import static com.google.common.base.Preconditions.checkNotNull;
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
import android.widget.TextView;

public class ValidationFragment extends DialogFragment {

	private Validator validator;

	public static ValidationFragment create(Validator validator) {
		ValidationFragment f = new ValidationFragment();
		f.validator = validator;
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		checkNotNull(validator, "Use static factory method.");
		int numOfMatched = validator.getAllMatched().size();
		int numOfUnexpected = validator.getAllUnexpected().size();
		int numOfExpected = validator.getAllExpected().size();
		int numOfMisplaced = validator.getAllMisplaced().size();
		int numOfMissing = numOfExpected - numOfMatched - numOfMisplaced;
		View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_feedback, null);
		setTextInLayout(dialogLayout, R.id.dialog_matched, String.valueOf(numOfMatched));
		setTextInLayout(dialogLayout, R.id.dialog_misplaced, String.valueOf(numOfMisplaced));
		setTextInLayout(dialogLayout, R.id.dialog_missing, String.valueOf(numOfMissing));
		setTextInLayout(dialogLayout, R.id.dialog_unexpected, String.valueOf(numOfUnexpected));
		String comment;
		if (validator.getMisplacedRefInfos().size() > 0) {
			comment = "Reference [" + validator.getMisplacedRefInfos().iterator().next().getName() + "] is misplaced.";
		} else if (validator.getMisplacedAttrs().size() > 0) {
			comment = "Attribute [" + validator.getMisplacedAttrs().iterator().next().getName() + "] is misplaced.";
		} else if (numOfMatched == numOfExpected) {
			comment = "Congrats!";
		} else {
			comment = "No helpful hint available, keep playing!";
		}
		setTextInLayout(dialogLayout, R.id.dialog_comment, comment);
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setView(dialogLayout).setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});
		return builder.create();
	}

	private void setTextInLayout(View dialogLayout, int id, String text) {
		TextView textView = (TextView) dialogLayout.findViewById(id);
		textView.setText(text);
	}
}
