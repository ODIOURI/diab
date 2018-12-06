/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.fit.google

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import it.diab.R

class FitActivity : AppCompatActivity() {

    private lateinit var mViewModel: FitViewModel

    private lateinit var mCoordinator: CoordinatorLayout
    private lateinit var mHeaderText: TextView
    private lateinit var mConnectButton: AppCompatButton
    private lateinit var mDisconnectButton: AppCompatButton
    private lateinit var mDeleteAllButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this)[FitViewModel::class.java]

        setContentView(R.layout.activity_fit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener { finish() }

        mCoordinator = findViewById(R.id.coordinator)
        mHeaderText = findViewById(R.id.fit_header_text)
        mConnectButton = findViewById(R.id.fit_connect_button)
        mDisconnectButton = findViewById(R.id.fit_disconnect_button)
        mDeleteAllButton = findViewById(R.id.fit_delete_all_button)

        mConnectButton.setOnClickListener { mViewModel.connect(this,
            FitActivity.GOOGLE_FIT_REQUEST_CODE
        ) }
        mDisconnectButton.setOnClickListener { confirmDisconnect() }
        mDeleteAllButton.setOnClickListener { confirmDelete() }

        setupUi(mViewModel.isConnected())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GOOGLE_FIT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    setupUi(true)
                } else {
                    showSnack(R.string.fit_login_error)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onDisconnect() {
        mViewModel.disconnect(this)

        showSnack(R.string.fit_disconnect_success)
        setupUi(false)
    }

    private fun onDeleteAll() {
        mViewModel.deleteAllData(this,
                { showSnack(R.string.fit_delete_success) },
                { showSnack(R.string.fit_delete_failure) })
    }

    private fun showSnack(@StringRes message: Int) {
        Snackbar.make(mCoordinator, getString(message), Snackbar.LENGTH_LONG)
                .show()
    }

    private fun confirmDisconnect() {
        AlertDialog.Builder(this)
                .setTitle(R.string.fit_disconnect_confim_title)
                .setMessage(R.string.fit_disconnect_confim_message)
                .setPositiveButton(R.string.fit_disconnect_confim_positive) { _, _ -> onDisconnect() }
            .setNegativeButton(R.string.cancel, null)
                .show()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
                .setTitle(R.string.fit_delete_confirm_title)
                .setMessage(R.string.fit_delete_confirm_message)
                .setPositiveButton(R.string.fit_delete_confim_positive) { _, _ -> onDeleteAll() }
            .setNegativeButton(R.string.cancel, null)
                .show()

    }

    private fun setupUi(isConnected: Boolean) {
        mHeaderText.setText(if (isConnected) R.string.fit_status_connected else R.string.fit_status_prompt)
        mConnectButton.visibility = if (isConnected) View.GONE else View.VISIBLE
        mDeleteAllButton.isEnabled = isConnected
        mDisconnectButton.isEnabled = isConnected
    }

    companion object {
        private const val GOOGLE_FIT_REQUEST_CODE = 281
    }
}