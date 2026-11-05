
package com.jaidev.idvcalculator

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.text.InputType

class MainActivity : Activity() {

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(20))
            setBackgroundColor(Color.rgb(245, 247, 250))
        }

        val title = TextView(this).apply {
            text = "JAIDEV IDV CALCULATOR"
            textSize = 24f
            setTextColor(Color.rgb(20, 55, 100))
            gravity = Gravity.CENTER
        }
        layout.addView(title)

        val subtitle = TextView(this).apply {
            text = "Vehicle Insurance Value Calculator"
            textSize = 14f
            gravity = Gravity.CENTER
        }
        layout.addView(subtitle)

        fun addLabel(text: String) {
            layout.addView(TextView(this).apply {
                this.text = text
                textSize = 16f
                setTextColor(Color.BLACK)
                setPadding(0, dp(12), 0, dp(5))
            })
        }

        addLabel("Select Vehicle Type")

        val vehicleSpinner = Spinner(this)
        vehicleSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Private Car", "Two Wheeler", "Commercial Vehicle")
        )
        layout.addView(vehicleSpinner)

        addLabel("Original Invoice Value (₹)")

        val priceInput = EditText(this).apply {
            hint = "Example: 800000"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        layout.addView(priceInput)

        addLabel("Vehicle Age")

        val ageSpinner = Spinner(this)
        ageSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "0 to 6 Months",
                "6 Months to 1 Year",
                "1 to 2 Years",
                "2 to 3 Years",
                "3 to 4 Years",
                "4 to 5 Years",
                "Above 5 Years"
            )
        )
        layout.addView(ageSpinner)

        addLabel("Accessories Value (₹)")

        val accessoriesInput = EditText(this).apply {
            hint = "Optional"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        layout.addView(accessoriesInput)

        val button = Button(this).apply {
            text = "CALCULATE IDV"
        }
        layout.addView(button)

        val result = TextView(this).apply {
            textSize = 20f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(20, 100, 65))
            setPadding(0, dp(24), 0, dp(12))
        }
        layout.addView(result)

        button.setOnClickListener {
            val price = priceInput.text.toString().toDoubleOrNull()
            val accessories =
                accessoriesInput.text.toString().toDoubleOrNull() ?: 0.0

            if (price == null || price <= 0) {
                priceInput.error = "Enter valid invoice value"
                return@setOnClickListener
            }

            if (accessories < 0) {
                accessoriesInput.error = "Enter valid accessories value"
                return@setOnClickListener
            }

            val index = ageSpinner.selectedItemPosition

            if (index == 6) {
                result.text = "Above 5 years: IDV mutually agreed with insurer."
                return@setOnClickListener
            }

            val rates = listOf(0.05, 0.15, 0.20, 0.30, 0.40, 0.50)
            val depreciation = rates[index]

            val vehicleIdv = price * (1 - depreciation)
            val accessoryIdv = accessories * (1 - depreciation)

            result.text = """
                Vehicle: ${vehicleSpinner.selectedItem}
                Depreciation: ${depreciation * 100}%

                Vehicle IDV: ₹${"%,.2f".format(vehicleIdv)}
                Accessories IDV: ₹${"%,.2f".format(accessoryIdv)}

                TOTAL IDV: ₹${"%,.2f".format(vehicleIdv + accessoryIdv)}
            """.trimIndent()
        }

        setContentView(ScrollView(this).apply {
            addView(layout)
        })
    }
}
