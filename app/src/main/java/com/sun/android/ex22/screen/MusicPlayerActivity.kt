package com.sun.android.ex22.screen

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.sun.android.R
import com.sun.android.databinding.ActivityMusicPlayerBinding
import com.sun.android.ex22.screen.listsong.MusicFragment
import com.sun.android.ex22.screen.listsong.PlaybackControllerFragment
import com.sun.android.ex22.utils.base.BaseActivity

class MusicPlayerActivity : BaseActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val binding by lazy { ActivityMusicPlayerBinding.inflate(layoutInflater) }

    override fun getViewLayout(): View {
        return binding.root
    }

    override fun initView() {
        checkPermission(READ_EXTERNAL_STORAGE_PERMISSION)
    }

    private fun checkPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    addView()
                } else {
                    onUserResponsePermissionRequest(permission)
                }
            }

            permissionLauncher.launch(permission)
        }
    }

    private fun onUserResponsePermissionRequest(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.permission_request_title))
                .setMessage(getString(R.string.permission_request_message))
                .setPositiveButton(getString(R.string.allow)
                ) { _, _ -> permissionLauncher.launch(permission) }
                .setNegativeButton(getString(R.string.cancel)
                ) { dialog, _ ->
                    run {
                        Toast.makeText(this, getString(R.string.permission_request_denied), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
        } else {
            Toast.makeText(this, getString(R.string.permission_request_denied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addView() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainerView.id, MusicFragment.newInstance())
            .commit()

        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainerPlaybackController.id, PlaybackControllerFragment.newInstance())
            .commit()
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
