package com.gms.app.utils

import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.animation.PathInterpolatorCompat
import com.gms.app.R
import com.gms.app.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBar(message: String?, rootView: View) {
    val snackBar = Snackbar.make(rootView, message!!, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.show()
}

fun Context.snackBarWithAction(
    message: String,
    actionTitle: String,
    rootView: View
) {
    val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    snackBar.duration = 10000
    snackBar.show()
}

fun Context.snackBarWithAction(
    message: String,
    actionTitle: String,
    rootView: View,
    dismiss: Boolean = true,
    action: () -> Unit
) {
    val snackBar: Snackbar? = if (dismiss)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    else
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)

    if (snackBar != null) {
        val view = snackBar.view
        val textView = view.findViewById<View>(R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        snackBar.setAction(actionTitle) {
            action.invoke()
            snackBar.dismiss()
        }
        snackBar.show()
    }
}

fun Context.flashAnimation(view: View) {
    val animator1 = ObjectAnimator.ofPropertyValuesHolder(
        view,
        PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, 1000f, 0f)
    )
    animator1.duration = 3000
    animator1.interpolator = PathInterpolatorCompat.create(0.29f, 0.87f, 1f, 1f)
    val animator2 = ObjectAnimator.ofPropertyValuesHolder(
        view,
        PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
    )
    animator2.duration = 2000
    animator2.interpolator = PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f)
    val animator3 = ObjectAnimator.ofPropertyValuesHolder(
        view,
        PropertyValuesHolder.ofKeyframe(
            View.ROTATION,
            Keyframe.ofFloat(0f, -45f),
            Keyframe.ofFloat(0.7f, -45f),
            Keyframe.ofFloat(1f, 0f)
        )
    )
    animator3.duration = 0
    animator3.interpolator = PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f)
    val animatorSet1 = AnimatorSet()
    animatorSet1.playTogether(animator1, animator2, animator3)
    val animatorSet2 = AnimatorSet()
    animatorSet2.playTogether(animatorSet1)
    animatorSet2.start()

}

fun getRandomColor(): Int {
    val rnd = Random()
    return Color.argb(
        255,
        rnd.nextInt(256),
        rnd.nextInt(256),
        rnd.nextInt(256)
    )
}




fun Activity.hideKeyboard() {
    var view: View? = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.refreshApp() {
    val intent = Intent(this, MainActivity::class.java)
    this.startActivity(intent)
    this.finish()
}

fun Activity.getItemViewWidth(pixel: Int): Int {
    val scale = resources.displayMetrics.density
    return (pixel * scale + 0.5f).toInt()
}

fun Activity.hideSystemUI() {
    val decorView = window.decorView
    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
}


fun Context.openBrowser(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}


fun getImageTempFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("en")).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}


fun Context.showPasswordText(view: EditText) {
    if (view.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        view.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    else
        view.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
}

fun Context.convertImageToBase64(filepath: String): String {
    val bm = BitmapFactory.decodeStream(FileInputStream(File(filepath)))
    bm.compress(CompressFormat.JPEG, 100, ByteArrayOutputStream())
    return android.util.Base64.encodeToString(
        ByteArrayOutputStream().toByteArray(),
        android.util.Base64.DEFAULT
    )
}

fun Context.convertImageToByte(filepath: String): ByteArray {
    val stream = ByteArrayOutputStream()
    val bm = BitmapFactory.decodeStream(FileInputStream(File(filepath)))
    bm.compress(CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}

fun setLocale(activity: Activity, languageCode: String?) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = activity.resources
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun soapRequestBuilder(methodName: String, lang: String): SoapObject? {
    val request = SoapObject(Constants.NameSpace, methodName)
    val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
    val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
    envelope.dotNet = true
    envelope.setOutputSoapObject(request)
    val language = PropertyInfo()
    language.setName("LG")
    language.value = lang
    language.setType(String::class.java)
    request.addProperty(language)
    androidHttpTransport.call((Constants.NameSpace + methodName), envelope)
    val resultsString = envelope.response as SoapObject
    val object1 = resultsString.getProperty(1) as SoapObject
    return if (object1.propertyCount > 0)
        object1.getProperty(0) as SoapObject
    else null
}

 fun addKeyPropertyString(request: SoapObject, key: String, value: String) {
    val property = PropertyInfo()
    property.setName(key)
    property.value = value
    property.setType(String::class.java)
    request.addProperty(property)
}

 fun addKeyPropertyInt(request: SoapObject, key: String, value: Int) {
    val property = PropertyInfo()
    property.setName(key)
    property.value = value
    property.setType(Int::class.java)
    request.addProperty(property)
}
fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()