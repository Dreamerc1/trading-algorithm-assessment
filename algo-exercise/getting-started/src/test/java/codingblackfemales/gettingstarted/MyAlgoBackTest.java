package codingblackfemales.gettingstarted;

import ch.qos.logback.classic.Logger;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test plugs together all of the infrastructure, including the order book (which you can trade against)
 * and the market data feed.
 *
 * If your algo adds orders to the book, they will reflect in your market data coming back from the order book.
 *
 * If you cross the srpead (i.e. you BUY an order with a price which is == or > askPrice()) you will match, and receive
 * a fill back into your order from the order book (visible from the algo in the childOrders of the state object.
 *
 * If you cancel the order your child order will show the order status as cancelled in the childOrders of the state object.
 *
 */
public class MyAlgoBackTest extends AbstractAlgoBackTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        return new MyAlgoLogic();
    }


    @Test
    public void testExampleBackTest() throws Exception {
        //create a sample market data tick....
        send(createTick());

        //ADD asserts when you have implemented your algo logic
        //assertEquals(container.getState().getChildOrders().size(), 3);

        //when: market data moves towards us
        send(createTick2());

        //then: get the state
        var state = container.getState();

        //Check things like filled quantity, cancelled order count etc....
        long filledQuantity = state.getChildOrders().stream().map(ChildOrder::getFilledQuantity).reduce(Long::sum).get();
        //and: check that our algo state was updated to reflect our fills when the market data
        assertEquals(250, filledQuantity);
    }
    @Test
    public void testOrderCreation() throws Exception {
        // Step 1: Simulate the initial market tick
        send(createTick());

        // Step 2: Verify if the expected number of child orders were created
        var state = container.getState();
        int expectedOrderCount = 15;  // Adjust based on your algo's behavior
        assertEquals("Expected number of orders created", expectedOrderCount, state.getChildOrders().size());

        // Step 3: Optionally, check specific order details like price, quantity, etc.
        var firstOrder = state.getChildOrders().get(0);  // Retrieve the first order
        assertEquals("Expected price for first order", 98, firstOrder.getPrice());
        assertEquals("Expected quantity for first order", 50, firstOrder.getQuantity());

        // Step 4: Simulate the next market tick (if applicable)
        send(createTick2());

        // Step 5: Optionally, validate additional order creation after market tick 2
        state = container.getState();  // Refresh state after the next tick
        int newExpectedOrderCount = 15;  // Adjust based on your algo's behavior after the second tick
        assertEquals("Expected number of orders after second tick", newExpectedOrderCount, state.getChildOrders().size());

        // Step 6: Check if the orders reflect the changes in the market (e.g., price adjustments, new orders)
        var updatedOrder = state.getChildOrders().get(0);  // Retrieve the first updated order
        assertEquals("Expected updated price for first order", 98, updatedOrder.getPrice());
        assertEquals("Expected updated quantity for first order", 50, updatedOrder.getQuantity());
    }
    @Test
    public void testCancelFirstOrder() throws Exception {
        // Step 1: Simulate market data to create orders
        send(createTick());  // Simulate initial tick to create orders

        // Step 2: Assert that orders were created
        assertEquals(15, container.getState().getChildOrders().size());

        // Step 3: Simulate market data again to trigger cancellation
        send(createTick2());  // Simulate new tick with conditions that would trigger cancellation

        // Step 4: Verify that the first order was canceled
        var state = container.getState();
        assertTrue("First active order should be canceled", state.getChildOrders().get(0).isCanceled());
    }
    @Test
    public void testCreateAndCancelOrders() throws Exception {
        // Step 1: Simulate the initial market tick to create orders
        for (int i = 0; i < 15; i++) {
            send(createTick()); // Create one order at each tick
        }

        // Step 2: Verify if the expected number of child orders were created
        var state = container.getState();
        assertEquals("Expected 15 child orders to be created.", 15, state.getChildOrders().size());

        // Step 3: Trigger cancellation by sending more ticks (since we have 15 orders)
        send(createTick()); // Should trigger cancellation of 1st, 5th, 10th

        // Step 4: Verify that the 1st, 5th, and 10th orders are canceled
        assertTrue("First order should be canceled", state.getChildOrders().get(0).isCanceled());
        assertTrue("Fifth order should be canceled", state.getChildOrders().get(4).isCanceled());
        assertTrue("Tenth order should be canceled", state.getChildOrders().get(9).isCanceled());
    }


}
