package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.BidLevel;
import messages.order.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    /*private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    private static final int MAX_ORDERS = 10; // Maximum number of child orders
    private static final long PRICE_THRESHOLD = 90; // 100 Price threshold for creating orders

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
        // Order Creation Logic
        /*if (state.getChildOrders().size() < MAX_ORDERS) {
            // Access the best bid price
            BidLevel bestBid = state.getBidAt(0);

            if (bestBid != null && bestBid.price > PRICE_THRESHOLD) {
                long price = bestBid.price;
                long quantity = 50; // Set your desired quantity

                logger.info("[MYALGO] Placing a BUY order for " + quantity + " @ " + price);
                return new CreateChildOrder(Side.BUY, quantity, price);
            } else {
                logger.warn("[MYALGO] No favorable bid levels available in the order book.");
            }
        } else {
            logger.info("[MYALGO] Maximum number of orders reached. No new order will be placed.");
        }

        // Cancellation Logic: Cancel the first active child order if there are any
        var activeOrders = state.getActiveChildOrders();
        if (!activeOrders.isEmpty()) {
            ChildOrder orderToCancel = activeOrders.get(0);  // Get the first active order
            logger.info("[MYALGO] Cancelling the first active order: " + orderToCancel);
            return new CancelChildOrder(orderToCancel);  // Cancel the order
        }

        // If no action is needed
        return NoAction.NoAction;
    }
}*/
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);
    private static final int MAX_ORDERS = 3;  // Create up to 3 orders

    @Override
    public Action evaluate(SimpleAlgoState state) {
        logger.info("[MYALGO] Evaluating...");

        // Check the total number of orders
        int totalOrders = state.getChildOrders().size();

        // Create up to 3 orders
        if (totalOrders < MAX_ORDERS) {
            BidLevel bestBid = state.getBidAt(0);
            if (bestBid != null) {
                long price = bestBid.price;
                long quantity = 50; // Example quantity
                logger.info("[MYALGO] Creating order #" + (totalOrders + 1) + " for " + quantity + " @ " + price);
                return new CreateChildOrder(Side.BUY, quantity, price);
            } else {
                logger.warn("[MYALGO] No bid levels available.");
            }
        }

        // Order cancellation logic (cancel the first active order)
        if (!state.getActiveChildOrders().isEmpty()) {
            ChildOrder firstOrder = state.getActiveChildOrders().get(0);
            if (!firstOrder.isCanceled()) {
                logger.info("[MYALGO] Cancelling the first order: " + firstOrder);
                firstOrder.setCanceled(true);  // Mark the order as canceled
                return new CancelChildOrder(firstOrder);
            }
        }


        // No further actions
        return NoAction.NoAction;
    }
}