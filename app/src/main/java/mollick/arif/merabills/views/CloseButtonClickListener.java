package mollick.arif.merabills.views;

import android.view.View;

import mollick.arif.merabills.models.PaymentData;

/** Listener to handle close button click. */
public interface CloseButtonClickListener {
    void onCloseButtonClicked(View v, PaymentData data);
}
