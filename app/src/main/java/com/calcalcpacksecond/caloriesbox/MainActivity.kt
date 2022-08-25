package com.calcalcpacksecond.caloriesbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.calcalcpacksecond.caloriesbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        try {
            val fragmentInstance =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.childFragmentManager?.fragments?.last()
            if (fragmentInstance is StartFragment) {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed()
                    return
                } else {
                    Snackbar.make(binding.root, "Press back again to exit", Snackbar.LENGTH_SHORT)
                        .show()
                }
                backPressedTime = System.currentTimeMillis()
                return
            }
            super.onBackPressed()
        } catch (e: Throwable) {
            Toast.makeText(this, "There is some error, try again", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}