package com.calcalcpacksecond.caloriesjump


import com.google.gson.annotations.SerializedName

data class CaloriesListResonseItem(
    @SerializedName("calories_per_hour")
    val caloriesPerHour: Int?,
    @SerializedName("duration_minutes")
    val durationMinutes: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("total_calories")
    val totalCalories: Int?
)