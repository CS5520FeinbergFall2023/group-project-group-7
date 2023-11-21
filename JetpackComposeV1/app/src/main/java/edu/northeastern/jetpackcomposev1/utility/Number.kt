package edu.northeastern.jetpackcomposev1.utility

fun convertSalary(salaryMin: Double, salaryMax: Double): String {
    var minStr = salaryMin.toInt().toString()
    var maxStr = salaryMax.toInt().toString()
    // add "," before to the 000
    minStr = minStr.substring(0, minStr.length - 3) + "," + minStr.substring(minStr.length - 3, minStr.length)
    maxStr = maxStr.substring(0, maxStr.length - 3) + "," + maxStr.substring(maxStr.length - 3, maxStr.length)
    return "$$minStr - $$maxStr a year"
}