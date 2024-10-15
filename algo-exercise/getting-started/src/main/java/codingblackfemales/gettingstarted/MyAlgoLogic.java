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

    private static final int MAX_ORDERS = 5; // Maximum number of child orders
    private static final long PRICE_DROP_THRESHOLD = 5; // Price drop threshold for canceling orders
    private static final long PRICE_THRESHOLD = 90; // Price threshold for creating orders

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
        if (state.getChildOrders().size() < MAX_ORDERS) {
            // Access the best bid price
            BidLevel bestBid = state.getBidAt(0);

            if (bestBid != null && bestBid.price > PRICE_THRESHOLD) {
                long price = bestBid.price;
                long quantity = 300; // Set your desired quantity

                logger.info("[MYALGO] Placing a BUY order for " + quantity + " @ " + price);
                return new CreateChildOrder(Side.BUY, quantity, price);
            } else {
                logger.warn("[MYALGO] No favorable bid levels available in the order book.");
            }
        } else {
            logger.info("[MYALGO] Maximum number of orders reached. No new order will be placed.");
        }

        // Order Cancellation Logic
        var activeOrders = state.getActiveChildOrders();
        if (!activeOrders.isEmpty()) {
            // Loop through active orders to see if they need canceling
            for (ChildOrder order : activeOrders) {
                BidLevel currentBid = state.getBidAt(0);

                // Only cancel if the current market price is unfavorable (e.g., 10 units below order price)
                if (currentBid != null && currentBid.price < order.getPrice() - PRICE_DROP_THRESHOLD) {
                    logger.info("[MYALGO] Cancelling order: " + order);
                    return new CancelChildOrder(order);
                }
            }
        }

        // If no action is needed
        return NoAction.NoAction;
    }
}
