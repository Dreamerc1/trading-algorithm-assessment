# Trading Algorithm Assessment

## Table of Contents
- [The Main Objective](#the-main-objective)
- [The Stretch Objective](#the-stretch-objective)
- [The UI Front End Objective](#the-ui-front-end-objective)
- [Project Structure](#project-structure)
- [Algorithm Features](#algorithm-features)
  - [Key Logic](#key-logic)
  - [Constraints and Parameters](#constraints-and-parameters)
- [Testing Overview](#testing-overview)
  - [Unit Tests (`MyAlgoTest`)](#unit-tests-myalgotest)
  - [Backtest (`MyAlgoBackTest`)](#backtest-myalgobacktest)
- [Getting Started](#getting-started)
  - [Running the Algorithm with IntelliJ](#running-the-algorithm-with-intellij)
  - [Run Unit Tests Using Command Line](#run-unit-tests-using-command-line)

## The Main Objective

The objective of this challenge is to write a simple trading algorithm that creates and cancels child orders.

## The Stretch Objective

The stretch objective of this challenge is to write an algo that can make money buy buying shares when the order book is cheaper, and selling them when the order book is more expensive.

## The UI Front End Objective

The UI objective is to create a front-end component that visually represents the order book, similar to the provided example, including bid and ask prices and quantities.

![Order Book UI Example](ui-front-end/public/market-depth.png)

## Project Structure
Main Challenge

- **MyAlgoLogic.java**: Core algorithm logic implementing buy and cancel strategies.
- **MyAlgoTest.java**: Unit tests for isolated testing of the algorithm's behavior under controlled tick data.
- **MyAlgoBackTest.java**: Comprehensive backtest simulating a live trading environment, with order fills.
- **AbstractAlgoTest.java** and **AbstractAlgoBackTest.java**: Base classes providing test infrastructure, including market simulation, tick data, and sequencing.

UI Challenge

- **MarketDepthFeature.css** / **MarketDepthFeature.tsx**: Defines the main UI feature for market depth.
- **MarketDepthPanel.css** / **MarketDepthPanel.tsx**: Represents the panel for market depth data.
- **PriceCell.css** / **PriceCell.tsx**: Component for displaying price cells.
- **QuantityCell.css** / **QuantityCell.tsx**: Component for displaying quantity cells.

## Algorithm Features

- ### Key Logic

- **Buy Logic**: Places buy orders if the current market ask price is within a defined `priceLimit`.
  - Creates up to 3 child orders with a fixed quantity of 50 units each, if the number of active orders is below the `MAX_ORDERS` limit.
  - Uses the best available bid level (`state.getBidAt(0)`) to determine the price for the new orders.
- **Cancel Logic**: Cancels the first active order if conditions are met.
  - Checks if there are active child orders and cancels the first one that hasn't been canceled.


### Constraints and Parameters

- **Maximum Orders**: Limits the number of active child orders to `MAX_ORDERS = 3` to manage risk and ensure strategic entry.
- **Quantity Per Order**: Each order is placed with a fixed quantity of 50 units.
- **Bid Level Evaluation**: Uses the best available bid (`state.getBidAt(0)`) to determine the price for new orders.
- **Order Cancellation**: Cancels the first active child order that is not yet canceled to free resources for new opportunities.


## Testing Overview

### Unit Tests (`MyAlgoTest`)

Tests the algorithm's logic in isolation, ensuring:

- **Buy Logic Test**: Verifies buy orders are only placed when conditions match `priceLimit`.
- **Cancel Logic Test**: Ensures orders are canceled correctly when required.

### Backtest (`MyAlgoBackTest`)

Simulates the algorithm in a live market environment, validating:

- **Order Fills**: Checks if expected quantities are filled.
- **Order Status**: Confirms correct categorization of partially and fully filled orders.
- **Profit Calculation**: Evaluates profit and verifies no negative outcomes.

## Testing Overview

### Unit Tests (`MyAlgoTest`)

Tests the algorithm's logic in isolation, ensuring:

- **Order Fills**: Checks the algorithm creates exactly three orders under standard conditions.
- **Order Cancellation Test**: After creating three orders, the first one is canceled upon the next tick.
- **Best Price Taken Test**: Ensures that the algorithm places orders at the best available bid price.
- **Order Quantity Test**: Verifies that each buy order has a quantity of 50.

### Backtest (`MyAlgoBackTest`)

Simulates the algorithm in a live market environment, validating:

- **Order Fills**: Checks if expected quantities are filled. Verifies that the orders added to the book are reflected in the market data.
- **Order Cancellation**: Confirms after creating three orders, the first one is canceled upon the next tick.
- **Maximum Orders Test**: Ensures that no more than the maximum number of orders (`MAX_ORDERS = 3`) are created.
- **Order Price Test**: Verifies that orders are placed at the best available price from the market data.
- **Algo Stop Test**: Ensures that after the first order cancellation, the algorithm stops creating new orders.
- **Order Quantity Test**: Ensures that the order quantity is as expected (50 units).

## Getting Started

### Running the Algorithm with IntelliJ

To get started with running the algorithm and its tests using IntelliJ, follow these steps:

1. **Open the Project**: Clone the repository and open the project in IntelliJ.
   ```bash
   git clone https://github.com/Dreamerc1/trading-algorithm-assessment.git
   ```
   Navigate to the project folder and open it with IntelliJ.

2. **Build the Project**: Make sure the project is built successfully by using the build tools in IntelliJ (e.g., Maven or Gradle, if applicable).

3. **Run Unit Tests**: To run the unit tests:
   - Navigate to `MyAlgoTest.java` or `MyAlgoBackTest.java`.
   - Right-click on the file and select **Run 'MyAlgoTest'** or **Run 'MyAlgoBackTest'**.
   - Alternatively, use the test runner configurations to run all tests.

4. **Viewing Test Results**: IntelliJ will display the test results in the **Run** panel, including any assertions and logs that indicate whether the algorithm behaved as expected.

### Run Unit Tests Using Command Line

```bash
run test -Dtest=MyAlgoTest
```

