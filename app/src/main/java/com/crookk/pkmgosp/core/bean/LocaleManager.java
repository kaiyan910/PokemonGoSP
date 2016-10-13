package com.crookk.pkmgosp.core.bean;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.crookk.pkmgosp.core.Preference_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@EBean(scope = EBean.Scope.Singleton)
public class LocaleManager {

    private static final String SEPARATOR = "_";

    @Pref
    Preference_ mPreference;

    public static final String ENGLISH = "en_US";
    public static final String T_CHINESE = "zh_HK";
    public static final String S_CHINESE = "zh_CN";

    public static List<String> VALID_LANGUAGE = new ArrayList<>();

    static {

        VALID_LANGUAGE.add(ENGLISH);
        VALID_LANGUAGE.add(T_CHINESE);
        VALID_LANGUAGE.add(S_CHINESE);
    }

    public boolean initialize(Context context) {

        String[] separate = separateLocale(getDefaultLocale());

        return updateResources(context, separate[0], separate[1]);
    }

    public boolean setLocale(Context context, String locale) {

        String[] separate = separateLocale(locale);

        return updateResources(context, separate[0], separate[1]);
    }

    private boolean updateResources(Context context, String language, String country) {

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    private String getDefaultLocale() {

        if (!mPreference.language().get().isEmpty()) {

            return mPreference.language().get();

        } else {

            String language = Locale.getDefault().getLanguage();
            String country = Locale.getDefault().getCountry();

            String locale = obtainValidLocale(language + SEPARATOR + country);

            mPreference.edit()
                    .language().put(locale)
                    .apply();

            return locale;
        }
    }

    private String[] separateLocale(String code) {
        return code.split(SEPARATOR);
    }

    private String obtainValidLocale(String locale) {

        if (VALID_LANGUAGE.contains(locale)) {
            return locale;
        } else if (locale.contains("zh")) {
            return T_CHINESE;
        }

        return ENGLISH;
    }
}
