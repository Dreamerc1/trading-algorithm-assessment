import React from 'react';
import { PriceCell } from './PriceCell';
import { QuantityCell } from './QuantityCell';
import './MarketDepthPanel.css';

interface MarketDepthRow {
  symbolLevel: string;
  level: number;
  bid: number;
  bidQuantity: number;
  offer: number;
  offerQuantity: number;
}

interface MarketDepthPanelProps {
  data: MarketDepthRow[];
}

export const MarketDepthPanel = (props: MarketDepthPanelProps) => {
  const { data } = props;

  return (
    <table className="MarketDepthPanel">
      <thead>
        <tr>
          <th colSpan={1}></th>
          <th colSpan={2}>Bid</th>
          <th colSpan={2}>Ask</th>
        </tr>
        <tr>
          <th></th>
          <th>Quantity</th>
          <th>Price</th>
          <th>Price</th>
          <th>Quantity</th>
        </tr>
      </thead>

      <tbody>
        {data.map((row, index) => (
          <tr key={row.symbolLevel}>
            <td className="row-index">{index}</td>
            <QuantityCell quantity={row.bidQuantity} type="bid" />
            <PriceCell price={row.bid} direction="up" />
            <PriceCell price={row.offer} direction="down" />
            <QuantityCell quantity={row.offerQuantity} type="ask" />
          </tr>
        ))}
      </tbody>
    </table>
  );
};
