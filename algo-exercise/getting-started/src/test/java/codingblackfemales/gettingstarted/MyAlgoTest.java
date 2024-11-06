package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This test is designed to check your algo behavior in isolation of the order book.
 *
 * You can tick in market data messages by creating new versions of createTick() (ex. createTick2, createTickMore etc..)
 *
 * You should then add behaviour to your algo to respond to that market data by creating or cancelling child orders.
 *
 * When you are comfortable you algo does what you expect, then you can move on to creating the MyAlgoBackTest.
 *
 */
public class MyAlgoTest extends AbstractAlgoTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        //this adds your algo logic to the container classes
        return new MyAlgoLogic();
    }


   @Test
    public void exampleTestDispatchThroughSequencer() throws Exception {

        //create a sample market data tick....
        send(createTick());


        //simple assert to check we had 3 orders created
        assertEquals(container.getState().getChildOrders().size(), 3);
    }



    //Test 1: Check that after creating 3 orders, the first one is canceled on the next tick.

    @Test
    public void testCancelFirstOrder() throws Exception {
        // Simulate 3 market ticks to create 3 orders
        for (int i = 0; i < 3; i++) {
            send(createTick());
        }

        // Assert that 3 orders have been created
        var state = container.getState();
        assertEquals("Expected 3 child orders to be created.", 3, state.getChildOrders().size());

        // Simulate another tick to trigger cancellation of the first order
        send(createTick());

        // Assert that the first order is canceled
        assertTrue("First order should be canceled.", state.getChildOrders().get(0).isCanceled());
    }


    @Test
    public void testBestPriceTaken() throws Exception {
        // Step 1: Simulate market data tick(s) with known bid/ask prices
        send(createTick());  // Simulate market condition

        // Step 2: Get the first order created by the algorithm
        var state = container.getState();
        var firstOrder = state.getChildOrders().get(0);

        // Step 3: Retrieve the best bid price from the market data
        long bestBidPrice = state.getBidAt(0).price;

        // Step 4: Assert that the order was placed at the best bid price
        assertEquals("Expected the order to be placed at the best bid price", bestBidPrice, firstOrder.getPrice());
    }
    //Test 2: Ensure that each buy order has a quantity of 50.
    @Test
    public void testBuysFiftyUnits() throws Exception {
        // Step 1: Simulate market data tick(s)
        send(createTick());  // First tick

        // Step 2: Get the first order created by the algorithm
        var state = container.getState();
        var firstOrder = state.getChildOrders().get(0);

        // Step 3: Assert that the order has a quantity of 50
        long expectedQuantity = 50;
        assertEquals("Expected each buy order to have a quantity of 50", expectedQuantity, firstOrder.getQuantity());
    }


}




