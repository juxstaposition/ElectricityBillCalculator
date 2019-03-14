package el.bill.calc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics

class NewItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        window.setLayout(width*7/10,height*7/10)
    }
}
