package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.marknkamau.justjava.R
import com.marknjunge.core.data.local.DrinksProvider
import com.marknkamau.justjava.data.models.CartItem

class EditCartDialog : DialogFragment(), View.OnClickListener {
    private lateinit var tvDrinkName: TextView
    private lateinit var tvQuantity: TextView
    private lateinit var cbCinnamon: CheckBox
    private lateinit var cbChocolate: CheckBox
    private lateinit var cbMarshmallows: CheckBox
    private lateinit var tvTotal: TextView
    private lateinit var imgMinusQty: ImageView
    private lateinit var imgAddQty: ImageView
    private lateinit var btnDelete: Button
    private lateinit var btnSave: Button

    private var quantity: Int = 0
    private lateinit var cartItem: CartItem

    lateinit var onComplete: (EditType, CartItem) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.dialog_edit_cart_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments

        if (args == null) {
            dismiss()
            return
        }

        cartItem = args.getParcelable(CART_ITEM) as CartItem

        tvDrinkName = view.findViewById(R.id.tvDrinkName) as TextView
        tvQuantity = view.findViewById(R.id.tvQuantity) as TextView
        cbCinnamon = view.findViewById(R.id.cbCinnamonDetail) as CheckBox
        cbChocolate = view.findViewById(R.id.cbChocolateDetail) as CheckBox
        cbMarshmallows = view.findViewById(R.id.cbMarshmallows) as CheckBox
        imgMinusQty = view.findViewById(R.id.imgMinusQty) as ImageView
        imgAddQty = view.findViewById(R.id.imgAddQty) as ImageView
        tvTotal = view.findViewById(R.id.tvTotal) as TextView
        btnDelete = view.findViewById(R.id.btnDelete) as Button
        btnSave = view.findViewById(R.id.btnSave) as Button

        tvDrinkName.text = cartItem.itemName
        quantity = cartItem.itemQty
        tvQuantity.text = quantity.toString()
        cbCinnamon.isChecked = cartItem.itemCinnamon
        cbChocolate.isChecked = cartItem.itemChoc
        cbMarshmallows.isChecked = cartItem.itemMarshmallow
        tvTotal.text = getString(R.string.price_listing, cartItem.itemPrice)

        imgMinusQty.setOnClickListener(this)
        imgAddQty.setOnClickListener(this)
        cbCinnamon.setOnClickListener(this)
        cbChocolate.setOnClickListener(this)
        cbMarshmallows.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            imgMinusQty -> minusQty()
            imgAddQty -> addQty()
            cbCinnamon -> updateSubtotal()
            cbChocolate -> updateSubtotal()
            cbMarshmallows -> updateSubtotal()
            btnDelete -> onComplete(EditType.DELETE, cartItem)
            btnSave -> saveChanges()
        }
    }

    private fun saveChanges() {
        val newItem = CartItem(
                cartItem.id,
                cartItem.itemName,
                quantity,
                cbCinnamon.isChecked,
                cbChocolate.isChecked,
                cbMarshmallows.isChecked,
                updateSubtotal()
        )

        onComplete(EditType.SAVE, newItem)
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
            if (TextUtils.equals(drink.drinkName, cartItem.itemName)) {
                base = Integer.parseInt(drink.drinkPrice)
            }
        }

        base *= quantity
        if (cbCinnamon.isChecked) {
            base += quantity * 100
        }
        if (cbChocolate.isChecked) {
            base += quantity * 100
        }
        if (cbMarshmallows.isChecked) {
            base += quantity * 100
        }
        tvTotal.text = resources.getString(R.string.price_listing, base)
        return base
    }

    enum class EditType {
        SAVE,
        DELETE
    }

    companion object {
        const val CART_ITEM = "item_cart"
    }
}
