package com.calcalcpacksecond.caloriesbox

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.calcalcpacksecond.caloriesbox.databinding.FragmentStartBinding
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class StartFragment : Fragment() {

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

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //test



        binding.btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                val result = api.getCaloriesBurned()
                if (result.isSuccessful){
                    Log.d("test_tag", "good")
                    result.body()?.forEach {
                        Log.d("test_tag", "name ${it.name}")
                        Log.d("test_tag", "name ${it.caloriesPerHour}")
                    }
                } else{
                    Log.d("test_tag", "baaad")
                }
            }


        }



        try {
            binding.btnConfirm.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_startFragment_to_resultFragment)
                } catch (e: Exception) {
                    Snackbar.make(binding.root, "There is some error, try again", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            binding.btnSettings.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_settingsFragment)
            }

        } catch (e: Exception) {
            Snackbar.make(binding.root, "There is some error, try again", Snackbar.LENGTH_LONG)
                .show()
        }
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}