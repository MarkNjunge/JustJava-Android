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
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.data.DrinksProvider
import com.marknkamau.justjava.data.CartRepositoryImpl

import com.marknkamau.justjava.utils.bindView

class EditCartDialog : DialogFragment(), View.OnClickListener {
    val tvDrinkName: TextView by bindView(R.id.tv_drink_name)
    val imgMinusQty: ImageView by bindView(R.id.img_minus_qty)
    val tvQuantity: TextView by bindView(R.id.tv_quantity)
    val imgAddQty: ImageView by bindView(R.id.img_add_qty)
//    var tvCinnamon: TextView by bindView(R.id.tv_cinnamon)
    lateinit var tvCinnamon: TextView
    val tvChocolate: TextView by bindView(R.id.tv_chocolate)
    val tvMarshmallows: TextView by bindView(R.id.tv_marshmallows)
    val imgDelete: ImageView by bindView(R.id.img_delete)
    val tvTotal: TextView by bindView(R.id.tv_total)
    val imgSave: ImageView by bindView(R.id.img_save)

    private var quantity: Int = 0
    private var item: CartItem? = null
    private lateinit var cartRepositoryImpl: CartRepositoryImpl
    private var cartResponse: CartAdapter.CartAdapterListener? = null
    private var withCinnamon = false
    private var withChocolate = false
    private var withMarshmallow = false

    fun setResponseListener(response: CartAdapter.CartAdapterListener) {
        cartResponse = response
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.edit_fragment, container, false)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        cartRepositoryImpl = CartRepositoryImpl

        if (args != null) {
            item = args.getParcelable<CartItem>(CART_ITEM)
        }

        tvCinnamon = view?.findViewById<TextView>(R.id.tv_cinnamon) as TextView

        tvCinnamon.setPadding(PADDING, PADDING, PADDING, PADDING)
        tvChocolate.setPadding(PADDING, PADDING, PADDING, PADDING)
        tvMarshmallows.setPadding(PADDING, PADDING, PADDING, PADDING)

        if (item != null) {
            tvDrinkName.text = item?.itemName
            quantity = Integer.parseInt(item?.itemQty)
            tvQuantity.text = quantity.toString()
            if (TextUtils.equals(item?.itemCinnamon, "true")) {
                setToppingOn(tvCinnamon)
                withCinnamon = true
            }
            if (TextUtils.equals(item?.itemChoc, "true")) {
                setToppingOn(tvChocolate)
                withChocolate = true
            }
            if (TextUtils.equals(item?.itemMarshmallow, "true")) {
                setToppingOn(tvMarshmallows)
                withMarshmallow = true
            }
            tvTotal.text = getString(R.string.ksh) + item!!.itemPrice
        } else {
            dismiss()
        }

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
                cartRepositoryImpl.deleteSingleItem(item!!)
                cartResponse!!.updateList()
                dismiss()
            }
            imgSave -> {
                dismiss()
                saveChanges()
            }
        }
    }

    private fun saveChanges() {
        cartRepositoryImpl.saveEdit(CartItem(item!!.itemID,
                item!!.itemName,
                quantity.toString(),
                withCinnamon.toString(),
                withChocolate.toString(),
                withMarshmallow.toString(),
                updateSubtotal()))
        cartResponse!!.updateList()
    }

    private fun setToppingOn(textView: TextView) {
        textView.setBackgroundResource(R.drawable.topping_on)
        textView.setPadding(PADDING, PADDING, PADDING, PADDING)
        textView.setTextColor(ContextCompat.getColor(activity, R.color.colorToppingOnText))
    }

    private fun setToppingOff(textView: TextView) {
        textView.setBackgroundResource(R.drawable.topping_off)
        textView.setPadding(PADDING, PADDING, PADDING, PADDING)
        textView.setTextColor(ContextCompat.getColor(activity, R.color.colorToppingOffText))
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
            if (TextUtils.equals(drink.drinkName, item?.itemName)) {
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
        tvTotal.text = resources.getString(R.string.ksh) + base
        return base
    }

    private fun switchCinnamon(selected: Boolean) {
        if (selected) {
            setToppingOff(tvCinnamon)
            withCinnamon = false
        } else {
            setToppingOn(tvCinnamon)
            withCinnamon = true
        }
    }

    private fun switchChocolate(selected: Boolean) {
        if (selected) {
            setToppingOff(tvChocolate)
            withChocolate = false
        } else {
            setToppingOn(tvChocolate)
            withChocolate = true
        }
    }

    private fun switchMarshmallow(selected: Boolean) {
        if (selected) {
            setToppingOff(tvMarshmallows)
            withMarshmallow = false
        } else {
            setToppingOn(tvMarshmallows)
            withMarshmallow = true
        }
    }

    companion object {
        val CART_ITEM = "item_cart"
        private val PADDING = 24
    }
}
