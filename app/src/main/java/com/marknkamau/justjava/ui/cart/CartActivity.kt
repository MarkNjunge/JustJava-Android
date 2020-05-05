package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.ToolbarActivity
import com.marknkamau.justjava.ui.checkout.CheckoutActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.item_cart_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : ToolbarActivity() {

    private val cartViewModel: CartViewModel by viewModel()
    private lateinit var items: List<CartItem>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.title = getString(R.string.cart)

        observeLoading()
        initializeRecyclerView()

        cartViewModel.getCartItems()

        btnCheckout.setOnClickListener {
            val clz = if (cartViewModel.isSignedIn()) CheckoutActivity::class.java else SignInActivity::class.java
            startActivity(Intent(this, clz))
        }
    }

    private fun observeLoading() {
        cartViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initializeRecyclerView() {
        val adapter = BaseRecyclerViewAdapter<CartItem>(R.layout.item_cart_item) { item ->
            tv_cartItem_productName.text = item.cartItem.productName
            tv_cartItem_quantity.text = "${item.cartItem.quantity}x"

            tv_cartItem_totalPrice.text =
                getString(R.string.price_listing, CurrencyFormatter.format(item.cartItem.totalPrice))
            tv_cartItem_options.text = item.options.joinToString(", ") { it.optionName }

            rootCartItem.setOnLongClickListener {
                cartViewModel.deleteItem(item)
                true
            }
        }

        rvCart.apply {
            addItemDecoration(DividerItemDecoration(this@CartActivity, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(this@CartActivity, RecyclerView.VERTICAL, false)
            this.adapter = adapter
        }

        handleRecyclerViewSwipe()

        cartViewModel.items.observe(this, Observer { items ->
            this.items = items
            if (items.isEmpty()) {
                layoutCartContent.visibility = View.GONE
                layoutCartEmpty.visibility = View.VISIBLE
            } else {
                layoutCartContent.visibility = View.VISIBLE
                layoutCartEmpty.visibility = View.GONE

                adapter.setItems(items)
                val cartTotal = items.map { it.cartItem.totalPrice }.foldRight(0.0, { total, acc -> total + acc })
                tvCartTotal.text = getString(R.string.price_listing, CurrencyFormatter.format(cartTotal))

                verifyCartItems()
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
                    val item = items[viewHolder.adapterPosition]
                    cartViewModel.deleteItem(item)
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
        itemTouchHelper.attachToRecyclerView(rvCart)
    }

    private fun verifyCartItems() {
        cartViewModel.verifyOrder(items).observe(this, Observer { resource ->
            btnCheckout.isEnabled = true
            when (resource) {
                is Resource.Success -> {
                    if (resource.data.isNotEmpty()) {
                        // TODO Handle changed products
                    }
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }
}
