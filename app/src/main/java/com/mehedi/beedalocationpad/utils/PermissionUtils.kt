package com.mehedi.beedalocationpad.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.mehedi.beedalocationpad.R

class PermissionUtils {
    companion object {
        fun showPermissionSettings(view: View, activity: FragmentActivity?, msg: String) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction(activity?.getString(R.string.open_settings)) {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity?.packageName, null)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity?.startActivity(intent)
            }.show()
        }

        fun showLocationService(view: View, activity: FragmentActivity?) {
            Snackbar.make(view, activity?.getString(R.string.location_service_needed) ?: "", Snackbar.LENGTH_LONG).setAction(activity?.getString(R.string.open_settings)) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity?.startActivity(intent)
            }.show()
        }
    }
}