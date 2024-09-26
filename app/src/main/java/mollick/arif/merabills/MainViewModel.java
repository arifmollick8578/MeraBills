package mollick.arif.merabills;

import static mollick.arif.merabills.utils.Constants.NO_PAYMENT_FOUND;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import mollick.arif.merabills.models.MeraBills;
import mollick.arif.merabills.models.PaymentData;
import mollick.arif.merabills.models.PaymentDetail;
import mollick.arif.merabills.models.SpinnerOption;
import mollick.arif.merabills.models.SpinnerOptionType;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<MeraBills> _meraBillsLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> _totalAmountHeaderLiveData = new MutableLiveData<>();
    /** Live data to get total amount header e.g. (Total Amount) */
    public LiveData<String> totalAmountHeaderLiveData = _totalAmountHeaderLiveData;

    private final MutableLiveData<Double> _totalAmountValueLiveData = new MutableLiveData<>(0.0);
    /** Live data to get total amount value e.g. (22.0), take double to support precisions. */
    public LiveData<Double> totalAmountValueLiveData = _totalAmountValueLiveData;

    private final MutableLiveData<String> _paymentHeaderLiveData = new MutableLiveData<>();
    /** Live data to get payment header e.g. (Payments). */
    public LiveData<String> paymentHeaderLiveData = _paymentHeaderLiveData;

    private final MutableLiveData<ArrayList<PaymentDetail>> _paymentDetailsLiveData = new MutableLiveData<>();
    /** Live data to get all payment details. */
    public LiveData<List<PaymentDetail>> paymentDetailsLiveData =
            Transformations.map(_paymentDetailsLiveData, Collections::unmodifiableList);

    /** Live data to generate message when no payment is shown. */
    public LiveData<String> noPaymentMessageLiveData =
            Transformations.map(_paymentDetailsLiveData, (paymentDetails -> {
                return paymentDetails.size() < 1 ? NO_PAYMENT_FOUND : "";
            }));

    private final MutableLiveData<String> _addPaymentTextLiveData = new MutableLiveData<>();
    /** Live data to get add payment button text e.g. (Add Payment). */
    public LiveData<String> addPaymentTextLiveData = _addPaymentTextLiveData;

    private final MutableLiveData<String> _saveTextLiveData = new MutableLiveData<>();
    /** Live data to get save button text e.g. (Save). */
    public LiveData<String> saveTextLiveData = _saveTextLiveData;

    private final ArrayList<SpinnerOptionType> _alreadyAddedPaymentTypes = new ArrayList<>();

    private final ArrayList<SpinnerOption> allOptions = new ArrayList<>();

    {
        setAllOptions();
        initData(MeraBills.DEFAULT);
    }

    public void reduceTotalAmount(Double amount) {
        _totalAmountValueLiveData.postValue(_totalAmountValueLiveData.getValue() - amount);
    }

    public void addTotalAmount(Double amount) {
        _totalAmountValueLiveData.postValue(_totalAmountValueLiveData.getValue() + amount);
    }

    public boolean addPaymentDetail(PaymentDetail paymentDetail) {
        boolean isAdded;
        ArrayList<PaymentDetail> paymentDetails = _paymentDetailsLiveData.getValue();
        if (paymentDetails == null) paymentDetails = new ArrayList<>();
        isAdded = paymentDetails.add(paymentDetail);
        _paymentDetailsLiveData.postValue(paymentDetails);
        _alreadyAddedPaymentTypes.add(paymentDetail.getSelectedOption().getType());
        return isAdded;
    }

    public boolean removePaymentDetail(PaymentData data) {
        PaymentDetail paymentDetail = null;
        boolean isRemoved = false;
        ArrayList<PaymentDetail> paymentDetails = _paymentDetailsLiveData.getValue();
        if (paymentDetails == null) {
            Log.d("MainViewModel", "Unable to delete payment detail, already empty.");
            return isRemoved;
        }
        for (PaymentDetail detail : paymentDetails) {
            if (Objects.equals(data.getTitle(), detail.getSelectedOption().getName())) {
                paymentDetail = detail;
            }
        }

        if (paymentDetail == null) {
            return isRemoved;
        } else {
            isRemoved = paymentDetails.remove(paymentDetail);
            _paymentDetailsLiveData.postValue(paymentDetails);
        }

        _alreadyAddedPaymentTypes.remove(paymentDetail.getSelectedOption().getType());
        return isRemoved;
    }

    public void initData(MeraBills meraBills) {
        _meraBillsLiveData.postValue(meraBills);
        for (PaymentDetail detail : meraBills.getPaymentDetails()) {
            _alreadyAddedPaymentTypes.add(detail.getSelectedOption().getType());
        }
        _totalAmountHeaderLiveData.postValue(meraBills.getTotalAmount());
        _totalAmountValueLiveData.postValue(meraBills.getTotalAmountValue());
        _paymentHeaderLiveData.postValue(meraBills.getPaymentsHeader());
        _paymentDetailsLiveData.postValue(new ArrayList(meraBills.getPaymentDetails()));
        _addPaymentTextLiveData.postValue(meraBills.getAddPaymentActionable());
        _saveTextLiveData.postValue(meraBills.getSave());
    }

    private void setAllOptions() {
        allOptions.add(new SpinnerOption("Cash", SpinnerOptionType.CASH));
        allOptions.add(new SpinnerOption("Bank Transfer", SpinnerOptionType.BANK_TRANSFER));
        allOptions.add(new SpinnerOption("Credit Card", SpinnerOptionType.CREDIT_CARD));
    }

    public List<SpinnerOption> getAvailableOptions() {
        List<SpinnerOption> options = new ArrayList<>();
        for (SpinnerOption option : allOptions) {
            if (!_alreadyAddedPaymentTypes.contains(option.getType())) {
                options.add(option);
            }
        }
        return options;
    }

    public MeraBills getMeraBills() {
        MeraBills meraBills = _meraBillsLiveData.getValue();
        if (meraBills == null) {
            meraBills = new MeraBills();
        }
        meraBills.setTotalAmount(_totalAmountHeaderLiveData.getValue());
        meraBills.setTotalAmountValue(_totalAmountValueLiveData.getValue());
        meraBills.setPaymentsHeader(_paymentHeaderLiveData.getValue());
        meraBills.setPaymentDetails(_paymentDetailsLiveData.getValue());
        meraBills.setAddPaymentActionable(_addPaymentTextLiveData.getValue());
        meraBills.setSave(_saveTextLiveData.getValue());
        meraBills.setNoPaymentsMessage(noPaymentMessageLiveData.getValue());
        return meraBills;
    }

}
