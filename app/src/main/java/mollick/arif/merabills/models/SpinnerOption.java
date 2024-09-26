package mollick.arif.merabills.models;

import androidx.annotation.NonNull;

/** Spinner options available for the app. */
public class SpinnerOption {
    private String name;
    private SpinnerOptionType type;

    public SpinnerOption(String name, SpinnerOptionType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public SpinnerOptionType getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
