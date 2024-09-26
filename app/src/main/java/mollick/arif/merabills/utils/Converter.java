package mollick.arif.merabills.utils;

import mollick.arif.merabills.models.PaymentData;
import mollick.arif.merabills.models.PaymentDetail;

public class Converter {
    /** Function to convert [PaymentDetail] to [PaymentData]. */
    public static PaymentData toPaymentData(PaymentDetail detail) {
        return new PaymentData(
                detail.getSelectedOption().getName(),
                detail.getAmount()
        );
    }
}
