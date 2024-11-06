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

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);
    private static final int MAX_ORDERS = 3;  // Create up to 3 orders
    private int totalOrdersCreated = 0;  // Counter for total orders created
    private boolean isStopped = false;   // Flag to stop the algorithm after cancellation

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