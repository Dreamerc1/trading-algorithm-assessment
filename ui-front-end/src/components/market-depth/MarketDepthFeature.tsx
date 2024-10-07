import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

// Register Chart.js components for the bar chart
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

// Test data (can replace with dynamic data if needed)
const testData: MarketDepthRow[] = [
  { symbolLevel: "1230", level: 0, bid: 3075, bidQuantity: 3075, offer: 50.19, offerQuantity: 2200 },
  { symbolLevel: "1231", level: 1, bid: 3251, bidQuantity: 3251, offer: 51.03, offerQuantity: 2263 },
  { symbolLevel: "1232", level: 2, bid: 3394, bidQuantity: 3394, offer: 51.62, offerQuantity: 2244 },
  { symbolLevel: "1233", level: 3, bid: 3408, bidQuantity: 3408, offer: 52.24, offerQuantity: 2457 },
  { symbolLevel: "1234", level: 4, bid: 3485, bidQuantity: 3485, offer: 53.70, offerQuantity: 2467 },
  { symbolLevel: "1235", level: 5, bid: 3559, bidQuantity: 3559, offer: 55.06, offerQuantity: 2631 },
  { symbolLevel: "1236", level: 6, bid: 3667, bidQuantity: 3667, offer: 56.58, offerQuantity: 2671 },
];

// MarketDepthFeature Component
export const MarketDepthFeature = () => {
  const data = testData;  // Use test data

  // Extract labels and data for bids and offers
  const priceLevels = data.map((level) => `Level ${level.level}`);
  const bidQuantities = data.map((level) => level.bidQuantity);
  const askQuantities = data.map((level) => level.offerQuantity);

  // Prepare chart data
  const chartData = {
    labels: priceLevels,  // X-axis: price levels (0, 1, 2, ...)
    datasets: [
      {
        label: 'Bid Quantities',
        data: bidQuantities,
        backgroundColor: 'blue',
        borderColor: 'blue',
        borderWidth: 1,
        hoverBackgroundColor: 'darkblue',
        stack: 'Stack 0',  // Place bids and asks on the same row
      },
      {
        label: 'Ask Quantities',
        data: askQuantities,
        backgroundColor: 'red',
        borderColor: 'red',
        borderWidth: 1,
        hoverBackgroundColor: 'darkred',
        stack: 'Stack 1',  // Place bids and asks on the same row
      },
    ],
  };

  // Chart options to format it as horizontal bars
  const chartOptions = {
    indexAxis: 'y',  // This makes the bar chart horizontal
    scales: {
      x: {
        beginAtZero: true,  // Start the X-axis from 0
      },
    },
    responsive: true,
    plugins: {
      legend: {
        position: 'top',  // Place legend at the top
      },
    },
  };

  return (
    <div>
      <Bar data={chartData} options={chartOptions} />
    </div>
  );
};
