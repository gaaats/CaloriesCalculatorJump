package com.calcalcpacksecond.caloriesjump

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.calcalcpacksecond.caloriesjump.databinding.FragmentResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultFragment : Fragment() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CaloriesService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    val api: CaloriesService by lazy {
        retrofit.create(CaloriesService::class.java)
    }


    var textResult = ""
    var textNames = ""
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProgBar()
        binding.btnCopy.setOnClickListener {
            try {
                saveToClipBoard()
            } catch (e: Exception) {
                Snackbar.make(binding.root, "There is some error, try again", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        binding.btnImgExit.setOnClickListener {
            initAlertDialog()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initProgBar() {
        lifecycleScope.launch {
            binding.imgMain.visibility = View.GONE
            binding.btnCopy.visibility = View.GONE
            binding.textHeader.visibility = View.GONE
            binding.tvNameActivities.visibility = View.GONE
            binding.tvResultText.visibility = View.GONE
            binding.btnImgExit.visibility = View.GONE
            binding.lottieAnimVaiting.visibility = View.VISIBLE
            binding.tvPleaseVaitLoading.visibility = View.VISIBLE
            delay(100)
            withContext(Dispatchers.IO) {
                try {
                    mainFunHere()
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        "There is some error, try again",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
            delay(2000)
            binding.imgMain.visibility = View.VISIBLE
            binding.textHeader.visibility = View.VISIBLE
            binding.btnCopy.visibility = View.VISIBLE
            binding.tvNameActivities.visibility = View.VISIBLE
            binding.tvResultText.visibility = View.VISIBLE
            binding.btnImgExit.visibility = View.VISIBLE
            binding.lottieAnimVaiting.visibility = View.GONE
            binding.tvPleaseVaitLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private suspend fun mainFunHere() {

        val result = api.getCaloriesBurned()
        if (result.isSuccessful) {
            Log.d("test_tag", "good")

            val tempResult = result.body()!!.random()

            textNames = tempResult.name!!
            textResult = tempResult.caloriesPerHour.toString()
        } else {
            Snackbar.make(
                binding.root,
                "There is some error, try again",
                Snackbar.LENGTH_LONG
            )
                .show()
        }

        binding.tvResultText.text = textResult
        binding.tvNameActivities.text = textNames
    }

    private fun saveToClipBoard() {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        ClipData.newPlainText("Encrypted text", textResult).also {
            clipboardManager.setPrimaryClip(it)
        }
        Snackbar.make(binding.root, "Saved!", Snackbar.LENGTH_LONG).show()
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you definitely want to log out, the current data will not be saved?")
            .setPositiveButton("Yes, Exit") { _, _ ->
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }
}