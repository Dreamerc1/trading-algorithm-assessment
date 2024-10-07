package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);
  
    private static final double BUY_THRESHOLD = 1.05;
    private static final double SELL_THRESHOLD = 0.95;
    private static final int MAX_ORDERS = 3; // Maximum number of child orders

    @Override
    public Action evaluate(SimpleAlgoState state) {
// Log the current state of the order book
        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

        /********
         *
         * Add your logic here....
         *
         */
        // Basic Order Creation Logic
        // Check if we have fewer than MAX_ORDERS child orders
        if (state.getChildOrders().size() < MAX_ORDERS) {
            // Access the best bid price
            BidLevel bestBid = state.getBidAt(0);
            // Create an order only if the price is above 90
            if (bestBid != null && bestBid.price > 90) {
                long price = bestBid.price;
                long quantity = 100; // Set your desired quantity

                logger.info("[MYALGO] Placing a BUY order for " + quantity + " @ " + price);
                return new CreateChildOrder(Side.BUY, quantity, price);
            } else {
                logger.warn("[MYALGO] No bid levels available in the order book.");
            }
        }

        // Basic Order Cancellation Logic
        // Check for active orders to cancel
        var activeOrders = state.getActiveChildOrders();
        if (!activeOrders.isEmpty()) {
            // Loop through active orders to see if they need cancelling
            for (ChildOrder order : activeOrders) {
                BidLevel currentBid = state.getBidAt(0);

                //Only cancel if the current market price is unfavorable
                // Example threshold: cancel if market moves against you by 10 units
                if (currentBid != null && currentBid.price < order.getPrice() - 10) {
                    logger.info("[MYALGO] Cancelling order: " + order);
                    return new CancelChildOrder(order);
                }

                // If no action is needed
                return NoAction.NoAction;
            }
        }