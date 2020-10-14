package com.live.dummy

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_profile_information.*
import org.systers.mentorship.R

class ProfileInformationActiviity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)

        val entryChip: Chip = getChip(entry_chip_group!!, "Hello World")!!
        val entryChip2: Chip = getChip(entry_chip_group, "Test")!!
        val entryChip3: Chip = getChip(entry_chip_group, "Test")!!
        val entryChip4: Chip = getChip(entry_chip_group, "Test")!!
        val entryChip5: Chip = getChip(entry_chip_group, "Test")!!
        entry_chip_group.addView(entryChip)
        entry_chip_group.addView(entryChip2)
        entry_chip_group.addView(entryChip3)
        entry_chip_group.addView(entryChip4)
        entry_chip_group.addView(entryChip5)
    }

    private fun getChip(entryChipGroup: ChipGroup, text: String): Chip? {
        val chip = Chip(this)
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.my_chip))
        val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 25f,
                resources.displayMetrics
        ).toInt()
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.text = text
        chip.setOnCloseIconClickListener { entryChipGroup.removeView(chip) }
        return chip
    }
}