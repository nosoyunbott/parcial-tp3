package com.ar.parcialtp3.domain

import android.os.Parcel
import android.os.Parcelable

class Card(name: String?, breed: String?, subBreed: String?, age: Int?, sex: String?, id: String?, adopted: Boolean?): Parcelable {

    var name: String = ""
    var breed: String = ""
    var subBreed: String = ""
    var age: Int = 0
    var sex: String = ""
    var id: String = ""
    var adopted: Boolean = false
//    var image: String = ""


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean()
    )

    init {
        this.name = name!!
        this.breed = breed!!
        this.subBreed = subBreed!!
        this.age = age!!
        this.sex = sex!!
        this.id = id!!
        this.adopted = adopted!!

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(breed)
        parcel.writeString(subBreed)
        parcel.writeInt(age)
        parcel.writeString(sex)
        parcel.writeBoolean(adopted)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }

}