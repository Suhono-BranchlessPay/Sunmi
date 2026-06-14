package com.branchlesspay.auditshield

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.branchlesspay.auditshield.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deviceInfoText.text = getString(
            R.string.device_info,
            DeviceInfo.model(),
            DeviceInfo.serial(this),
        )

        startService(Intent(this, BpAuditService::class.java))

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.testAnchorButton.setOnClickListener {
            sendTestAnchor()
        }
    }

    private fun sendTestAnchor() {
        if (!Prefs.isConfigured(this)) {
            binding.statusText.text = getString(R.string.status_error, "Configure license key in Settings")
            binding.resultText.visibility = View.VISIBLE
            binding.resultText.text = "Open Settings and save your BP license key first."
            return
        }

        binding.statusText.text = getString(R.string.status_testing)
        binding.resultText.visibility = View.GONE
        binding.testAnchorButton.isEnabled = false

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                val client = BpApiClient(
                    licenseKey = Prefs.getLicenseKey(this@MainActivity),
                    apiUrl = Prefs.getApiUrl(this@MainActivity),
                )
                client.postAnchor(BpAnchorPayload.testTransaction(this@MainActivity))
            }

            binding.testAnchorButton.isEnabled = true
            if (result.ok && result.httpStatus in 200..299) {
                binding.statusText.text = getString(R.string.status_success, result.httpStatus)
                binding.resultText.visibility = View.VISIBLE
                binding.resultText.text = buildString {
                    append(getString(R.string.anchor_success, result.status ?: "queued"))
                    append("\n\nanchor_id: ")
                    append(result.anchorId ?: "-")
                    append("\n\n")
                    append(getString(R.string.verify_url_label))
                    append(": ")
                    append(result.verifyUrl ?: "-")
                }
            } else {
                binding.statusText.text = getString(
                    R.string.status_error,
                    result.error ?: "HTTP ${result.httpStatus}",
                )
                binding.resultText.visibility = View.VISIBLE
                binding.resultText.text = result.rawBody.ifBlank { result.error ?: "Unknown error" }
            }
        }
    }
}
