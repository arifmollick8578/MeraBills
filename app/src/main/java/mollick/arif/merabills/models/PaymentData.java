package mollick.arif.merabills.models;

/** Contains data related to payment items. */
public class PaymentData {
    private String title;
    private Double value = 0.0;

    public PaymentData(String title, Double value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public Double getValue() {
        return value;
    }
}