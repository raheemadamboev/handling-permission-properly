package xyz.teamgravity.handlingpermissionproperly

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import xyz.teamgravity.handlingpermissionproperly.ui.theme.HandlingPermissionProperlyTheme

class MainActivity : ComponentActivity() {

    companion object {
        private val REQUESTED_PERMISSIONS = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandlingPermissionProperlyTheme {
                val permissionsState = rememberMultiplePermissionsState(permissions = REQUESTED_PERMISSIONS)

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START) {
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                })

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        permissionsState.permissions.forEach { permission ->
                            when (permission.permission) {
                                Manifest.permission.CAMERA -> {
                                    when {
                                        permission.hasPermission -> {
                                            Text(text = "Camera permission accepted")
                                        }
                                        permission.shouldShowRationale -> {
                                            Text(text = "Camera permission needed for something")
                                        }
                                        permission.permanentlyDenied() -> {
                                            Text(text = "Camera permission needs to be granted from settings")
                                        }
                                    }
                                }
                                Manifest.permission.RECORD_AUDIO -> {
                                    when {
                                        permission.hasPermission -> {
                                            Text(text = "Record audio permission accepted")
                                        }
                                        permission.shouldShowRationale -> {
                                            Text(text = "Record audio permission needed for something")
                                        }
                                        permission.permanentlyDenied() -> {
                                            Text(text = "Record audio permission needs to be granted from settings")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}