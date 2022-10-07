package com.example.brauportv2

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.brauportv2.databinding.ActivityMainBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.ui.dialog.DialogWarningFragment
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModel
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var startList: List<BrewHistoryItem>

    private val viewModel: BrewHistoryViewModel by viewModels {
        BrewHistoryViewModelFactory(
            (application as BaseApplication).brewHistoryDatabase.brewHistoryDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val service = NotificationService(applicationContext)

        service.createNotificationChannel(
            getString(R.string.channel_name),
            getString(R.string.channel_description)
        )

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val actualDate = formatter.format(Calendar.getInstance().time)

        lifecycleScope.launch {
            viewModel.allBrewHistoryItems.collect { it ->
                startList = it.map { it.toBrewHistoryItem() }
                startList.forEach { brewHistoryItem ->
                    val endOfFermentation = formatter.parse(brewHistoryItem.bEndOfFermentation)
                    endOfFermentation?.let {
                        val dateIsOver = endOfFermentation.before(formatter.parse(actualDate))

                        if (dateIsOver && !brewHistoryItem.brewFinished) {
                            brewHistoryItem.brewFinished = true

                            service.showNotification(
                                getString(R.string.notification_title),
                                String.format(
                                    getString(R.string.notification_text), brewHistoryItem.bName
                                )
                            )
                        }
                    }
                    onItemUpdate(brewHistoryItem)
                }
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment, R.id.recipe_fragment, R.id.malt_stock_fragment,
                R.id.hop_stock_fragment, R.id.yeast_stock_fragment, R.id.brew_fragment,
                R.id.brew_history_fragment
            )
        )
        setSupportActionBar(binding.mainAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainBottomNav.setupWithNavController(navController)

        val defaultDisplay =
            DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)
        val displayContext = createDisplayContext(defaultDisplay!!)

        val width = displayContext.resources.displayMetrics.widthPixels
        val height = displayContext.resources.displayMetrics.heightPixels

        if (width < 480 || height < 800) {
            val dialog = DialogWarningFragment()
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "warningDialog")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val currentFocus: View? = currentFocus
            if (currentFocus is EditText) {
                val outRect = Rect()
                currentFocus.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    currentFocus.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun onItemUpdate(item: BrewHistoryItem) {
        viewModel.updateBrewHistoryItem(
            item.bId,
            item.bName,
            item.bMaltList,
            item.bRestList,
            item.bHoppingList,
            item.bYeast,
            item.bMainBrew,
            item.bDateOfCompletion,
            item.bEndOfFermentation,
            item.cardColor,
            item.brewFinished
        )
    }
}