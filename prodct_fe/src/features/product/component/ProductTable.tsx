import React, { useState } from "react";
import { useProducts } from "../hook/useProduct";
import ProductForm from "./ProductForm";
import ProductItem from "./ProductItem";

const ProductTable: React.FC = () => {
  const {
    products,
    loading,
    createProduct,
    editProduct,
    removeProduct,
    getProductsByQyery,
    reloadProducts
  } = useProducts();
  const [searchQuery, setSearchQuery] = useState<string>("");

  const handleFetchByName = async () => {
    if (searchQuery) {
      await getProductsByQyery(searchQuery);
    }
  };

  const handleFetchClear = async () => {
    setSearchQuery("");
    await reloadProducts();
  };

  return (
    <div>
      <div style={{ display: "flex" }}>
        <div>
          <h2>Search Products</h2>
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Enter search query"
            className="mb-1"
          />
          <button className="ml-1" onClick={handleFetchByName}>Search</button>
          <button className="ml-1 button-danger" onClick={handleFetchClear}>Clear</button>
        </div>
        <ProductForm style={{ alignSelf: "flex-end", marginLeft: "auto" }} onSubmit={createProduct} />
      </div>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="styled-table">
          <thead>
            <tr>
              <th>Id</th>
              <th>Name</th>
              <th className="right-align">Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <ProductItem
                key={product.id}
                product={product}
                onEdit={editProduct}
                onDelete={removeProduct}
              />
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ProductTable;
