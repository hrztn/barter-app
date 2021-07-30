package com.example.barterapp.model;

import java.io.Serializable;

public class TradeModelClass implements Serializable {
    private String tradeID;
    private String userOneId;
    private String userTwoId;
    private String chat_id;
    private boolean isCompletedByuserOne=false;
    private boolean isCompletedByuserTwo=false;
    private TradeItemModelClass itemOne;
    private TradeItemModelClass itemTwo;
    private String time;
    private boolean isCanceled=false;

    public TradeModelClass() {
    }

    public TradeModelClass(String tradeID, String userOneId, String userTwoId, String chat_id, TradeItemModelClass itemOne, TradeItemModelClass itemTwo,String time) {
        this.tradeID = tradeID;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.chat_id = chat_id;
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
    }

    public TradeModelClass(String tradeID, String userOneId, String userTwoId, String chat_id, boolean isCompletedByuserOne, boolean isCompletedByuserTwo, TradeItemModelClass itemOne, TradeItemModelClass itemTwo) {
        this.tradeID = tradeID;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.chat_id = chat_id;
        this.isCompletedByuserOne = isCompletedByuserOne;
        this.isCompletedByuserTwo = isCompletedByuserTwo;
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    public String getUserOneId() {
        return userOneId;
    }

    public void setUserOneId(String userOneId) {
        this.userOneId = userOneId;
    }

    public String getUserTwoId() {
        return userTwoId;
    }

    public void setUserTwoId(String userTwoId) {
        this.userTwoId = userTwoId;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public boolean isCompletedByuserOne() {
        return isCompletedByuserOne;
    }

    public void setCompletedByuserOne(boolean completedByuserOne) {
        isCompletedByuserOne = completedByuserOne;
    }

    public boolean isCompletedByuserTwo() {
        return isCompletedByuserTwo;
    }

    public void setCompletedByuserTwo(boolean completedByuserTwo) {
        isCompletedByuserTwo = completedByuserTwo;
    }

    public TradeItemModelClass getItemOne() {
        return itemOne;
    }

    public void setItemOne(TradeItemModelClass itemOne) {
        this.itemOne = itemOne;
    }

    public TradeItemModelClass getItemTwo() {
        return itemTwo;
    }

    public void setItemTwo(TradeItemModelClass itemTwo) {
        this.itemTwo = itemTwo;
    }



}
