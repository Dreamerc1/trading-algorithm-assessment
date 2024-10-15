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
        assertEquals(container.getState().getChildOrders().size(), 5);
    }

    @Test
    public void testOrderCreation() throws Exception {
        // Step 1: Send market data to trigger order creation
        send(createTick());  // Simulate market conditions

        // Step 2: Assert that 5 child orders were created
        assertEquals("Expected 5 child orders to be created.", 5, container.getState().getChildOrders().size());
    }
}




