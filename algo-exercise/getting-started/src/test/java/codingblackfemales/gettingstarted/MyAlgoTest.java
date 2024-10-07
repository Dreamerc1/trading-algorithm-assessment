package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import org.junit.Test;

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


   /* @Test
    public void testDispatchThroughSequencer() throws Exception {

        //create a sample market data tick....
        send(createTick());


        //simple assert to check we had 3 orders created
        assertEquals(container.getState().getChildOrders().size(), 3);
    }*/
   @Test
   public void testDispatchThroughSequencer() throws Exception {
       // Step 1: Simulate initial market data
       send(createTick());  // Simulate market data using the existing createTick() from AbstractAlgoTest

       // Step 2: Simulate the message being received, which triggers the algorithm logic
       container.onMessage(createTick());  // Simulate message received by the container

       // Step 3: Check if an order is created
       // Since the container processes actions, we assume the action would have been processed
       assertTrue(container.getState().getChildOrders().size() > 0);  // Check that an order has been created

       // Step 4: Simulate another tick (with the same market data)
       send(createTick());  // Simulate the same market data again
       container.onMessage(createTick());  // Trigger logic again

       // Step 5: Check if an order is cancelled
       // The test should check the current state to ensure cancellation happens based on your logic
       // Depending on how cancellations are tracked, you can assert cancellation logic here
       // For now, we assume cancellation might reduce the number of active child orders
       assertTrue(container.getState().getActiveChildOrders().size() == 0);  // Ensure orders were cancelled
   }
}

