package mollick.arif.merabills.models;

import java.util.ArrayList;
import java.util.List;

/** Class to contains all data related to main activity. */
public class MeraBills {

    public static final MeraBills DEFAULT = new MeraBills(
            "Total Amount",
            0.0,
            "Payments",
            new ArrayList<>(),
            "Add payment",
            "",
            "Save"
    );
    private String totalAmount = "Total Amount";
    private Double totalAmountValue = 0.0;
    private String paymentsHeader = "Payments";
    private List<PaymentDetail> paymentDetails = new ArrayList<>();
    private String addPaymentActionable = "Add payment";
    private String noPaymentsMessage = "";
    private String save = "Save";

    public MeraBills() {
    }

    public MeraBills(String totalAmount, Double totalAmountValue, String paymentsHeader, List<PaymentDetail> paymentDetails, String addPaymentActionable, String noPaymentsMessage, String save) {
        this.totalAmount = totalAmount;
        this.totalAmountValue = totalAmountValue;
        this.paymentsHeader = paymentsHeader;
        this.paymentDetails = paymentDetails;
        this.addPaymentActionable = addPaymentActionable;
        this.noPaymentsMessage = noPaymentsMessage;
        this.save = save;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmountValue() {
        return totalAmountValue;
    }

    public void setTotalAmountValue(Double totalAmountValue) {
        this.totalAmountValue = totalAmountValue;
    }

    public String getPaymentsHeader() {
        return paymentsHeader;
    }

    public void setPaymentsHeader(String paymentsHeader) {
        this.paymentsHeader = paymentsHeader;
    }

    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getAddPaymentActionable() {
        return addPaymentActionable;
    }

    public void setAddPaymentActionable(String addPaymentActionable) {
        this.addPaymentActionable = addPaymentActionable;
    }

    public String getNoPaymentsMessage() {
        return paymentDetails.size() >= 1 ? null : noPaymentsMessage;
    }

    public void setNoPaymentsMessage(String noPaymentsMessage) {
        this.noPaymentsMessage = noPaymentsMessage;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
