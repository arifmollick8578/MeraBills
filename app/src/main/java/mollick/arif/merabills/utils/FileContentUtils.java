package mollick.arif.merabills.utils;

import com.google.gson.Gson;

import mollick.arif.merabills.models.MeraBills;

public class FileContentUtils {
    private static final Gson gson = new Gson();

    /** Generate JSON from [MeraBills] class. */
    public static String generateJSON(MeraBills meraBills) {
        return gson.toJson(meraBills);
    }

    /** Genereate [MeraBills] class from JSON string. */
    public static MeraBills fetchData(String json) {
        return gson.fromJson(json, MeraBills.class);
    }
}
