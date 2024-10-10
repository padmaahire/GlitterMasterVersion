package com.goglitter.ui

import android.content.Context
import android.util.TypedValue
import com.goglitter.utils.AuthenticationException
import com.goglitter.utils.NetworkErrorException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Utils {
    internal fun getPixels(context: Context, valueInDp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp.toFloat(), r.displayMetrics)
        return px.toInt()
    }

    internal fun getPixels(context: Context, valueInDp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, r.displayMetrics)
        return px.toInt()
    }

    internal fun getPixelsSp(context: Context, valueInSp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, valueInSp.toFloat(), r.displayMetrics)
        return px.toInt()
    }

    internal fun getPixelsSp(context: Context, valueInSp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, valueInSp, r.displayMetrics)
        return px.toInt()
    }


        fun resolveError(e: Throwable): String {
            var error = e

            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkErrorException(errorMessage = "Connection error!")
                }
                is ConnectException -> {
                    //error = NetworkErrorException(errorMessage = "${e.localizedMessage}")
                    error = NetworkErrorException(errorMessage = "No internet access!")
                }
                is UnknownHostException -> {
                  //  error = NetworkErrorException(errorMessage = "No address associated with hostname")
                    error = NetworkErrorException(errorMessage = "No internet access!")
                }

            }

            if(e is HttpException){
                when(e.code()){
                    502 -> {
                        error = NetworkErrorException(e.code(),  "Internal error!")
                    }
                    401 -> {
                        throw AuthenticationException("Authentication error!")
                    }
                    400 -> {
                        error = NetworkErrorException.parseException(e)
                    }
                }
            }
            return error.localizedMessage
        }


}