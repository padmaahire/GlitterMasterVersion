package com.goglitter.utils

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author: Vishal Shegaonkar
 * @Date: 10/12/22
 */

//private const val TIME_STAMP_FORMAT = "EEEE, MMMM d, yyyy - hh:mm:ss a"
private const val TIME_STAMP_FORMAT = "MM-yyyy"
private const val DATE_FORMAT = "yyyy-MM-dd"
private const val DATE_FORMAT_MONTH = "MMM-YY"

val Context.myAppPreferences: SharedPreferences
    get() = getSharedPreferences(
        "${this.packageName}_${this.javaClass.simpleName}",
        MODE_PRIVATE
    )

inline fun <reified T : Any> SharedPreferences.getObject(key: String): T? {
    return Gson().fromJson<T>(getString(key, null), T::class.java)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T {
    return when (T::class) {
        Boolean::class -> getBoolean(key, defaultValue as? Boolean? ?: false) as T
        Float::class -> getFloat(key, defaultValue as? Float? ?: 0.0f) as T
        Int::class -> getInt(key, defaultValue as? Int? ?: 0) as T
        Long::class -> getLong(key, defaultValue as? Long? ?: 0L) as T
        String::class -> getString(key, defaultValue as? String? ?: "") as T
        else -> {
            if (defaultValue is Set<*>) {
                getStringSet(key, defaultValue as Set<String>) as T
            } else {
                val typeName = T::class.java.simpleName
                throw Error("Unable to get shared preference with value type '$typeName'. Use getObject")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
inline operator fun <reified T : Any> SharedPreferences.set(key: String, value: T) {
    with(edit()) {
        when (T::class) {
            Boolean::class -> putBoolean(key, value as Boolean)
            Float::class -> putFloat(key, value as Float)
            Int::class -> putInt(key, value as Int)
            Long::class -> putLong(key, value as Long)
            String::class -> putString(key, value as String)
            else -> {
                if (value is Set<*>) {
                    putStringSet(key, value as Set<String>)
                } else {
                    val json = Gson().toJson(value)
                    putString(key, json)
                }
            }
        }
        commit()
    }
}

inline fun <reified T : JSONConvertable> String.toObject(): T = Gson().fromJson(this, T::class.java)

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false

}

fun Long.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun String.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Int.getMonth(): String {
    return when (this) {
        0 -> "January"
        1 -> "February"
        2 -> "March"
        3 -> "April"
        4 -> "May"
        5 -> "June"
        6 -> "July"
        7 -> "August"
        8 -> "Septembar"
        9 -> "October"
        10 -> "November"
        11 -> "December"
        else -> ""
    }
}

fun String.TimeAgo(): String {
    var timeAgo: String = ""
    try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val past = format.parse(this)
        val dateFormated = SimpleDateFormat("dd MMM").format(past)
        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
        if (seconds < 60) {
            timeAgo = "$seconds sec. ago"
        } else if (minutes < 60) {
            timeAgo = "$minutes min. ago"
        } else if (hours < 24) {
            timeAgo = "$hours hrs. ago"
        } else {

            timeAgo = dateFormated
        }

    } catch (e: Exception) {

    }
    return timeAgo
}

fun String.getMonth(): String {
    return when (this) {
        "0" -> "January"
        "1" -> "February"
        "2" -> "March"
        "3" -> "April"
        "4" -> "May"
        "5" -> "June"
        "6" -> "July"
        "7" -> "August"
        "8" -> "Septembar"
        "9" -> "October"
        "00" -> "January"
        "01" -> "February"
        "02" -> "March"
        "03" -> "April"
        "04" -> "May"
        "05" -> "June"
        "06" -> "July"
        "07" -> "August"
        "08" -> "Septembar"
        "09" -> "October"
        "10" -> "November"
        "11" -> "December"
        else -> ""
    }
}

fun String.getDifference(currentDate: String, finalDate: String): String {
    try {
        //val currentDate = "06/24/2018"
        //val finalDate = "04/23/2020"
        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("MM/dd/yyyy")
        date1 = dates.parse(currentDate)
        date2 = dates.parse(finalDate)
        val difference: Long = abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        val dayDifference = differenceDates.toString()
        return dayDifference
    } catch (e: Exception) {

    }
    return ""
}

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query

}

fun String?.emptyToNull(): String? {
    return if (this == null || this.isEmpty()) null else this
}


/*fun String.getTextDrawable(): TextDrawable? {
    val generator = ColorGenerator.MATERIAL
    val color = generator.randomColor
    //val color = generator.getColor("vishal.4fox@gmail.com")
    //val color = generator.getColor(Color.RED)
    return TextDrawable.Builder()
        .setWidth(100)
        .setHeight(100)
        .setColor(color)
        .setShape(TextDrawable.SHAPE_ROUND)
        .setText(this)
        .build()
}*/

/*fun <T> ImageView.loadCircularImage(
    model: T, borderInDip: Float = 0F, borderColor: Int = R.color.evolve_blue,
) {
    Glide.with(context)
        .asBitmap()
        .load(model)
        .apply(RequestOptions.circleCropTransform())
        .into(object : BitmapImageViewTarget(this) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                setImageDrawable(resource.run {
                    RoundedBitmapDrawableFactory.create(
                        resources, if (context.convertDpToPixel(borderInDip) > 0) {
                            createBitmapWithBorder(
                                context.convertDpToPixel(borderInDip),
                                borderColor,
                                context
                            )
                        } else {
                            this
                        }
                    ).apply {
                        isCircular = true
                    }
                })
            }
        })
}*/

/**
 * Create a new bordered bitmap with the specified borderSize and borderColor
 *
 * @param borderSize - The border size in pixels
 * @param borderColor - The border color
 * @param context - Android context for resolving the color resource
 * @return A new bordered bitmap with the specified borderSize and borderColor
 */
/*fun Bitmap.createBitmapWithBorder(
    borderSize: Float,
    borderColor: Int = R.color.pr,
    context: Context,
): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = min(halfWidth, halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset, height + borderOffset, Bitmap.Config.ARGB_8888
    )

    // Center coordinates of the image
    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        // Set transparent initial area
        drawARGB(0, 0, 0, 0)
    }

    // Draw the transparent initial area
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    // Draw the image
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    // Draw the createBitmapWithBorder
    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = ContextCompat.getColor(context, borderColor)
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
}*/

fun Context.convertDpToPixel(dp: Float): Float {
    return if (this != null) {
        val resources = this.resources
        val metrics = resources.displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    } else {
        val metrics = Resources.getSystem().displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        this.strokeWidth = 10f
        this.centerRadius = 50f
        start()
    }
}
/*
fun ImageView.load(uri : String?, initial :String?, progress : CircularProgressDrawable){
    try {
        if(uri != null || !uri.equals("null")){
            val options = RequestOptions()
                .placeholder(progress)
                .error(R.drawable.user)
            if(initial != null ){
                Glide.with(context)
                    .setDefaultRequestOptions(options)
                    .load(Constants.BASE_URL+uri)
                    .placeholder(initial.getTextDrawable())
                    .into(this)
            }else{
                Glide.with(context)
                    .setDefaultRequestOptions(options)
                    .load(Constants.BASE_URL+uri)
                    .into(this)
            }
        }else{
            val options = RequestOptions()
                .placeholder(progress)
                .error(R.drawable.user)
            if(initial != null ){
                Glide.with(context)
                    .setDefaultRequestOptions(options)
                    .load("")
                    .placeholder(initial.getTextDrawable())
                    .into(this)
            }
        }

    }catch (e :Exception){}
}
*/

/*@BindingAdapter(value = ["imageUrl", "initial"], requireAll = false)
fun loadImage(imageView: ImageView ,uri: String?, initial: String?){
    imageView.load(uri,initial, getProgressDrawable(imageView.context))
}*/

/*fun String?.capitalize(): String? {
    return this.capitalize()
}*/

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.afterLayout(what: () -> Unit) {
    if (isLaidOut) {
        what.invoke()
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                what.invoke()
            }
        })
    }

}

