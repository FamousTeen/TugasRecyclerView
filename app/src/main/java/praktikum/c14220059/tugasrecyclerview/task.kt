package praktikum.c14220059.tugasrecyclerview

import android.content.res.TypedArray
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class task(
    var image: Int,
    var date: String,
    var title: String,
    var description: String,
    var status: String
) : Parcelable
