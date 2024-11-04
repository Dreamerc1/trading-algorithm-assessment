interface MarketDepthPanelProps {
  data: MarketDepthRow[];
}

export const MarketDepthPanel = (props: MarketDepthPanelProps) => {
  return (
    <table className="MarketDepthPanel">
      <thead>
        <tr>
          <th colSpan={2}>Bid</th>
          <th colSpan={2}>Ask</th>
        </tr>
        <tr>
          <th>Quantity</th>
          <th>Price</th>
          <th>Price</th>
          <th>Quantity</th>
        </tr>
      </thead>
      <tbody>
        {props.data.map((row, index) => (
          <tr key={index}>
            <td className="bid-quantity">{row.bidQuantity}</td>
            <td className="bid-price">
              <span className="direction">{row.bidDirection === 'up' ? '↑' : '↓'}</span>
              {row.bidPrice}
            </td>
            <td className="ask-price">
              <span className="direction">{row.askDirection === 'up' ? '↑' : '↓'}</span>
              {row.askPrice}
            </td>
            <td className="ask-quantity">{row.askQuantity}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};
