package com.marknkamau.justjava.ui.addressBook

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ActivityAddressBookBinding
import com.marknkamau.justjava.ui.addAddress.AddAddressActivity
import com.marknkamau.justjava.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressBookActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressBookBinding
    private val addressBookViewModel: AddressBookViewModel by viewModels()
    private lateinit var adapter: AddressAdapter
    override var requiresSignedIn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Address Book"

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val address = result.data?.extras!![AddAddressActivity.ADDRESS_KEY] as Address

                    addressBookViewModel.addAddress(address).observe(this, { resource ->
                        when (resource) {
                            is Resource.Success -> addressBookViewModel.getAddresses()
                            is Resource.Failure -> handleApiError(resource)
                        }
                    })
                }
            }
        binding.btnAddAddress.setOnClickListener {
            resultLauncher.launch(Intent(this, AddAddressActivity::class.java))
        }
        binding.fabAddAddress.setOnClickListener {
            resultLauncher.launch(Intent(this, AddAddressActivity::class.java))
        }

        observeLoading()
        initializeRecyclerView()

        addressBookViewModel.getAddresses()
    }

    private fun observeLoading() {
        addressBookViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun initializeRecyclerView() {
        adapter = AddressAdapter { address ->
            deleteAddress(address)
        }

        binding.rvAddresses.apply {
            addItemDecoration(DividerItemDecoration(this@AddressBookActivity, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(this@AddressBookActivity, RecyclerView.VERTICAL, false)
        }
        binding.rvAddresses.adapter = adapter
        handleRecyclerViewSwipe()

        addressBookViewModel.user.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    TransitionManager.beginDelayedTransition(binding.root)
                    if (resource.data.address.isEmpty()) {
                        binding.layoutAddressesContent.visibility = View.GONE
                        binding.layoutAddressesEmpty.visibility = View.VISIBLE
                    } else {
                        binding.layoutAddressesContent.visibility = View.VISIBLE
                        binding.layoutAddressesEmpty.visibility = View.GONE
                        adapter.setItems(resource.data.address)
                    }
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun deleteAddress(address: Address) {
        addressBookViewModel.deleteAddress(address).observe(this, { resource ->
            when (resource) {
                is Resource.Success -> addressBookViewModel.getAddresses()
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun handleRecyclerViewSwipe() {
        val swipeBackground = ColorDrawable(Color.parseColor("#DFDFDF"))
        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        val touchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    // Not used in swipe
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = adapter.items[viewHolder.bindingAdapterPosition]
                    deleteAddress(item)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float, // How far along the x-axis it has moved. > 0 = Right, < 0 = Left
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                    if (dX > 0) {
                        swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                        deleteIcon.setBounds(
                            itemView.left + iconMargin,
                            itemView.top + iconMargin,
                            itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                            itemView.bottom - iconMargin
                        )
                    } else {
                        swipeBackground.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        deleteIcon.setBounds(
                            itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                            itemView.top + iconMargin,
                            itemView.right - iconMargin,
                            itemView.bottom - iconMargin
                        )
                    }
                    swipeBackground.draw(c)

                    // Prevent permanent changes to the canvas.
                    // Restored after clipping.
                    c.save()

                    // Prevent icon from drawing over the item by clipping it at the same points as the background color
                    if (dX > 0) {
                        c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    } else {
                        c.clipRect(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    }
                    deleteIcon.draw(c)

                    // Restore the state of the canvas before clipping to prevent clipping elements that shouldn't
                    c.restore()

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvAddresses)
    }
}
