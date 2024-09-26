package mollick.arif.merabills.models;

/** Contains all information from add payment alert dialog. */
public class PaymentDetail {
    private Double amount;
    private SpinnerOption selectedOption;
    private String provider;
    private String transactionRef;

    public PaymentDetail() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SpinnerOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(SpinnerOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}
