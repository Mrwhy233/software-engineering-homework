package com.example.vhr.fragment.staff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StaffViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StaffViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is staff fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}