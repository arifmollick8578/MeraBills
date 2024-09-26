package mollick.arif.merabills.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import mollick.arif.merabills.R;
import mollick.arif.merabills.models.PaymentData;

/** Custom view to display payment items in main activity. */
public class PaymentItemView extends LinearLayout {

    public PaymentItemView(Context context, PaymentData data, CloseButtonClickListener listener) {
        super(context);
        initView(context, data, listener);
    }

    public PaymentItemView(
            Context context,
            AttributeSet attr,
            PaymentData data,
            CloseButtonClickListener listener
    ) {
        super(context, attr);
        initView(context, data, listener);
    }

    public PaymentItemView(
            Context context,
            AttributeSet attr,
            int defStyle,
            PaymentData data,
            CloseButtonClickListener listener
    ) {
        super(context, attr, defStyle);
        initView(context, data, listener);
    }

    private void initView(Context context, PaymentData data, CloseButtonClickListener listener) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.payment_item_view,
                /* root = */ null,
                /* attachToRoot = */ false
        );
        setData(v, data, listener);
        this.addView(v);
    }

    private void setData(View v, PaymentData data, CloseButtonClickListener listener) {
        TextView title = v.findViewById(R.id.title);
        TextView value = v.findViewById(R.id.value);
        ImageButton deleteItemButton = v.findViewById(R.id.delete_item);

        title.setText(v.getResources().getString(R.string.payment_title, data.getTitle()));
        value.setText(data.getValue().toString());
        deleteItemButton.setOnClickListener((view) -> {
            listener.onCloseButtonClicked(this, data);
        });
    }
}

