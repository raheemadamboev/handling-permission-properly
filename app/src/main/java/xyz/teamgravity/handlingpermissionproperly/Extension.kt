package xyz.teamgravity.handlingpermissionproperly

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.permanentlyDenied() = !shouldShowRationale && !hasPermission