package com.example.barterapp.model;

import java.io.Serializable;

public class TradeReqModelClass implements Serializable {
    private TradeItemModelClass requestedItem;
    private TradeItemModelClass offered_Item;
    private String message;
    private String requestId;
    private String chat_id;
    private Boolean isAccepted;
    private Boolean isRejected;

    public TradeReqModelClass() {
        isAccepted=false;
        isRejected=false;
    }

    public TradeReqModelClass(String requestId, String chat_id, TradeItemModelClass requestedItem,
                                  TradeItemModelClass offered_Item, String message ) {
        this.requestedItem = requestedItem;
        this.offered_Item = offered_Item;
        this.message = message;
        this.requestId = requestId;
        this.chat_id = chat_id;
        isAccepted=false;
        isRejected=false;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Boolean getRejected() {
        return isRejected;
    }

    public void setRejected(Boolean rejected) {
        isRejected = rejected;
    }

    public TradeItemModelClass getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(TradeItemModelClass requestedItem) {
        this.requestedItem = requestedItem;
    }

    public TradeItemModelClass getOffered_Item() {
        return offered_Item;
    }

    public void setOffered_Item(TradeItemModelClass offered_Item) {
        this.offered_Item = offered_Item;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }


}
