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
import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.data.DrinksProvider
import com.marknkamau.justjava.models.CartItemRoom
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class EditCartDialog(val cartDao: CartDao) : DialogFragment(), View.OnClickListener {
    lateinit var tvDrinkName: TextView
    lateinit var tvQuantity: TextView
    lateinit var tvChocolate: TextView
    lateinit var tvMarshmallows: TextView
    lateinit var tvTotal: TextView
    lateinit var tvCinnamon: TextView
    lateinit var imgMinusQty: ImageView
    lateinit var imgDelete: ImageView
    lateinit var imgAddQty: ImageView
    lateinit var imgSave: ImageView


    private var quantity: Int = 0
    private var item: CartItemRoom? = null
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

        if (args != null) {
            item = args.getParcelable<CartItemRoom>(CART_ITEM)
        }

        tvDrinkName = view?.findViewById<TextView>(R.id.tv_drink_name) as TextView
        tvQuantity = view.findViewById<TextView>(R.id.tv_quantity) as TextView
        tvChocolate = view.findViewById<TextView>(R.id.tv_chocolate) as TextView
        tvMarshmallows = view.findViewById<TextView>(R.id.tv_marshmallows) as TextView
        tvTotal = view.findViewById<TextView>(R.id.tv_total) as TextView
        tvCinnamon = view.findViewById<TextView>(R.id.tv_cinnamon) as TextView
        imgMinusQty = view.findViewById<ImageView>(R.id.img_minus_qty) as ImageView
        imgDelete = view.findViewById<ImageView>(R.id.img_delete) as ImageView
        imgAddQty = view.findViewById<ImageView>(R.id.img_add_qty) as ImageView
        imgSave = view.findViewById<ImageView>(R.id.img_save) as ImageView

        tvCinnamon.setPadding(PADDING, PADDING, PADDING, PADDING)
        tvChocolate.setPadding(PADDING, PADDING, PADDING, PADDING)
        tvMarshmallows.setPadding(PADDING, PADDING, PADDING, PADDING)

        if (item != null) {
            tvDrinkName.text = item?.itemName
            quantity = item?.itemQty!!
            tvQuantity.text = quantity.toString()
            if (item?.itemCinnamon ?: false) {
                setToppingOn(tvCinnamon)
                withCinnamon = true
            }
            if (item?.itemChoc ?: false) {
                setToppingOn(tvChocolate)
                withChocolate = true
            }
            if (item?.itemMarshmallow ?: false) {
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
                Single.fromCallable { cartDao.deleteItem(item!!) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    cartResponse?.updateList()
                                    dismiss()
                                },
                                {
                                    t: Throwable? ->
                                    Timber.e(t)
                                }
                        )

            }
            imgSave -> {
                saveChanges()
            }
        }
    }

    private fun saveChanges() {
        Timber.d("save")
        val item1 = CartItemRoom(
                item!!.id,
                item!!.itemName,
                quantity,
                withCinnamon,
                withChocolate,
                withMarshmallow,
                updateSubtotal()
        )
        val callable = Single.fromCallable {
            cartDao.updateItem(item1)
        }
        callable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ ->
                            dismiss()
                            cartResponse?.updateList()
                        },
                        { t ->
                            Timber.e(t)
                        }
                )

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
