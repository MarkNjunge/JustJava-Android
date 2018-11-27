package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.local.CartDao
import com.marknjunge.core.data.local.DrinksProvider
import com.marknjunge.core.model.OrderItem

class EditCartDialog : DialogFragment(), View.OnClickListener {
    private lateinit var tvDrinkName: TextView
    private lateinit var tvQuantity: TextView
    private lateinit var tvChocolate: TextView
    private lateinit var tvMarshmallows: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvCinnamon: TextView
    private lateinit var imgMinusQty: ImageView
    private lateinit var imgDelete: ImageView
    private lateinit var imgAddQty: ImageView
    private lateinit var imgSave: ImageView

    private var quantity: Int = 0
    private lateinit var item: OrderItem
    private var withCinnamon = false
    private var withChocolate = false
    private var withMarshmallow = false

    lateinit var cartDao: CartDao

    lateinit var onComplete: (EditType, OrderItem) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.edit_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments

        if (args == null) {
            dismiss()
            return
        }

        item = args.getParcelable(CART_ITEM)

        tvDrinkName = view.findViewById(R.id.tvDrinkName) as TextView
        tvQuantity = view.findViewById(R.id.tvQuantity) as TextView
        tvChocolate = view.findViewById(R.id.tvChocolate) as TextView
        tvMarshmallows = view.findViewById(R.id.tvMarshmallows) as TextView
        tvTotal = view.findViewById(R.id.tv_total) as TextView
        tvCinnamon = view.findViewById(R.id.tvToppings) as TextView
        imgMinusQty = view.findViewById(R.id.imgMinusQty) as ImageView
        imgDelete = view.findViewById(R.id.img_delete) as ImageView
        imgAddQty = view.findViewById(R.id.imgAddQty) as ImageView
        imgSave = view.findViewById(R.id.img_save) as ImageView

        tvCinnamon.setPadding(PADDING, PADDING, PADDING, PADDING)
        tvChocolate.setPadding(PADDING, PADDING, PADDING, PADDING)
        tvMarshmallows.setPadding(PADDING, PADDING, PADDING, PADDING)

        tvDrinkName.text = item.itemName
        quantity = item.itemQty
        tvQuantity.text = quantity.toString()
        if (item.itemCinnamon) {
            setToppingOn(tvCinnamon)
            withCinnamon = true
        }
        if (item.itemChoc) {
            setToppingOn(tvChocolate)
            withChocolate = true
        }
        if (item.itemMarshmallow) {
            setToppingOn(tvMarshmallows)
            withMarshmallow = true
        }
        tvTotal.text = getString(R.string.price_listing, item.itemPrice)

        imgMinusQty.setOnClickListener(this)
        imgAddQty.setOnClickListener(this)
        tvCinnamon.setOnClickListener(this)
        tvChocolate.setOnClickListener(this)
        tvMarshmallows.setOnClickListener(this)
        imgDelete.setOnClickListener(this)
        imgSave.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            imgMinusQty -> minusQty()
            imgAddQty -> addQty()
            tvCinnamon -> {
                switchCinnamon(withCinnamon)
                updateSubtotal()
            }
            tvChocolate -> {
                switchChocolate(withChocolate)
                updateSubtotal()
            }
            tvMarshmallows -> {
                switchMarshmallow(withMarshmallow)
                updateSubtotal()
            }
            imgDelete -> {
                onComplete(EditType.DELETE, item)
            }
            imgSave -> {
                saveChanges()
            }
        }
    }

    private fun saveChanges() {
        val newItem = OrderItem(
                item.id,
                item.itemName,
                quantity,
                withCinnamon,
                withChocolate,
                withMarshmallow,
                updateSubtotal()
        )

        onComplete(EditType.SAVE, newItem)
    }

    private fun setToppingOn(textView: TextView) {
        textView.setBackgroundResource(R.drawable.topping_on)
        textView.setPadding(PADDING, PADDING, PADDING, PADDING)
        textView.setTextColor(ContextCompat.getColor(context!!, R.color.colorToppingOnText))
    }

    private fun setToppingOff(textView: TextView) {
        textView.setBackgroundResource(R.drawable.topping_off)
        textView.setPadding(PADDING, PADDING, PADDING, PADDING)
        textView.setTextColor(ContextCompat.getColor(context!!, R.color.colorToppingOffText))
    }

    private fun minusQty() {
        if (quantity > 1) {
            quantity -= 1
        }
        tvQuantity.text = quantity.toString()
        updateSubtotal()
    }

    private fun addQty() {
        quantity += 1
        tvQuantity.text = quantity.toString()
        updateSubtotal()
    }


    @SuppressLint("SetTextI18n")
    private fun updateSubtotal(): Int {
        val drinks = DrinksProvider.drinksList

        var base = 0
        for (drink in drinks) {
            if (TextUtils.equals(drink.drinkName, item.itemName)) {
                base = Integer.parseInt(drink.drinkPrice)
            }
        }

        base *= quantity
        if (withCinnamon) {
            base += quantity * 100
        }
        if (withChocolate) {
            base += quantity * 100
        }
        if (withMarshmallow) {
            base += quantity * 100
        }
        tvTotal.text = resources.getString(R.string.price_listing, base)
        return base
    }

    private fun switchCinnamon(selected: Boolean) {
        withCinnamon = if (selected) {
            setToppingOff(tvCinnamon)
            false
        } else {
            setToppingOn(tvCinnamon)
            true
        }
    }

    private fun switchChocolate(selected: Boolean) {
        withChocolate = if (selected) {
            setToppingOff(tvChocolate)
            false
        } else {
            setToppingOn(tvChocolate)
            true
        }
    }

    private fun switchMarshmallow(selected: Boolean) {
        withMarshmallow = if (selected) {
            setToppingOff(tvMarshmallows)
            false
        } else {
            setToppingOn(tvMarshmallows)
            true
        }
    }

    enum class EditType {
        SAVE,
        DELETE
    }

    companion object {
        const val CART_ITEM = "item_cart"
        private const val PADDING = 24
    }
}
