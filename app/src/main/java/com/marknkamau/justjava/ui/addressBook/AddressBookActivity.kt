package com.marknkamau.justjava.ui.addressBook

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.addAddress.AddAddressActivity
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_address_book.*
import kotlinx.android.synthetic.main.item_address.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddressBookActivity : BaseActivity() {

    companion object {
        private const val ADD_ADDRESS_REQ = 99
    }

    private val addressBookViewModel: AddressBookViewModel by viewModel()
    private lateinit var adapter: BaseRecyclerViewAdapter<Address>
    override var requiresSignedIn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_book)
        supportActionBar?.title = "Address Book"

        btnAddAddress.setOnClickListener {
            startActivityForResult(Intent(this, AddAddressActivity::class.java), ADD_ADDRESS_REQ)
        }
        fabAddAddress.setOnClickListener {
            startActivityForResult(Intent(this, AddAddressActivity::class.java), ADD_ADDRESS_REQ)
        }

        observeLoading()
        initializeRecyclerView()

        addressBookViewModel.getAddresses()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ADDRESS_REQ && resultCode == Activity.RESULT_OK) {
            val address = data?.extras!![AddAddressActivity.ADDRESS_KEY] as Address

            addressBookViewModel.addAddress(address).observe(this, Observer { resource ->
                when (resource) {
                    is Resource.Success -> addressBookViewModel.getAddresses()
                    is Resource.Failure -> handleApiError(resource)
                }
            })
        }
    }

    private fun observeLoading() {
        addressBookViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun initializeRecyclerView() {
        adapter = BaseRecyclerViewAdapter(R.layout.item_address) { item ->
            tvStreetAddress.text = item.streetAddress
            tvDeliveryInstructions.text = item.deliveryInstructions
            tvDeliveryInstructions.visibility = if (item.deliveryInstructions == null) View.GONE else View.VISIBLE

            rootAddress.setOnLongClickListener {
                deleteAddress(item)
                true
            }
        }

        rvAddresses.apply {
            addItemDecoration(DividerItemDecoration(this@AddressBookActivity, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(this@AddressBookActivity, RecyclerView.VERTICAL, false)
        }
        rvAddresses.adapter = adapter
        handleRecyclerViewSwipe()

        addressBookViewModel.user.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    TransitionManager.beginDelayedTransition(rootAddressBook)
                    if (resource.data.address.isEmpty()) {
                        layoutAddressesContent.visibility = View.GONE
                        layoutAddressesEmpty.visibility = View.VISIBLE
                    } else {
                        layoutAddressesContent.visibility = View.VISIBLE
                        layoutAddressesEmpty.visibility = View.GONE
                        adapter.setItems(resource.data.address)
                    }
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun deleteAddress(address: Address) {
        addressBookViewModel.deleteAddress(address).observe(this, Observer { resource ->
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
                    val item = adapter.items[viewHolder.adapterPosition]
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
        itemTouchHelper.attachToRecyclerView(rvAddresses)
    }
}
