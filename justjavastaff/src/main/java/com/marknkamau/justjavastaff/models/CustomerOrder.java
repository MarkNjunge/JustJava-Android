package com.marknkamau.justjavastaff.models;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerOrder implements Parcelable {
    private String orderID;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String additionalComments;
    private String orderStatus;
    private String timestamp;
    private String totalPrice;
    private String itemsCount;

    public CustomerOrder(String orderID, String customerName, String customerPhone, String deliveryAddress, String additionalComments, String orderStatus, String timestamp, String totalPrice, String itemsCount) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.additionalComments = additionalComments;
        this.orderStatus = orderStatus;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.itemsCount = itemsCount;
    }

    public String getOrderID() {
        return orderID;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "orderID='" + orderID + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", additionalComments='" + additionalComments + '\'' +
                ", currentStatus='" + orderStatus + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", itemsCount='" + itemsCount + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.customerName);
        dest.writeString(this.customerPhone);
        dest.writeString(this.deliveryAddress);
        dest.writeString(this.additionalComments);
        dest.writeString(this.orderStatus);
        dest.writeString(this.timestamp);
        dest.writeString(this.totalPrice);
        dest.writeString(this.itemsCount);
    }

    protected CustomerOrder(Parcel in) {
        this.orderID = in.readString();
        this.customerName = in.readString();
        this.customerPhone = in.readString();
        this.deliveryAddress = in.readString();
        this.additionalComments = in.readString();
        this.orderStatus = in.readString();
        this.timestamp = in.readString();
        this.totalPrice = in.readString();
        this.itemsCount = in.readString();
    }

    public static final Creator<CustomerOrder> CREATOR = new Creator<CustomerOrder>() {
        @Override
        public CustomerOrder createFromParcel(Parcel source) {
            return new CustomerOrder(source);
        }

        @Override
        public CustomerOrder[] newArray(int size) {
            return new CustomerOrder[size];
        }
    };
}
