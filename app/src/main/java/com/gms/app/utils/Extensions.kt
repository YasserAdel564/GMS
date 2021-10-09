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
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.fragment.app.Fragment
import com.gms.app.R
import com.gms.app.ui.MainActivity
import com.gms.app.utils.Constants.RC_CAPTURE_IMAGE
import com.gms.app.utils.Constants.RC_IMAGES
import com.google.android.material.snackbar.Snackbar
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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

fun Context.openBrowser(url: String?) {
    if (url != null && url.isNotEmpty() && url != Constants.Exceptions.Null.value) {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

fun Context.encodeImage(bm: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}
fun Fragment.openCamera(): Uri? {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val photoURI = FileProvider.getUriForFile(
        requireContext(),
        "${requireContext().packageName}.provider",
        getImageTempFile(requireContext())
    )
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
    startActivityForResult(intent, RC_CAPTURE_IMAGE)
    return photoURI
}

fun Fragment.openGallery(allowMultiple: Boolean) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.type = "image/*"
    startActivityForResult(intent, RC_IMAGES)
}
fun Context.getFilePathForN(
    uri: Uri
): String? {
    val returnCursor: Cursor? =
        this.contentResolver.query(uri, null, null, null, null)
    val nameIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex: Int = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name: String = returnCursor.getString(nameIndex)
    val size = returnCursor.getLong(sizeIndex).toString()
    val file = File(this.filesDir, name)
    try {
        val inputStream = this.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable = inputStream!!.available()
        val bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream.read(buffers).also { read = it } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream.close()
        outputStream.close()
    } catch (e: Exception) {
    }
    return file.path
}