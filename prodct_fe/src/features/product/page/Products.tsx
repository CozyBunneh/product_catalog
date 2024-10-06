import React from "react";
import ProductTable from "../component/ProductTable";

const Products: React.FC = () => {
  return (
    <div className="m-2">
      <h1>Products</h1>
      <ProductTable />
    </div>
  );
};

export default Products;
