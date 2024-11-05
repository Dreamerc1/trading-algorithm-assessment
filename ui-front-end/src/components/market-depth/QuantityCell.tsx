/* import React from 'react';
import './QuantityCell.css';

export interface QuantityCellProps {
  quantity: number;
  type: 'bid' | 'ask';
}

export const QuantityCell = (props: QuantityCellProps) => {
  const { quantity, type } = props;

  return (
    <td className={`QuantityCell ${type}`}>
      <div className="bar" style={{ width: `${Math.min(100, (quantity / 2000) * 100)}%` }}></div>
      {quantity}
    </td>
  );
}; */
import React from 'react';
import './QuantityCell.css';

export interface QuantityCellProps {
  quantity: number;
  type: 'bid' | 'ask';
}

export const QuantityCell = (props: QuantityCellProps) => {
  const { quantity, type } = props;

  return (
    <td className={`QuantityCell ${type}`}>
      <div className="bar" style={{ width: `${Math.min(100, (quantity / 4000) * 100)}%` }}></div>
      <span className="quantity-value">{quantity}</span>
    </td>
  );
};
