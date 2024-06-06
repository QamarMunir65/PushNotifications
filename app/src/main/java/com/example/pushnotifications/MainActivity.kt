package com.example.pushnotifications

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allDenied
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity(), PermissionRequest.Listener {
    private val request by lazy {
        permissionsBuilder(
            android.Manifest.permission.POST_NOTIFICATIONS
        ).build()
    }
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        request.addListener(this)
        request.send()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = NotificationAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        NotificationManager.notifications.observe(this, Observer { notifications ->
            notifications?.let { adapter.setNotifications(it) }
        })

    }
override fun onPermissionsResult(result: List<PermissionStatus>) {
    when {
        result.anyPermanentlyDenied() -> showPermanentlyDeniedDialog()
        result.anyShouldShowRationale() -> showRationaleDialog(request)
        result.allGranted() -> {
        }

        result.allDenied() -> {
        }
    }
}

    private fun showRationaleDialog(request: PermissionRequest) {
        android.app.AlertDialog.Builder(this).setTitle("Permission Required")
            .setMessage("Permission is required to proceed")
            .setPositiveButton("Request Again") { _, _ ->
                // Send the request again.
                request.send()
            }.setNegativeButton(android.R.string.cancel) { _, _ ->
            }.show()
    }

    private fun showPermanentlyDeniedDialog() {
        android.app.AlertDialog.Builder(this).setTitle("Permission Required")
            .setMessage("permission is required to proceed")
            .setPositiveButton("Open Settings") { _, _ ->
                // Open the app's settings.
                val intent = createAppSettingsIntent()
                startActivity(intent)
            }.setNegativeButton(android.R.string.cancel) { _, _ ->
            }.show()
    }

    private fun createAppSettingsIntent() = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", baseContext.packageName, null)
    }

}