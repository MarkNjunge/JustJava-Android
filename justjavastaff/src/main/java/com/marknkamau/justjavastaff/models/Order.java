package com.marknkamau.justjavastaff.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Order implements Parcelable {
    private String orderId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String additionalComments;
    private OrderStatus status;
    private Date timestamp;
    private int totalPrice;
    private int itemsCount;

    public Order(String orderId, String customerName, String customerPhone, String deliveryAddress, String additionalComments, String status, Date timestamp, int totalPrice, int itemsCount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.additionalComments = additionalComments;
        this.status = OrderStatus.valueOf(status);
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.itemsCount = itemsCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.customerName);
        dest.writeString(this.customerPhone);
        dest.writeString(this.deliveryAddress);
        dest.writeString(this.additionalComments);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
        dest.writeInt(this.totalPrice);
        dest.writeInt(this.itemsCount);
    }

    protected Order(Parcel in) {
        this.orderId = in.readString();
        this.customerName = in.readString();
        this.customerPhone = in.readString();
        this.deliveryAddress = in.readString();
        this.additionalComments = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : OrderStatus.values()[tmpStatus];
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
        this.totalPrice = in.readInt();
        this.itemsCount = in.readInt();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
