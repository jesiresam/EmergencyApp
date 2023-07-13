package com.example.salamaemergencyapp

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.NonCancellable

val myDB: Any = TODO()
var i: Any = TODO()

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity(val REQUEST_LOCATION: Int) : AppCompatActivity() {
   lateinit var b1: Button
    lateinit var b2: Button
    lateinit var danger: Button
    lateinit var accident: Button
    var myDB: DatabaseHandler? = null
    var x = ""
    var y = ""
    lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        b1 = findViewById(R.id.button)
        b2 = findViewById(R.id.button2)
        myDB = DatabaseHandler(this)
        danger = findViewById(R.id.danger)
        accident = findViewById(R.id.accident)
        val mp: MediaPlayer = MediaPlayer.create(
            getApplicationContext(),
            R.raw.mixkit_retro_emergency_notification_alarm_2970
        )
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPS()
        } else {
            startTrack()
        }
        b1.setOnClickListener {
                val i = Intent(getApplicationContext(), Register::class.java)
                startActivity(i)
            }


            b2.setOnClickListener {
                theft()
            }
        danger.setOnClickListener {

                danger()
            }

        accident.setOnClickListener {
                accident()
            }

    }

    private fun accident() {
        loadData(101002)
    }

    private fun danger() {
        loadData(100001)
    }

    private fun theft() {
        loadData(100001)
    }

    private fun loadData(call: Int) {
        val thelist: ArrayList<String> = ArrayList()
        val data: Cursor = myDB!!.getListContents()
        if (data.getCount() === 0) {
            Toast.makeText(this@MainActivity, "no content to show", Toast.LENGTH_SHORT).show()
        } else {
            val msg = "I need Help. Latitude : $x Longitude : $y"
            var number = ""
            while (data.moveToNext()) {
                thelist.add(data.getString(1))
                number = number + data.getString(1) + if (data.isLast()) "" else ";"
                call(call)
            }
            if (!thelist.isEmpty()) {
                sendSMS(number, msg, true)
            }
        }
    }

    private fun sendSMS(number: String, msg: String, b: Boolean) {
        val intent = Intent(getApplicationContext(), MainActivity::class.java)
        val pi: PendingIntent = PendingIntent.getActivity(
            getApplicationContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val sms: SmsManager = SmsManager.getDefault()
        for (s in loadContact()) {
            sms.sendTextMessage(s, null, msg, pi, null)
        }
    }

    private fun call(call: Int) {
        val i = Intent(Intent.ACTION_CALL)
        i.setData(Uri.parse("tel:$call"))
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.CALL_PHONE
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(i)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf<String>(Manifest.permission.CALL_PHONE), 1)
            }
        }
    }

    private fun startTrack() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            val mFusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(object : OnSuccessListener<Location?> {

                    override fun onSuccess(locationGPS: Location?) {
                        // GPS location can be null if GPS is switched off
                        if (locationGPS != null) {
                            val lat: Double = locationGPS.getLatitude()
                            val lon: Double = locationGPS.getLongitude()
                            x = String.valueOf(lat)
                            y = String.valueOf(lon)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "UNABLE TO FIND LOCATION",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
                .addOnFailureListener(object : OnFailureListener {

                    override fun onFailure(e: Exception) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location")
                        e.printStackTrace()
                    }
                })
        }
    }

    private fun onGPS() {
        var box = AlertDialog.Builder(this)
        box.setMessage("Enable GPS?")
        box.setPositiveButton(
            "Enable",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> Settings.ACTION_LOCATION_SOURCE_SETTINGS  })

        box.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, id -> NonCancellable.cancel() })
        var alert = box.create()
        alert.setTitle("Salama Safety app")
        alert.show()


    }}

private fun String.Companion.valueOf(lon: Double): String {
    TODO("Not yet implemented")
}

private fun loadContact(): ArrayList<String> {
        val theList: ArrayList<String> = ArrayList()
        val data: Cursor = myDB.getListContents()
        if (data.getCount() === 0) {
            Toast.makeText(requireActivity(),"There is no content", Toast.LENGTH_SHORT).show()
        } 
        else {
            while (data.moveToNext()) {
                theList.add(data.getString(1))
            }
        }
        return theList
    }

fun requireActivity(): Context? {
    TODO("Not yet implemented")
}

private fun Any.getListContents(): Cursor {
    TODO("Not yet implemented")
}

fun onKeyDown(keyCode: Int, event: KeyEvent) {
        if (event.getKeyCode() === KeyEvent.KEYCODE_POWER) {
            i++
            if (i == 2) {
                //do something
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "102301"))
                startActivity(intent)
                i = 0
            }
        }
        onKeyDown(keyCode, event)
    }

private operator fun Any.inc(): Any {
    TODO("Not yet implemented")
}

fun startActivity(intent: Intent) {
    TODO("Not yet implemented")
}

private object location  {
        private val TODO: Location? = null
        private const val REQUEST_CHECK_CODE = 8989
        private const val REQUEST_LOCATION = 1
        var i = 0
    }
