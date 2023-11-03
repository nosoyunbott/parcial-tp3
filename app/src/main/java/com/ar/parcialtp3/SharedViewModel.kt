package com.ar.parcialtp3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val selectedImages: MutableLiveData<List<String>> = MutableLiveData()


}