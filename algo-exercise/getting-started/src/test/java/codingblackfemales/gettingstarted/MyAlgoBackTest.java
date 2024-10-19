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
    public void testOrdersAreFilled() throws Exception {
        // Step 1: Send the initial market data tick to create orders
        send(createTick());

        // Step 2: Verify that 3 orders were created
        var state = container.getState();
        assertEquals("Expected 3 child orders to be created.", 3, state.getChildOrders().size());

        // Step 3: Send the second market data tick to move the market towards the orders
        send(createTick2());

        // Step 4: Wait for the orders to be filled
        // Depending on your framework, you might need to allow some time or steps for the fills to be processed

        // Step 5: Retrieve the updated state
        state = container.getState();

        // Step 6: Calculate the total filled quantity
        long filledQuantity = state.getChildOrders().stream()
                .mapToLong(ChildOrder::getFilledQuantity)
                .sum();

        // Step 7: Assert that the filled quantity matches the expected value
        long expectedFilledQuantity = 150; // 3 orders * 50 units each
        assertEquals("Orders should be filled and total filled quantity should be 150", expectedFilledQuantity, filledQuantity);

        // Step 8: Optionally, verify that the algorithm has stopped
        // Since we modified the logic to stop after orders are filled
    }



    @Test
    public void testAlgoStopsAfterFirstCancel() throws Exception {
        // Step 1: Simulate market conditions
        send(createTick());  // Send initial market tick to create orders

        // Step 2: Assert that the expected number of child orders were created
        var state = container.getState();
        assertEquals("Expected 3 child orders to be created.", 3, state.getChildOrders().size());

        // Step 3: Simulate further market conditions that trigger the cancel
        send(createTick());  // Market update that should trigger cancel

        // Step 4: Check if the first order was canceled
        var firstOrder = state.getChildOrders().get(0);
        assertTrue("First order should be canceled.", firstOrder.isCanceled());

        // Step 5: Verify that no further actions are taken after the first cancel
        send(createTick());  // Send another tick to ensure the algorithm has stopped
        assertEquals("No new orders should be created after the algorithm stops.", 3, state.getChildOrders().size());
    }
    @Test
    public void testNoMoreThanThreeOrdersCreated() throws Exception {
        // Step 1: Simulate multiple market ticks
        send(createTick());  // First tick
        send(createTick());  // Second tick
        send(createTick());  // Third tick

        // Step 2: Assert that no more than 3 orders are created
        var state = container.getState();
        assertEquals("Expected no more than 3 orders to be created.", 3, state.getChildOrders().size());
    }

    @Test
    public void testOrderCreatedAtBestPrice() throws Exception {
        // Step 1: Simulate market tick with different prices
        send(createTick());  // Send initial tick

        // Step 2: Assert that the first order was created at the best price
        var state = container.getState();
        var firstOrder = state.getChildOrders().get(0);
        long expectedPrice = 98;  // Best price from the market data
        assertEquals("Expected order to be created at the best price.", expectedPrice, firstOrder.getPrice());
    }
    @Test
    public void testOrderQuantityIsFifty() throws Exception {
        // Step 1: Simulate market conditions
        send(createTick());  // Send initial tick to create an order

        // Step 2: Assert that the first order has a quantity of 50
        var state = container.getState();
        var firstOrder = state.getChildOrders().get(0);
        assertEquals("Expected order quantity to be 50.", 50, firstOrder.getQuantity());
    }
    @Test
    public void testFirstOrderDetails() throws Exception {
        //create a sample market data tick....
        send(createTick());

        // ADD assertions when you have implemented your algo logic
        var state = container.getState();
        int totalOrders = state.getChildOrders().size();
        assertEquals("Expected number of orders", 3, totalOrders); // Check the expected number of orders

        // Check the details of the first order
        ChildOrder firstOrder = state.getChildOrders().get(0);
        assertEquals("Expected price for the first order.", 98L, firstOrder.getPrice());
        assertEquals("Expected quantity for the first order.", 50L, firstOrder.getQuantity());
        assertEquals("Expected side for the first order.", "BUY", firstOrder.getSide().toString());
    }




}
