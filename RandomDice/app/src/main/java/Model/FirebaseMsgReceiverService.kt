package Model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.randomdice.MainActivity
import com.example.randomdice.MapActivity
import com.example.randomdice.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.security.KeyStore

open class FirebaseMsgReceiverService: FirebaseMessagingService() {

    private val activityRQ: Int = 0

    override fun onMessageReceived(remoteMsg: RemoteMessage) {
        super.onMessageReceived(remoteMsg)
        if(remoteMsg.notification != null){
            generateNotification(remoteMsg.notification!!.title!!, remoteMsg.notification!!.body!!)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("New Token", "New token: $token")
    }

    fun generateNotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent= PendingIntent.getActivity(this, activityRQ, intent, PendingIntent.FLAG_ONE_SHOT)
        val fatCatImg = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ami_fat_cat_fgcat
        )
        val builder = NotificationCompat.Builder(
            applicationContext,
            getString(R.string.channelId))
            .setSmallIcon(R.drawable.ami_fat_cat_fgcat)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setLargeIcon(fatCatImg)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(getString(R.string.channelId), getString(R.string.channelName), NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }
}