import React from 'react';
import './PriceCell.css';

export interface PriceCellProps {
  price: number;
  direction: 'up' | 'down';
}

export const PriceCell = (props: PriceCellProps) => {
  const { price, direction } = props;

  return (
    <td className="PriceCell">
      <span className={`arrow ${direction === 'up' ? 'up' : 'down'}`}>
        {direction === 'up' ? '▲' : '▼'}
      </span>
      {price.toFixed(2)}
    </td>
  );
};

