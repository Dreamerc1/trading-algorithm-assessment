interface MarketDepthPanelProps {
  data: MarketDepthRow[];
}

export const MarketDepthPanel = (props: MarketDepthPanelProps) => {
  return (
    <table className="MarketDepthPanel">
      <thead>
        <tr>
          <th>Price</th>
          <th>Quantity</th>
          {/* Add other headers based on data structure */}
        </tr>
      </thead>
      <tbody>
        {props.data.map((row, index) => (
          <tr key={index}>
            <td>{row.price}</td>
            <td>{row.quantity}</td>
            {/* Add other cells based on data structure */}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

