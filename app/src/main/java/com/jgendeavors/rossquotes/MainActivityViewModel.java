package com.jgendeavors.rossquotes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The MainActivityViewModel class is responsible for providing general-purpose data to UI compoments.
 */
public class MainActivityViewModel extends ViewModel {

    // Instance variables
    private MutableLiveData<Boolean> mIsPremiumPurchased = new MutableLiveData<>();


    // API methods

    public LiveData<Boolean> getIsPremiumPurchased() { return mIsPremiumPurchased; }

    public void setIsPremiumPurchased(boolean value) { mIsPremiumPurchased.postValue(value); }
}
