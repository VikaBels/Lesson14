package com.example.lesson_14.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

class Element(private val name: String?, private val quantity: Int) : Parcelable, Externalizable {
    constructor() : this(null, 0) {}

    var safeName: String = name.orEmpty()
        set(value) {
            field = value.ifEmpty {
                "empty line"
            }
        }

    var safeQuantity: Int = quantity
        set(value) {
            field = if (value < 0) {
                0
            } else {
                value
            }
        }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeExternal(p0: ObjectOutput?) {
        p0?.writeObject(safeName)
        p0?.writeObject(safeQuantity)
    }

    override fun readExternal(p0: ObjectInput?) {
        safeName = p0?.readObject().toString()
        safeQuantity = p0?.readObject() as Int
    }
}
