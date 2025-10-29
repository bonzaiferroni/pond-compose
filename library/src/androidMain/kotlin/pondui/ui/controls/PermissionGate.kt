package pondui.ui.controls

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionGate(
    permissions: List<String>,
    deniedContent: @Composable () -> Unit,
    grantedContent: @Composable () -> Unit
) {
    var hasAll by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        hasAll = permissions.all { p -> results[p] == true }
    }

    LaunchedEffect(permissions) {
        val alreadyGranted = permissions.all { p ->
            ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED
        }
        if (alreadyGranted) {
            hasAll = true
        } else {
            launcher.launch(permissions.toTypedArray())
        }
    }

    when (hasAll) {
        true -> grantedContent()
        false -> deniedContent()
        null -> Unit // awaiting result; show nothing (or a spinner if ye fancy)
    }
}

@Composable
fun <T> permissionGate(
    permissions: List<String>,
    grantedContent: @Composable () -> T
): T? {
    var hasAll by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        hasAll = permissions.all { p -> results[p] == true }
    }

    LaunchedEffect(permissions) {
        val alreadyGranted = permissions.all { p ->
            ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED
        }
        if (alreadyGranted) {
            hasAll = true
        } else {
            launcher.launch(permissions.toTypedArray())
        }
    }

    return when (hasAll) {
        true -> grantedContent()
        else -> null // awaiting result; show nothing (or a spinner if ye fancy)
    }
}