package com.annaalfiani.gmcapps.ui.intro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.utils.Utilities
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage

class IntroActivity : AppIntro2(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val sliderPage = SliderPage().apply {
            title = resources.getString(R.string.Now_Playing)
            titleColor = Color.parseColor("#050505")
            description = resources.getString(R.string.Now_Playing_dec)
            descColor = Color.parseColor("#050505")
            imageDrawable = R.drawable.video_film
            bgColor = Color.parseColor("#ffffff")
        }

        val sliderPage2 = SliderPage().apply {
            title = resources.getString(R.string.pre_order)
            titleColor = Color.parseColor("#050505")
            description = resources.getString(R.string.pre_order_dec)
            descColor = Color.parseColor("#050505")
            imageDrawable = R.drawable.movies
            bgColor = Color.parseColor("#ffffff")
        }

        val sliderPage3 = SliderPage().apply {
            title = resources.getString(R.string.Cashlesss)
            titleColor = Color.parseColor("#050505")
            description = resources.getString(R.string.cassless_dec)
            descColor = Color.parseColor("#050505")
            imageDrawable = R.drawable.payment_method
            bgColor = Color.parseColor("#ffffff")
        }
        addSlide(AppIntroFragment.newInstance(sliderPage))
        addSlide(AppIntroFragment.newInstance(sliderPage2))
        addSlide(AppIntroFragment.newInstance(sliderPage3))
        setZoomAnimation()
        isSkipButtonEnabled = false
        isVibrateOn = true
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Utilities.setFirstTime(this, false).also {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }
}