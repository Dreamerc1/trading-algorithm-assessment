package codingblackfemales.sotw;

import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;

import java.util.List;

public interface SimpleAlgoState {
//Returns the symbol of the asset being traded.
    public String getSymbol();
/*  These methods return the number of bid levels 
(buy orders) and ask levels (sell orders) in the order book.*/
    public int getBidLevels();
    public int getAskLevels();

    public BidLevel getBidAt(int index);
    public AskLevel getAskAt(int index);

    public List<ChildOrder> getChildOrders();

    public List<ChildOrder> getActiveChildOrders();
/*Returns the ID of the instrument being traded, 
which could be useful if you're working with multiple assets.*/
    public long getInstrumentId();
}
