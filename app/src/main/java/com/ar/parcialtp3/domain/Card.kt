package com.ar.parcialtp3.domain

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp
import java.util.Date

class Card(name: String?, breed: String?, subBreed: String?, age: Int?, sex: String?, id: String?, location: String?, adopted: Boolean?, createDate: Date?, image: String?): Parcelable {

    var name: String = ""
    var breed: String = ""
    var subBreed: String = ""
    var age: Int = 0
    var sex: String = ""
    var id: String = ""
    var location: String = ""
    var adopted: Boolean = false
    var createDate: Date? = null
    var image: String = ""


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        Date(parcel.readLong()),
        parcel.readString()

    )

    init {
        this.name = name!!
        this.breed = breed!!
        this.subBreed = subBreed!!
        this.age = age!!
        this.sex = sex!!
        this.id = id!!
        this.location = location!!
        this.adopted = adopted!!
        this.createDate = createDate
        this.image = image!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(breed)
        parcel.writeString(subBreed)
        parcel.writeInt(age)
        parcel.writeString(sex)
        parcel.writeString(location)
        parcel.writeBoolean(adopted)
        createDate?.let { parcel.writeLong(it.time) }
        parcel.writeString(image)

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