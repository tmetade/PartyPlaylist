package com.example.partyplaylist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()
{
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()

        connectButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val clientId: String = remoteConfig.getString("spotify_client_id")
        val scopes = getResources().getStringArray(R.array.spotify_scopes);
        val redirectUri = getString(R.string.spotify_callback_uri_scheme) + "://" + getString(R.string.spotify_callback_uri_host)

        val builder: AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)
        builder.setScopes(scopes)
        val request: AuthorizationRequest = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse = AuthorizationClient.getResponse(resultCode, intent)
            when (response.getType()) {
                AuthorizationResponse.Type.TOKEN -> {
                    CredentialsManager.saveCredentials(this, response.accessToken)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(this, "Unable to login", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Something happened", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}