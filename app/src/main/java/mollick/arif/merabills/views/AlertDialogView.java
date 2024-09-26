package mollick.arif.merabills.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import mollick.arif.merabills.R;
import mollick.arif.merabills.models.PaymentDetail;
import mollick.arif.merabills.models.SpinnerOption;
import mollick.arif.merabills.models.SpinnerOptionType;

/** Custom view to show on alert dialog to add payment. */
public class AlertDialogView extends ConstraintLayout {
    private final PaymentDetail paymentDetail = new PaymentDetail();
    private EditText paymentAmountEt;
    private EditText provider;
    private EditText transactionRef;
    private Spinner spinner;
    private TextView allAddedMessage;

    public AlertDialogView(Context context, List<SpinnerOption> options) {
        super(context);
        initView(context, options);
    }

    public AlertDialogView(Context context, AttributeSet attr, List<SpinnerOption> options) {
        super(context, attr);
        initView(context, options);
    }

    public AlertDialogView(Context context, AttributeSet attr, int defStyle, List<SpinnerOption> options) {
        super(context, attr, defStyle);
        initView(context, options);
    }

    private void initView(Context context, List<SpinnerOption> options) {
        View v = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null, false);
        initializeViews(v);
        setupSpinner(context, options);
        paymentAmountEt.setVisibility(options.size() >= 1 ? View.VISIBLE : View.GONE);
        allAddedMessage.setVisibility(options.size() < 1 ? View.VISIBLE : View.GONE);

        this.addView(v);
    }

    private void initializeViews(View v) {
        paymentAmountEt = v.findViewById(R.id.payment_amount_et);
        provider = v.findViewById(R.id.provider);
        transactionRef = v.findViewById(R.id.transaction_ref);
        spinner = v.findViewById(R.id.spinner);
        allAddedMessage = v.findViewById(R.id.all_added);
    }

    private void setupSpinner(Context context, List<SpinnerOption> options) {
        if (options.size() < 1) return;
        ArrayAdapter<SpinnerOption> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // set default payment option selection
        paymentDetail.setSelectedOption(options.get(0));

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SpinnerOption ro = (SpinnerOption) adapterView.getItemAtPosition(i);
                        if (ro.getType() != SpinnerOptionType.CASH) {
                            provider.setVisibility(View.VISIBLE);
                            transactionRef.setVisibility(View.VISIBLE);
                        } else {
                            provider.setVisibility(View.GONE);
                            transactionRef.setVisibility(View.GONE);
                        }
                        paymentDetail.setSelectedOption(ro);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );
    }

    public PaymentDetail getPaymentDetail() {
        if (allAddedMessage.getVisibility() == View.VISIBLE) {
            return null;
        }

        String amountText = paymentAmountEt.getText().toString();
        if (amountText.isEmpty()) {
            paymentDetail.setAmount(0.0);
        } else {
            paymentDetail.setAmount(
                    Double.parseDouble(amountText)
            );
        }

        if (paymentDetail.getSelectedOption().getType() != SpinnerOptionType.CASH) {
            paymentDetail.setProvider(this.provider.getText().toString());
            paymentDetail.setTransactionRef(this.transactionRef.getText().toString());
        } else {
            paymentDetail.setProvider(null);
            paymentDetail.setTransactionRef(null);
        }

        return paymentDetail;
    }
}
