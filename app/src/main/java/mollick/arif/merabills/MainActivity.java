package mollick.arif.merabills;

import static mollick.arif.merabills.utils.Constants.FILE_NAME;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import mollick.arif.merabills.models.PaymentData;
import mollick.arif.merabills.models.PaymentDetail;
import mollick.arif.merabills.utils.Converter;
import mollick.arif.merabills.utils.FileContentUtils;
import mollick.arif.merabills.utils.FileUtils;
import mollick.arif.merabills.views.AlertDialogView;
import mollick.arif.merabills.views.CloseButtonClickListener;
import mollick.arif.merabills.views.PaymentItemView;

public class MainActivity extends AppCompatActivity implements CloseButtonClickListener {

    private TextView totalAmountTitle;
    private TextView totalAmountValue;
    private TextView paymentHeader;
    private LinearLayout paymentContainer;
    private TextView addPaymentButton;
    private Button saveButton;
    private TextView noPayment;
    private MainViewModel viewModel;
    private boolean isDialogAlreadyShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initializeViews();
        setupViews();
        initObservers();
        if (viewModel.paymentDetailsLiveData.getValue() == null) {
            readDataFromFile();
        }
    }

    private void initializeViews() {
        totalAmountTitle = findViewById(R.id.total_amount);
        totalAmountValue = findViewById(R.id.total_amount_value);
        paymentHeader = findViewById(R.id.payments_header);
        paymentContainer = findViewById(R.id.payment_container);
        addPaymentButton = findViewById(R.id.add_payment);
        saveButton = findViewById(R.id.save_button);
        noPayment = findViewById(R.id.no_payment_text);
    }

    private void setupViews() {
        addPaymentButton.setPaintFlags(addPaymentButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        addPaymentButton.setOnClickListener(this::showPaymentDialog);
        saveButton.setOnClickListener((view) -> {
            boolean isSuccess = FileUtils.writeToFile(this, FILE_NAME, generateJsonValues());
            int messageResId = isSuccess ? R.string.payment_saved_message : R.string.error_message;
            showSnackbar(view, messageResId);
        });
    }

    private void showPaymentDialog(View view) {
        if (isDialogAlreadyShowing) return;

        // content of the alert dialog view (amount view, spinner e.g.)
        AlertDialogView alertDialogView = new AlertDialogView(this, viewModel.getAvailableOptions());

        new AlertDialog.Builder(view.getContext())
                .setTitle("Add Payment")
                .setPositiveButton("OK", (DialogInterface dialogInterface, int i) -> {
                            PaymentDetail newlyAddedPaymentDetail = alertDialogView.getPaymentDetail();
                            if (newlyAddedPaymentDetail == null) return;
                            paymentContainer.addView(
                                    new PaymentItemView(
                                            this,
                                            Converter.toPaymentData(newlyAddedPaymentDetail),
                                            this
                                    )
                            );
                            viewModel.addTotalAmount(newlyAddedPaymentDetail.getAmount());
                            boolean isAdded = viewModel.addPaymentDetail(newlyAddedPaymentDetail);
                            int messageResId = isAdded ? R.string.payment_added_message : R.string.error_message;
                            showSnackbar(view, messageResId);
                            isDialogAlreadyShowing = false;
                        }
                )
                .setNegativeButton("CANCEL", (DialogInterface dialogInterface, int i) -> {
                    isDialogAlreadyShowing = false;
                })
                .setView(alertDialogView)
                .setCancelable(false)
                .create()
                .show();
        isDialogAlreadyShowing = true;
    }

    private void initObservers() {
        viewModel.totalAmountHeaderLiveData.observe(this, (header) -> {
            totalAmountTitle.setText(
                    getString(R.string.total_amount_header, header)
            );
        });

        viewModel.totalAmountValueLiveData.observe(this, (value) -> {
            totalAmountValue.setText(value.toString());
        });

        viewModel.paymentHeaderLiveData.observe(this, (value) -> {
            paymentHeader.setText(value);
        });

        viewModel.paymentDetailsLiveData.observe(this, (paymentDetails -> {
            paymentContainer.removeAllViews();
            if (paymentDetails.size() >= 1) {
                for (PaymentDetail detail : paymentDetails) {
                    paymentContainer.addView(
                            new PaymentItemView(this, Converter.toPaymentData(detail), this)
                    );
                }
            }
        }));

        viewModel.addPaymentTextLiveData.observe(this, (value) -> {
            addPaymentButton.setText(value);
        });

        viewModel.saveTextLiveData.observe(this, (value) -> {
            saveButton.setText(value);
        });

        viewModel.noPaymentMessageLiveData.observe(this, (value) -> {
            noPayment.setText(value);
            if (value.isEmpty()) {
                noPayment.setVisibility(View.GONE);
            } else {
                noPayment.setVisibility(View.VISIBLE);
            }
        });
    }

    private void readDataFromFile() {
        String result = FileUtils.readFileFrom(this, FILE_NAME);
        if (result != null) {
            viewModel.initData(
                    FileContentUtils.fetchData(result)
            );
        }
    }

    private String generateJsonValues() {
        // Generate JSON from mera bills class data
        return FileContentUtils.generateJSON(viewModel.getMeraBills());
    }

    @Override
    public void onCloseButtonClicked(View v, PaymentData data) {
        viewModel.reduceTotalAmount(data.getValue());
        boolean isRemoved = viewModel.removePaymentDetail(data);
        int removedMessage = isRemoved ? R.string.payment_removed_message : R.string.error_message;
        showSnackbar(v, removedMessage);
    }

    private void showSnackbar(View v, int messageResId) {
        Snackbar.make(v, getString(messageResId), Snackbar.LENGTH_LONG).show();
    }
}
