package com.example.simpleappwithmoderntechnologies.model

import java.math.BigDecimal

data class Rate(val baseCurrency: Currency, val quoteCurrency: Currency, val price: BigDecimal)