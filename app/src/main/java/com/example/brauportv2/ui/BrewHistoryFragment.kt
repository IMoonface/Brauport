package com.example.brauportv2.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.NotificationService
import com.example.brauportv2.R
import com.example.brauportv2.adapter.BrewHistoryAdapter
import com.example.brauportv2.databinding.FragmentBrewHistoryBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.dialog.DialogDeleteFragment
import com.example.brauportv2.ui.dialog.DialogRecipeInspectFragment
import com.example.brauportv2.ui.objects.RecipeDataSource.updateEndOfFermentation
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModel
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BrewHistoryFragment : Fragment() {

    private var _binding: FragmentBrewHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewHistoryAdapter
    private var startList = emptyList<BrewHistoryItem>()

    private val viewModel: BrewHistoryViewModel by activityViewModels {
        BrewHistoryViewModelFactory(
            (activity?.application as BaseApplication).brewHistoryDatabase.brewHistoryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewHistoryBinding.inflate(inflater, container, false)

        val service = NotificationService(requireContext())

        service.createNotificationChannel(
            getString(R.string.channel_name),
            getString(R.string.channel_description)
        )

        adapter = BrewHistoryAdapter(this::onInspectItem, this::onItemClick, this::onDeleteClick)
        binding.brewHistoryRecyclerView.adapter = adapter

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val actualDate = formatter.format(Calendar.getInstance().time)

        lifecycleScope.launch {
            viewModel.allBrewHistoryItems.collect { it ->
                startList = it.map { it.toBrewHistoryItem() }
                startList.forEach { brewHistoryItem ->
                    val endOfFermentation = formatter.parse(brewHistoryItem.bEndOfFermentation)
                    endOfFermentation?.let {
                        val dateIsOver = endOfFermentation.before(formatter.parse(actualDate))
                        if (dateIsOver) {
                            brewHistoryItem.cardColor = Color.GRAY
                        } else {
                            brewHistoryItem.cardColor = Color.GREEN
                        }
                    }
                    if (!updateEndOfFermentation)
                        onItemUpdate(brewHistoryItem)
                    updateEndOfFermentation = false
                }
                adapter.submitList(startList)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onInspectItem(item: BrewHistoryItem) {
        val dialog = DialogRecipeInspectFragment(item, true)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onItemClick(item: BrewHistoryItem) {
        val dialog = DialogCookingFragment(true, item, this::onDialogCookingDismiss)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onDeleteClick(item: BrewHistoryItem) {
        val dialog = DialogDeleteFragment(item, this::onDeleteConfirm)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeDeleteDialog")
    }

    private fun onDeleteConfirm(item: BrewHistoryItem) {
        viewModel.deleteBrewHistoryItem(item)
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

    private fun onDialogCookingDismiss(abort: Boolean) {}
}