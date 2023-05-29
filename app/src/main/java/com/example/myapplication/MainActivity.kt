package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MyApplicationTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationAndAlertScreen()

                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresNotificationsPermission() {
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    val isPermissionEnabled = remember { mutableStateOf(true) }
    val areOtherViewsVisible = remember { mutableStateOf(true) }

    Column {
        if (isPermissionEnabled.value && cameraPermissionState.status.isGranted) {
            if (areOtherViewsVisible.value) {
                Text("Camera permission Granted")
            }
        } else {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                "The camera is important for this app. Please grant the permission."
            } else {
                "Camera permission required for this feature to be available. " +
                        "Please grant the permission."
            }
            if (areOtherViewsVisible.value) {
                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
        if (isPermissionEnabled.value) {
            Button(onClick = {
                isPermissionEnabled.value = false
                areOtherViewsVisible.value = true
                navigateToAppSettings(context)
            }) {
                Text("Disable permissions")
            }
        }
    }
}

private fun navigateToAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}

@Composable
fun NotificationAndAlertScreen(){
    var isOtherLayoutVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(Utils.darkClr)
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text =
                    "Don’t miss a moment",
                    color = Color.White,
                    fontFamily = Utils.font,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Turn on notification for latest game updates, news, scores, team updates,\n" +
                            "special offers and much more.",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = Utils.font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Divider(thickness = 1.dp, color = Utils.lightClr,
                    modifier = Modifier.padding(top = 5.dp))
            }
        }

        AlertSRow(title = "First Alert", onCheckedChange = {checked->
            isOtherLayoutVisible = checked

        })
        if (isOtherLayoutVisible){
            Column {
                AlertSRow(title = "SecondAlert", onCheckedChange = {false})
                AlertSRow(title ="ThirdAlert", onCheckedChange = {false})

            }

        }




    }

}


@Preview(device = Devices.NEXUS_7)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        NotificationAndAlertScreen()
    }
}


@Composable
private fun AlertSRow(
    title: String,
    state: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    val checkedState = remember { mutableStateOf(state) }

    Row(
        modifier = Modifier
            .background(Utils.lightClr)
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 5.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (checkedState.value) "On" else "Off",
                color = if (checkedState.value) Color.White else Color.White.copy(alpha = 0.5f),
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 5.dp)
            )

            Spacer(modifier = Modifier.padding(end = 5.dp))
            Switch(
                checked = checkedState.value,
                modifier = Modifier.padding(end = 5.dp),
                onCheckedChange = { checked ->
                    checkedState.value = checked
                    onCheckedChange(checked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Blue,
                    uncheckedThumbColor = Color.LightGray,
                    checkedTrackColor = Color.White.copy(alpha = 0.9f),
                    uncheckedTrackColor = Color.White.copy(alpha = 0.9f)
                )
            )
        }
    }
}
