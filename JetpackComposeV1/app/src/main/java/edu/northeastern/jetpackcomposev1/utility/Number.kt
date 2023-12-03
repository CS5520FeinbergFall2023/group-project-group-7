package edu.northeastern.jetpackcomposev1.utility

import android.util.Log

fun convertSalary(salary_is_predicted: String, salaryMin: Double, salaryMax: Double): String {
    var minStr = salaryMin.toInt().toString()
    var maxStr = salaryMax.toInt().toString()

    if (minStr == "0" && maxStr == "0") {
        return "Salary not provided"
    }
    else {
        // add "," before to the 000
        minStr = minStr.substring(0, minStr.length - 3) + "," + minStr.substring(minStr.length - 3, minStr.length)
        maxStr = maxStr.substring(0, maxStr.length - 3) + "," + maxStr.substring(maxStr.length - 3, maxStr.length)
        if(salary_is_predicted == "1") {
            return "Estimated $$minStr a year"
        }
        else {
            return "$$minStr - $$maxStr a year"
        }
    }
}

fun convertNumberOfJobs(count: Int): String {
    return if (count == 0) "0 job find" else "$count jobs find"
}