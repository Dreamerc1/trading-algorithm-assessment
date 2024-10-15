package codingblackfemales.gettingstarted;

import ch.qos.logback.classic.Logger;
import codingblackfemales.algo.AlgoLogic;
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
        //long filledQuantity = state.getChildOrders().stream().map(ChildOrder::getFilledQuantity).reduce(Long::sum).get();
        //and: check that our algo state was updated to reflect our fills when the market data
        //assertEquals(225, filledQuantity);
    }
    @Test
    public void testOrderCreation() throws Exception {
        // Step 1: Simulate the initial market tick
        send(createTick());

        // Step 2: Verify if the expected number of child orders were created
        var state = container.getState();
        int expectedOrderCount = 5;  // Adjust based on your algo's behavior
        assertEquals("Expected number of orders created", expectedOrderCount, state.getChildOrders().size());

        // Step 3: Optionally, check specific order details like price, quantity, etc.
        var firstOrder = state.getChildOrders().get(0);  // Retrieve the first order
        assertEquals("Expected price for first order", 98, firstOrder.getPrice());
        assertEquals("Expected quantity for first order", 300, firstOrder.getQuantity());

        // Step 4: Simulate the next market tick (if applicable)
        send(createTick2());

        // Step 5: Optionally, validate additional order creation after market tick 2
        state = container.getState();  // Refresh state after the next tick
        int newExpectedOrderCount = 5;  // Adjust based on your algo's behavior after the second tick
        assertEquals("Expected number of orders after second tick", newExpectedOrderCount, state.getChildOrders().size());

        // Step 6: Check if the orders reflect the changes in the market (e.g., price adjustments, new orders)
        var updatedOrder = state.getChildOrders().get(0);  // Retrieve the first updated order
        assertEquals("Expected updated price for first order", 98, updatedOrder.getPrice());
        assertEquals("Expected updated quantity for first order", 300, updatedOrder.getQuantity());
    }



}