/*
fun RecyclerView.addScrollListener(onScroll: (position :Int) -> Unit) {
    var lastPosition = 0
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager is LinearLayoutManager) {
                val currentVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                */
/*if (lastPosition != currentVisibleItemPosition && currentVisibleItemPosition != RecyclerView.NO_POSITION) {
                    onScroll.invoke(currentVisibleItemPosition)
                    lastPosition = currentVisibleItemPosition
                }*//*

                lastPosition = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                //lastPosition = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                val fvp= (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition();
                val fcvp=  (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition();
                val flp = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition();
                val flcp =(layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition();
                //Log.e("TAG", "findFirstVisibleItemPosition: $fvp ")
                //Log.e("TAG", "Current findFirstCompletelyVisibleItemPosition: $fcvp ")
                //Log.e("TAG", "findLastVisibleItemPosition: $flp ")
                Log.e("TAG", "findLastCompletelyVisibleItemPosition: $flcp ")

            }
        }
    })
}
*/


fun RecyclerView.addScrollListener(onScroll: (position: Int) -> Unit) {
    var lastPosition = 0
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager is LinearLayoutManager) {
                val currentVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (lastPosition != currentVisibleItemPosition && currentVisibleItemPosition != RecyclerView.NO_POSITION) {
                    onScroll.invoke(currentVisibleItemPosition)
                    lastPosition = currentVisibleItemPosition
                }
            }
        }
    })
}
