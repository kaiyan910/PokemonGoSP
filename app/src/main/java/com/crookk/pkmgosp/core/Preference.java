package com.crookk.pkmgosp.core;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preference {

    @DefaultString("")
    String language();

    @DefaultInt(0)
    int dataVersion();
}
