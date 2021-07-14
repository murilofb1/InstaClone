package com.murilofb.instaclone.helper


import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions {
    companion object {
        /*
        Para validar uma permissão no android precisaremos de uma activity
        e a string da permissão em si, pode ser passada também um array de strings para validar
        varias permissoes de uma vez

        As permissoes podem ser obtidas através do Manifest.permission
        Ex: Manifest.permission.READ_EXTERNAL_STORAGE para armazenamento externo ou
        Manifest.permission.CAMERA para a camera

        Utiliza-se o ContextCompat.checkSelfPermission passando como parametro a activity e a permission
        para checar se ela já foi permitida

        Se ainda não foi pedido a permissão exibir mensagem para o usuário através do ActivityCompat.requestPermissions
        passando como parâmetro a activity, o array de permissoes e o requestCode
         */
        fun validatePermission(activity: AppCompatActivity, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                when (ContextCompat.checkSelfPermission(activity, permission)) {
                    PackageManager.PERMISSION_GRANTED -> true //Caso tenha sido permitido
                    else -> {//Caso tenha sido negado
                        ActivityCompat.requestPermissions(activity, arrayOf(permission), 0)
                        false
                    }
                }
            } else true
        }
    }
}