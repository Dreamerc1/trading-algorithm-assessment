package codingblackfemales.gettingstarted;

/*
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

    */
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
         *//*

        // Order Creation Logic
        */
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
}*//*

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
}*/

import codingblackfemales.action.Action;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import messages.order.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import static codingblackfemales.action.NoAction.NoAction;

public class MyAlgoLogic implements AlgoLogic {
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);
    private static final int MAX_ORDERS = 3;  // Create up to 3 buy and 3 sell orders
    private boolean isStopped = false;   // Flag to stop the algorithm after end-of-day

    // Separate queues for buy and sell orders
    private Queue<ChildOrder> buyOrderQueue = new LinkedList<>();
    private Queue<ChildOrder> sellOrderQueue = new LinkedList<>();

    @Override
    public Action evaluate(SimpleAlgoState state) {
        logger.info("[MYALGO] Evaluating...");

        // Categorize child orders into buy and sell orders
        categorizeChildOrders(state.getChildOrders());

        try {
            // Buy Order Logic
            if (buyOrderQueue.size() < MAX_ORDERS) {
                BidLevel bestBid = state.getBidAt(0);
                if (bestBid != null) {
                    long price = bestBid.price;
                    long quantity = 50; // Example quantity
                    logger.info("[MYALGO] Creating Buy Order #" + (buyOrderQueue.size() + 1) + " for " + quantity + " @ " + price);

                    // Properly declare newBuyOrder using the CreateChildOrder constructor
                    CreateChildOrder newBuyOrder = new CreateChildOrder(Side.BUY, quantity, price);

                    // Add the order to the buy queue
                    buyOrderQueue.add(newBuyOrder);

                    // Return newBuyOrder as Action
                    return newBuyOrder;
                } else {
                    logger.warn("[MYALGO] No bid levels available.");
                }
            }

            // Sell Order Logic
            if (sellOrderQueue.size() < MAX_ORDERS) {
                AskLevel bestAsk = state.getAskAt(0);
                if (bestAsk != null) {
                    long price = bestAsk.price;
                    long quantity = 50; // Example quantity
                    logger.info("[MYALGO] Creating Sell Order #" + (sellOrderQueue.size() + 1) + " for " + quantity + " @ " + price);

                    // Properly declare newSellOrder using the CreateChildOrder constructor
                    CreateChildOrder newSellOrder = new CreateChildOrder(Side.SELL, quantity, price);

                    // Add the order to the sell queue
                    sellOrderQueue.add(newSellOrder);

                    // Return newSellOrder as Action
                    return newSellOrder;
                } else {
                    logger.warn("[MYALGO] No ask levels available.");
                }
            }

            // End of Day Logic: Cancel All Orders
            if (isEndOfDay() && !isStopped) {
                logger.info("[MYALGO] End of day reached, cancelling all active orders...");
                cancelAllActiveOrders();
                isStopped = true; // Set flag to indicate that end-of-day operations have been executed
                return NoAction; // Return NoAction (also of type Action)
            }

        } catch (Exception e) {
            logger.error("[MYALGO] General error in evaluation: " + e.getMessage(), e);
        }

        // No further actions
        return NoAction; // Default action if no other action is taken
    }

    // Helper method to categorize child orders into buy and sell orders
    private void categorizeChildOrders(List<ChildOrder> childOrders) {
        buyOrderQueue.clear();
        sellOrderQueue.clear();

        for (ChildOrder order : childOrders) {
            // Ensure that order.getSide() returns the correct value for categorization
            if (order.getSide() == Side.BUY && !order.isCanceled()) {
                buyOrderQueue.add(order);
            } else if (order.getSide() == Side.SELL && !order.isCanceled()) {
                sellOrderQueue.add(order);
            }
        }
    }

    private boolean isEndOfDay() {
        // London trading hours are from 8:00 AM to 4:30 PM
        LocalTime currentTime = LocalTime.now();
        LocalTime marketCloseTime = LocalTime.of(16, 30);
        return currentTime.isAfter(marketCloseTime);
    }

    private void cancelAllActiveOrders() {
        try {
            // Cancel all buy orders
            while (!buyOrderQueue.isEmpty()) {
                ChildOrder orderToCancel = buyOrderQueue.poll();
                if (orderToCancel != null && !orderToCancel.isCanceled()) {
                    logger.info("[MYALGO] Cancelling Buy Order: " + orderToCancel);
                    orderToCancel.setCanceled(true); // Mark the order as canceled
                }
            }

            // Cancel all sell orders
            while (!sellOrderQueue.isEmpty()) {
                ChildOrder orderToCancel = sellOrderQueue.poll();
                if (orderToCancel != null && !orderToCancel.isCanceled()) {
                    logger.info("[MYALGO] Cancelling Sell Order: " + orderToCancel);
                    orderToCancel.setCanceled(true); // Mark the order as canceled
                }
            }
        } catch (Exception e) {
            logger.error("[MYALGO] Error during order cancellation: " + e.getMessage(), e);
        }
    }
}