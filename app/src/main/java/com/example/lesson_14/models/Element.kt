package com.example.lesson_14.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

class Element @JvmOverloads constructor(
    name: String? = null,
    quantity: Int = 0
) : Parcelable, Externalizable {

    var safeName: String? = name

    var safeQuantity: Int = quantity

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(safeName.orEmpty())
        parcel.writeInt(safeQuantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeExternal(p0: ObjectOutput?) {
        p0?.writeObject(safeName)
        p0?.writeObject(safeQuantity)
    }

    override fun readExternal(p0: ObjectInput?) {
        safeName = p0?.readObject().toString()
        safeQuantity = p0?.readObject() as Int
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }
}
