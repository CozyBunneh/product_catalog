import React, { useEffect, useState } from "react";
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
  const [debouncedSearchQuery, setDebouncedSearchQuery] = useState(searchQuery);
  const debounceDelay = 300;

  // Update the debounced search query after a delay
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearchQuery(searchQuery);
    }, debounceDelay);

    // Cleanup function to clear the timeout if the component unmounts or if searchQuery changes
    return () => {
      clearTimeout(handler);
    };
  }, [searchQuery]);

  // Effect to perform the search when the debounced search query changes
  useEffect(() => {
    if (debouncedSearchQuery) {
      handleFetchByQuery();
    } else {
      handleFetchClear();
    }
  }, [debouncedSearchQuery]);

  const handleFetchByQuery = async () => {
    if (debouncedSearchQuery) {
      await getProductsByQyery(debouncedSearchQuery);
    }
  };

  const handleFetchClear = async () => {
    setSearchQuery("");
    setDebouncedSearchQuery("");
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
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
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
